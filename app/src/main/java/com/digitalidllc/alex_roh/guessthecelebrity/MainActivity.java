package com.digitalidllc.alex_roh.guessthecelebrity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Pair<String,String>> celebrities;

    public class dataDownloader extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                String result="";
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;

            }
            catch(Exception e){
                e.printStackTrace();
                return "Failed";
            }
        }
    }

    public void selectAnswer(View view){
        Log.i("Button","select answer");

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataDownloader downloader = new dataDownloader();
        String result="";
        try{
            result=downloader.execute("http://www.posh24.se/kandisar").get();
        }catch(Exception e){
            e.printStackTrace();
        }
        Log.i("Result: ", result);
    }
}
