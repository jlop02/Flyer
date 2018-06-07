package com.example.dustinadams.listviewexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/*This is the class of the Activity that gets called when the user selects
an item from the list.
 */

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent i = getIntent();
        String title = i.getStringExtra("title");
        String description = i.getStringExtra("description");

        TextView t = (TextView)findViewById(R.id.textView);
        TextView d = (TextView)findViewById(R.id.textView2);

        t.setText(title);
        d.setText(description);
    }

    protected void onResume(Bundle savedInstanceState){
        //
    }
}
