package com.example.lo.flyers;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import java.lang.Math;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

public class Main2Activity extends AppCompatActivity {

    ImageButton btnCamera;
    Button uploadBtn;
    ImageView View;
    Firebase myFirebase;
    EditText field1, field2, field3, field4;
    String strTitle, strTime, strLocation, strDetails, lat,lng,timeS,timeB;

    Uri imageUri;
    final int PICK_IMAGE_REQUEST = 71;

    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference dataBaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setTitle("Add New Flyer");

        btnCamera = findViewById(R.id.button);
        uploadBtn = findViewById(R.id.button3);
        View = findViewById(R.id.imageView);
        field1 = findViewById(R.id.plain_text_input);
        field2 = findViewById(R.id.plain_text_input2);
        field3 = findViewById(R.id.plain_text_input3);
        field4 = findViewById(R.id.plain_text_input4);

        strTime = field1.getText().toString();
        strLocation = field2.getText().toString();
        strDetails = field3.getText().toString();
        strTitle = field4.getText().toString();

        myFirebase = new Firebase("https://flyers-f278b.firebaseio.com");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        dataBaseReference = FirebaseDatabase.getInstance().getReference();
        btnCamera.setVisibility(android.view.View.VISIBLE);
        View.setVisibility(android.view.View.INVISIBLE);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
                View.setVisibility(android.view.View.VISIBLE);
                btnCamera.setVisibility(android.view.View.INVISIBLE);
            }
        });
        View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFlyer();
                new GetCoordinates().execute(field2.getText().toString().replace(" ","+"));
            }
        });
    }

    void chooseImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void uploadFlyer() {
        strTime = field1.getText().toString().trim();
        strLocation = field2.getText().toString().trim();
        strDetails = field3.getText().toString().trim();
        strTitle = field4.getText().toString().trim();

        // TEST AGAIN
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
        format.setTimeZone(TimeZone.getTimeZone("PST"));

        SimpleDateFormat newFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try{
            date = format.parse(strTime);
            timeS = newFormat.format(date);

            //double big = 100000000 - Double.parseDouble(timeS);
            int num = Integer.parseInt(timeS);
            int big = 100000000 - num;
            timeB = Integer.toString(big);
        } catch(Exception e) {
            e.printStackTrace();
        }

        if(strTitle.isEmpty()){
            field4.setError("Title is required");
            field4.requestFocus();
            return;
        }

        if(strTime.isEmpty()){
            field1.setError("Date is required MM-DD-YYYY");
            field1.requestFocus();
            return;
        }

        if(strLocation.isEmpty()){
            field2.setError("Location is required");
            field2.requestFocus();
            return;
        }

        if(strDetails.isEmpty()){
            field3.setError("Details are required");
            field3.requestFocus();
            return;
        }

        if (imageUri != null) {
            //add image to firebase storage
            //need unique name for image file, so use time in milliseconds (changes so fast always unique)
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));
            //put the file in the storage
            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   // strTime = field1.getText().toString().trim();
                    //strLocation = field2.getText().toString().trim();
                    //strDetails = field3.getText().toString().trim();

                    //make new upload item - download URL is the url for downloading the img from storage
                    Upload upload = new Upload(strTitle, strDetails, strTime, strLocation,
                            taskSnapshot.getDownloadUrl().toString(), lat,lng,timeS,timeB);
                    //create new entry in database w/ unique id
                    String uploadId = dataBaseReference.push().getKey();
                    //now upload ALL information about flyer to the unique id in the database
                    dataBaseReference.child(uploadId).setValue(upload);
                }
            });

            //Intent intent = new Intent(Main2Activity.this, MainActivity.class);
            //startActivity(intent);
            Intent intent = new Intent(Main2Activity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        //the flyer thing wasn't completely filled out, don't upload and display error message
        else {

            Toast.makeText(this, "No flyer image was selected!", Toast.LENGTH_SHORT).show();

        }


//        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Userss").child(user_id).push();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();
            View.setImageURI(imageUri);
        }
    }

    //returns .png, .jpg, whatever file type is
    //i dont really know how it works lol but it does
    //i followed a tutorial
    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private class GetCoordinates extends AsyncTask<String,Void,String> {
        ProgressDialog dialog = new ProgressDialog(Main2Activity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please wait....");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String response;
            try{
                String address = strings[0];
                LatLongData http = new LatLongData();
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s",address);
                response = http.getData(url);
                return response;
            }
            catch (Exception ex)
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jsonObject = new JSONObject(s);

                lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lat").toString();
                lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lng").toString();

                //txtCoord.setText(String.format("Coordinates : %s / %s ",lat,lng));

                if(dialog.isShowing())
                    dialog.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

//    void uploadImage() {
//        if (filePath != null) {
//            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
//            ref.putFile(filePath);
//        }
//    }

}
