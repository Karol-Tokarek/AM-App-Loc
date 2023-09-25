package com.example.gettinglocationapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ShowMap extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationSource, LocationListener {

    private Geocoder geocoder;

    private String getAddressFrom(double latitude, double longitude) {
        geocoder = new Geocoder(this, Locale.getDefault());
        String result = "";
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

    public void setRouteDraw() {
//        GeoApiContext context = new GeoApiContext.Builder()
//                .apiKey("AIzaSyBrPt88vvoPDDn_imh-RzCXl5Ha2F2LYig")
//                .build();
//        DirectionsApiRequest req = DirectionsApi.getDirections(context, "41.385064,2.173403", "40.416775,-3.70379");
    }

    boolean isPermissionGranted;
    FloatingActionButton floatingbtn;
    FloatingActionButton floatingbtn2;
    Button searchbtn;
    FloatingActionButton savebtn;
    Button wyznacztrasebtn;
    double distance = 0;
    public double lang;
    public double lat;
    String address;
    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;
    TextView txtkm;
    LocationManager locationManager;
    private GeoApiContext geoApiContext;


    public FusedLocationProviderClient mLocationClient;


    private void checkMyPermission() {

        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        return;  /////pobieranie permisji (komunikat czy dajesz permisje aplikacji)

    }

    private boolean checkifgpson()
    {
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            Toast.makeText(getApplicationContext(), "GPS jest wyłączony !!! Włącz go, aby zlokalizować się na mapie!", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
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



        EditText edfrom = (EditText)findViewById(R.id.editTextTextPersonName);
        EditText edto = (EditText)findViewById(R.id.editTextTextPersonName2);

        checkMyPermission();

            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            supportMapFragment.getMapAsync(this);

        floatingbtn = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingbtn2 = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        mLocationClient = LocationServices.getFusedLocationProviderClient(this);
        savebtn = (FloatingActionButton) findViewById(R.id.floatingActionButton3);
        searchbtn = (Button) findViewById(R.id.button2);
        wyznacztrasebtn = (Button) findViewById(R.id.button);
         txtkm = (TextView) findViewById(R.id.textView3);


        wyznacztrasebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double addressfromlat = 0;
                double addressfromlang = 0;
                double addresstolat = 0;
                double addresstolang = 0;

                String from = String.valueOf(edfrom.getText());
                String to = String.valueOf(edto.getText());



                Geocoder geocoder2 = new Geocoder(getApplicationContext());
                List<Address> addresslist;

                try {
                    addresslist = geocoder2.getFromLocationName(from, 1);
                    if(addresslist!=null)
                    {
                         addressfromlat = addresslist.get(0).getLatitude();
                         addressfromlang= addresslist.get(0).getLongitude();




                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    addresslist = geocoder2.getFromLocationName(to, 1);
                    if(addresslist!=null)
                    {
                        addresstolat = addresslist.get(0).getLatitude();
                        addresstolang= addresslist.get(0).getLongitude();




                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Location startPoint=new Location("locationA");
                startPoint.setLatitude(addressfromlat);
                startPoint.setLongitude(addressfromlang);
                //
                Location endPoint=new Location("locationB");
                endPoint.setLatitude(addresstolat);
                endPoint.setLongitude(addresstolang);

                 distance=startPoint.distanceTo(endPoint);
                 distance = distance/1000;

                Toast toast = Toast.makeText(getApplicationContext(), "Dystans w linii prostejk:"+String.valueOf(distance), Toast.LENGTH_SHORT);
                toast.show();

                LatLng origin = new LatLng(addressfromlat, addressfromlang);
                LatLng destination = new LatLng(addresstolat, addresstolang);

                // Rysowanie trasy między punktem początkowym a końcowym
                txtkm.setText("");
                mGoogleMap.clear();
                drawRoute(origin, destination);


            }
        });


        floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkifgpson()==true) {
                    getCurrLoc();
                }
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

    private void drawRoute(LatLng origin, LatLng destination) {
        geoApiContext = new GeoApiContext.Builder()
                .apiKey("AIzaSyC9ityMjHyHQXh0VPj0EmR0-LblJrTKR1o")
                .build();

        DirectionsResult directionsResult = DirectionsApi.newRequest(geoApiContext)
                .mode(TravelMode.DRIVING)
                .origin(new com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                .destination(new com.google.maps.model.LatLng(destination.latitude, destination.longitude))
                .awaitIgnoreError(); // Oczekiwanie na wynik z Directions API

        if (directionsResult != null) {
            long distanceInMeters = directionsResult.routes[0].legs[0].distance.inMeters;

            double distanceInKilometers = distanceInMeters / 1000.0;

            Toast.makeText(getApplicationContext(), "Długość trasy: " + String.valueOf(distanceInKilometers) + " km", Toast.LENGTH_SHORT).show();
            txtkm.setText("Dystans: \n" + distanceInKilometers + " km");
            // Tworzenie linii trasy na mapie
            List<com.google.maps.model.LatLng> decodedPath = directionsResult.routes[0].overviewPolyline.decodePath();
            PolylineOptions polylineOptions = new PolylineOptions();
            for (com.google.maps.model.LatLng point : decodedPath) {
                LatLng latLng = new LatLng(point.lat, point.lng);
                polylineOptions.add(latLng);
            }
            polylineOptions.width(15).color(Color.BLUE);

            Polyline polyline = mGoogleMap.addPolyline(polylineOptions);

            // Przesunięcie kamery na środek trasy
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(origin);
            builder.include(destination);
            LatLngBounds bounds = builder.build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 50);
            mGoogleMap.animateCamera(cameraUpdate);
        }
    }


    @SuppressLint("MissingPermission")
    private void getCurrLoc() {
        EditText ed = (EditText)findViewById(R.id.editTextTextPersonName);
        mLocationClient.getLastLocation().addOnCompleteListener(task ->{
            if(task.isSuccessful())
            {
                Location loc = task.getResult();
                address = getAddressFrom(loc.getLatitude(), loc.getLongitude());
                gotolocation(loc.getLatitude(), loc.getLongitude());
                ed.setText(address);
            }
        });

    }

    private void gotolocation(double latitude, double longitude) {
        LatLng latlng = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, 17);
        mGoogleMap.moveCamera(cameraUpdate);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //mGoogleMap.addMarker(new MarkerOptions().position(latlng).title(""+ "" + address));

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
        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);

        //mGoogleMap.setTrafficEnabled(true);
        mLocationClient.getLastLocation().addOnSuccessListener(this, location ->{

           if(location != null)
           {
               double latitude = location.getLatitude();
               double longtitude = location.getLongitude();
               float zoom = 17.0f;
               LatLng markerPosition = new LatLng(latitude, longtitude);
               mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, zoom));
           }


        });


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

    @Override
    public void onLocationChanged(@NonNull Location location) {

     //  getCurrLoc();


//                double latitude = location.getLatitude();
//                double longtitude = location.getLongitude();
//                float zoom = 17.0f;
//                LatLng markerPosition = new LatLng(latitude, longtitude);
//                mGoogleMap.clear();
//                gotolocation(latitude, longtitude);
//                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, zoom));





    }
}
