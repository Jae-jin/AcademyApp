package com.example.jung_jaejin.myproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ResultPage extends AppCompatActivity {
    private Button result;
    private int NumOfRight;
    private int NumOfProblem;
    private int IsPass;
    private TextView howmuch;
    private TextView passorfail;

    private ArrayList<String> wrong_wordlist;
    private ArrayList<String>  wrong_meanlist;

    private String temp_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);
        Intent intent = getIntent();

        NumOfRight = intent.getIntExtra("numOfRight", 0);
        NumOfProblem = intent.getIntExtra("numOfProblem",120);
        IsPass = intent.getIntExtra("pass",0);

        wrong_wordlist = intent.getStringArrayListExtra("wrong_wordlist");
        wrong_meanlist = intent.getStringArrayListExtra("wrong_meanlist");



        howmuch = (TextView) findViewById(R.id.Howmuch);
        passorfail = (TextView) findViewById(R.id.PassOrFail);
        result = (Button)findViewById(R.id.result);



        if(IsPass == 1){
            howmuch.setText(NumOfRight + "/" + NumOfProblem);
            passorfail.setText("PASS");
            result.setText("완료");

            // _data.txt 파일 읽기
            try{
                StringBuffer data = new StringBuffer();
                FileInputStream fis = openFileInput("_data.txt");
                BufferedReader buffer = new BufferedReader(new InputStreamReader(fis));
                String str = buffer.readLine();
                while(str != null){
                    data.append(str+"\n");
                    str = buffer.readLine();
                }
                Log.d("TEST: ", String.valueOf(data));
                temp_data = String.valueOf(data);
                Log.d("TEST: ",temp_data);
                buffer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // data.txt 에 최종 복습 파일 쓰기, Context.MODE_APPEND 경우 이어 쓰기
            try{
                FileOutputStream last_fos = openFileOutput("data.txt",Context.MODE_APPEND);

                PrintWriter writer = new PrintWriter(last_fos);

                writer.print(temp_data);
                writer.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        else{
            howmuch.setText(NumOfRight + "/" + NumOfProblem);
            passorfail.setText("Fail");
            result.setText("다시하기");
//            Log.d("틀린 단어:", wrong_wordlist.get(0));
//            Log.d("틀린 단어:", wrong_meanlist.get(0));

            // 파일 쓰기, Context.MODE_PRIVATE 경우 새로 쓰기

            try {
                FileOutputStream fos = openFileOutput("_data.txt", Context.MODE_PRIVATE);

                PrintWriter writer = new PrintWriter(fos);
                for(int i =0;i<NumOfProblem-NumOfRight;i++)
                {
                    writer.println(wrong_wordlist.get(i)+" "+wrong_meanlist.get(i));
//                    Log.d("확인:",wrong_wordlist.get(i));
                }
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        result.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),month.class);
                startActivity(intent);
            }
        });
    }
}
