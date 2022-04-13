package com.example.taverncrawler;

import android.app.Application;

import com.example.taverncrawler.models.Review;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Review.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_id))
                .clientKey(getString(R.string.back4app_ck))
                .server(getString(R.string.back4app_url))
                .build());
    }
}
