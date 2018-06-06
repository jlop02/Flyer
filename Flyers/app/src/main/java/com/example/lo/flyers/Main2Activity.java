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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main2Activity extends AppCompatActivity {

    ImageButton btnCamera;
    Button uploadBtn;
    ImageView View;
    Firebase myFirebase;
    EditText field1, field2, field3, field4;
    String strTitle, strTime, strLocation, strDetails;

    Uri imageUri;
    final int PICK_IMAGE_REQUEST = 71;

    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference dataBaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

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


        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFlyer();
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

        if(strTitle.isEmpty()){
            field4.setError("Title is required");
            field4.requestFocus();
            return;
        }

        if(strTime.isEmpty()){
            field1.setError("Time is required");
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
                            taskSnapshot.getDownloadUrl().toString());
                    //create new entry in database w/ unique id
                    String uploadId = dataBaseReference.push().getKey();
                    //now upload ALL information about flyer to the unique id in the database
                    dataBaseReference.child(uploadId).setValue(upload);
                }
            });

            Intent intent = new Intent(Main2Activity.this, MainActivity.class);
            startActivity(intent);
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

//    void uploadImage() {
//        if (filePath != null) {
//            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
//            ref.putFile(filePath);
//        }
//    }

}
