package com.example.lo.flyers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class UploadDetails extends AppCompatActivity {
    ImageView imgView;
    TextView textView, localView, detailView;
    ProgressBar progressBar;

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
    }
}
