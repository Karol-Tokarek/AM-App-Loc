package com.example.gettinglocationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public LocationManager loc;
    private TextView h, w;
    private Geocoder geocoder;
    private DrawerLayout drawerLayout;
    private double he, wi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button showMap = (Button)findViewById(R.id.buttonShowMap);
        Button btnLog = (Button)findViewById(R.id.buttonShowMap2);
        Button btnHistory = (Button)findViewById(R.id.buttonShowMap3);


        loc = (LocationManager)getSystemService(Context.LOCATION_SERVICE);


        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ShowMap.class);
                startActivity(intent);
            }


        });

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), LogIn.class);
                startActivity(intent);
            }


        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), HistoryMap.class);
                startActivity(intent);

            }


        });


    }
}