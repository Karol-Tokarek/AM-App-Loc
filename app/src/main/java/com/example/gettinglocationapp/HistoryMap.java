package com.example.gettinglocationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Date;
import java.util.Map;

public class HistoryMap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_map);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView txt = (TextView)findViewById(R.id.textView7);
//        Date date = new Date();
//        String dt = String.valueOf(date.getTime());
        SharedPreferences shared = getSharedPreferences("com.example.ShowMap", MODE_PRIVATE);
        Map<String, ?> all = shared.getAll();
        String channel = "";
        for (Map.Entry<String, ?> entry : all.entrySet()) {

            channel += "" + entry.getValue() + "\n\n";
        }

//        String channel = (shared.getString("Location", ""));
        txt.setText(channel);




    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}