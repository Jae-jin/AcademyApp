package com.example.jung_jaejin.myproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class ResultPage extends AppCompatActivity {
    private Button result;
    private int NumOfRight;
    private int NumOfProblem;
    private int IsPass;
    private TextView howmuch;
    private TextView passorfail;
    private String user_id;
    private String grade;
    private String classss;
    private int filenum;
    private int day;
    private int realday;
    private int time;
    private int getPlus;
    private static String TAG = "ResultPage";
    private ArrayList<String> wrong_wordlist;
    private ArrayList<String>  wrong_meanlist;

    private String temp_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_result_page);
        Intent intent = getIntent();

        NumOfRight = intent.getIntExtra("numOfRight", 0);
        NumOfProblem = intent.getIntExtra("numOfProblem",120);
        IsPass = intent.getIntExtra("pass",0);
        user_id = intent.getStringExtra("user_id");
        grade = intent.getStringExtra("grade");
        classss = intent.getStringExtra("class");
        filenum = intent.getIntExtra("filenum",0);
        day = intent.getIntExtra("day",0);
        time = intent.getIntExtra("time",0);
        realday = intent.getIntExtra("realday",0);
        getPlus = intent.getIntExtra("plus",0);

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
//            try{
//                StringBuffer data = new StringBuffer();
//                FileInputStream fis = openFileInput("_data.txt");
//                BufferedReader buffer = new BufferedReader(new InputStreamReader(fis));
//                String str = buffer.readLine();
//                while(str != null){
//                    data.append(str+"\n");
//                    str = buffer.readLine();
//                }
//                Log.d("TEST: ", String.valueOf(data));
//                temp_data = String.valueOf(data);
//                Log.d("TEST: ",temp_data);
//                buffer.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            // data.txt 에 최종 복습 파일 쓰기, Context.MODE_APPEND 경우 이어 쓰기
//            try{
//                FileOutputStream last_fos = openFileOutput("data.txt",Context.MODE_APPEND);
//
//                PrintWriter writer = new PrintWriter(last_fos);
//
//                writer.print(temp_data);
//                writer.close();
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
        }

        else{
            howmuch.setText(NumOfRight + "/" + NumOfProblem);
            passorfail.setText("Fail");
            result.setText("완료");
//            Log.d("틀린 단어:", wrong_wordlist.get(0));
//            Log.d("틀린 단어:", wrong_meanlist.get(0));

            // 파일 쓰기, Context.MODE_PRIVATE 경우 새로 쓰기

//            try {
//                FileOutputStream fos = openFileOutput("_data.txt", Context.MODE_PRIVATE);
//
//                PrintWriter writer = new PrintWriter(fos);
//                for(int i =0;i<(NumOfProblem-NumOfRight);i++)
//                {
//                    writer.println(wrong_wordlist.get(i)+" "+wrong_meanlist.get(i));
////                    Log.d("확인:",wrong_wordlist.get(i));
//                }
//                writer.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
        }

        result.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                if(IsPass == 1 && (realday == day + 1)) {
                    gotomain1 gogo = new gotomain1();
                    gogo.execute(user_id, Integer.toString(day), Integer.toString(time + 1), "일", grade, classss, Integer.toString(NumOfRight));
                }
                else
                {
                    gotomain gogo = new gotomain();
                    gogo.execute(user_id, Integer.toString(day), Integer.toString(time + 1), "일", grade, classss, Integer.toString(NumOfRight));
                }
                }
        });
    }

    class gotomain extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            progressDialog = progressDialog.show(ResultPage.this, "Please Wait", null,
                    true, true);
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response - " + result);
            if(result.equals("success")==true){

                if((day % 3) == 2) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ResultPage.this);
                    dialog.setTitle("데이터베이스 입력 성공 및 주간 시험 보기")
                            .setPositiveButton("주간 시험 보기", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(),Weekteststart.class);
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
                            })
                            .setNegativeButton("주간 시험 안보기", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AlertDialog.Builder dialog11 = new AlertDialog.Builder(ResultPage.this);
                                    dialog11.setTitle("복습단어 기록")
                                            .setPositiveButton("기록하기", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    try {
                                                        FileOutputStream fos = openFileOutput("data.txt", Context.MODE_APPEND);

                                                        PrintWriter writer = new PrintWriter(fos);
                                                        for(int i =0;i<(NumOfProblem-NumOfRight);i++)
                                                        {
                                                            writer.println(wrong_wordlist.get(i)+" "+wrong_meanlist.get(i));
//                    Log.d("확인:",wrong_wordlist.get(i));
                                                        }
                                                        writer.close();
                                                    } catch (FileNotFoundException e) {
                                                        e.printStackTrace();
                                                    }
                                                    Intent intent = new Intent(getApplicationContext(),month.class);
                                                    intent.putExtra("user_id", user_id);
                                                    intent.putExtra("grade",grade);
                                                    intent.putExtra("class",classss);
                                                    intent.putExtra("filenum",filenum);
                                                    intent.putExtra("day",realday);
                                                    intent.putExtra("plus",getPlus);
                                                    startActivity(intent);


                                                }
                                            })
                                            .setNegativeButton("기록 안하기", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                    Intent intent = new Intent(getApplicationContext(),month.class);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("grade",grade);
                                    intent.putExtra("class",classss);
                                    intent.putExtra("filenum",filenum);
                                    intent.putExtra("day",realday);
                                                    intent.putExtra("plus",getPlus);
                                    startActivity(intent);
                                                }
                                            }).create().show();
//                                    Intent intent = new Intent(getApplicationContext(),month.class);
//                                    intent.putExtra("user_id", user_id);
//                                    intent.putExtra("grade",grade);
//                                    intent.putExtra("class",classss);
//                                    intent.putExtra("filenum",filenum);
//                                    intent.putExtra("day",realday);
//                                    startActivity(intent);
                                }
                            }).create().show();
                }
                else{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ResultPage.this);
                    dialog.setTitle("데이터베이스 입력 성공")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AlertDialog.Builder dialog11 = new AlertDialog.Builder(ResultPage.this);
                                    dialog11.setTitle("복습단어 기록")
                                            .setPositiveButton("기록하기", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    try {
                                                        FileOutputStream fos = openFileOutput("data.txt", Context.MODE_APPEND);

                                                        PrintWriter writer = new PrintWriter(fos);
                                                        for(int i =0;i<(NumOfProblem-NumOfRight);i++)
                                                        {
                                                            writer.println(wrong_wordlist.get(i)+" "+wrong_meanlist.get(i));
//                    Log.d("확인:",wrong_wordlist.get(i));
                                                        }
                                                        writer.close();
                                                    } catch (FileNotFoundException e) {
                                                        e.printStackTrace();
                                                    }
                                                    Intent intent = new Intent(getApplicationContext(),month.class);
                                                    intent.putExtra("user_id", user_id);
                                                    intent.putExtra("grade",grade);
                                                    intent.putExtra("class",classss);
                                                    intent.putExtra("filenum",filenum);
                                                    intent.putExtra("day",realday);
                                                    intent.putExtra("plus",getPlus);
                                                    startActivity(intent);


                                                }
                                            })
                                            .setNegativeButton("기록 안하기", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    Intent intent = new Intent(getApplicationContext(),month.class);
                                                    intent.putExtra("user_id", user_id);
                                                    intent.putExtra("grade",grade);
                                                    intent.putExtra("class",classss);
                                                    intent.putExtra("filenum",filenum);
                                                    intent.putExtra("day",realday);
                                                    intent.putExtra("plus",getPlus);
                                                    startActivity(intent);
                                                }
                                            }).create().show();
                                }
                            }).create().show();
                }
        }


            else{
                AlertDialog.Builder dialog = new AlertDialog.Builder(ResultPage.this);
                dialog.setTitle("데이터베이스 입력 실패")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
            }





        }
        @Override
        protected String doInBackground(String... strings) {
            String serverURL = "http://ec2-13-125-229-159.ap-northeast-2.compute.amazonaws.com/insertscore.php";
            String postParameters = "Id=" + strings[0] + "&Day="+strings[1]+"&Time="+strings[2]
                    +"&Type="+strings[3]+"&Grade="+strings[4]+"&Class="+strings[5]+"&Score="+strings[6];
            try{
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - "+responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK){
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;
                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "error";
        }
    }
    class gotomain1 extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            progressDialog = progressDialog.show(ResultPage.this, "Please Wait", null,
                    true, true);
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response - " + result);
            if(result.equals("success")==true){

                if((day % 3) == 2) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ResultPage.this);
                    dialog.setTitle("데이터베이스 입력 성공")
                            .setMessage("주간간시험은 main화면에서 해당 day를 재클릭함으로써 볼 수 있습니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AlertDialog.Builder dialog11 = new AlertDialog.Builder(ResultPage.this);
                                    dialog11.setTitle("복습단어 기록")
                                            .setPositiveButton("기록하기", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    try {
                                                        FileOutputStream fos = openFileOutput("data.txt", Context.MODE_APPEND);

                                                        PrintWriter writer = new PrintWriter(fos);
                                                        for(int i =0;i<(NumOfProblem-NumOfRight);i++)
                                                        {
                                                            writer.println(wrong_wordlist.get(i)+" "+wrong_meanlist.get(i));
//                    Log.d("확인:",wrong_wordlist.get(i));
                                                        }
                                                        writer.close();
                                                    } catch (FileNotFoundException e) {
                                                        e.printStackTrace();
                                                    }
                                                    Intent intent = new Intent(getApplicationContext(),month.class);
                                                    intent.putExtra("user_id", user_id);
                                                    intent.putExtra("grade",grade);
                                                    intent.putExtra("class",classss);
                                                    intent.putExtra("filenum",filenum);
                                                    intent.putExtra("day",realday-getPlus);
                                                    intent.putExtra("plus",getPlus+1);
                                                    startActivity(intent);


                                                }
                                            })
                                            .setNegativeButton("기록 안하기", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    Intent intent = new Intent(getApplicationContext(),month.class);
                                                    intent.putExtra("user_id", user_id);
                                                    intent.putExtra("grade",grade);
                                                    intent.putExtra("class",classss);
                                                    intent.putExtra("filenum",filenum);
                                                    intent.putExtra("day",realday-getPlus);
                                                    intent.putExtra("plus",getPlus+1);
                                                    startActivity(intent);
                                                }
                                            }).create().show();
                                }
                            }).create().show();
                }
                else{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ResultPage.this);
                    dialog.setTitle("데이터베이스 입력 성공")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AlertDialog.Builder dialog11 = new AlertDialog.Builder(ResultPage.this);
                                    dialog11.setTitle("복습단어 기록")
                                            .setPositiveButton("기록하기", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    try {
                                                        FileOutputStream fos = openFileOutput("data.txt", Context.MODE_APPEND);

                                                        PrintWriter writer = new PrintWriter(fos);
                                                        for(int i =0;i<(NumOfProblem-NumOfRight);i++)
                                                        {
                                                            writer.println(wrong_wordlist.get(i)+" "+wrong_meanlist.get(i));
//                    Log.d("확인:",wrong_wordlist.get(i));
                                                        }
                                                        writer.close();
                                                    } catch (FileNotFoundException e) {
                                                        e.printStackTrace();
                                                    }
                                                    Intent intent = new Intent(getApplicationContext(),month.class);
                                                    intent.putExtra("user_id", user_id);
                                                    intent.putExtra("grade",grade);
                                                    intent.putExtra("class",classss);
                                                    intent.putExtra("filenum",filenum);
                                                    intent.putExtra("day",realday-getPlus);
                                                    intent.putExtra("plus",getPlus+1);
                                                    startActivity(intent);


                                                }
                                            })
                                            .setNegativeButton("기록 안하기", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    Intent intent = new Intent(getApplicationContext(),month.class);
                                                    intent.putExtra("user_id", user_id);
                                                    intent.putExtra("grade",grade);
                                                    intent.putExtra("class",classss);
                                                    intent.putExtra("filenum",filenum);
                                                    intent.putExtra("day",realday-getPlus);
                                                    intent.putExtra("plus",getPlus+1);
                                                    startActivity(intent);
                                                }
                                            }).create().show();
                                }
                            }).create().show();
                }
            }


            else{
                AlertDialog.Builder dialog = new AlertDialog.Builder(ResultPage.this);
                dialog.setTitle("데이터베이스 입력 실패")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
            }





        }
        @Override
        protected String doInBackground(String... strings) {
            String serverURL = "http://ec2-13-125-229-159.ap-northeast-2.compute.amazonaws.com/insertscoreplus.php";
            String postParameters = "Id=" + strings[0] + "&Day="+strings[1]+"&Time="+strings[2]
                    +"&Type="+strings[3]+"&Grade="+strings[4]+"&Class="+strings[5]+"&Score="+strings[6];
            try{
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - "+responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK){
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;
                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "error";
        }
    }
    @Override
    public void onBackPressed() {

        // Alert을 이용해 종료시키기
        AlertDialog.Builder dialog = new AlertDialog.Builder(ResultPage.this);
        dialog  .setTitle("시험 종료")
                .setMessage("데이터베이스에 기록하지 않고 main화면으로 가시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(),month.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("grade",grade);
                        intent.putExtra("class",classss);
                        intent.putExtra("filenum",filenum);
                        intent.putExtra("day",day-getPlus);
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
