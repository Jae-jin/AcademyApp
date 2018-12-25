package com.example.jung_jaejin.myproject;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Test_review extends AppCompatActivity {

    private TextView timer;
    private TextView process;
    private TextView problem;
    private TextView answer1;
    private TextView answer2;
    private TextView answer3;
    private TextView answer4;
    private int choice;
    private int realanswer;
    private int NumOfRight;
    private int NumOfProblem;
    private int Numrandom;
    private int SelectProblem;
    private int limittime=5;
    private int currenttime;
    private int fulltime = 30;
    private int gonext=0;
    private int start;

    private ArrayList<String> wordlist = new ArrayList<>();
    private ArrayList<String> meanlist = new ArrayList<>();
    private ArrayList<Integer> randomlist = new ArrayList<>();
    private ArrayList<Integer> problemlist = new ArrayList<>();

    private ArrayList<String>  wrong_wordlist = new ArrayList<>(); //틀린 단어가 저장될 리스트
    private ArrayList<String>  wrong_meanlist = new ArrayList<>(); //틀린 단어 의미가 저장될 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_review);
        Random random = new Random();

        timer = (TextView)findViewById(R.id.timer);
        process = (TextView)findViewById(R.id.process);
        problem = (TextView)findViewById(R.id.problem);
        answer1 = (TextView)findViewById(R.id.answer1);
        answer2 = (TextView)findViewById(R.id.answer2);
        answer3 = (TextView)findViewById(R.id.answer3);
        answer4 = (TextView)findViewById(R.id.answer4);

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

        try{
            StringBuffer data = new StringBuffer();
            FileInputStream fis = openFileInput("_data.txt");
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fis));
            String str = buffer.readLine();
            while(str != null){
                data.append(str+"\n");
                String[] array = str.split(" ");
                wordlist.add(array[0]);
                meanlist.add(array[1]);
                Log.d("word:",array[0]);
                Log.d("mean:",array[1]);
                str = buffer.readLine();
            }
            buffer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        currenttime = fulltime;
        timer.setText(currenttime+"/"+fulltime);
        NumOfProblem = wordlist.size();

        for(int start1 = 0; start1 < wordlist.size();start1++) {
            randomlist.add(start1);
        }
        start=0;

        Collections.shuffle(randomlist);

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
                        currenttime--;
                        limittime--;
                        timer.setText(currenttime + "/" + fulltime);
                        if (currenttime == 0) {
                            Intent intent = new Intent(getApplicationContext(), ResultPage.class);
                            intent.putExtra("numOfRight", NumOfRight);
                            intent.putExtra("numOfProblem", NumOfProblem);
                            if (NumOfProblem == NumOfRight) {
                                intent.putExtra("pass", 1);
                            } else {
                                intent.putExtra("pass", 0);
                            }
                            startActivity(intent);
                        } else {
                            Message message = handler.obtainMessage(1);
                            handler.sendMessageDelayed(message, 1000);
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
                                answer1.setText("1. " + meanlist.get(problemlist.get(0)));
                                answer2.setText("2. " + meanlist.get(problemlist.get(1)));
                                answer3.setText("3. " + meanlist.get(problemlist.get(2)));
                                answer4.setText("4. " + meanlist.get(problemlist.get(3)));

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
                                answer1.setText("1. " + wordlist.get(problemlist.get(0)));
                                answer2.setText("2. " + wordlist.get(problemlist.get(1)));
                                answer3.setText("3. " + wordlist.get(problemlist.get(2)));
                                answer4.setText("4. " + wordlist.get(problemlist.get(3)));

                                Message message1 = handler.obtainMessage(3);
                                handler.sendMessage(message1);
                            }

                        } else {
                            Intent intent = new Intent(getApplicationContext(), ResultPage.class);
                            intent.putExtra("numOfRight", NumOfRight);
                            intent.putExtra("numOfProblem", NumOfProblem);
                            intent.putExtra("wrong_meanlist", wrong_meanlist);
                            intent.putExtra("wrong_wordlist", wrong_wordlist);

                            if (NumOfProblem == NumOfRight) {
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
                                limittime = 5;
                                gonext = 0;
                                start++;
                                Message message1 = handler.obtainMessage(2);
                                handler.sendMessage(message1);
                            } else {
                                limittime = 5;
                                gonext = 0;
                                //Log.d("값",wordlist.get(randomlist.get(start)));
                                //Log.d("값",meanlist.get(randomlist.get(start)));

                                wrong_meanlist.add(meanlist.get(randomlist.get(start)));
                                wrong_wordlist.add(wordlist.get(randomlist.get(start)));

                                start++;



                                Message message1 = handler.obtainMessage(2);
                                handler.sendMessage(message1);


                            }
                        }


                        if (gonext == 0 && limittime > 0) {
                            Message message2 = handler.obtainMessage(3);
                            handler.sendMessage(message2);
                        }

                        if (limittime == 0) {

                            wrong_meanlist.add(meanlist.get(randomlist.get(start)));
                            wrong_wordlist.add(wordlist.get(randomlist.get(start)));

                            start++;
                            limittime = 5;
                            Message message1 = handler.obtainMessage(2);
                            handler.sendMessage(message1);

                        }

                        break;
                }

            }
        };
    }
}
