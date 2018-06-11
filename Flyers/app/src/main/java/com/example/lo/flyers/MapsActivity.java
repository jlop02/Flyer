package com.example.lo.flyers;

import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mUsers;
    Marker marker;
    String Lat,Lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        ChildEventListener mChildEventListener;
        mUsers = FirebaseDatabase.getInstance().getReference();
        mUsers.push().setValue(marker);

        mUsers.addValueEventListener(valueEventListener);



    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.

     @Override public void onMapReady(GoogleMap googleMap) {
     mMap = googleMap;

     // Add a marker in Sydney and move the camera
     LatLng sydney = new LatLng(-34, 151);
     mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
     mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
     } */
    @Override
    public void onMapReady (GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        LatLng cruz = new LatLng(36.9741, -122.0308);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cruz));
    }

    // mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try {
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    Upload user = s.getValue(Upload.class);
                    Lat = user.getLat();
                    double latitude = Double.parseDouble(Lat);
                    Lng = user.getLng();
                    double longitude = Double.parseDouble(Lng);
                    String title = user.getTitle();
                    //LatLng cruz = new LatLng(36.9741,-122.0308);
                    LatLng location = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(location).title(title)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                }
            } catch (NullPointerException e) {
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };



    @Override
    public void onLocationChanged (Location location){

    }

    @Override
    public void onStatusChanged (String provider,int status, Bundle extras){

    }

    @Override
    public void onProviderEnabled (String provider){

    }

    @Override
    public void onProviderDisabled (String provider){

    }

    @Override
    public boolean onMarkerClick (Marker marker){
        return false;
    }

}

