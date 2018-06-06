package com.example.lo.flyers;

import android.app.Application;

import com.firebase.client.Firebase;

public class Flyers extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
