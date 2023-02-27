package com.example.mapsappnew;


import static com.google.android.libraries.places.api.Places.initialize;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private ImageView mGps;
    //Vars
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(@NonNull Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(@NonNull Marker marker) {

                LinearLayout info = new LinearLayout(MapActivity.this);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(MapActivity.this);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(MapActivity.this);
                snippet.setTextColor(Color.GRAY);
                snippet.setGravity(Gravity.CENTER);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
        //Array of locations for markers
        LatLng[] locations = {
                new LatLng (53.376726165542095, -1.4729627899877071), // Costa Coffee, The Moor
                new LatLng(53.38253356205727, -1.4687213439736098), // Starbucks, Fargate
        };

        //Add markers to map
        Marker costaTheMoor = mMap.addMarker(new MarkerOptions().position(locations[0]).title("Costa Coffee, The Moor").snippet("Click to expand"));
        assert costaTheMoor != null;
        costaTheMoor.setTag("cMoor");

        Marker starbucksFargate = mMap.addMarker(new MarkerOptions().position(locations[1]).title("Starbucks, Fargate").snippet("Click to expand"));
        assert starbucksFargate != null;
        starbucksFargate.setTag("sFargate");


        if (mLocationPermissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            init();
        }

        googleMap.setOnInfoWindowClickListener(markers -> {
            // Create an Intent to start the InfoActivity
            Intent cMoor = new Intent(MapActivity.this, CostaMoor.class);
            Intent sFargate = new Intent(MapActivity.this, StarbucksFargate.class);

            if (Objects.equals(markers.getTag(), "cMoor")) {
                // Start the InfoActivity
                startActivity(cMoor);
            }

            if (Objects.equals(markers.getTag(), "sFargate")) {
                // Start the InfoActivity
                startActivity(sFargate);
            }

        });

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mGps = (ImageView) findViewById(R.id.ic_gps);

        getLocationPermission();


    }














    private void init() {


        Log.d(TAG, "init: initializing");

        initialize(getApplicationContext(), "AIzaSyBxLGtdGOuY0Tb14XiWqzDOdcfYzRe38UI");

        mGps.setOnClickListener(view -> {
            Log.d(TAG, "onClick: clicked gps icon");

           getDeviceLocation();

        });


        HideKeyboard();
    }


    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices location");

        FusedLocationProviderClient mLocationProvider = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionGranted) {

                Task<Location> location = mLocationProvider.getLastLocation();
                location.addOnCompleteListener((OnCompleteListener<Location>) task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: found device location");
                        Location currentLocation = (Location) task.getResult();

                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())
                        );


                    } else {
                        Log.d(TAG, "onComplete: unable to find device location");
                        Toast.makeText(MapActivity.this, "Unable to get device location", Toast.LENGTH_SHORT).show();
                    }

                });

            }

        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException:  " + e.getMessage());

        }


    }


    private void moveCamera(LatLng latLng) {
        Log.d(TAG, "moveCamera: moving camera to: Lat: " + latLng.latitude + ". Lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, MapActivity.DEFAULT_ZOOM));

        if (!"My Location".equals("My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title("My Location");
            mMap.addMarker(options);
        }
        HideKeyboard();
    }


    private void InitializeMap() {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        assert mapFragment != null;
        mapFragment.getMapAsync(MapActivity.this);
    }


    // Get location permissions
    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                InitializeMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionResult: called");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        mLocationPermissionGranted = false;
                        Log.d(TAG, "onRequestPermissionResult: permission failed");
                        return;
                    }
                }
                Log.d(TAG, "onRequestPermissionResult: permission granted");
                mLocationPermissionGranted = true;

                //Initialize the map
                InitializeMap();
            }
        }
    }

    private void HideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


    }

}



