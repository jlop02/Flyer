package com.example.lo.flyers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class UploadDetails extends AppCompatActivity implements OnMapReadyCallback{
    ImageView imgView;
    TextView textView, localView, detailView;
    ProgressBar progressBar;
    private MapView mapView;
    private GoogleMap gmap;
    String Lat, Lng;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_details);
        setTitle("Details");

        imgView = findViewById(R.id.imageView2);
        textView = findViewById(R.id.timeView);
        localView = findViewById(R.id.textView);
        detailView = findViewById(R.id.detailView);
        progressBar = findViewById(R.id.progressBar2);


         //imgView.setImageResource(getIntent().getStringExtra("imageUrl"));
        //Picasso.get().load(currentUpload.getImageUrl()).fit().centerInside().into(holder.imageView);
        Intent getImage = getIntent();
        String gettingImageUrl = getImage.getStringExtra("imageUrl");
        progressBar.setVisibility(View.VISIBLE);
        Picasso.get().load(gettingImageUrl).fit().centerInside().into(imgView, new com.squareup.picasso.Callback(){
            @Override
            public void onSuccess(){
                progressBar.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onError(Exception e){

            }

        });

         textView.setText("Date: "+ getIntent().getStringExtra("time"));
         localView.setText("Location: "+ getIntent().getStringExtra("location"));
         detailView.setText("Details: "+ getIntent().getStringExtra("details"));

        try {
            Bundle mapViewBundle = null;
            if (savedInstanceState != null) {
                mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
            }

            mapView = findViewById(R.id.map_view);
            mapView.onCreate(mapViewBundle);
            mapView.getMapAsync(this);
        }catch (NullPointerException e){}
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            Lat = getIntent().getStringExtra("lat");
            double Latitude = Double.parseDouble(Lat);
            Lng = getIntent().getStringExtra("lng");
            double Longitude = Double.parseDouble(Lng);

            gmap = googleMap;
            gmap.setMinZoomPreference(14);
            //gmap.setMaxZoomPreference(16);
            gmap.setIndoorEnabled(true);
            UiSettings uiSettings = gmap.getUiSettings();
            uiSettings.setIndoorLevelPickerEnabled(true);
            uiSettings.setMyLocationButtonEnabled(true);
            uiSettings.setMapToolbarEnabled(true);
            uiSettings.setCompassEnabled(true);
            uiSettings.setZoomControlsEnabled(true);

            LatLng map = new LatLng(Latitude, Longitude);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(map);
            gmap.addMarker(markerOptions);

            gmap.moveCamera(CameraUpdateFactory.newLatLng(map));
        }catch (NullPointerException e){}
    }
}
