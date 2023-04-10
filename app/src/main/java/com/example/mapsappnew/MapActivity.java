package com.example.mapsappnew;


import static com.google.android.libraries.places.api.Places.initialize;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.mapsappnew.product.MainReviewActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Objects;
import java.util.Vector;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MapActivity";
    //////////////////////////// Location Vars /////////////////////////////////////////////////////
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionGranted = false;
    Location currentLocation;
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////// Map Vars /////////////////////////////////////////////
    private GoogleMap mMap;
    public Marker[] cafes = new Marker[3];
    public Marker[] fastFood = new Marker[3];
    public Marker[] pubs = new Marker[1];

    public Marker[] allMarkers = new Marker[7];

    public Marker[] gt5 = new Marker[2];

    public Marker[] gt10 = new Marker[2];
    private static final float DEFAULT_ZOOM = 15f;

    public Vector<Marker> lt5 = new Vector<>();

    String[] Status;
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // Reference to this class class
    public static MapActivity mapActivity;

    // Reference to the drawer layout
    DrawerLayout drawer;

    public Float distanceInMiles;

    private AppBarLayout appBarLayout;

    private AppBarLayout appBarLayout2;

    int[] WifiInts;

    public boolean closed = false;



    //////////////////////////// OnCreate Function /////////////////////////////////////////////////
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        // Set context for mapActivity reference
        mapActivity = this;

        // calls the onCreate() method of the parent class of the current activity. This is required to ensure that the activity is properly initialized.
        super.onCreate(savedInstanceState);

        // Set the layout for the activity
        setContentView(R.layout.activity_map);

        // Find the views by their IDs
        appBarLayout = findViewById(R.id.appBarLayout);
        appBarLayout2 = findViewById(R.id.appBarLayout2);
        ImageView searchIcon = findViewById(R.id.imageView);
        findViewById(R.id.filter);
        ImageView backArrow = findViewById(R.id.backArrow);


        searchIcon.setOnClickListener(view -> {
            appBarLayout.setVisibility(View.GONE);
            appBarLayout2.setVisibility(View.VISIBLE);
        });

        backArrow.setOnClickListener(view -> {
            appBarLayout.setVisibility(View.VISIBLE);
            appBarLayout2.setVisibility(View.GONE);
        });

        CustomSearchView searchView = findViewById(R.id.searchView);

        String[] autocompleteData = {
                "Costa, The Moor",
                "Starbucks, Fargate",
                "McDonald's, Farm Road",
                "JiangTea",
                "Wetherspoons, Benjamin Huntsman",
                "KFC, Queens Road",
                "Mcdonald's, High Street"};

        // Set up the adapter with your autocomplete data
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, autocompleteData);
        searchView.updateAutocompleteData(adapter);

        searchView.getAutoCompleteTextView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Update autocomplete suggestions
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.equals("Costa, The Moor")) {
                    MoveCamera(cafes[0].getPosition());
                }
                if (query.equals("Starbucks, Fargate")) {
                    MoveCamera(cafes[1].getPosition());
                }
                if (query.equals("JiangTea")) {
                    MoveCamera(cafes[2].getPosition());
                }
                if (query.equals("McDonald's, Farm Road")) {
                    MoveCamera(fastFood[0].getPosition());
                }
                if (query.equals("KFC, Queens Road")) {
                    MoveCamera(fastFood[2].getPosition());
                }
                if (query.equals("Mcdonald's, High Street")) {
                    MoveCamera(fastFood[1].getPosition());
                }
                if (query.equals("Wetherspoons, Benjamin Huntsman")) {
                    MoveCamera(pubs[0].getPosition());
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Do something when the text has changed
                return false;
            }
        });


        // call the function to get the location permissions
        GetLocationPermission();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////// Initialize Function ///////////////////////////////////////////////
    private void init() {

        // Initialize the Application
        Log.d(TAG, "init: initializing");

        // Run the function to check the current time and set the open/closed status of the venues
        checkIfOpen();

        // Reference the gps icon
        ImageView mGps = findViewById(R.id.ic_gps);

        // Set the API key for the map
        initialize(getApplicationContext(), "AIzaSyBxLGtdGOuY0Tb14XiWqzDOdcfYzRe38UI");

        // Run the getDeviceLocation method when the gps icon is clicked
        mGps.setOnClickListener(view -> {
            Log.d(TAG, "onClick: clicked gps icon");

            // Call the getDeviceLocation method
            GetDeviceLocation();
        });
        // Hide the keyboard when the user starts the application
        HideKeyboard();

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////// Initialize Map Function ///////////////////////////////////////////
    private void InitializeMap() {

        Log.d(TAG, "initMap: initializing map");

        // Reference the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        // Assert that the map fragment is not null
        assert mapFragment != null;

        // Call the getMapAsync method
        mapFragment.getMapAsync(MapActivity.this);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////// OnMapReady Function ///////////////////////////////////////////////
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Toast.makeText(this, "Map Loaded", Toast.LENGTH_SHORT).show();

        // On app relaunch set the filter checkboxes back to default values
        SharedPreferences prefs = getSharedPreferences("checkboxPrefs", MODE_PRIVATE);
        prefs.edit().putBoolean("cafeCheckBox", true).apply();
        prefs.edit().putBoolean("fastFoodCheckBox", true).apply();
        prefs.edit().putBoolean("pubsCheckBox", true).apply();
        prefs.edit().putBoolean("lt5mi", false).apply();
        prefs.edit().putBoolean("lt10mi", false).apply();
        prefs.edit().putBoolean("slowCheckBox", true).apply();
        prefs.edit().putBoolean("mediumCheckBox", true).apply();
        prefs.edit().putBoolean("fastCheckBox", true).apply();



        // Set the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the drawer layout
        drawer = findViewById(R.id.drawer_layout);

        // Create a new ActionBarDrawerToggle and set the drawer listener
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_close, R.string.drawer_open);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Create a reference to the NavigationView, set the text style and set the listener
        NavigationView mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setItemTextAppearance(R.style.NavigationTheme);
        mNavigationView.setItemBackgroundResource(R.drawable.main_border_no_bevel);
        mNavigationView.setItemVerticalPadding(1);
        mNavigationView.setNavigationItemSelectedListener(this);

        // Set the filter Image
        ImageView filter = findViewById(R.id.filter);

        // Set the context for the filter activity
        Context context = this;

        // Set the onClickListener for the filter Image
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create a new instance of the filter activity
                FilterActivity filterView = new FilterActivity(context);

                // Show the filter activity
                filterView.show();
            }
        });

        // Set the map
        mMap = googleMap;

        // Set the map style
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.night));

        // Set the map type
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(@NonNull Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @SuppressLint("SetTextI18n")
            @Override
            public View getInfoContents(@NonNull Marker marker) {

                // Create a new int array to set the WiFi speed depending on the marker


                checkIfOpen();

                // Set WiFi ints to the WifiSpeeds array
                Resources res = getResources();
                WifiInts = res.getIntArray(R.array.WifiSpeeds);


                // Start at 0
                int i = 0;

                // Check which marker is clicked and set the array index to the correct value
                if (Objects.equals(marker.getTag(), "cMoor")) {
                    i = 0;
                } else if (Objects.equals(marker.getTag(), "sFargate")) {
                    i = 1;
                } else if (Objects.equals(marker.getTag(), "mFarmRoad")) {
                    i = 2;
                } else if (Objects.equals(marker.getTag(), "mHighStreet")) {
                    i = 3;
                } else if (Objects.equals(marker.getTag(), "kQueensRoad")) {
                    i = 4;
                } else if (Objects.equals(marker.getTag(), "JiangTea")) {
                    i = 5;
                } else if (Objects.equals(marker.getTag(), "wBenjaminHuntsman")) {
                    i = 6;
                }

                // Create a new LinearLayout
                LinearLayout info = new LinearLayout(MapActivity.this);
                info.setOrientation(LinearLayout.VERTICAL);

                // Create a new TextView for the title, snippet and WiFi speed
                TextView title = new TextView(MapActivity.this);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                // Create a new TextView for the snippet
                TextView snippet = new TextView(MapActivity.this);
                snippet.setTextColor(Color.GRAY);
                snippet.setGravity(Gravity.CENTER);
                snippet.setText(marker.getSnippet());

                // Create a new TextView for the WiFi speed
                TextView WifiSpeed = new TextView(MapActivity.this);
                WifiSpeed.setTextColor(Color.GREEN);
                WifiSpeed.setText(WifiInts[i] + " Mbps");
                WifiSpeed.setGravity(Gravity.CENTER);

                // Create a new TextView for the status
                TextView status = new TextView(MapActivity.this);
                status.setTextColor(Color.GREEN);
                status.setText(Status[i]);
                status.setGravity(Gravity.CENTER);

                // Set the colour of the status depending on whether the cafe is open or closed
                if (status.getText().equals("Closed"))
                    status.setTextColor(Color.RED);
                else
                    status.setTextColor(Color.GREEN);

                // Set the colour of the WiFi speed depending on the speed
                if (WifiInts[i] <= 20) {
                    WifiSpeed.setTextColor(Color.RED);
                }

                if(WifiInts[i] > 20) {
                    WifiSpeed.setTextColor(Color.parseColor("#FFFF9800"));
                }

                if(WifiInts[i] > 40) {
                    WifiSpeed.setTextColor(Color.GREEN);
                }


                // Add the TextViews to the LinearLayout
                info.addView(title);
                info.addView(snippet);
                info.addView(WifiSpeed);
                info.addView(status);

                return info;
            }
        });

        //Array of locations for markers
        LatLng[] locations = {
                //Main Markers
                new LatLng(53.376726165542095, -1.4729627899877071), // Costa Coffee, The Moor
                new LatLng(53.38253356205727, -1.4687213439736098), // Starbucks, Fargate
                new LatLng(53.37515170930672, -1.4624034194698188), // McDonald's, Farm Road
                new LatLng(53.383343604445024, -1.4680682444491735), // McDonald's, High Street
                new LatLng(53.37007962311809, -1.4654074968813695), // KFC, Queens Road
                new LatLng(53.384095821075555, -1.474506607259936), // JIANGTEA
                new LatLng(53.38011517908938, -1.472511044137303), // Wetherspoons, Benjamin Huntsman

                // >5mi Markers
                new LatLng(53.46511772445294, -1.4933177346655304),
                new LatLng(53.426577593990025, -1.368416045317529),

                // >10mi Markers
                new LatLng(53.68683758712618, -1.5097370922264681),
                new LatLng(53.69969850108208, -1.425970983566787),

        };

        //Array of venue names
        String[] venueNames;

        // Set the venue names to the VenueNames array
        Resources res = getResources();
        venueNames = res.getStringArray(R.array.VenueNames);

        // Set the prompt for the info window
        String Prompt = "Click to expand";

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Cafe List
        // Cafe 0 = Costa Coffee, The Moor
        // Cafe 1 = Starbucks, Fargate
        // Cafe 2 = JIANGTEA

        // Set the title, position, snippet and icon for each marker
        cafes[0] = mMap.addMarker(new MarkerOptions()
                .title(venueNames[0])
                .position(locations[0])
                .snippet(Prompt)
                .icon(BitmapDescriptorFromVector(getApplicationContext(), R.drawable.cafe_marker)));
        assert cafes[0] != null;
        cafes[0].setTag("cMoor");

        cafes[1] = mMap.addMarker(new MarkerOptions()
                .title(venueNames[1])
                .position(locations[1])
                .snippet(Prompt)
                .icon(BitmapDescriptorFromVector(getApplicationContext(), R.drawable.cafe_marker)));
        assert cafes[1] != null;
        cafes[1].setTag("sFargate");

        cafes[2] = mMap.addMarker(new MarkerOptions()
                .title(venueNames[5])
                .position(locations[5])
                .snippet(Prompt)
                .icon(BitmapDescriptorFromVector(getApplicationContext(), R.drawable.cafe_marker)));
        assert cafes[2] != null;
        cafes[2].setTag("JiangTea");
        ////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Fast Food List
        // Fast Food 0 = McDonald's, Farm Road
        // Fast Food 1 = McDonald's, High Street
        // Fast Food 2 = KFC, Queens Road

        // Set the title, position, snippet and icon for each marker
        fastFood[0] = mMap.addMarker(new MarkerOptions()
                .title(venueNames[2])
                .position(locations[2])
                .snippet(Prompt)
                .icon(BitmapDescriptorFromVector(getApplicationContext(), R.drawable.restauraunt_marker)));
        assert fastFood[0] != null;
        fastFood[0].setTag("mFarmRoad");

        fastFood[1] = mMap.addMarker(new MarkerOptions()
                .title(venueNames[3])
                .position(locations[3])
                .snippet(Prompt)
                .icon(BitmapDescriptorFromVector(getApplicationContext(), R.drawable.restauraunt_marker)));
        assert fastFood[1] != null;
        fastFood[1].setTag("mHighStreet");

        fastFood[2] = mMap.addMarker(new MarkerOptions()
                .title(venueNames[4])
                .position(locations[4])
                .snippet(Prompt)
                .icon(BitmapDescriptorFromVector(getApplicationContext(), R.drawable.restauraunt_marker)));
        assert fastFood[2] != null;
        fastFood[2].setTag("kQueensRoad");
        ////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Pub List
        // Pub 0 = Wetherspoons, Benjamin Huntsman

        // Set the title, position, snippet and icon for each marker
        pubs[0] = mMap.addMarker(new MarkerOptions()
                .title(venueNames[6])
                .position(locations[6])
                .snippet(Prompt)
                .icon(BitmapDescriptorFromVector(getApplicationContext(), R.drawable.cafe_marker)));
        assert pubs[0] != null;
        pubs[0].setTag("wBenjaminHuntsman");
        /////////////////////////////////////////////////////////////////////////////////////////////

        gt5[0] = mMap.addMarker(new MarkerOptions()
                .title(">5mi Marker1")
                .position(locations[7])
                .snippet(Prompt)
                .icon(BitmapDescriptorFromVector(getApplicationContext(), R.drawable.cafe_marker)));

        gt5[1] = mMap.addMarker(new MarkerOptions()
                .title(">5mi Marker2")
                .position(locations[8])
                .snippet(Prompt)
                .icon(BitmapDescriptorFromVector(getApplicationContext(), R.drawable.cafe_marker)));

        gt10[0] = mMap.addMarker(new MarkerOptions()
                .title(">10mi Marker1")
                .position(locations[9])
                .snippet(Prompt)
                .icon(BitmapDescriptorFromVector(getApplicationContext(), R.drawable.cafe_marker)));

        gt10[1] = mMap.addMarker(new MarkerOptions()
                .title(">10mi Marker2")
                .position(locations[10])
                .snippet(Prompt)
                .icon(BitmapDescriptorFromVector(getApplicationContext(), R.drawable.cafe_marker)));


        if (mLocationPermissionGranted) {
            GetDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            init();
        }

        // Change values within the "VenueInfo" activity depending on which marker is clicked
        googleMap.setOnInfoWindowClickListener(markers -> {

            // Create an Intent to start the VenueInfo Activity
            Intent venueInfo = new Intent(MapActivity.this, VenueInfo.class);

            // Check to see which Marker was clicked
            if (Objects.equals(markers.getTag(), "cMoor")) {
                // Pass the venueInt to the InfoActivity
                venueInfo.putExtra("venueInt", 0);
                // Start the InfoActivity
                startActivity(venueInfo);
            }

            if (Objects.equals(markers.getTag(), "sFargate")) {
                venueInfo.putExtra("venueInt", 1);
                startActivity(venueInfo);
            }

            if (Objects.equals(markers.getTag(), "mFarmRoad")) {
                venueInfo.putExtra("venueInt", 2);
                startActivity(venueInfo);
            }

            if (Objects.equals(markers.getTag(), "mHighStreet")) {
                venueInfo.putExtra("venueInt", 3);
                startActivity(venueInfo);
            }

            if (Objects.equals(markers.getTag(), "kQueensRoad")) {
                venueInfo.putExtra("venueInt", 4);
                startActivity(venueInfo);
            }

            if (Objects.equals(markers.getTag(), "JiangTea")) {
                venueInfo.putExtra("venueInt", 5);
                startActivity(venueInfo);
            }

            if (Objects.equals(markers.getTag(), "wBenjaminHuntsman")) {
                venueInfo.putExtra("venueInt", 6);
                startActivity(venueInfo);
            }
        });

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////// Check to see if venues are open/closed ////////////////////////////
    public void checkIfOpen() {

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        Resources res = getResources();
        Status = new String[7];
        Status = res.getStringArray(R.array.VenueStatus);

        // Venue open / closing times for weekdays
        if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (LocalTime.now().isBefore(LocalTime.of(18, 0)) && LocalTime.now().isAfter(LocalTime.of(6, 30))) {

                    Status[0] = "Open";
                } else {

                    Status[0] = "Closed";
                }

                if(dayOfWeek == Calendar.THURSDAY || dayOfWeek == Calendar.FRIDAY || dayOfWeek == Calendar.MONDAY) {

                    Status[1] = "Open";
                }

                if(LocalTime.now().isBefore(LocalTime.of(23, 0)) && LocalTime.now().isAfter(LocalTime.of(6, 0))) {

                    Status[2] = "Open";
                } else {

                    Status[2] = "Closed";

                }

                if(LocalTime.now().isBefore(LocalTime.of(2, 0)) && LocalTime.now().isAfter(LocalTime.of(5, 0))) {

                    Status[3] = "Open";
                } else {

                    Status[3] = "Closed";

                }

                if(LocalTime.now().isBefore(LocalTime.of(22, 0)) && LocalTime.now().isAfter(LocalTime.of(11, 0))) {

                    Status[4] = "Open";
                } else {

                    Status[4] = "Closed";

                }

                if(LocalTime.now().isBefore(LocalTime.of(20, 30)) && LocalTime.now().isAfter(LocalTime.of(11, 30))) {

                    Status[5] = "Open";
                } else {

                    Status[5] = "Closed";

                }
                // Benjamin HuntsMan
                if(LocalTime.now().isBefore(LocalTime.of(0, 0)) && LocalTime.now().isAfter(LocalTime.of(8, 0)) && dayOfWeek != Calendar.FRIDAY) {

                    Status[6] = "Open";
                } else {

                    Status[6] = "Closed";

                }
            }
        }

        // Venue open / closing times for weekends
        if(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)
        {
            if(dayOfWeek == Calendar.SATURDAY) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (LocalTime.now().isBefore(LocalTime.of(18, 0)) && LocalTime.now().isAfter(LocalTime.of(8, 0))) {

                        Status[0] = "Open";
                    } else {

                        Status[0] = "Closed";

                    }
                    if (LocalTime.now().isBefore(LocalTime.of(19, 0)) && LocalTime.now().isAfter(LocalTime.of(7, 30))) {

                        Status[1] = "Open";
                    } else {

                        Status[1] = "Closed";

                    }
                }
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////// Create Drawable Asset for Map Markers /////////////////////////////
    private BitmapDescriptor BitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        assert vectorDrawable != null;
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 125, 125, true);
        return BitmapDescriptorFactory.fromBitmap(scaledBitmap);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////// On Back Pressed ///////////////////////////////////////////////////
    @Override
    public void onBackPressed() {

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////// Get Device Location ////////////////////////////////////////////////
    private void GetDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices location");

        FusedLocationProviderClient mLocationProvider = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionGranted) {

                Task<Location> location = mLocationProvider.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: found device location");
                        currentLocation = task.getResult();

                        MoveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())
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
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////// MoveCamera Function ////////////////////////////////////////////////
    public void MoveCamera(LatLng latLng) {
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
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////// Get Location Permissions ///////////////////////////////////////////
    private void GetLocationPermission() {

        // Create an array of location permissions
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        // Check if the fine location permission is granted
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Check if the coarse location permission is granted
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Set the location permission to true
                mLocationPermissionGranted = true;
                // Initialize the map
                InitializeMap();

                // If only the fine location permission is granted
            } else {
                // Request for the coarse location permission
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
            // If the fine location permission is not granted
        } else {
            // Request for both fine and coarse location permissions
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////// Request Location Permissions ///////////////////////////////////////
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Call the parent method to handle the result
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Set the location permission to false by default
        mLocationPermissionGranted = false;

        // Check if the request code matches the location permission request code
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            // Check if there are any results
            if (grantResults.length > 0) {
                // Loop through the grant results
                for (int grantResult : grantResults) {
                    // Check if the permission was granted
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        mLocationPermissionGranted = false;
                        return;
                    }
                }
                // Set the location permission to true
                mLocationPermissionGranted = true;
                //Initialize the map
                InitializeMap();
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////// Hide Keyboard Function /////////////////////////////////////////////
    private void HideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////// Navigation Drawer Button Functions ////////////////////////////////
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // Get the id of the item selected
        int id = item.getItemId();

        // Get the resources
        Resources res = getResources();

        // On "Staying Safe" button click
        if (id == R.id.staying_safe) {
            // Open the Staying Safe Activity
            Intent intent = new Intent(MapActivity.this, MainActivity.class);
            startActivity(intent);
        }

        // On "List View" button click
        if (id == R.id.list_view) {

            // Check times to see if venues are open, allows next activity to update open/closed status
            checkIfOpen();

            // get the current location to check the distance between the markers and the current location
            GetDeviceLocation();
            LatLng currentLoc = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

            // get the markers locations
            LatLng[] markerLoc = new LatLng[7];
                markerLoc[0] = cafes[0].getPosition();
                markerLoc[1] = cafes[1].getPosition();
                markerLoc[2] = cafes[2].getPosition();
                markerLoc[3] = fastFood[0].getPosition();
                markerLoc[4] = fastFood[1].getPosition();
                markerLoc[5] = fastFood[2].getPosition();
                markerLoc[6] = pubs[0].getPosition();

            // get the venueDistances String array
            String[] venueDistances = res.getStringArray(R.array.VenueDistance); // get the String array

            // get the venueStatus String array
            String[] venueStatus = res.getStringArray(R.array.VenueStatus);

            // calculate the distance between the current location and the markers
            for(int i = 0; i < markerLoc.length; i++)
            {
                LatLng markerLatLng = markerLoc[i];
                if (markerLatLng != null) {
                    float[] distance = new float[1];
                    Location.distanceBetween(currentLoc.latitude, currentLoc.longitude, markerLatLng.latitude, markerLatLng.longitude, distance);
                    float distanceInMeters = distance[0];
                     distanceInMiles = distanceInMeters / 1609.344f;
                    @SuppressLint("DefaultLocale") String formattedDistance = String.format("%.2f", distanceInMiles); // format to 2 decimal places
                    venueDistances[i] = formattedDistance + " mi";
                } else {
                    venueDistances[i] = "N/A";
                }
            }

            // Start the VenueListView Activity
            Intent intent = new Intent(this, VenueListView.class);



            // Pass the venueStatus and venueDistances arrays to the next activity and start it
            intent.putExtra("venueDistances", venueDistances);
            intent.putExtra("venueStatus", Status);
            startActivity(intent);
        }

        if (id == R.id.reviews) {
            // Open the Settings Activity
            Intent intent = new Intent(MapActivity.this, MainReviewActivity.class);
            startActivity(intent);
        }

        // Close Navigation Drawer
        drawer.closeDrawers();
        return true;
    }

    //////////////////////////// Show/Hide Cafes ///////////////////////////////////////////////////
    public void hideCafes() {
        for (Marker cafe : cafes) {
            cafe.setVisible(false);
        }
    }

    public void showCafes() {

        for (Marker cafe : cafes) {
            cafe.setVisible(true);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////// Show/Hide Fast Food ///////////////////////////////////////////////

    public void hideFastFood() {
        for (Marker fastFood : fastFood) {
            fastFood.setVisible(false);
        }
    }

    public void showFastFood() {
        for (Marker fastFood : fastFood) {
            fastFood.setVisible(true);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////// Show/Hide Pubs ////////////////////////////////////////////////////

    public void hidePubs() {
        for (Marker pub : pubs) {
            pub.setVisible(false);

        }
    }

    public void showPubs() {
        for (Marker pub : pubs) {
            pub.setVisible(true);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////// Show/Hide Markers >5mi ////////////////////////////////////////////

    public void hidegt5Markers() {
        for (Marker gt5 : gt5) {
            gt5.setVisible(false);

            }
        }

    public void showgt5Markers() {
        for (Marker gt5 : gt5) {
            gt5.setVisible(true);

            }
        }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////// Show/Hide Markers >10mi ////////////////////////////////////////////

    public void hidegt10Markers() {
        for (Marker gt10 : gt10) {
            gt10.setVisible(false);
            hidegt5Markers();
            }
        }

    public void showgt10Markers() {
        for (Marker gt10 : gt10) {
            gt10.setVisible(true);
            showgt5Markers();
            }
        }

    public void hideSlowMarkers() {
        fastFood[2].setVisible(false);
    }

    public void showSlowMarkers() {
        fastFood[2].setVisible(true);
    }

    public void hideMediumMarkers() {
        fastFood[0].setVisible(false);
        cafes[1].setVisible(false);
        cafes[2].setVisible(false);
        pubs[0].setVisible(false);
    }

    public void showMediumMarkers() {
        fastFood[0].setVisible(true);
        cafes[1].setVisible(true);
        cafes[2].setVisible(true);
        pubs[0].setVisible(true);
    }
    public void hideFastMarkers() {
        cafes[0].setVisible(false);
        fastFood[1].setVisible(false);
    }


    public void showFastMarkers() {
        cafes[0].setVisible(true);
        fastFood[1].setVisible(true);
    }
}






