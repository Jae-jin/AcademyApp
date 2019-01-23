package com.example.jung_jaejin.myproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ssomai.android.scalablelayout.ScalableLayout;

import org.apache.log4j.chainsaw.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class month extends AppCompatActivity implements View.OnClickListener {

    private ScalableLayout scalableLayout;
    private String getId;
    private String getGrade;
    private String getClass;
    private int getFilenum;
    private int getDay;
    private int row;
    private Button todayTest;
    private Button review_test;
    private static String TAG = "month";
    private int buttonlimit;
    private Button[] mButton;
    private ArrayList<Integer> gradelist= new ArrayList<>();
    private ArrayList<Integer> timelist = new ArrayList<>();
    private ArrayList<Integer> maxlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);
        scalableLayout = (ScalableLayout)findViewById(R.id.scaleid);

        review_test = (Button)findViewById(R.id.review_test);
        Intent intent = getIntent();


        getId = intent.getStringExtra("user_id");
        getGrade = intent.getStringExtra("grade");
        getClass = intent.getStringExtra("class");
        getFilenum = intent.getIntExtra("filenum",0);
        getDay = intent.getIntExtra("day",0);
        Grade task = new Grade();
        task.execute(getId);

        switch (getFilenum){
            case 1:
                buttonlimit = 36;
                break;
            case 2:
                buttonlimit = 28;
                break;
            case 3:
                buttonlimit = 41;
                break;
            case 4:
                buttonlimit = 66;
                break;
        }
        for(int i = 0; i<buttonlimit*5;i++)
        {
            gradelist.add(-1);
        }
        if(buttonlimit%3 ==0){
            row = buttonlimit/3;
        }
        else
        {
            row = (buttonlimit/3)+1;
        }
        mButton = new Button[buttonlimit];
        for(int i = 0;i<row;i++)
        {
            for(int j = 0;j<3;j++)
            {
                if(3*i+(j+1)<=buttonlimit) {
                    Button idbutton = new Button(this);
                    mButton[3*i+j] = idbutton;
                    mButton[3*i+j].setTag(3*i+j);
                    scalableLayout.addView(idbutton, 100 + 300 * j, 400 + 250 * i, 200, 200);
                    scalableLayout.setScale_TextSize(idbutton, 40);
                }
                else{
                    continue;
                }
            }
        }
        for(int i=0;i<buttonlimit;i++){
                                mButton[i].setOnClickListener(this);
        }

        review_test.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),Studystart_review.class);
                intent.putExtra("user_id", getId);
                intent.putExtra("grade",getGrade);
                intent.putExtra("class",getClass);
                intent.putExtra("filenum",getFilenum);
                intent.putExtra("realday",getDay);
                intent.putExtra("maxscore",maxlist.get(getDay-1));
                startActivity(intent);
            }
        });


//        todayTest = (Button)findViewById(R.id.todayTest);
//
//        todayTest.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v){
//                Intent intent = new Intent(getApplicationContext(),Studystart.class);
//                startActivity(intent);
//            }
//        });

    }

    @Override
    public void onClick(View v) {
            Button newButton = (Button) v;

            for(Button tempButton : mButton){
                if(tempButton == newButton)
                {
                    int position = (Integer)v.getTag();
                    Intent intent = new Intent(month.this, Studystart.class);
                    intent.putExtra("user_id", getId);
                    intent.putExtra("grade",getGrade);
                    intent.putExtra("class",getClass);
                    intent.putExtra("filenum",getFilenum);
                    intent.putExtra("day",position);
                    intent.putExtra("realday",getDay);
                    intent.putExtra("time",timelist.get(position));
                    startActivity(intent);
                }
            }
    }

    class Grade extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            progressDialog = progressDialog.show(month.this, "Please Wait", null,
                    true, true);
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response - " + result);
            String main[] = result.split("Q");
            if(main.length !=1) {
                String temp[] = main[1].split("&");
                int length = temp.length;
                int start = 0;

                if (main[0].equals("ok") == true) {
                    for (int i = 0; i < length; i++) {
                        String getarray1 = temp[i].split(",")[0];
                        String getarray2 = temp[i].split(",")[1];
                        String getarray3 = temp[i].split(",")[2];
                        int qq = Integer.parseInt(getarray1);
                        int qqq = Integer.parseInt(getarray2);
                        int qqqq = Integer.parseInt(getarray3);
                        while (qqq != start ) {
                            start++;
                        }

                        gradelist.set(5 * start + qqqq-1, qq);

                    }
//                Toast.makeText(month.this," : " + gradelist.get(7),Toast.LENGTH_LONG).show();
                    for (int i = 0; i < buttonlimit; i++) {
                        int max = 0;
                        int maxtime = 0;
                        int pass = 0;
                        for (int j = 0; j < 5; j++) {
                            if (gradelist.get(5 * i + j) != -1 && pass == 0) {
                                if (gradelist.get(5 * i + j) > max) {
                                    max = gradelist.get(5 * i + j);
                                    maxtime = j+1;
                                }
                                else {
                                    maxtime = j+1;
                                }
                                if (j == 4) {
                                    maxlist.add(max);
                                    timelist.add(j);
                                }
                            } else if (gradelist.get(5 * i + j) == -1 && j != 0 && pass == 0) {

                                maxlist.add(max);
                                timelist.add(maxtime);
                                pass = 1;
                            } else if (gradelist.get(5 * i + j) == -1 && j == 0 && pass == 0) {
                                maxtime = j;
                                maxlist.add(max);
                                timelist.add(maxtime);
                                pass = 1;
                            } else {

                            }

                        }

                    }
                    for (int i = 0; i < buttonlimit; i++) {
                        mButton[i].setTag(i);
//                    mButton[i].setOnClickListener(this);
                        if (i+1 == getDay) {
                            mButton[i].setText("Day" + (i + 1) + "\n" + "Today");
                        } else if (i+1 < getDay) {
                            mButton[i].setText("Day" + (i + 1) + "\n" + maxlist.get(i) + "/" + timelist.get(i));
                        } else {
                            mButton[i].setText("Day" + (i + 1) + "\n" + "Lock");
                            mButton[i].setEnabled(false);
                        }
                    }
                }
            }

            else{
                AlertDialog.Builder dialog = new AlertDialog.Builder(month.this);
                dialog.setTitle("기록 없음")
                        .setMessage("아직 기록된 성적이 없습니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
                for (int i = 0; i < buttonlimit; i++) {
                    int max = 0;
                    int maxtime = 0;
                    int pass = 0;
                    for (int j = 0; j < 5; j++) {
                        if (gradelist.get(5 * i + j) != -1 && pass == 0) {
                            if (gradelist.get(5 * i + j) > max) {
                                max = gradelist.get(5 * i + j);
                                maxtime = j;
                            }
                            if (j == 4) {
                                maxlist.add(max);
                                timelist.add(j);
                            }
                        } else if (gradelist.get(5 * i + j) == -1 && j != 0 && pass == 0) {
                            maxtime = j - 1;
                            maxlist.add(max);
                            timelist.add(maxtime);
                            pass = 1;
                        } else if (gradelist.get(5 * i + j) == -1 && j == 0 && pass == 0) {
                            maxtime = j;
                            maxlist.add(max);
                            timelist.add(maxtime);
                            pass = 1;
                        } else {

                        }

                    }

                }
                for (int i = 0; i < buttonlimit; i++) {
                    mButton[i].setTag(i);
//                    mButton[i].setOnClickListener(this);
                    if (i+1 == getDay) {
                        mButton[i].setText("Day" + (i + 1) + "\n" + "Today");
                    } else if (i +1< getDay) {
                        mButton[i].setText("Day" + (i + 1) + "\n" + maxlist.get(i) + "/" + timelist.get(i));
                    } else {
                        mButton[i].setText("Day" + (i + 1) + "\n" + "Lock");
                        mButton[i].setEnabled(false);
                    }
                }

            }


        }

        @Override
        protected String doInBackground(String... params){
            String serverURL = "http://ec2-13-125-229-159.ap-northeast-2.compute.amazonaws.com/getgrade.php";
            String postParameters = "Id=" + params[0];
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

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "error";
        }

    }

    @Override
    public void onBackPressed(){

        AlertDialog.Builder dialog = new AlertDialog.Builder(month.this);
        dialog.setTitle("로그 아웃")
                .setMessage("로그인 창으로 돌아가시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
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
