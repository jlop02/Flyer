package com.example.lo.flyers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class UploadDetails extends AppCompatActivity {
    ImageView imgView;
    TextView textView, localView, detailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_details);

        imgView = findViewById(R.id.imageView2);
        textView = findViewById(R.id.timeView);
        localView = findViewById(R.id.textView);
        detailView = findViewById(R.id.detailView);

         //imgView.setImageResource(getIntent().getStringExtra("imageUrl"));
        //Picasso.get().load(currentUpload.getImageUrl()).fit().centerInside().into(holder.imageView);
        Intent getImage = getIntent();
        String gettingImageUrl = getImage.getStringExtra("imageUrl");
        Picasso.get().load(gettingImageUrl).fit().centerInside().into(imgView);

         textView.setText("Time: "+ getIntent().getStringExtra("time"));
         localView.setText("Location: "+ getIntent().getStringExtra("location"));
         detailView.setText("Details: "+ getIntent().getStringExtra("details"));
    }
}
