package com.example.lo.flyers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import android.support.design.widget.FloatingActionButton;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.google.firebase.auth.FirebaseAuth.*;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton btnMove;


    private FirebaseAuth mAuth;
    private Firebase firebase;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private RecyclerView recyclerView;
    private FlyerAdapter adapter;
    private DatabaseReference databasereference;
    private List<Upload> uploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Flyers");

        btnMove = findViewById(R.id.button2);
        if(user != null){
            btnMove.setVisibility(View.VISIBLE);
        }else {
            btnMove.setVisibility(View.GONE);
        }

        btnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recycler);
        //below line makes it more efficient
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //can't initialize new list, but declare as list bc more flexible
        uploads = new ArrayList<>();
        adapter = new FlyerAdapter(this,uploads);
        recyclerView.setAdapter(adapter);

        databasereference = FirebaseDatabase.getInstance().getReference();

        //event listener for changes in data base
        databasereference.addValueEventListener(valueEventListener);

            //will be called on create method and if there are changes to database ref

    }
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clear out uploads because data will be added all over again
                //don't know if there's a more efficient way of doing it - add only the
                //data not already in uploads?
                uploads.clear();
                if(dataSnapshot.exists()) {
                    //add all data in database to uploads
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Upload upload = postSnapshot.getValue(Upload.class);
                        if(upload.getImageUrl()!=null)
                            {uploads.add(upload);}
                    }
                }
                adapter.notifyDataSetChanged();
                //set adapter
                adapter = new FlyerAdapter(MainActivity.this, uploads);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //this method will be called if there is an error in the database
                //(no permiission to view items, etc.
            }
        };


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem items = menu.findItem(R.id.itemLog);

        if(user != null){
            items.setVisible(false);
        }

        //Gets rid of logout button when not logged in
        MenuItem logOut = menu.findItem(R.id.item2);
        if(user==null) {
            logOut.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.itemLog:

                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;

            case R.id.item1:
                if(item.getItemId()==R.id.item1){
                LinearLayoutManager layoutManager=(LinearLayoutManager) recyclerView.getLayoutManager();
                layoutManager.scrollToPositionWithOffset(0,0);}
                return true;

            case R.id.item2:
                if(item.getItemId()==R.id.item2)
                {Toast.makeText(this, "LogOut was selected", Toast.LENGTH_SHORT).show();
                getInstance().signOut();

                Intent intent1 = new Intent(this,MainActivity.class);
                startActivity(intent1);}
                return true;

            case R.id.item4:
                if(item.getItemId()==R.id.item4){
                Toast.makeText(this, "Map was selected", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(this,MapsActivity.class);
                startActivity(intent2);}
                return true;

            case R.id.item5:
                Toast.makeText(this, "Sort", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.SubItem1:
                Toast.makeText(this, "Nearest Date", Toast.LENGTH_SHORT).show();
                Query query = FirebaseDatabase.getInstance().getReference("").orderByChild("timeS");
                query.addListenerForSingleValueEvent(valueEventListener);
                return true;

            case R.id.SubItem2:
                Toast.makeText(this, "Furthest Date", Toast.LENGTH_SHORT).show();
                Query query2 = FirebaseDatabase.getInstance().getReference("").orderByChild("timeB");
                query2.addListenerForSingleValueEvent(valueEventListener);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
