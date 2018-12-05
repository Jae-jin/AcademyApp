package com.example.jung_jaejin.myproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ResultPage extends AppCompatActivity {
    Button result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        result = (Button)findViewById(R.id.result);

        result.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),month.class);
                startActivity(intent);
            }
        });
    }
}
