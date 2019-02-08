package com.example.jung_jaejin.myproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ResultofWeekly extends AppCompatActivity {
    private Button resultbutton;
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
    private static String TAG = "ResultofMonthly";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultof_weekly);
        Intent intent = getIntent();

        NumOfRight = intent.getIntExtra("numOfRight", 0);
        NumOfProblem = intent.getIntExtra("numOfProblem",120);
        IsPass = intent.getIntExtra("pass",0);
        user_id = intent.getStringExtra("user_id");
        grade = intent.getStringExtra("grade");
        classss = intent.getStringExtra("class");
        filenum = intent.getIntExtra("filenum",0);
        day = intent.getIntExtra("day",0);
        realday = intent.getIntExtra("realday",0);
        gettime gettimeclass = new gettime();
        gettimeclass.execute(user_id,"주",Integer.toString(day));
        howmuch = (TextView) findViewById(R.id.Howmuch);
        passorfail = (TextView) findViewById(R.id.PassOrFail);
        resultbutton = (Button)findViewById(R.id.result);
        resultbutton.setEnabled(false);
        if(IsPass == 1){
            howmuch.setText(NumOfRight + "/" + NumOfProblem);
            passorfail.setText("PASS");
            resultbutton.setText("완료");
        }

        else{
            howmuch.setText(NumOfRight + "/" + NumOfProblem);
            passorfail.setText("Fail");
            resultbutton.setText("선택창으로 돌아가기");


        }

        resultbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                gotomain gogo = new gotomain();
                gogo.execute(user_id,Integer.toString(day),Integer.toString(time+1),"주",grade,classss,Integer.toString(NumOfRight));
            }
        });
    }
    class gotomain extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            progressDialog = progressDialog.show(ResultofWeekly.this, "Please Wait", null,
                    true, true);
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response - " + result);
            if(result.equals("success")==true){

                if((day % 12 )== 0) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ResultofWeekly.this);
                    dialog.setTitle("데이터베이스 입력 성공 및 월간시험 보기")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(), MonthlyTestStart.class);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("grade",grade);
                                    intent.putExtra("class",classss);
                                    intent.putExtra("filenum",filenum);
                                    intent.putExtra("day",day);
                                    intent.putExtra("realday",realday);
                                    startActivity(intent);
                                }
                            })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), month.class);
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("grade", grade);
                            intent.putExtra("class", classss);
                            intent.putExtra("filenum", filenum);
                            intent.putExtra("day", realday);
                            startActivity(intent);
                        }
                    }).create().show();
                }
                else{

                    AlertDialog.Builder dialog = new AlertDialog.Builder(ResultofWeekly.this);
                    dialog.setTitle("데이터베이스 입력 성공")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(), month.class);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("grade", grade);
                                    intent.putExtra("class", classss);
                                    intent.putExtra("filenum", filenum);
                                    intent.putExtra("day", realday);
                                    startActivity(intent);
                                }
                            }).create().show();
                }
            }
            else{
                AlertDialog.Builder dialog = new AlertDialog.Builder(ResultofWeekly.this);
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

    class gettime extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            progressDialog = progressDialog.show(ResultofWeekly.this, "Please Wait", null,
                    true, true);
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "POST response - " + result);
            String main[] = result.split("Q");
            if(main[0].equals("ok")==true) {
                if (main[1].equals("No") == false) {
                    time = Integer.parseInt(main[1]);
                    resultbutton.setEnabled(true);
                } else {
                    time = 0;
                    resultbutton.setEnabled(true);
                }
            }
            else{
                AlertDialog.Builder dialog = new AlertDialog.Builder(ResultofWeekly.this);
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
            String serverURL = "http://ec2-13-125-229-159.ap-northeast-2.compute.amazonaws.com/getmaxtime.php";
            String postParameters = "Id=" + strings[0] + "&Type" + strings[1] + "&Day" + strings[2];
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
}
