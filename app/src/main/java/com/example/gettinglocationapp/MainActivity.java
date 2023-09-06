package com.example.gettinglocationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    private double he, wi;

    private String getAddressFrom(double latitude, double longitude) {
        geocoder = new Geocoder(this, Locale.getDefault());
String result = "Geolocation address:\n";
try {
List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
for (Address address : addresses) {
for (int i = 0, j = address.getMaxAddressLineIndex(); i <= j; i++) {
result += address.getAddressLine(i) + "\n";
}
result += "\n\n";
}
} catch (IOException e) {
}
return result;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button showMap = (Button)findViewById(R.id.buttonShowMap);
        Button start = (Button)findViewById(R.id.buttonstart);
        Button stop = (Button)findViewById(R.id.buttonstop);

        loc = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                String width = String.valueOf(location.getLatitude());
                String height = String.valueOf(location.getLongitude());
                he = Double.parseDouble(height);
                wi = Double.parseDouble(width);

                h = (TextView) findViewById(R.id.textViewHeight);
                h.setText("Wysokosc"+height);

                w = (TextView) findViewById(R.id.textViewWidth);
                w.setText("Szerokosc"+width);

                h.setText(String.valueOf(getAddressFrom(wi, he)).toString());




            }





        };

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                    return;  /////pobieranie permisji (komunikat czy dajesz permisje aplikacji)
                }
                start.setEnabled(false); //blokowanie przyciskow
                stop.setEnabled(true);
                loc.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) listener);
                //wlaczanie gpsa !!


            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                start.setEnabled(true);
                stop.setEnabled(false); ///blokowanie przyciskow
                loc.removeUpdates((LocationListener) listener);
                ///blokowanie zbierania GPSa

            }


        });

        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent = new Intent(MainActivity.this, ShowMap.class);
                MainActivity.this.startActivity(intent);
            }


        });

        //private TextView szerokosc, wysokosc

    }
}