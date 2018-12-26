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


    private String getId;
    private String getGrade;
    private String getClass;
    private int getFilenum;
    private int getDay;
    Button todayTest;
    private Button review_test;
    private static String TAG = "month";
    private Button[] mButton = new Button[36];
    private ArrayList<Integer> gradelist= new ArrayList<>();
    private ArrayList<Integer> timelist = new ArrayList<>();
    private ArrayList<Integer> maxlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);
        for(int i = 0; i<180;i++)
        {
            gradelist.add(-1);
        }
        mButton[0] = (Button)findViewById(R.id.day1);
        mButton[1] = (Button)findViewById(R.id.day2);
        mButton[2] = (Button)findViewById(R.id.day3);
        mButton[3] = (Button)findViewById(R.id.day4);
        mButton[4] = (Button)findViewById(R.id.day5);
        mButton[5] = (Button)findViewById(R.id.day6);
        mButton[6] = (Button)findViewById(R.id.day7);
        mButton[7] = (Button)findViewById(R.id.day8);
        mButton[8] = (Button)findViewById(R.id.day9);
        mButton[9] = (Button)findViewById(R.id.day10);
        mButton[10] = (Button)findViewById(R.id.day11);
        mButton[11] = (Button)findViewById(R.id.day12);
        mButton[12] = (Button)findViewById(R.id.day13);
        mButton[13] = (Button)findViewById(R.id.day14);
        mButton[14] = (Button)findViewById(R.id.day15);
        mButton[15] = (Button)findViewById(R.id.day16);
        mButton[16] = (Button)findViewById(R.id.day17);
        mButton[17] = (Button)findViewById(R.id.day18);
        mButton[18] = (Button)findViewById(R.id.day19);
        mButton[19] = (Button)findViewById(R.id.day20);
        mButton[20] = (Button)findViewById(R.id.day21);
        mButton[21] = (Button)findViewById(R.id.day22);
        mButton[22] = (Button)findViewById(R.id.day23);
        mButton[23] = (Button)findViewById(R.id.day24);
        mButton[24] = (Button)findViewById(R.id.day25);
        mButton[25] = (Button)findViewById(R.id.day26);
        mButton[26] = (Button)findViewById(R.id.day27);
        mButton[27] = (Button)findViewById(R.id.day28);
        mButton[28] = (Button)findViewById(R.id.day29);
        mButton[29] = (Button)findViewById(R.id.day30);
        mButton[30] = (Button)findViewById(R.id.day31);
        mButton[31] = (Button)findViewById(R.id.day32);
        mButton[32] = (Button)findViewById(R.id.day33);
        mButton[33] = (Button)findViewById(R.id.day34);
        mButton[34] = (Button)findViewById(R.id.day35);
        mButton[35] = (Button)findViewById(R.id.day36);
        review_test = (Button)findViewById(R.id.review_test);
        Intent intent = getIntent();


        getId = intent.getStringExtra("user_id");
        getGrade = intent.getStringExtra("grade");
        getClass = intent.getStringExtra("class");
        getFilenum = Integer.parseInt(intent.getStringExtra("filenum"));
        getDay = Integer.parseInt(intent.getStringExtra("day"));
        Grade task = new Grade();
        task.execute(getId);


        for(int i=0;i<36;i++){
                                mButton[i].setOnClickListener(this);
        }

        review_test.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),Studystart_review.class);
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
            String temp[] = main[1].split("&");
            int length = temp.length;
            int start = 0;

            if(main[0].equals("ok") == true){
                for(int i = 0;i<length;i++) {
                    String getarray1 = temp[i].split(",")[0];
                    String getarray2 = temp[i].split(",")[1];
                    String getarray3 = temp[i].split(",")[2];
                    int qq = Integer.parseInt(getarray1);
                    int qqq = Integer.parseInt(getarray2);
                    int qqqq = Integer.parseInt(getarray3);
                    while(qqq != start){
                        start++;
                    }

                    gradelist.set(5*start+qqqq,qq);

                }
//                Toast.makeText(month.this," : " + gradelist.get(7),Toast.LENGTH_LONG).show();
                for(int i = 0; i<36;i++){
                    int max = 0;
                    int maxtime = 0;
                    int pass = 0;
                    for(int j= 0;j<5;j++){
                        if(gradelist.get(5*i+j)!=-1 && pass == 0) {
                            if (gradelist.get(5 * i + j) > max) {
                                max = gradelist.get(5 * i + j);
                                maxtime = j;
                            }
                            if(j == 4){
                                maxlist.add(max);
                                timelist.add(j);
                            }
                        }
                        else if(gradelist.get(5*i+j)==-1 && j !=0 && pass == 0){
                            maxtime = j-1;
                            maxlist.add(max);
                            timelist.add(maxtime);
                            pass = 1;
                        }
                        else if(gradelist.get(5*i+j)==-1 && j ==0 && pass == 0){
                            maxtime = j;
                            maxlist.add(max);
                            timelist.add(maxtime);
                            pass = 1;
                        }
                        else{

                        }

                    }

                }
                for(int i = 0; i<36;i++){
                    mButton[i].setTag(i);
//                    mButton[i].setOnClickListener(this);
                    if(i == getDay) {
                        mButton[i].setText("Day" + (i+1) + "\n" + "Today");
                    }
                    else if(i <getDay){
                        mButton[i].setText("Day" + (i+1) + "\n" + maxlist.get(i)+ "/"+timelist.get(i));
                    }
                    else{
                        mButton[i].setText("Day" + (i+1) + "\n" + "Lock");
                        mButton[i].setEnabled(false);
                    }
                }
            }

            else{
                AlertDialog.Builder dialog = new AlertDialog.Builder(month.this);
                dialog.setTitle("송신 에러")
                        .setMessage("에러 코드 : "+result)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
            }


        }

        @Override
        protected String doInBackground(String... params){
            String serverURL = "http://ymc7737.cafe24.com/appmake/getgrade.php";
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
}
