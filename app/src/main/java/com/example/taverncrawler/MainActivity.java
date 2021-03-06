package com.example.taverncrawler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.taverncrawler.fragments.FeedFragment;
import com.example.taverncrawler.fragments.ProfileFragment;
import com.example.taverncrawler.fragments.RouteFragment;
import com.example.taverncrawler.fragments.ReviewsFragment;
import com.example.taverncrawler.fragments.SettingsFragment;

import com.example.taverncrawler.viewmodels.RouteViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.parse.ParseUser;

import okhttp3.Route;

/**
 * An {@link AppCompatActivity} class that handles navigation between fragments
 */
public class MainActivity extends AppCompatActivity implements LocationListener {
    public static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RouteViewModel routeViewModel;
    private NavigationView navigationView;
    private double latitude, longitude;
    private Location currentLocation;
    private LocationManager locationManager;
    private boolean isGPSEnabled, isNetworkEnabled;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 0;

    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        navigationView = findViewById(R.id.navigation_view);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        drawerLayout = findViewById(R.id.drawer_layout);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        routeViewModel = new ViewModelProvider(this).get(RouteViewModel.class);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        FragmentManager fragmentManager = getSupportFragmentManager();
        getLocation(isGPSEnabled, isNetworkEnabled);
        if(currentLocation != null) {
            latitude = currentLocation.getLatitude();
            longitude = currentLocation.getLongitude();
            Toast.makeText(MainActivity.this, "Latitude is: " + String.valueOf(latitude) + "Longitude is: " + String.valueOf(longitude), Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Latitude is: " + String.valueOf(latitude) + "Longitude is: " + String.valueOf(longitude));
        }
        else {
            Log.i(TAG, "Was not able to get location of device.");
            Toast.makeText(MainActivity.this, "Was not able to retrieve current location of device", Toast.LENGTH_SHORT).show();
        }
        Bundle profileData = getIntent().getExtras();
        String username = profileData.getString("username");
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = new Intent(this, LoginActivity.class);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch(item.getItemId()) {
                    case R.id.action_profile:
                        fragment = new ProfileFragment();
                        transaction.replace(R.id.container, fragment).commit();
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.action_settings:
                        fragment = new SettingsFragment();
                        Toast.makeText(MainActivity.this, "You have clicked on settings!", Toast.LENGTH_SHORT).show();
                        transaction.replace(R.id.container, fragment).commit();
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.action_logout:
                        SharedPreferences sharedPref = MainActivity.this.getSharedPreferences("Login Check", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean(getString(R.string.userLogged), false);
                        editor.commit();

                        ParseUser.logOut();
                        startActivity(intent);
                        finish();
                        drawerLayout.closeDrawers();
                        return true;
                }
                return false;
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch(item.getItemId()) {
                    case R.id.action_feed:
                        fragment = new FeedFragment();
                        Bundle bundle = new Bundle();
                        bundle.putDouble("lat", latitude);
                        bundle.putDouble("longi", longitude);
                        fragment.setArguments(bundle);
                        transaction.replace(R.id.container, fragment).commit();
                        return true;
                    case R.id.action_route:
                        fragment = new RouteFragment();
                        Bundle bundle1 = new Bundle();
                        bundle1.putDouble("lat", latitude);
                        bundle1.putDouble("longi", longitude);
                        fragment.setArguments(bundle1);
                        //fragment = new ReviewsFragment();
                        transaction.replace(R.id.container, fragment).commit();
                        return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_feed);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        actionBarDrawerToggle.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    private void requestPermission() {
        try {
            if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getLocation (boolean gps_provider_enabled, boolean network_provider_enabled) {
        if (network_provider_enabled) {
            Log.i(TAG, "Network Provider is enabled");
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            if (locationManager != null) {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    currentLocation = location;
                } else {
                    Log.i(TAG, "Network Provider failed to return location.");
                }
            }
        }
        if (gps_provider_enabled) {
            if (currentLocation == null) {
                Log.i(TAG, "GPS Provider is enabled");
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (locationManager != null) {
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        currentLocation = location;
                    } else {
                        Log.i(TAG, "GPS Provider failed to return location");
                    }

                }
            }
        }
        else {
            Log.i(TAG, "Network Provider and GPS provider both false");
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {}
}