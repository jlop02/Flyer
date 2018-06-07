package com.example.dustinadams.listviewexample;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class addevent_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event);


        Button addEvent = findViewById(R.id.addevent);

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText edtTitle = findViewById(R.id.editTitle);
                EditText edtDescription = findViewById(R.id.editDescription);
                String titleInput= edtTitle.getText().toString().trim();
                String descInput = edtDescription.getText().toString().trim();

                ListData ld = new ListData();
                ld.title = titleInput;
                ld.description = descInput;


            }
        });






    }
}
