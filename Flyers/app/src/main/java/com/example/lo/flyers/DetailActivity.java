package com.example.lo.flyers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity{
    public JSONObject jos= null;
    public JSONArray ja = null;
    public JSONObject jo = null;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //recieve data from main activity for one element
        Intent i getIntent();
        String title = i.getStringExtra("title");
        String description = i.getStringExtra("date");
        String location = i.getStringExtra("location");
        String date = i.getStringExtra("date");
        position = i.getIntExtra("position",0);


        //set the objects for the xml


    }
}
