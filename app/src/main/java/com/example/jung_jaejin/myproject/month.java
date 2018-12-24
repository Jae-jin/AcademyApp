package com.example.jung_jaejin.myproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class month extends AppCompatActivity {


    private Spinner spinner;
    Button todayTest;
    Button review_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);







        todayTest = (Button)findViewById(R.id.todayTest);
        review_test = (Button)findViewById(R.id.review_test);

        todayTest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),Studystart.class);
                startActivity(intent);
            }
        });
        review_test.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),Studystart_review.class);
                startActivity(intent);
            }
        });

    }
}
