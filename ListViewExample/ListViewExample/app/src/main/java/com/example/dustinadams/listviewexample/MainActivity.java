/*
This program gives an example of showing a list view with corresponding event listeners for the
list items. Also shows a menu button with a listener implemented.
 Author: Dustin Adams
 date: 4-25-2018
 */
package com.example.dustinadams.listviewexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import java.util.ArrayList;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    private final String TAG = "ListView";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.data_list_view);

        // Put the json data into the String json

        String json = null;

        try {
            InputStream is = getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (java.io.IOException ex) {
            ex.printStackTrace();
            return;
        }
        // JSON data is now in json variable


        // Put json data into a JSONArray
        JSONArray dataList;
        JSONObject jsonO;
        try {
            jsonO = new JSONObject(json);

            dataList = jsonO.getJSONArray("data");
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // The ListData class (created by me) simply holds a String for title and String for description.
        // See the ListData class for more details.
        final ArrayList<ListData> list = new ArrayList<ListData>();

        // create ListData objects from data and store in ArrayList called list
        for(int i = 0; i < dataList.length(); i++){

            ListData ld = new ListData();
            try {
                ld.title = dataList.getJSONObject(i).getString("title");
                ld.description = dataList.getJSONObject(i).getString("description");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }


            list.add(ld);
        }

        // Create an array and assign each element to be the title
        // field of each of the ListData objects (from the array list)
        String[] listItems = new String[list.size()];

        for(int i = 0; i < list.size(); i++){
            ListData listD = list.get(i);
            listItems[i] = listD.title;
        }

        // Show the list view with the each list item an element from listItems
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        mListView.setAdapter(adapter);

        // Set an OnItemClickListener for each of the list items
        final Context context = this;
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ListData selected = list.get(position);

                // Create an Intent to reference our new activity, then call startActivity
                // to transition into the new Activity.
                Intent detailIntent = new Intent(context, DetailActivity.class);

                // pass some key value pairs to the next Activity (via the Intent)
                detailIntent.putExtra("title", selected.title);
                detailIntent.putExtra("description", selected.description);

                startActivity(detailIntent);
            }

        });

    }

    // This method will just show the menu item (which is our button "ADD")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        // the menu being referenced here is the menu.xml from res/menu/menu.xml
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    /* Here is the event handler for the menu button that I forgot in class.
    The value returned by item.getItemID() is
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, String.format("" + item.getItemId()));
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_favorite:
                /*the R.id.action_favorite is the ID of our button (defined in strings.xml).
                Change Activity here (if that's what you're intending to do, which is probably is).
                 */
                openAddEvent();
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void openAddEvent(){
        Intent intent = new Intent(this, addevent_activity.class);
        startActivity(intent);
    }
}
