package com.example.jung_jaejin.myproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Studystart_review extends AppCompatActivity {
    Button studyreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studystart_review);
        studyreview = (Button)findViewById(R.id.studyreview);

        studyreview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),Review.class);
                startActivity(intent);
            }
        });
    }
}
