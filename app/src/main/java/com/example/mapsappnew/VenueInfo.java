package com.example.mapsappnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class VenueInfo extends AppCompatActivity {

    public int venueInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venueinfo);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Initialize string arrays
        String[] Names;
        String[] Addresses;
        String[] OpenTimes;
        String[] PhoneNumbers;
        String[] WifiSpeeds;
        String[] Websites;

        // Initialize resources class
        Resources res = getResources();

        // Assign string arrays to the string arrays in the string.xml file
        Names = res.getStringArray(R.array.VenueNames);
        Addresses = res.getStringArray(R.array.VenueAddresses);
        OpenTimes = res.getStringArray(R.array.VenueOpenTimes);
        PhoneNumbers = res.getStringArray(R.array.VenuePhoneNumbers);
        WifiSpeeds = res.getStringArray(R.array.VenueWifiSpeeds);
        Websites = res.getStringArray(R.array.VenueWebsites);

        // Initialize text views from layout file
        TextView Name = (TextView)findViewById(R.id.titleText);
        TextView Address = (TextView)findViewById(R.id.AddressText);
        TextView OpenTime = (TextView)findViewById(R.id.OpenTimesText);
        TextView PhoneNumber = (TextView)findViewById(R.id.editTextNumber);
        TextView WifiSpeed = (TextView)findViewById(R.id.WifiSpeedText);

        // Get the intent from the map activity
        Intent i = getIntent();

        // Get the venueInt from the map activity
        venueInt = i.getIntExtra("venueInt", 0);

        // Text view values for Costa The Moor
        if (venueInt == 0) {
            Name.setText(Names[0]);
            Address.setText(Addresses[0]);
            OpenTime.setText(OpenTimes[0]);
            PhoneNumber.setText(PhoneNumbers[0]);
            WifiSpeed.setText(WifiSpeeds[0]);

        }
        // Text view values for Starbucks Fargate
        if (venueInt == 1) {
            Name.setText(Names[1]);
            Address.setText(Addresses[1]);
            OpenTime.setText(OpenTimes[1]);
            PhoneNumber.setText(PhoneNumbers[1]);
            WifiSpeed.setText(WifiSpeeds[1]);
        }


        // Take user to website after clicking on the website icon
        ImageView img = (ImageView)findViewById(R.id.costaMoreWebsiteIcon);
        img.setOnClickListener(new View.OnClickListener() {
                                   public void onClick(View v) {
                                       Intent intent = new Intent();
                                       intent.setAction(Intent.ACTION_VIEW);
                                       intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                       intent.setData(Uri.parse(Websites[venueInt]));
                                       startActivity(intent);
                                   }
                               });

        super.onStart();
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








