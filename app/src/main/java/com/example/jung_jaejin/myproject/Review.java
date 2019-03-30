package com.example.jung_jaejin.myproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Locale;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import static android.speech.tts.TextToSpeech.ERROR;

public class Review extends AppCompatActivity implements View.OnClickListener {
    private Button finishbutton;
    private TextView word;
    private TextView mean;
    private TextView numofword;
    private String getId;
    private String getGrade;
    private String getClass;
    private int getFilenum;
    private int getday;
    private int getscore;
    private int getPlus;
    private TextToSpeech tts;
    private ArrayList<String> wordlist = new ArrayList<>();//영어단어 집어 넣는 리스트
    private ArrayList<String> meanlist = new ArrayList<>();//영어의미 집어 넣는 리스트
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_review);
        Intent intent = getIntent();
        getId = intent.getStringExtra("user_id");
        getGrade = intent.getStringExtra("grade");
        getClass = intent.getStringExtra("class");
        getFilenum = intent.getIntExtra("filenum",0);
        getday = intent.getIntExtra("realday",0);
        getscore = intent.getIntExtra("maxscore",0);
        getPlus = intent.getIntExtra("plus",0);
        finishbutton = (Button) findViewById(R.id.buttonnextre);
        finishbutton.setOnClickListener(this);
        word = (TextView) findViewById(R.id.wordR);
        mean = (TextView) findViewById(R.id.meanR);
        numofword = (TextView) findViewById(R.id.NumOfWordR);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR){
                    tts.setLanguage(Locale.ENGLISH);
                }
            }
        });//영어 단어 소리 내기 위한 객체 생성


            try {
                StringBuffer data = new StringBuffer();
                FileInputStream fis = openFileInput("data.txt");
                BufferedReader buffer = new BufferedReader(new InputStreamReader(fis));
                String str = buffer.readLine();
                if(str != null) {
                    while (str != null) {
                        data.append(str + "\n");
                        Log.d("TEST  ", str);
                        String[] array = str.split(" ", 2);
                        wordlist.add(array[0]);
                        meanlist.add(array[1]);
                        Log.d("word:", array[0]);
                        Log.d("mean:", array[1]);
                        str = buffer.readLine();
                    }
                }
                else{
                    Log.d("Tag : ","단어없다.");
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Review.this);
                    dialog.setTitle("복습 단어가 없음.")
                            .setMessage("복습할 단어가 없습니다.")
                            .setPositiveButton("복습할 단어가 없습니다.", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(),month.class);
                                    intent.putExtra("user_id", getId);
                                    intent.putExtra("grade",getGrade);
                                    intent.putExtra("class",getClass);
                                    intent.putExtra("filenum",getFilenum);
                                    intent.putExtra("day",getday);
                                    intent.putExtra("plus",getPlus);
                                    startActivity(intent);
                                }
                            }).create().show();
                }
                buffer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        Message message = handler.obtainMessage(1);
        handler.sendMessageDelayed(message, 1000);//1초 대기

    }
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what) {
                case 1:
                    Message message = handler.obtainMessage(2);
                    handler.sendMessageDelayed(message, 2000);//2초 있다가 단어를 불러온다.

                    break;
                case 2:
                    if (i < wordlist.size()) {//단어 아직 남았을 때
                        word.setText(wordlist.get(i));
                        mean.setText("");//뜻칸 비워두고
                        numofword.setText(i + 1 + "/" + wordlist.size());//i는 몇 번째 단어인지
                        Message message1 = handler.obtainMessage(3);
                        tts.speak(wordlist.get(i), TextToSpeech.QUEUE_FLUSH, null);//음성 발사
                        handler.sendMessageDelayed(message1, 2000);//2초 있다가 뜻까지 같이 표시한다.
                    } else {
                        if(getday % 6 == 0){
                        try {
                            FileOutputStream last_fos = openFileOutput("data.txt", Context.MODE_PRIVATE);

                            PrintWriter writer = new PrintWriter(last_fos);

                            writer.print("");
                            writer.close();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                            try {
                                FileOutputStream last_fos = openFileOutput("data.txt", Context.MODE_PRIVATE);

                                PrintWriter writer = new PrintWriter(last_fos);

                                writer.print("");
                                writer.close();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                        removeMessages(3);
                        Intent intent = new Intent(getApplicationContext(), month.class);//다음 화면으로 넘어간다.
                            intent.putExtra("user_id", getId);
                            intent.putExtra("grade",getGrade);
                            intent.putExtra("class",getClass);
                            intent.putExtra("filenum",getFilenum);
                            intent.putExtra("day",getday);
                            intent.putExtra("plus",getPlus);
                            tts.shutdown();
                            startActivity(intent);
                    }
            }
                    break;

                case 3:
                    word.setText(wordlist.get(i));
                    mean.setText(meanlist.get(i));//뜻표시
                    numofword.setText(i + 1 + "/" + wordlist.size());
                    tts.setSpeechRate(0.8f);
                    tts.speak(wordlist.get(i),TextToSpeech.QUEUE_FLUSH,null);
                    Message message2 = handler.obtainMessage(2);
                    i++;//다음 단어로~
                    handler.sendMessageDelayed(message2, 2000);//2초 있다가 다음 단어 표시
                    break;

                case 4:
                    removeMessages(1);
                    removeMessages(2);
                    removeMessages(3);
                    Intent intent = new Intent(getApplicationContext(),month.class);//다음 화면으로 넘어간다.
                    intent.putExtra("user_id", getId);
                    intent.putExtra("grade",getGrade);
                    intent.putExtra("class",getClass);
                    intent.putExtra("filenum",getFilenum);
                    intent.putExtra("day",getday);
                    intent.putExtra("plus",getPlus);
                    tts.shutdown();
                    startActivity(intent);
                    break;

                case 5:
                    removeMessages(1);
                    removeMessages(2);
                    removeMessages(3);
                    removeMessages(4);
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        Message message = handler.obtainMessage(4);
        handler.sendMessageDelayed(message, 1000);
    }
    @Override
    public void onBackPressed() {

        // Alert을 이용해 종료시키기
        AlertDialog.Builder dialog = new AlertDialog.Builder(Review.this);
        dialog  .setTitle("시험 종료")
                .setMessage("복습 준비 화면으로 돌아가시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Message message = handler.obtainMessage(5);
                        handler.sendMessage(message);
                        Intent intent = new Intent(getApplicationContext(),Studystart_review.class);
                        intent.putExtra("user_id", getId);
                        intent.putExtra("grade",getGrade);
                        intent.putExtra("class",getClass);
                        intent.putExtra("filenum",getFilenum);
                        intent.putExtra("day",getday-getPlus);
                        intent.putExtra("maxscore",getscore);
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
