package com.example.taverncrawler;

import android.app.Application;

<<<<<<< HEAD
import com.example.taverncrawler.models.User;
=======
import com.example.taverncrawler.models.Review;
>>>>>>> 5e7b118560ddda467c8815d7d75a1ab66959eb05
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

<<<<<<< HEAD
        ParseObject.registerSubclass(User.class);
=======
        ParseObject.registerSubclass(Review.class);

>>>>>>> 5e7b118560ddda467c8815d7d75a1ab66959eb05
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_id))
                .clientKey(getString(R.string.back4app_ck))
                .server(getString(R.string.back4app_url))
                .build());
    }
}
