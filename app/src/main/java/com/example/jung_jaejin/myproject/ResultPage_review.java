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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ResultPage_review extends AppCompatActivity {

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
        setContentView(R.layout.activity_result_page_review);

        Intent intent = getIntent();

        NumOfRight = intent.getIntExtra("numOfRight", 0);
        NumOfProblem = intent.getIntExtra("numOfProblem",120);
        IsPass = intent.getIntExtra("pass",0);


        howmuch = (TextView) findViewById(R.id.Howmuch);
        passorfail = (TextView) findViewById(R.id.PassOrFail);
        result = (Button)findViewById(R.id.result);



        if(IsPass == 1){
            howmuch.setText(NumOfRight + "/" + NumOfProblem);
            passorfail.setText("PASS");
            result.setText("완료");

            try{
                FileOutputStream last_fos = openFileOutput("_data.txt",Context.MODE_PRIVATE);

                PrintWriter writer = new PrintWriter(last_fos);

                writer.print("");
                writer.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        else{
            howmuch.setText(NumOfRight + "/" + NumOfProblem);
            passorfail.setText("Fail");
            result.setText("다시하기");
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
