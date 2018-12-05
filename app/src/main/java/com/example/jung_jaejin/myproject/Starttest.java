package com.example.jung_jaejin.myproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Starttest extends AppCompatActivity {
    Button startTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starttest);

        startTest = (Button)findViewById(R.id.startTest);

        startTest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),Test.class);
                startActivity(intent);
            }
        });
    }
}
