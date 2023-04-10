package com.example.mapsappnew;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mapsappnew.product.MainReviewActivity;
import com.example.mapsappnew.reviews.ReviewActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

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


        // Initialize resources class
        Resources res = getResources();

        String[] Names;
        String[] Addresses;
        String[] OpenTimes;
        String[] PhoneNumbers;
        String[] WifiSpeeds;
        String[] Websites;

        // Assign string arrays to the string arrays in the string.xml file
        Names = res.getStringArray(R.array.VenueNames);
        Addresses = res.getStringArray(R.array.VenueAddresses);
        OpenTimes = res.getStringArray(R.array.VenueOpenTimes);
        PhoneNumbers = res.getStringArray(R.array.VenuePhoneNumbers);
        WifiSpeeds = res.getStringArray(R.array.VenueWifiSpeeds);
        Websites = res.getStringArray(R.array.VenueWebsites);

        // Initialize text views from layout file
        TextView Name = findViewById(R.id.titleText);
        TextView Address = findViewById(R.id.AddressText);
        TextView OpenTime = findViewById(R.id.OpenTimesText);
        TextView PhoneNumber = findViewById(R.id.editTextNumber);
        TextView WifiSpeed = findViewById(R.id.WifiSpeedText);


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

        // Text view values for McDonald's Farm Road
        if (venueInt == 2) {
            Name.setText(Names[2]);
            Address.setText(Addresses[2]);
            OpenTime.setText(OpenTimes[2]);
            PhoneNumber.setText(PhoneNumbers[2]);
            WifiSpeed.setText(WifiSpeeds[2]);
        }

        // Text view values for McDonald's High Street
        if (venueInt == 3) {
            Name.setText(Names[3]);
            Address.setText(Addresses[3]);
            OpenTime.setText(OpenTimes[3]);
            PhoneNumber.setText(PhoneNumbers[3]);
            WifiSpeed.setText(WifiSpeeds[3]);
        }

        // Text view values for KFC Queen's Road
        if (venueInt == 4) {
            Name.setText(Names[4]);
            Address.setText(Addresses[4]);
            OpenTime.setText(OpenTimes[4]);
            PhoneNumber.setText(PhoneNumbers[4]);
            WifiSpeed.setText(WifiSpeeds[4]);
        }

        // Text view values for Jiangtea
        if (venueInt == 5) {
            Name.setText(Names[5]);
            Address.setText(Addresses[5]);
            OpenTime.setText(OpenTimes[5]);
            PhoneNumber.setText(PhoneNumbers[5]);
            WifiSpeed.setText(WifiSpeeds[5]);
        }

        // Text view values for Wetherspoons Benjamin Huntsman
        if (venueInt == 6) {
            Name.setText(Names[6]);
            Address.setText(Addresses[6]);
            OpenTime.setText(OpenTimes[6]);
            PhoneNumber.setText(PhoneNumbers[6]);
            WifiSpeed.setText(WifiSpeeds[6]);
        }



        // Take user to website after clicking on the website icon
        ImageView img = findViewById(R.id.costaMoreWebsiteIcon);
        img.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(Websites[venueInt]));
            startActivity(intent);
        });



        super.onStart();
        // Display map fragment at the bottom of the page
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        assert mapFragment != null;
        mapFragment.getMapAsync(googleMap -> {

            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(mapFragment.requireActivity(), R.raw.night));

            LatLng[] locations = {
                    new LatLng (53.376726165542095, -1.4729627899877071), // Costa Coffee, The Moor
                    new LatLng(53.38253356205727, -1.4687213439736098), // Starbucks, Fargate
                    new LatLng (53.37515170930672, -1.4624034194698188), // McDonald's, Farm Road
                    new LatLng(53.383343604445024, -1.4680682444491735), // McDonald's, High Street
                    new LatLng (53.37007962311809, -1.4654074968813695), // KFC, Queens Road
                    new LatLng(53.384095821075555, -1.474506607259936), // JIANGTEA
                    new LatLng(53.38011517908938, -1.472511044137303), // Wetherspoons, Benjamin Huntsman
            };

            if(venueInt == 0) {
                LatLng loc = locations[0];
                Marker marker = googleMap.addMarker(new MarkerOptions().position(loc));
                assert marker != null;
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
            }

            if(venueInt == 1) {
                LatLng loc = locations[1];
                Marker marker = googleMap.addMarker(new MarkerOptions().position(loc));
                assert marker != null;
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
            }

            if(venueInt == 2) {
                LatLng loc = locations[2];
                Marker marker = googleMap.addMarker(new MarkerOptions().position(loc));
                assert marker != null;
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
            }

            if(venueInt == 3) {
                LatLng loc = locations[3];
                Marker marker = googleMap.addMarker(new MarkerOptions().position(loc));
                assert marker != null;
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
            }

            if(venueInt == 4) {
                LatLng loc = locations[4];
                Marker marker = googleMap.addMarker(new MarkerOptions().position(loc));
                assert marker != null;
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
            }

            if(venueInt == 5) {
                LatLng loc = locations[5];
                Marker marker = googleMap.addMarker(new MarkerOptions().position(loc));
                assert marker != null;
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
            }

            if(venueInt == 6) {
                LatLng loc = locations[6];
                Marker marker = googleMap.addMarker(new MarkerOptions().position(loc));
                assert marker != null;
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
            }

            UiSettings uiSettings = googleMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(false);
            uiSettings.setAllGesturesEnabled(false);


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








