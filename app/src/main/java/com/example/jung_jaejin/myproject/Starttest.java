package com.example.jung_jaejin.myproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Starttest extends AppCompatActivity {
    private Button startTest;
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_starttest);
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        grade = intent.getStringExtra("grade");
        classss = intent.getStringExtra("class");
        filenum = intent.getIntExtra("filenum",0);
        day = intent.getIntExtra("day",0);
        time = intent.getIntExtra("time",0);
        realday = intent.getIntExtra("realday",0);

        startTest = (Button)findViewById(R.id.startTest);

        startTest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                if((day %6) == 5)
                {
                    Intent intent = new Intent(getApplicationContext(),Showtipsofdaytest.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("grade",grade);
                    intent.putExtra("class",classss);
                    intent.putExtra("filenum",filenum);
                    intent.putExtra("day",day);
                    intent.putExtra("time",time);
                    intent.putExtra("realday",realday);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(),Test.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("grade",grade);
                    intent.putExtra("class",classss);
                    intent.putExtra("filenum",filenum);
                    intent.putExtra("day",day);
                    intent.putExtra("time",time);
                    intent.putExtra("realday",realday);
                    startActivity(intent);
                }
            }
        });
    }
    @Override
    public void onBackPressed() {

        // Alert을 이용해 종료시키기
        AlertDialog.Builder dialog = new AlertDialog.Builder(Starttest.this);
        dialog  .setTitle("시험 종료")
                .setMessage("시험을 보지 않고 main화면으로 가시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(),month.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("grade",grade);
                        intent.putExtra("class",classss);
                        intent.putExtra("filenum",filenum);
                        intent.putExtra("day",realday);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
    }

}
