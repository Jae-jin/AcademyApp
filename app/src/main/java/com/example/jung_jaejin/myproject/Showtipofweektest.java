package com.example.jung_jaejin.myproject;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Showtipofweektest extends AppCompatActivity {

    private String user_id;
    private String grade;
    private String classss;
    private int filenum;
    private int day;
    private int time;
    private int realday;
    private int currenttime = 6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtipofweektest);
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        grade = intent.getStringExtra("grade");
        classss = intent.getStringExtra("class");
        filenum = intent.getIntExtra("filenum",0);
        day = intent.getIntExtra("day",0);
        time = intent.getIntExtra("time",0);
        realday = intent.getIntExtra("realday",0);
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
                        Intent intent = new Intent(getApplicationContext(),WeeklyTest.class);
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
                        Message message = handler.obtainMessage(1);
                        handler.sendMessageDelayed(message,1000);
                    }
                    break;
            }
        }
    };
}
