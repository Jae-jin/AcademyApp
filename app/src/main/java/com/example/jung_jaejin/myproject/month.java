package com.example.jung_jaejin.myproject;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);
        spinner = (Spinner)findViewById(R.id.spinner);

        final ArrayList<String> list = new ArrayList<>();
        list.add("2018.12.31 ~ 2018.01.25");
        list.add("2018.01.28 ~ 2018.02.22");
        list.add("2018.02.25 ~ 2018.03.22");
        list.add("2018.03.25 ~ 2018.04.19");

        ArrayAdapter spinnerAdapter;
        spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list);
        spinner.setAdapter(spinnerAdapter);

        todayTest = (Button)findViewById(R.id.todayTest);

        todayTest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),Studystart.class);
                startActivity(intent);
            }
        });

    }
}
