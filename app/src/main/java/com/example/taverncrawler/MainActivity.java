package com.example.taverncrawler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.taverncrawler.fragments.ProfileFragment;
import com.example.taverncrawler.fragments.ReviewsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.parse.ParseUser;

/**
 * An {@link AppCompatActivity} class that handles navigation between fragments
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        final Fragment fragment = null;

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
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch(item.getItemId()) {
                    case R.id.action_profile:
                        fragment = new ProfileFragment();
                        transaction.replace(R.id.container, fragment).commit();
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.action_settings:
                        Toast.makeText(MainActivity.this, "You have clicked on settings!", Toast.LENGTH_SHORT).show();
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
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Fragment fragment;
                switch(item.getItemId()) {
                    case R.id.action_feed:
                        fragment = new ReviewsFragment();
                        transaction.replace(R.id.container, fragment).commit();
                        return true;
                    case R.id.action_route:
                        fragment = null;
                        transaction.replace(R.id.container, fragment).commit();
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        actionBarDrawerToggle.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

}