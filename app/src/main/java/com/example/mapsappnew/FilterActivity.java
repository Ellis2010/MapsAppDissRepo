package com.example.mapsappnew;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Arrays;


public class FilterActivity extends Dialog {

    int[] wifiSpeeds;
    public MapActivity mapActivity;
    public FilterActivity(Context context) {
        super(context);
        setContentView(R.layout.filter_layout);
        // add your code here
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mapActivity = new MapActivity();

        Resources res = getContext().getResources();

        wifiSpeeds = res.getIntArray(R.array.WifiSpeeds);

        Toast.makeText(getContext(), Arrays.toString(wifiSpeeds), Toast.LENGTH_SHORT).show();

        // Set the dialog dimensions
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.TOP;
        lp.y = 165;
        getWindow().setAttributes(lp);

        setContentView(R.layout.filter_layout);

        // Venue type checkboxes
        CheckBox cafeCheckBox = findViewById(R.id.cafeCheckbox);
        CheckBox fastFoodCheckBox = findViewById(R.id.fastFoodCheckbox);
        CheckBox pubsCheckBox = findViewById(R.id.pubsCheckbox);

        // Distance Checkboxes
        CheckBox lt5mi = findViewById(R.id.lt5);
        CheckBox lt10mi = findViewById(R.id.lt10);

        //Speed Checkboxes
        CheckBox slowCheckBox = findViewById(R.id.slowCheckbox);
        CheckBox mediumCheckBox = findViewById(R.id.mediumCheckbox);
        CheckBox fastCheckBox = findViewById(R.id.fastCheckbox);

        // Open/Closed Checkboxes
        CheckBox openCheckBox = findViewById(R.id.openCheckbox);
        CheckBox closedCheckBox = findViewById(R.id.closedCheckbox);



        Button applyButton = findViewById(R.id.button_apply);



        SharedPreferences prefs = getContext().getSharedPreferences("checkboxPrefs", MODE_PRIVATE);

        // Set the click listener for the apply button
        applyButton.setOnClickListener(v -> {

            if (!cafeCheckBox.isChecked() && MapActivity.mapActivity != null) {
                MapActivity.mapActivity.hideCafes();
            } else {
                assert MapActivity.mapActivity != null;
                MapActivity.mapActivity.showCafes();
            }


            if (!fastFoodCheckBox.isChecked() && MapActivity.mapActivity != null) {
                MapActivity.mapActivity.hideFastFood();
            } else {
                assert MapActivity.mapActivity != null;
                MapActivity.mapActivity.showFastFood();
            }


            if (!pubsCheckBox.isChecked() && MapActivity.mapActivity != null) {
                MapActivity.mapActivity.hidePubs();
            } else {
                assert MapActivity.mapActivity != null;
                MapActivity.mapActivity.showPubs();
            }

            if (lt5mi.isChecked() && MapActivity.mapActivity != null) {
                MapActivity.mapActivity.hidegt5Markers();
            } else {
                assert MapActivity.mapActivity != null;
                MapActivity.mapActivity.showgt5Markers();
            }

            if (lt10mi.isChecked() && MapActivity.mapActivity != null) {
                MapActivity.mapActivity.hidegt10Markers();
            } else {
                assert MapActivity.mapActivity != null;
                MapActivity.mapActivity.showgt10Markers();
            }

            if (slowCheckBox.isChecked() && MapActivity.mapActivity != null) {
                MapActivity.mapActivity.showSlowMarkers();
            } else {
                assert MapActivity.mapActivity != null;
                MapActivity.mapActivity.hideSlowMarkers();
            }

            if (mediumCheckBox.isChecked() && MapActivity.mapActivity != null) {
                MapActivity.mapActivity.showMediumMarkers();
            } else {
                assert MapActivity.mapActivity != null;
                MapActivity.mapActivity.hideMediumMarkers();
            }

            if (fastCheckBox.isChecked() && MapActivity.mapActivity != null) {
                MapActivity.mapActivity.showFastMarkers();
            } else {
                assert MapActivity.mapActivity != null;
                MapActivity.mapActivity.hideFastMarkers();
            }

            /*if (openCheckBox.isChecked() && MapActivity.mapActivity != null) {
                MapActivity.mapActivity.closed = false;
            } else {
                assert MapActivity.mapActivity != null;
                MapActivity.mapActivity.closed = true;
            }*/

           /* if (closedCheckBox.isChecked() && MapActivity.mapActivity != null) {
                MapActivity.mapActivity.closed = false;
            } else {
                assert MapActivity.mapActivity != null;
                MapActivity.mapActivity.closed = true;
            }*/

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("cafeCheckBox", cafeCheckBox.isChecked());
            editor.putBoolean("fastFoodCheckBox", fastFoodCheckBox.isChecked());
            editor.putBoolean("pubsCheckBox", pubsCheckBox.isChecked());
            editor.putBoolean("lt5mi", lt5mi.isChecked());
            editor.putBoolean("lt10mi", lt10mi.isChecked());
            editor.putBoolean("slowCheckBox", slowCheckBox.isChecked());
            editor.putBoolean("mediumCheckBox", mediumCheckBox.isChecked());
            editor.putBoolean("fastCheckBox", fastCheckBox.isChecked());
            editor.putBoolean("openCheckBox", openCheckBox.isChecked());
            editor.putBoolean("closedCheckBox", closedCheckBox.isChecked());
            editor.apply();
            dismiss(); // Close the dialog
        });

        // Set the initial checkbox status based on SharedPreferences
        cafeCheckBox.setChecked(prefs.getBoolean("cafeCheckBox", true));
        fastFoodCheckBox.setChecked(prefs.getBoolean("fastFoodCheckBox", true));
        pubsCheckBox.setChecked(prefs.getBoolean("pubsCheckBox", true));
        lt5mi.setChecked(prefs.getBoolean("lt5mi", false));
        lt10mi.setChecked(prefs.getBoolean("lt10mi", false));
        slowCheckBox.setChecked(prefs.getBoolean("slowCheckBox", true));
        mediumCheckBox.setChecked(prefs.getBoolean("mediumCheckBox", true));
        fastCheckBox.setChecked(prefs.getBoolean("fastCheckBox", true));
        openCheckBox.setChecked(prefs.getBoolean("openCheckBox", true));
        closedCheckBox.setChecked(prefs.getBoolean("closedCheckBox", true));

        // Set the width of the checkboxes container to MATCH_PARENT
        LinearLayout checkboxesContainer = findViewById(R.id.checkboxes_container);
        ViewGroup.LayoutParams layoutParams = checkboxesContainer.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        checkboxesContainer.setLayoutParams(layoutParams);
    }


}





