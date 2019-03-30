package com.example.jung_jaejin.myproject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Color;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Test extends AppCompatActivity {
    private TextView timer;
    private TextView process;
    private TextView problem;
    private TextView answer1;
    private TextView answer2;
    private TextView answer3;
    private TextView answer4;
    private TextView frame1;
    private TextView frame2;
    private TextView frame3;
    private TextView frame4;

    private int choice;
    private int realanswer;
    private int NumOfRight=0;
    private int NumOfProblem;
    private int Numrandom;
    private int realday;
    private int SelectProblem;
    private float limittime=7;
    private float currenttime;
    private float fulltime;
    private int gonext=0;
    private int start;
    private String user_id;
    private String grade;
    private String classss;
    private int filenum;
    private int day;
    private int time;
    private int getPlus;
    private ArrayList<String> wordlist = new ArrayList<>();
    private ArrayList<String> meanlist = new ArrayList<>();
    private ArrayList<Integer> randomlist = new ArrayList<>();
    private ArrayList<Integer> problemlist = new ArrayList<>();

    private ArrayList<String>  wrong_wordlist = new ArrayList<>(); //틀린 단어가 저장될 리스트
    private ArrayList<String>  wrong_meanlist = new ArrayList<>(); //틀린 단어 의미가 저장될 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_test);
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        grade = intent.getStringExtra("grade");
        classss = intent.getStringExtra("class");
        filenum = intent.getIntExtra("filenum",0);
        day = intent.getIntExtra("day",0);
        time = intent.getIntExtra("time",0);
        realday = intent.getIntExtra("realday",0);
        getPlus = intent.getIntExtra("plus",0);
        Random random = new Random();
        timer = (TextView)findViewById(R.id.timer);
        process = (TextView)findViewById(R.id.process);

        problem = (TextView)findViewById(R.id.problem);
        answer1 = (TextView)findViewById(R.id.answer1);
        answer2 = (TextView)findViewById(R.id.answer2);
        answer3 = (TextView)findViewById(R.id.answer3);
        answer4 = (TextView)findViewById(R.id.answer4);
        frame1 = (TextView)findViewById(R.id.frame1);
        frame2 = (TextView)findViewById(R.id.frame2);
        frame3 = (TextView)findViewById(R.id.frame3);
        frame4 = (TextView)findViewById(R.id.frame4);
        problem.setBackgroundResource(R.drawable.edge1);
        frame1.setBackgroundResource(R.drawable.edge4);
        frame2.setBackgroundResource(R.drawable.edge4);
        frame3.setBackgroundResource(R.drawable.edge4);
        frame4.setBackgroundResource(R.drawable.edge4);

        answer1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                gonext = 1;
                choice = 0;
            }
        });
        answer2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                gonext = 1;
                choice = 1;
            }
        });
        answer3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                gonext = 1;
                choice = 2;
            }
        });
        answer4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                gonext = 1;
                choice = 3;
            }
        });

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


        NumOfProblem = wordlist.size();

        for(int start1 = 0; start1 < wordlist.size();start1++) {
            randomlist.add(start1);
        }
        start=0;

        Collections.shuffle(randomlist);
        fulltime = (float) (NumOfProblem*8.5);
        currenttime = fulltime;
        timer.setText((int)currenttime+"/"+(int)fulltime);
        Message message = handler.obtainMessage(1);
        handler.sendMessageDelayed(message, 1000);
        Message message1 = handler.obtainMessage(2);
        handler.sendMessageDelayed(message1, 1000);

    }



    private Handler handler;

    {
        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        currenttime = (float) (currenttime - 0.5);
                        limittime = (float)(limittime - 0.5);
                        timer.setText((int)currenttime + "/" + (int)fulltime);
                        if (currenttime == -1) {
                            removeMessages(1);
                            removeMessages(2);
                            removeMessages(3);
                            Intent intent = new Intent(getApplicationContext(), ResultPage.class);
                            intent.putExtra("numOfRight", NumOfRight);
                            intent.putExtra("numOfProblem", NumOfProblem);
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("grade",grade);
                            intent.putExtra("class",classss);
                            intent.putExtra("filenum",filenum);
                            intent.putExtra("day",day);
                            intent.putExtra("time",time);
                            intent.putExtra("realday",realday);
                            intent.putExtra("wrong_meanlist", wrong_meanlist);
                            intent.putExtra("wrong_wordlist", wrong_wordlist);
                            intent.putExtra("plus",getPlus);
                            if (NumOfRight > 105) {
                                intent.putExtra("pass", 1);
                            } else {
                                intent.putExtra("pass", 0);
                            }
                            startActivity(intent);
                        } else {
                            Message message = handler.obtainMessage(1);
                            handler.sendMessageDelayed(message, 500);
                        }
                        break;

                    case 2:
                        if (start <= wordlist.size() - 1) {
                            Random random1 = new Random();
                            SelectProblem = random1.nextInt(2);
                            process.setText(start + 1 + "/" + NumOfProblem);
                            problemlist.clear();
                            if (SelectProblem == 0) {

                                problemlist.add(randomlist.get(start));
                                Numrandom = random1.nextInt(wordlist.size());
                                while (problemlist.contains(Numrandom) == true) {
                                    Numrandom = random1.nextInt(wordlist.size());
                                }
                                problemlist.add(Numrandom);
                                Numrandom = random1.nextInt(wordlist.size());
                                while (problemlist.contains(Numrandom) == true) {
                                    Numrandom = random1.nextInt(wordlist.size());
                                }
                                problemlist.add(Numrandom);

                                Numrandom = random1.nextInt(wordlist.size());
                                while (problemlist.contains(Numrandom) == true) {
                                    Numrandom = random1.nextInt(wordlist.size());
                                }
                                problemlist.add(Numrandom);
                                Collections.shuffle(problemlist);
                                problem.setText(wordlist.get(randomlist.get(start)));
                                realanswer = problemlist.indexOf(randomlist.get(start));
                                answer1.setText(meanlist.get(problemlist.get(0)));
                                answer2.setText(meanlist.get(problemlist.get(1)));
                                answer3.setText(meanlist.get(problemlist.get(2)));
                                answer4.setText(meanlist.get(problemlist.get(3)));

                                Message message2 = handler.obtainMessage(3);
                                handler.sendMessage(message2);

                            }//단어뜻 고르는 문제
                            else {

                                problemlist.add(randomlist.get(start));
                                Numrandom = random1.nextInt(wordlist.size());
                                while (problemlist.contains(Numrandom) == true) {
                                    Numrandom = random1.nextInt(wordlist.size());
                                }
                                problemlist.add(Numrandom);
                                Numrandom = random1.nextInt(wordlist.size());
                                while (problemlist.contains(Numrandom) == true) {
                                    Numrandom = random1.nextInt(wordlist.size());
                                }
                                problemlist.add(Numrandom);

                                Numrandom = random1.nextInt(wordlist.size());
                                while (problemlist.contains(Numrandom) == true) {
                                    Numrandom = random1.nextInt(wordlist.size());
                                }
                                problemlist.add(Numrandom);
                                Collections.shuffle(problemlist);
                                realanswer = problemlist.indexOf(randomlist.get(start));
                                problem.setText(meanlist.get(randomlist.get(start)));
                                answer1.setText( wordlist.get(problemlist.get(0)));
                                answer2.setText( wordlist.get(problemlist.get(1)));
                                answer3.setText( wordlist.get(problemlist.get(2)));
                                answer4.setText( wordlist.get(problemlist.get(3)));

                                Message message1 = handler.obtainMessage(3);
                                handler.sendMessage(message1);
                            }

                        } else {
                            removeMessages(1);
                            removeMessages(3);
                            Intent intent = new Intent(getApplicationContext(), ResultPage.class);
                            intent.putExtra("numOfRight", NumOfRight);
                            intent.putExtra("numOfProblem", NumOfProblem);
                            intent.putExtra("wrong_meanlist", wrong_meanlist);
                            intent.putExtra("wrong_wordlist", wrong_wordlist);
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("grade",grade);
                            intent.putExtra("class",classss);
                            intent.putExtra("filenum",filenum);
                            intent.putExtra("day",day);
                            intent.putExtra("time",time);
                            intent.putExtra("realday",realday);
                            intent.putExtra("plus",getPlus);
                            if (NumOfRight > 105) {
                                intent.putExtra("pass", 1);
                            } else {
                                intent.putExtra("pass", 0);
                            }
                            startActivity(intent);
                        }
                        break;

                    case 3:
                        if (gonext == 1 && limittime > 0) {
                            if (choice == realanswer) {
                                NumOfRight++;
                                limittime = (float) 8.5;
                                gonext = 0;
                                start++;
                                Message message1 = handler.obtainMessage(2);
                                handler.sendMessageDelayed(message1,1500);
                            } else {

                                if(start < meanlist.size()) {
                                    limittime = (float) 8.5;
                                    gonext = 0;
                                    //Log.d("값",wordlist.get(randomlist.get(start)));
                                    //Log.d("값",meanlist.get(randomlist.get(start)));

                                    wrong_meanlist.add(meanlist.get(randomlist.get(start)));
                                    wrong_wordlist.add(wordlist.get(randomlist.get(start)));

                                    start++;


                                    Message message1 = handler.obtainMessage(2);
                                    handler.sendMessageDelayed(message1,1500);
                                }

                            }
                        }


                        if (gonext == 0 && limittime > 0) {
                            Message message2 = handler.obtainMessage(3);
                            handler.sendMessage(message2);
                        }

                        if (limittime == 0) {
                            if(start < meanlist.size()) {
                                wrong_meanlist.add(meanlist.get(randomlist.get(start)));
                                wrong_wordlist.add(wordlist.get(randomlist.get(start)));

                                start++;
                                limittime = (float) 8.5;
                                Message message1 = handler.obtainMessage(2);
                                handler.sendMessageDelayed(message1,1500);
                            }
                        }

                        break;

                    case 4:

                        removeMessages(1);
                        removeMessages(2);
                        removeMessages(3);
                        removeMessages(4);
                        break;

                }

            }
        };
    }
    @Override
    public void onBackPressed() {

        // Alert을 이용해 종료시키기
        AlertDialog.Builder dialog = new AlertDialog.Builder(Test.this);
        dialog  .setTitle("시험 종료")
                .setMessage("일일 시험을 종료하시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Message message = handler.obtainMessage(4);
                        handler.sendMessage(message);
                        Intent intent = new Intent(getApplicationContext(),month.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("grade",grade);
                        intent.putExtra("class",classss);
                        intent.putExtra("filenum",filenum);
                        intent.putExtra("day",realday-getPlus);
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
