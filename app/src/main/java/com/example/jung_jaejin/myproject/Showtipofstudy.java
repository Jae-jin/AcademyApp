package com.example.jung_jaejin.myproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Showtipofstudy extends AppCompatActivity {

    private String user_id;
    private String grade;
    private String classss;
    private int filenum;
    private int day;
    private int time;
    private int realday;
    private int currenttime = 4;
    private int getPlus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_showtipofstudy);
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        grade = intent.getStringExtra("grade");
        classss = intent.getStringExtra("class");
        filenum = intent.getIntExtra("filenum",0);
        day = intent.getIntExtra("day",0);
        time = intent.getIntExtra("time",0);
        realday = intent.getIntExtra("realday",0);
        getPlus = intent.getIntExtra("plus",0);

        Message message = handler.obtainMessage(1);
        handler.sendMessageDelayed(message,1000);
    }
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case 1:
                currenttime--;
                if(currenttime == 0){
                    removeMessages(1);
                    Intent intent = new Intent(getApplicationContext(),Study1.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("grade",grade);
                    intent.putExtra("class",classss);
                    intent.putExtra("filenum",filenum);
                    intent.putExtra("day",day);
                    intent.putExtra("time",time);
                    intent.putExtra("realday",realday);
                    intent.putExtra("plus",getPlus);
                    startActivity(intent);
                }
                else{
                    Message message = handler.obtainMessage(1);
                    handler.sendMessageDelayed(message,1000);
                }
                    break;

                case 2:
                    removeMessages(1);
                    break;
            }
        }
    };
    @Override
    public void onBackPressed() {

        // Alert을 이용해 종료시키기
        AlertDialog.Builder dialog = new AlertDialog.Builder(Showtipofstudy.this);
        dialog  .setTitle("공부 종료")
                .setMessage("공부 시작화면으로 돌아가시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Message message = handler.obtainMessage(2);
                        handler.sendMessage(message);
                        Intent intent = new Intent(getApplicationContext(), MonthlyTestStart.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("grade",grade);
                        intent.putExtra("class",classss);
                        intent.putExtra("filenum",filenum);
                        intent.putExtra("day",day);
                        intent.putExtra("realday",realday);
                        intent.putExtra("time",time);
                        intent.putExtra("plus",getPlus);
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
