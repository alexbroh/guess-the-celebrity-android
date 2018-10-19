package com.digitalidllc.alex_roh.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Pair<String,String>> celebrities;
    private Button[] buttons;
    private ImageView celebrityIV;
    private int currentCelebrity=0;

    private class dataDownloader extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                StringBuilder stringBuilder = new StringBuilder();
                while (data != -1) {
                    char current = (char) data;
                    stringBuilder.append(current);
                    data = reader.read();
                }
                return stringBuilder.toString();

            }
            catch(Exception e){
                e.printStackTrace();
                return "Failed";
            }
        }
    }


    private class ImageDownloader extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();

                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }


    private void parseData(String data){
        Pattern pattern = Pattern.compile("(?x)\n" +
                "<img\\ src=\\\"([\\w\\W]+?)\\\"\\ \n" +
                "alt=\\\"([\\w\\W]+?)\\\"\\/>");

        Matcher matcher = pattern.matcher(data);

        while(matcher.find()){
            String link = matcher.group(1); Log.i("Link",link);
            String name = matcher.group(2); Log.i("name", name);
            if(link!=null&&name!=null) {
                Pair<String, String> celebrity = new Pair<String, String>(name, link);
                if(celebrity!=null)
                celebrities.add(celebrity);
                else Log.e("Error","Celebrity null");
            }
        }
    }

    private void displayNextCelebrity(){
        //get next celebrity
        Pair<String, String> celebrity = celebrities.get(currentCelebrity);

        //set choices
        Random rand = new Random();
        int answerIndex = rand.nextInt(4);
        Set<String> celebritiesSelected = new HashSet<>();

        //add the chosen celebrity
        celebritiesSelected.add(celebrity.first);

        //display
        for(int i=0; i<4; ++i){
            if(i!=answerIndex){ //wrong answers
                String randomCelebrity=celebrities.get(rand.nextInt(celebrities.size())).first;
                while(celebritiesSelected.contains(randomCelebrity)) { //make sure there are no duplicates
                    randomCelebrity=celebrities.get(rand.nextInt(celebrities.size())).first;
                }
                //add name to set, so we don't get repeats
                celebritiesSelected.add(randomCelebrity);
                buttons[i].setText(randomCelebrity);
            } else{ //right answer
                buttons[i].setText(celebrity.first);
                //image
                ImageDownloader task = new ImageDownloader();
                Bitmap myImage;
                try {
                    myImage = task.execute(celebrity.second).get();
                    celebrityIV.setImageBitmap(myImage);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void setUpVariables(){
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        buttons = new Button[4];
        buttons[0]=button1; buttons[1]=button2; buttons[2] = button3; buttons[3]=button4;


        celebrityIV = findViewById(R.id.celebrityIV);
    }

    public void selectAnswer(View view){
        Log.i("Button","select answer");
        Button selected = (Button) view;
        //correct answer
        if(celebrities.get(currentCelebrity).first==selected.getText().toString()){
            Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            //wrong answer
            Toast.makeText(getApplicationContext(), "Wrong!", Toast.LENGTH_SHORT).show();
        }

        //let's go to next celebrity
        currentCelebrity++;
        //if at end of arraylist, reset index
        if(currentCelebrity==celebrities.size()) currentCelebrity=0;
        displayNextCelebrity();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpVariables();

        celebrities=new ArrayList<Pair<String,String>>();
        dataDownloader downloader = new dataDownloader();
        String result="";
        try{
            result=downloader.execute("http://www.posh24.se/kandisar").get();
        }catch(Exception e){
            e.printStackTrace();
        }
        parseData(result);

        displayNextCelebrity();
    }
}
