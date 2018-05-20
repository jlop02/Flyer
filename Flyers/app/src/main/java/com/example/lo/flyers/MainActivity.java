package com.example.lo.flyers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnMove;
    public JSONObject jos=null;
    public JSONArray ja=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*once you've created a flyer and go back to the main screen,
    onResume() will be called and add your flyer to the top of the
    screen.
     */
    protected void onResume() {
        super.onResume();

        //for now, just change the default imageview to the picture that you took

        //get default imageview
        ImageView mainScreenImage = findViewById(R.id.flyer_test);

        //get filepath for the image you took bc android is finicky about this apparently
        String fname = getFilesDir().getAbsolutePath()+"/flyerImage.png";

        //check if .png file exists - will only be created after main2activity is finished
        File file = new File(fname);
        if(file.exists()) {
            Log.d("file", "FILE EXISTS");
            //get bitmap from file
            Bitmap bm = BitmapFactory.decodeFile(fname);
            //set bitmap
            mainScreenImage.setImageBitmap(bm);
            //above pulled from https://stackoverflow.com/questions/2688169/how-to-load-an-imageview-from-a-png-file
        }

        //OKAY HOPE THAT WORKS!!!


        //List Implementation
        ListView list = findViewById(R.id.list_event);
        TextView text = findViewById(R.id.textNoEvent);
        text.setVisibility(View.INVISIBLE);

        //get the data from file
        try {
            File f = new File(getFilesDir(), "file.ser");
            FileInputStream fi = new FileInputStream(f);
            ObjectInputStream o = new ObjectInputStream(fi);

            String j = null;
            try {
                j = (String) o.readObject();
            } catch (ClassNotFoundException c) {
                c.printStackTrace();
            }
            try {
                jos = new JSONObject(j);
                ja = jos.getJSONArray("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            final ArrayList<ListData> aList = new ArrayList<>();
            for(int i =0; i<ja.length(); i++){
                ListData ld = new ListData();
                try{
                    ld.title = ja.getJSONObject(i).getString("title");
                    ld.Date = ja.getJSONObject(i).getString("date");
                    ld.Location = ja.getJSONObject(i).getString("location");
                    ld.Description = ja.getJSONObject(i).getString("description");
                }
                catch(JSONException el)
                {
                    el.printStackTrace();
                }
                aList.add(ld);
            }

            //Create a list that shows the elements in the listview
            String[] listitems = new String[aList.size()];

            for(int i=0; i<aList.size(); i++){
                ListData listD = aList.get(i);
                listitems[i] = listD.title;
            }

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,listitems);
            list.setAdapter(adapter);

            //tapping on an item in the listview will go to a new activity with more details
            final Context context = this;
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ListData selected = aList.get(position);

                    //set date from MainActivity to detailActivity
                    Intent detailIntent = new Intent(context, DetailActivity.class);

                    detailIntent.putExtra("title",selected.title);
                    detailIntent.putExtra("date", selected.Date);
                    detailIntent.putExtra("location",selected.Location);
                    detailIntent.putExtra("description",selected.Description);
                    startActivity(detailIntent);
                }
            });
            if(aList.size()<1){
                text.setVisibility(View.VISIBLE);
            }
        }
        catch(IOException e){
            list.setEnabled(false);
            list.setVisibility(View.INVISIBLE);
            text.setVisibility(View.VISIBLE);
        }
    }

    public void  OnMyClick(View view){
        final Context context = this;
        btnMove = findViewById(R.id.button2);
        btnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Main2Activity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.item1:
                Toast.makeText(this, "Home was selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.item2:
                Toast.makeText(this, "LogIn was selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.item3:
                Toast.makeText(this, "Saved was selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.item4:
                Toast.makeText(this, "Map was selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.item5:
                Toast.makeText(this, "Sort was selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.SubItem1:
                Toast.makeText(this, "New was selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.SubItem2:
                Toast.makeText(this, "Saved was selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.SubItem3:
                Toast.makeText(this, "Liked was selected", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
