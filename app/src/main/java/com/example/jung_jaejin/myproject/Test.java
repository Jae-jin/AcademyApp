package com.example.jung_jaejin.myproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private int choice;//답 선택
    private int realanswer;//문제의 답
    private int NumOfRight;//몇 개 맞았는지
    private int NumOfProblem;//전체 문제 수
    private int Numrandom;
    private int SelectProblem;//어떤 문제를 낼 지 랜덤으로 고른다.
    private int limittime=5;//한문제당 5초
    private int currenttime;
    private int fulltime = 30;//전체 제한 시간
    private int gonext=0;//다음 문제로 넘어갈 수 있게
    private int start;//문제 몇 번 푸는지

    private ArrayList<String> wordlist = new ArrayList<>();
    private ArrayList<String> meanlist = new ArrayList<>();
    private ArrayList<Integer> randomlist = new ArrayList<>();
    private ArrayList<Integer> problemlist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
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
        });//1번 클릭시
        answer2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                gonext = 1;
                choice = 1;
            }
        });//2번 클릭시
        answer3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                gonext = 1;
                choice = 2;
            }
        });//3번 클릭시
        answer4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                gonext = 1;
                choice = 3;
            }
        });//4번 클릭시

        try {
            AssetManager am = getAssets();//assets파일에서 불러오기 위한 작업
            InputStream is = am.open("English1.xls");
            Workbook wb = Workbook.getWorkbook(is);
            if(wb != null) {
                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                if(sheet != null) {

                    int colTotal = sheet.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet.getColumn(colTotal - 1).length;

                    StringBuilder sb;
                    for (int row = rowIndexStart; row < rowTotal; row++) {

                        String getword = sheet.getCell(0, row).getContents();
                        String getmean = sheet.getCell(1, row).getContents();
                        wordlist.add(getword);//엑셀에서 불러온 영어단어들을 리스트에 집어넣는다.
                        meanlist.add(getmean);//엑셀에서 불러온 영어단어의 뜻들을 리스트에 집어넣는다.
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }//엑셀 불러오는 부분

        currenttime = fulltime;//시작할 때 현재 시간과 제한시간을 똑같이 둔다.
        timer.setText(currenttime+"/"+fulltime);
        NumOfProblem = wordlist.size();//전체 문제 수를 단어의 개수로 파악한다.

        for(int start1 = 0; start1 < wordlist.size();start1++) {
            randomlist.add(start1);//문제를 만들기 위한 리스트를 만든다.
        }
        start=0;

        Collections.shuffle(randomlist);//문제 순서가 랜덤으로 되게 하기 위해서 리스트를 shuffle한다.

        Message message = handler.obtainMessage(1);
        handler.sendMessageDelayed(message, 1000);//제한시간 감소하는 핸들러(1초 뒤에 실행)
        Message message1 = handler.obtainMessage(2);//문제를 표시하는 핸들러
        handler.sendMessageDelayed(message1, 1000);

        }





    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case 1:
                    currenttime--;//현재 남은 시간 1초 줄이고
                    limittime--;//문제당 남은시간 1초 줄이고
                    timer.setText(currenttime+"/"+fulltime);//표시한다.
                    if(currenttime == 0){//총 주어진 시간을 다 쓴 경우에 자동으로 다음 액티비티로 넘어간다.
                        Intent intent = new Intent(getApplicationContext(),ResultPage.class);
                        intent.putExtra("numOfRight",NumOfRight);//몇개 맞았는지 넘기고
                        intent.putExtra("numOfProblem",NumOfProblem);//총 문제 몇개인지 넘기고
                        if(NumOfProblem == NumOfRight) {//다 맞았을 경우
                            intent.putExtra("pass", 1);
                        }
                        else{//하나라도 틀린 경우
                            intent.putExtra("pass",0);
                        }
                        startActivity(intent);//다음으로 넘어간다.
                    }
                    else {
                        Message message = handler.obtainMessage(1);
                        handler.sendMessageDelayed(message, 1000);//제한시간이 0초가 아니면 시간을 줄이기 위해서 1초뒤에 다시 이 핸들러를 소환한다.
                    }
                    break;

                case 2:
                    if(start <= wordlist.size()-1) {//문제를 다 풀지 않았을 시에
                        Random random1 = new Random();
                        SelectProblem = random1.nextInt(2);//문제 유형을 고르기 위한 난수 생성
                       process.setText(start + 1 + "/" + NumOfProblem);
                       problemlist.clear();
                      if (SelectProblem == 0) {//뜻 고르는 문제

                            problemlist.add(randomlist.get(start));//보기 리스트에 현재 문제의 답이 들어가있다.
                            Numrandom = random1.nextInt(wordlist.size());//보기를 만들기 위해 답이 아닌 다른 보기를 집어넣는다.
                            while (problemlist.contains(Numrandom) == true) {//보기 중복을 피하기 위한 단계
                                Numrandom = random1.nextInt(wordlist.size());
                            }problemlist.add(Numrandom);
                            Numrandom = random1.nextInt(wordlist.size());//보기를 만들기 위해 답이 아닌 다른 보기를 집어넣는다.
                            while (problemlist.contains(Numrandom) == true) {//보기 중복을 피하기 위한 단계
                                Numrandom = random1.nextInt(wordlist.size());
                            }problemlist.add(Numrandom);

                            Numrandom = random1.nextInt(wordlist.size());//보기를 만들기 위해 답이 아닌 다른 보기를 집어넣는다.
                            while (problemlist.contains(Numrandom) == true) {//보기 중복을 피하기 위한 단계
                                Numrandom = random1.nextInt(wordlist.size());
                            }problemlist.add(Numrandom);
                            Collections.shuffle(problemlist);
                            problem.setText(wordlist.get(randomlist.get(start)));//현재 문제 표시
                            realanswer = problemlist.indexOf(randomlist.get(start));//답이 몇번이지 파악
                            answer1.setText("1. " + meanlist.get(problemlist.get(0)));//문제 1번보기 세팅
                            answer2.setText("2. " + meanlist.get(problemlist.get(1)));//문제 2번보기 세팅
                            answer3.setText("3. " + meanlist.get(problemlist.get(2)));//문제 3번보기 세팅
                            answer4.setText("4. " + meanlist.get(problemlist.get(3)));//문제 4번보기 세팅

                          Message message2 = handler.obtainMessage(3);
                          handler.sendMessage(message2);

                    }
                        else {//단어 스펠링 고르는 문제(밑에는 단어 뜻 고르는 부분이랑 똑같다.)

                          problemlist.add(randomlist.get(start));
                          Numrandom = random1.nextInt(wordlist.size());
                          while (problemlist.contains(Numrandom) == true) {
                              Numrandom = random1.nextInt(wordlist.size());
                          }problemlist.add(Numrandom);
                          Numrandom = random1.nextInt(wordlist.size());
                          while (problemlist.contains(Numrandom) == true) {
                              Numrandom = random1.nextInt(wordlist.size());
                          }problemlist.add(Numrandom);

                          Numrandom = random1.nextInt(wordlist.size());
                          while (problemlist.contains(Numrandom) == true) {
                              Numrandom = random1.nextInt(wordlist.size());
                          }problemlist.add(Numrandom);
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

                    }
                    else {//문제를 다 풀었을 시에 다음 액티비티로 넘긴다.
                        Intent intent = new Intent(getApplicationContext(), ResultPage.class);
                        intent.putExtra("numOfRight", NumOfRight);
                        intent.putExtra("numOfProblem", NumOfProblem);
                        if (NumOfProblem == NumOfRight) {
                            intent.putExtra("pass", 1);
                        } else {
                            intent.putExtra("pass", 0);
                        }
                        startActivity(intent);
                    }
                    break;

                case 3:
                    if (gonext == 1 && limittime > 0) {//제한시간 내에 문제를 풀었을 시에
                        if (choice == realanswer) {//문제를 풀었는데 맞았을 경우
                            NumOfRight++;//문제 맞은 개수 증가
                            limittime = 5;//문제 제한시간 초기화
                            gonext = 0;//다음 문제에서 바로 안넘어가게 설정
                            start++;//문제 번호 증가
                            Message message1 = handler.obtainMessage(2);//다음문제로
                            handler.sendMessage(message1);
                        }
                    else{//제한시간 내에 풀었는데 틀린 경우
                            limittime = 5;//문제 제한시간 초기화
                            gonext = 0;//다음 문제에서 바로 안넘어가게 설정
                            start++;//문제 번호 증가
                            Message message1 = handler.obtainMessage(2);//다음문제로
                            handler.sendMessage(message1);
                        }
                    }


                    if(gonext == 0 && limittime > 0) {//아직 문제풀 시간이 남은 경우
                        Message message2 = handler.obtainMessage(3);
                        handler.sendMessage(message2);//3번째 핸들러를 불러 문제를 풀었는지 못 풀었는지 확인한다.
                    }
                    if(limittime == 0){//제한 시간 내에 못 푼 경우
                        start++;//문제 번호 증가
                        limittime = 5;//문제당 제한 시간 초기화
                        Message message1 = handler.obtainMessage(2);//다음 문제로 넘어간다.
                        handler.sendMessage(message1);
                    }

                    break;
            }

        }
    };

}
