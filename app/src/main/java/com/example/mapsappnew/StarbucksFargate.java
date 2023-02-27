package com.example.mapsappnew;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class StarbucksFargate extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starbucksfargate);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                      LatLng loc = new LatLng(53.38253356205727, -1.4687213439736098);



                Marker marker = googleMap.addMarker(new MarkerOptions().position(loc));

                assert marker != null;
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
                UiSettings uiSettings = googleMap.getUiSettings();
                uiSettings.setZoomControlsEnabled(false);
                uiSettings.setAllGesturesEnabled(false);

            }
        });


        // Animate the activity appearing from the side
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }



    @Override
    protected void onPause() {
        super.onPause();

        // Animate the activity sliding out to the left when the back action is triggered
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }




}








