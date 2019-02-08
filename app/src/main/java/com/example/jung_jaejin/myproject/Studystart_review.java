package com.example.jung_jaejin.myproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class Studystart_review extends AppCompatActivity {
    private    Button studyreview;
    private Button clearreview;
    private String getId;
    private String getGrade;
    private String getClass;
    private int getFilenum;
    private int getday;
    private int getscore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_studystart_review);
        studyreview = (Button)findViewById(R.id.studyreview);
        clearreview = (Button)findViewById(R.id.clearreview);
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
        clearreview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Studystart_review.this);
                dialog.setTitle("복습단어 초기화")
                        .setMessage("정말로 초기화하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    FileOutputStream last_fos = openFileOutput("data.txt", Context.MODE_PRIVATE);

                                    PrintWriter writer = new PrintWriter(last_fos);

                                    writer.print("");
                                    writer.close();

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    FileOutputStream last_fos = openFileOutput("_data.txt", Context.MODE_PRIVATE);

                                    PrintWriter writer = new PrintWriter(last_fos);

                                    writer.print("");
                                    writer.close();

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
            }
        });
    }
    @Override
    public void onBackPressed() {

        // Alert을 이용해 종료시키기
        AlertDialog.Builder dialog = new AlertDialog.Builder(Studystart_review.this);
        dialog  .setTitle("복습 종료")
                .setMessage("복습을 종료하고 main화면으로 가시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(),month.class);
                        intent.putExtra("user_id", getId);
                        intent.putExtra("grade",getGrade);
                        intent.putExtra("class",getClass);
                        intent.putExtra("filenum",getFilenum);
                        intent.putExtra("day",getday);
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
