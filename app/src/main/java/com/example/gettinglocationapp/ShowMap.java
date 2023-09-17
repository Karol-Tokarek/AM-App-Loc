package com.example.gettinglocationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.ApiKey;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ShowMap extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationSource {

    private Geocoder geocoder;

    private String getAddressFrom(double latitude, double longitude) {
        geocoder = new Geocoder(this, Locale.getDefault());
        String result = "Adres:";
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            for (Address address : addresses) {
                for (int i = 0, j = address.getMaxAddressLineIndex(); i <= j; i++) {
                    result += address.getAddressLine(i) + "";
                }
                result += "";
            }
        } catch (IOException e) {
        }
        return result;

    }

    boolean isPermissionGranted;
    FloatingActionButton floatingbtn;
    Button searchbtn;
    FloatingActionButton savebtn;

    public double lang;
    public double lat;
    String address;
    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;
    public LocationManager loc;



    public FusedLocationProviderClient mLocationClient;


    private void checkMyPermission() {

        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        return;  /////pobieranie permisji (komunikat czy dajesz permisje aplikacji)

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.ShowMap", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_show_map);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        checkMyPermission();

            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            supportMapFragment.getMapAsync(this);

        floatingbtn = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        mLocationClient = LocationServices.getFusedLocationProviderClient(this);
        savebtn = (FloatingActionButton) findViewById(R.id.floatingActionButton3);
        searchbtn = (Button) findViewById(R.id.button2);

        floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrLoc();
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ed = (EditText)findViewById(R.id.editTextTextPersonName);
                String street = String.valueOf(ed.getText());

                Geocoder geocoder2 = new Geocoder(getApplicationContext());
                List<Address> addresslist;

                try {
                    addresslist = geocoder2.getFromLocationName(street, 1);
                    if(addresslist!=null)
                    {
                        double lat = addresslist.get(0).getLatitude();
                        double longitude = addresslist.get(0).getLongitude();
                        address = getAddressFrom(lat, longitude);
                        gotolocation(lat, longitude);



                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //getCurrLoc();
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uuid = UUID.randomUUID().toString();
//                SharedPreferences shared = getSharedPreferences("com.example.ShowMap", MODE_PRIVATE);
//                shared.getString("Location", "");
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Location"+uuid, address);
                Toast toast = Toast.makeText(getApplicationContext(), "Zapisano twoją lokalizacje: "+address+"", Toast.LENGTH_SHORT);
                toast.show();
                editor.apply();

            }
        });

    }


    @SuppressLint("MissingPermission")
    private void getCurrLoc() {

        mLocationClient.getLastLocation().addOnCompleteListener(task ->{
            if(task.isSuccessful())
            {
                Location loc = task.getResult();
                address = getAddressFrom(loc.getLatitude(), loc.getLongitude());
                gotolocation(loc.getLatitude(), loc.getLongitude());
            }
        });

    }

    private void gotolocation(double latitude, double longitude) {
        LatLng latlng = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, 18);
        mGoogleMap.moveCamera(cameraUpdate);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.addMarker(new MarkerOptions().position(latlng).title(""+ "" + address));

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {


        super.onPointerCaptureChanged(hasCapture);
        // IdzDoLokacji(49.0002, 54.020);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onStart() {

        super.onStart();



    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void activate(@NonNull OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }
}
