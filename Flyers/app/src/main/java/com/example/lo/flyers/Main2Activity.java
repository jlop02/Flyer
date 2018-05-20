package com.example.lo.flyers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class Main2Activity extends AppCompatActivity {

    Button btnCamera;
    ImageView View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btnCamera = findViewById(R.id.button);
        View = findViewById(R.id.imageView);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

    }

    public void saveFlyer(View view) {
        //everything about the flyer will be stored in a JSON object
        //EXCEPT the image is stored as a .png file which is created
        //when the image is captured (since JSON only stores strings/ints)
        JSONObject jo = new JSONObject();

        //get all appropriate info from flyer
        EditText titleEditText=(EditText)findViewById(R.id.titleEditText);
        String title = titleEditText.getText().toString().trim();

        EditText timeEditText = (EditText) findViewById(R.id.time);
        String time = timeEditText.getText().toString().trim();

        EditText locationEditText = (EditText) findViewById(R.id.location);
        String location = locationEditText.getText().toString().trim();

        EditText detailsEditText = (EditText) findViewById(R.id.details);
        String details = locationEditText.getText().toString().trim();

        //put all info into json object
        try {
            jo.put("title",title);
            jo.put("time", time);
            jo.put("location", location);
            jo.put("details", details);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //open a file (called flyer.ser per convention)
        //and write the json obj as a string to it
        try {
            FileOutputStream f = openFileOutput("flyer.ser", Context.MODE_PRIVATE);
            ObjectOutputStream o = new ObjectOutputStream(f);
            Log.d("test", "objectoutputstream created");
            o.writeObject(jo.toString());
            Log.d("test", "jo written to objectoutputstream");
            o.close();
            f.close();
        } catch (FileNotFoundException e) {
            Log.d("error", "File not found");
        } catch (IOException e) {
            Log.d("error", "error exception");
        }


        //go back to MainActivity
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        View.setImageBitmap(bitmap);

        //save bitmap to file
        //this is so that it can be added to the main screen once flyer is created
        try {
            FileOutputStream out = openFileOutput("flyerImage.png", Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
            Log.d("file", "FILE CREATED");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
