package com.example.lo.flyers;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FlyerAdapter extends RecyclerView.Adapter<FlyerAdapter.FlyerViewHolder> {

    private Context context;
    private List<Upload> uploads;

    public FlyerAdapter(Context context, List<Upload> uploads) {
        this.context = context;
        this.uploads = uploads;
    }

    @NonNull
    @Override
    public FlyerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.flyer_cardview, parent, false);
        return new FlyerViewHolder(v, context, uploads);
    }

    @Override
    public void onBindViewHolder(@NonNull FlyerViewHolder holder, int position) {
        Upload currentUpload = uploads.get(position);
        holder.textViewName.setText(currentUpload.getTitle());
        //holder.textViewName.setText(currentUpload.getLocation());
        //Picasso is a library that makes image stuff a LOT easier
        //see on http://square.github.io/picasso/
        Picasso.get().load(currentUpload.getImageUrl()).fit().centerInside().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    public class FlyerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //THIS IS EVERYTHING THAT WILL BE DISPLAYED ON MAIN PAGE
        //ADD MORE IF YOU WANT
        public TextView textViewName;
        public ImageView imageView;
        //List<Upload> uploads = new List<Upload>();
        List<Upload> uploads = new ArrayList<Upload>();
        Context context;

        public FlyerViewHolder(View flyerView,Context context, List<Upload> uploads) {
            super(flyerView);
            this.uploads = uploads;
            this.context = context;
            flyerView.setOnClickListener(this);
            textViewName = flyerView.findViewById(R.id.title);
            imageView = flyerView.findViewById(R.id.image);



        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Upload upload = uploads.get(position);

            Intent intent = new Intent(this.context, UploadDetails.class);
            intent.putExtra("imageUrl", upload.getImageUrl());
            intent.putExtra("time", upload.getTime());
            intent.putExtra("location", upload.getLocation());
            intent.putExtra("details", upload.getDetails());
            intent.putExtra("lat", upload.getLat());
            intent.putExtra("lng", upload.getLng());
            this.context.startActivity(intent);
        }
    }
}
