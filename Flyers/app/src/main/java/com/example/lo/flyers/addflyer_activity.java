package com.example.lo.flyers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class addflyer_activity extends AppCompatActivity {
    public JSONObject jo = null;
    public JSONArray ja= null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

    }

}
