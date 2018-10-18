package com.digitalidllc.alex_roh.guessthecelebrity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public class dataDownloader extends AsyncTask<String, Void, Pair<String,String>>{
        @Override
        protected Pair<String, String> doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
            }
            catch(Exception e){
                e.printStackTrace();
                return null;
            }
            return null;
        }
    }

    public void selectAnswer(View view){
        Log.i("Button","select answer");

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
