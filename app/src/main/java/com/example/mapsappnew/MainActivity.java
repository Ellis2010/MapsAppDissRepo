package com.example.mapsappnew;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity"; // A constant string used for logging
    private static final int ERROR_DIALOG_REQUEST = 9001; // A constant used for identifying the error dialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE); // Retrieves the shared preferences
        boolean isFirstLaunch = prefs.getBoolean("isFirstLaunch", true); // Retrieves the boolean value of isFirstLaunch from the shared preferences

        if (isFirstLaunch) { // Checks whether the app is launched for the first time
            prefs.edit().putBoolean("isFirstLaunch", false).apply(); // Sets the value of isFirstLaunch to false in the shared preferences
        }

        if (isFirstLaunch) { // Checks whether the app is launched for the first time
            if (savedInstanceState == null) { // Checks whether there is a saved instance state
                getSupportFragmentManager() // Gets the support fragment manager
                        .beginTransaction()
                        .add(R.id.container, new PagerFragment()) // Adds the PagerFragment to the container
                        .commit();
            }
        } else { // If the app is not launched for the first time
            Intent intent = new Intent(MainActivity.this, MapActivity.class); // Creates a new intent to start the MapActivity
            startActivity(intent); // Starts the MapActivity
        }

        if (isServicesOk()) { // Checks whether Google Play Services is available on the device
            setContentView(R.layout.activity_main);
        }
    }

    // Checks to see if the device has the latest version of Google Play Services
    public boolean isServicesOk() {
        Log.d(TAG, "isServicesOK: checking google services version"); // Logs a message

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this); // Checks whether Google Play Services is available on the device

        if (available == ConnectionResult.SUCCESS) { // If Google Play Services is available
            // Everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working"); // Logs a message
            return true; // Returns true
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            // If there is an error that can be resolved
            Log.d(TAG, "isServicesOK: an error occurred but it can be fixed"); // Logs a message
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST); // Gets the error dialog
            assert dialog != null;
            dialog.show(); // Shows the error dialog
        } else { // If there is an error that cannot be resolved
            Toast.makeText(this, "Unable to make map requests", Toast.LENGTH_SHORT).show(); // Displays a toast message
        }
        return true; // Returns true
    }


}