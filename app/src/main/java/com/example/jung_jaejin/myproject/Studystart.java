package com.example.jung_jaejin.myproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Studystart extends AppCompatActivity {
    Button studyStart;
    private String user_id;
    private String grade;
    private String classss;
    private int filenum;
    private int day;
    private int time;
    private int realday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studystart);
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        grade = intent.getStringExtra("grade");
        classss = intent.getStringExtra("class");
        filenum = intent.getIntExtra("filenum",0);
        day = intent.getIntExtra("day",0);
        time = intent.getIntExtra("time",0);
        realday = intent.getIntExtra("realday",0);


        studyStart = (Button)findViewById(R.id.studyStart);

        studyStart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),Study1.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("grade",grade);
                intent.putExtra("class",classss);
                intent.putExtra("filenum",filenum);
                intent.putExtra("day",day);
                intent.putExtra("time",time);
                intent.putExtra("realday",realday);
                startActivity(intent);

            }
        });
    }
}
