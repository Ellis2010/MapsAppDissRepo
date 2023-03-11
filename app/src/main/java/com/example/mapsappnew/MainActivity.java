package com.example.mapsappnew;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    private boolean isFirstLaunch;
    private static final int ERROR_DIALOG_REQUEST = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        isFirstLaunch = prefs.getBoolean("isFirstLaunch", true);
        if (isFirstLaunch) {
            prefs.edit().putBoolean("isFirstLaunch", false).apply();
        }

        if (isFirstLaunch) {
            if (savedInstanceState == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, new PagerFragment())
                        .commit();
            }
        } else
        {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        }

        //if Google Play Services are authenticated proceed to initialization
       // if(isServicesOk()) {
         //   initialize();
        //}
    }



    private void initialize() {
       // Button btnMap = findViewById(R.id.btnMap);
       // btnMap.setOnClickListener(new View.OnClickListener() {
         //   @Override
           // public void onClick(View view) {
              //  Intent intent = new Intent(MainActivity.this, MapActivity.class);
              //  startActivity(intent);
        //    }
       // });



    }




    //Check to see if device has latest version of google play services
    public boolean isServicesOk(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything if fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error is present but it can be resolved
            Log.d(TAG, "isServicesOK: an error occurred but it can be fixed");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else {
            Toast.makeText(this, "Unable to make map requests", Toast.LENGTH_SHORT).show();
        }
        return true;

        }
    }


