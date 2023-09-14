import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.gettinglocationapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ShowMap2 extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    boolean isPermissionGranted;
    FloatingActionButton floatingbtn;
    public LocationManager loc;
    public double lang;
    public double lat;
    GoogleMap mGoogleMap;

    private FusedLocationProviderClient mLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_map);
        checkMyPermission();
        zaladujMape();

        loc = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                double width = Double.parseDouble(String.valueOf(location.getLatitude()));
                double height = Double.parseDouble(String.valueOf(location.getLongitude()));
                lat = width;
                lang = height;
            }

        };

        mLocationClient = LocationServices.getFusedLocationProviderClient(this);


        floatingbtn = (FloatingActionButton) findViewById(R.id.floatingActionButton);


        floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                loc.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) listener);

                // IdzDoLokacji(49.0002, 54.020);
                PobierzLokalizacje();
                 IdzDoLokacji(49.0002, 54.020);

            }


        });


    }

    //@SuppressLint("MissingPermission")
    private void zaladujMape() {
//https://stackoverflow.com/questions/34582370/how-can-i-show-current-location-on-a-google-map-on-android-marshmallow
        if (isPermissionGranted) {
            SupportMapFragment suppMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            suppMapFragment.getMapAsync(this);
        }


    }

    private void checkMyPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            return;
        }
//        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
//            return;  /////pobieranie permisji (komunikat czy dajesz permisje aplikacji)
//        }
    }

    public void list() {

        LocationListener listener = new LocationListener() {

            @Override
            public void onLocationChanged(@NonNull Location location) {

                String width = String.valueOf(location.getLatitude());
                String height = String.valueOf(location.getLongitude());
                double he = Double.parseDouble(height);
                double wi = Double.parseDouble(width);
                lang = wi;
                lat = he;

                IdzDoLokacji(he, wi);


            }

        };
    }

    @SuppressLint("MissingPermission")
    private void PobierzLokalizacje() {

//        mLocationClient.getLastLocation().addOnCompleteListener(this, location ->
//        {
//            if(location != null)
//            {
//
//            }
//
//
//        });


    }

    @SuppressLint("MissingPermission")
    private void IdzDoLokacji(double latitude, double longitude) {

        LatLng Latlng = new LatLng(52.095713, 17.728129);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(Latlng, 17);
        mGoogleMap.moveCamera(cameraUpdate);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


    }

    //    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = (GoogleMap) googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
       // mGoogleMap.setMyLocationEnabled(true);
        //list();
       // IdzDoLokacji(lat, lang);
        IdzDoLokacji(10.00, 10.000);
        LatLng Latlng = new LatLng(52.095713, 17.728129);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(Latlng, 17);
        mGoogleMap.moveCamera(cameraUpdate);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
        IdzDoLokacji(49.0002, 54.020);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
