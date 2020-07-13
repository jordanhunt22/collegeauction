package com.example.collegeauction;

import android.app.Application;

import com.example.collegeauction.Models.Listing;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Register Parse models
        ParseObject.registerSubclass(Listing.class);

        // Set applicationId, and server server based on the values in the Heroku settings.
        // ClientKey is not needed unless explicitly configured
        // Any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("jordan-collegeauction") // should correspond to APP_ID env variable
                .clientKey("FBUFinalApplication")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://jordan-collegeauction.herokuapp.com/parse/").build());
    }
}

