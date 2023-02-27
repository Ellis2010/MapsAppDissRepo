package com.example.mapsappnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CostaMoor extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costamoor);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Take user to website after clicking on the website icon
        ImageView img = (ImageView)findViewById(R.id.costaMoreWebsiteIcon);
        img.setOnClickListener(new View.OnClickListener() {
                                   public void onClick(View v) {
                                       Intent intent = new Intent();
                                       intent.setAction(Intent.ACTION_VIEW);
                                       intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                       intent.setData(Uri.parse("https://www.costa.co.uk/locations/store-locator/map?latitude=53.37653&longitude=-1.4729700000000037&open=1"));
                                       startActivity(intent);
                                   }
                               });

        // Display map fragment at the bottom of the page
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                LatLng loc = new LatLng (53.376726165542095, -1.4729627899877071);
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








