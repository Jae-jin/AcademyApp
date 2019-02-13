package com.example.jung_jaejin.myproject;

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


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import static android.speech.tts.TextToSpeech.ERROR;


public class Study1 extends AppCompatActivity implements View.OnClickListener {
    private TextView word;
    private TextView mean;
    private TextView numofword;
    private Button gonext;
    private TextToSpeech tts;
    private ArrayList<String> wordlist = new ArrayList<>();//영어단어 집어 넣는 리스트
    private ArrayList<String> meanlist = new ArrayList<>();//영어의미 집어 넣는 리스트
    private int i = 0;//
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
        setContentView(R.layout.activity_study1);
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        grade = intent.getStringExtra("grade");
        classss = intent.getStringExtra("class");
        filenum = intent.getIntExtra("filenum",0);
        day = intent.getIntExtra("day",0);
        time = intent.getIntExtra("time",0);
        realday = intent.getIntExtra("realday",0);
        gonext = (Button)findViewById(R.id.buttonnext);
        word = (TextView) findViewById(R.id.word);
        mean = (TextView) findViewById(R.id.mean);
        numofword = (TextView) findViewById(R.id.NumOfWord);
        gonext.setOnClickListener(this);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR){
                    tts.setLanguage(Locale.ENGLISH);
                }
            }
        });//영어 단어 소리 내기 위한 객체 생성
        try {
            AssetManager am = getAssets();
            InputStream is = null;
            switch (filenum){
                case 1:
                    is = am.open("중1-중2 Day 1-41 단어.xls");
                    break;
                case 2:
                    is = am.open("중3-고1 Day 1-66 단어.xls");
                    break;
                case 3:
                    is = am.open("고2-고3 Day 1-36 단어.xls");
                    break;
                case 4:
                    is = am.open("수능 Day 1-28 단어.xls");
                    break;
                case 5:
                    is = am.open("해커스 텝스 보카 600 day 1-10.xls");
                    break;
                case 6:
                    is = am.open("해커스 텝스 보카 800 day 1-25.xls");
                    break;
                case 7:
                    is = am.open("해커스 텝스 보카 900 day 1-8.xls");
                    break;
                case 8:
                    is = am.open("해커스 텝스 보카 주요단어 day 1-12.xls");
                    break;

            }
            Workbook wb = Workbook.getWorkbook(is);
            if(wb != null) {
                Sheet sheet = wb.getSheet(day);   // 시트 불러오기
                if(sheet != null) {

                    int colTotal = sheet.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet.getColumn(colTotal - 1).length;

                    StringBuilder sb;
                    for (int row = rowIndexStart; row < rowTotal; row++) {

                        String getword = sheet.getCell(0, row).getContents();
                        String getmean = sheet.getCell(1, row).getContents();
                        wordlist.add(getword);
                        meanlist.add(getmean);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

        Message message = handler.obtainMessage(1);
        handler.sendMessageDelayed(message, 1000);//1초 대기

    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case 1:
                        Message message = handler.obtainMessage(2);
                        handler.sendMessageDelayed(message, 2000);//2초 있다가 단어를 불러온다.

                    break;
                case 2:
                        if(i<wordlist.size()) {//단어 아직 남았을 때
                            word.setText(wordlist.get(i));
                            mean.setText("");//뜻칸 비워두고
                            numofword.setText(i + 1 + "/" + wordlist.size());//i는 몇 번째 단어인지
                            Message message1 = handler.obtainMessage(3);
                            tts.setSpeechRate(0.8f);
                            tts.speak(wordlist.get(i),TextToSpeech.QUEUE_FLUSH,null);//음성 발사
                            handler.sendMessageDelayed(message1, 2000);//2초 있다가 뜻까지 같이 표시한다.
                        }
                    else {//단어 다 봤을 경우
                            removeMessages(1);
                            removeMessages(2);
                            removeMessages(3);
                            Intent intent = new Intent(getApplicationContext(),Starttest.class);//다음 화면으로 넘어간다.
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("grade",grade);
                            intent.putExtra("class",classss);
                            intent.putExtra("filenum",filenum);
                            intent.putExtra("day",day);
                            intent.putExtra("time",time);
                            intent.putExtra("realday",realday);
                            tts.shutdown();
                            startActivity(intent);
                        }
                            break;

                case 3:
                            word.setText(wordlist.get(i));
                            mean.setText(meanlist.get(i));//뜻표시
                            numofword.setText(i + 1 + "/" + wordlist.size());
                            tts.speak(wordlist.get(i),TextToSpeech.QUEUE_FLUSH,null);
                            Message message2 = handler.obtainMessage(2);
                            i++;//다음 단어로~
                            handler.sendMessageDelayed(message2, 2000);//2초 있다가 다음 단어 표시

                    break;

                case 4:
                    removeMessages(1);
                    removeMessages(2);
                    removeMessages(3);
                    Intent intent = new Intent(getApplicationContext(),Starttest.class);//다음 화면으로 넘어간다.
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("grade",grade);
                    intent.putExtra("class",classss);
                    intent.putExtra("filenum",filenum);
                    intent.putExtra("day",day);
                    intent.putExtra("realday",realday);
                    intent.putExtra("time",time);
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
        AlertDialog.Builder dialog = new AlertDialog.Builder(Study1.this);
        dialog  .setTitle("학습 종료")
                .setMessage("학습 시작화면으로 돌아가시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Message message = handler.obtainMessage(5);
                        handler.sendMessage(message);
                        Intent intent = new Intent(getApplicationContext(),Studystart.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("grade",grade);
                        intent.putExtra("class",classss);
                        intent.putExtra("filenum",filenum);
                        intent.putExtra("day",day);
                        intent.putExtra("realday",realday);
                        intent.putExtra("time",time);
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
