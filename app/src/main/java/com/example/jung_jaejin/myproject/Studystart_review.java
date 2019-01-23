package com.example.jung_jaejin.myproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Studystart_review extends AppCompatActivity {
    private    Button studyreview;
    private String getId;
    private String getGrade;
    private String getClass;
    private int getFilenum;
    private int getday;
    private int getscore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studystart_review);
        studyreview = (Button)findViewById(R.id.studyreview);
        Intent intent = getIntent();
        getId = intent.getStringExtra("user_id");
        getGrade = intent.getStringExtra("grade");
        getClass = intent.getStringExtra("class");
        getFilenum = intent.getIntExtra("filenum",0);
        getday = intent.getIntExtra("realday",0);
        getscore = intent.getIntExtra("maxscore",0);

        studyreview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),Review.class);
                intent.putExtra("user_id", getId);
                intent.putExtra("grade",getGrade);
                intent.putExtra("class",getClass);
                intent.putExtra("filenum",getFilenum);
                intent.putExtra("realday",getday);
                intent.putExtra("maxscore",getscore);
                startActivity(intent);
            }
        });
    }
}
