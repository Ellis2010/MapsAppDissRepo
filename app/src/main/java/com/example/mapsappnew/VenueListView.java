package com.example.mapsappnew;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class VenueListView extends AppCompatActivity {

   public  ListAdapter listAdapter;
    public static ArrayList<ListData> listDataArrayList;
    public ListData listData;
    public ListData listData2;

    public int venueInt;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_view_layout);

        ListView listView = new ListView(this);
        listView = findViewById(R.id.listView);

        // Initialize string arrays
        String[] venueNames;
        Resources res = getResources();
        venueNames = res.getStringArray(R.array.VenueNames);

        String[] venueAddresses;
        venueAddresses = res.getStringArray(R.array.VenueAddressesShort);

        String[] venueDistances;
        venueDistances = getIntent().getStringArrayExtra("venueDistances");

        String[] venueStatus;
        venueStatus = getIntent().getStringArrayExtra("venueStatus");

        SharedPreferences prefs = getSharedPreferences("checkboxPrefs", MODE_PRIVATE);
        boolean checked = prefs.getBoolean("checkbox1", true);

        listDataArrayList = new ArrayList<>();

        for(int i = 0; i < venueNames.length; i++) {
            listData = new ListData(venueNames[i], venueAddresses[i], venueDistances[i], venueStatus[i]);
            listDataArrayList.add(listData);
        }

        ImageView backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(v -> {
            // Go back to map activity
           onBackPressed();
        });


        listAdapter = new ListAdapter(this, listDataArrayList);
        listView.setAdapter(listAdapter);
        listView.setClickable(true);

        listView.setOnItemClickListener((parent, view, position, id) -> {

            Intent venueInfo = new Intent(VenueListView.this, VenueInfo.class);
            // Get the venueInt from the map activity



            if(position == 0) {
                venueInfo.putExtra("venueInt", 0);

                startActivity(venueInfo);
            }

            else if(position == 1) {

                venueInfo.putExtra("venueInt", 1);

                startActivity(venueInfo);
            }

            if (position == 2) {
                venueInfo.putExtra("venueInt", 2);

                startActivity(venueInfo);
            }

            if (position == 3) {
                venueInfo.putExtra("venueInt", 3);

                startActivity(venueInfo);
            }

            if (position == 4) {
                venueInfo.putExtra("venueInt", 4);

                startActivity(venueInfo);
            }

            if (position == 5) {
                venueInfo.putExtra("venueInt", 5);

                startActivity(venueInfo);
            }

            if (position == 6) {
                venueInfo.putExtra("venueInt", 6);

                startActivity(venueInfo);
            }

        });


    }







}
