package com.example.jung_jaejin.myproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

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

public class ChangeWord extends AppCompatActivity {
    private static String TAG = "ChangeWord";
    private RadioGroup gradegroup;
    private RadioGroup classgroup;
    private RadioGroup excelgroup;
    private EditText getday;
    private Button finishbutton;
    private String grade;
    private String classss;
    private String filenum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_change_word);
        gradegroup = (RadioGroup) findViewById(R.id.gradegroup);
        classgroup = (RadioGroup) findViewById(R.id.classgroup);
        excelgroup = (RadioGroup) findViewById(R.id.wordgroup);
        getday = (EditText)findViewById(R.id.getday);
        finishbutton = (Button) findViewById(R.id.finish);

        gradegroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.middle1pick){
                    gradegroup.check(checkedId);
                    grade = "중1";
                }
                if(checkedId == R.id.middle2pick){
                    gradegroup.check(checkedId);
                    grade = "중2";
                }
                if(checkedId == R.id.middle3pick){
                    gradegroup.check(checkedId);
                    grade = "중3";
                }
                if(checkedId == R.id.high1pick){
                    gradegroup.check(checkedId);
                    grade = "고1";
                }
                if(checkedId == R.id.high2pick){
                    gradegroup.check(checkedId);
                    grade = "고2";
                }
                if(checkedId == R.id.high3pick){
                    gradegroup.check(checkedId);
                    grade = "고3";
                }

            }
        });

        classgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.spick){
                    classgroup.check(checkedId);
                    classss = "S";
                }
                if(checkedId == R.id.apick){
                    classgroup.check(checkedId);
                    classss = "A";
                }
                if(checkedId == R.id.bpick){
                    classgroup.check(checkedId);
                    classss = "B";
                }
                if(checkedId == R.id.zpick){
                    classgroup.check(checkedId);
                    classss = "Z";
                }
            }
        });

        excelgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.excel1pick){
                    excelgroup.check(checkedId);
                    filenum = "1";
                }
                if(checkedId == R.id.excel2pick){
                    excelgroup.check(checkedId);
                    filenum = "2";
                }
                if(checkedId == R.id.excel3pick){
                    excelgroup.check(checkedId);
                    filenum = "3";
                }
                if(checkedId == R.id.excel4pick){
                    excelgroup.check(checkedId);
                    filenum = "4";
                }
                if(checkedId == R.id.excel5pick){
                    excelgroup.check(checkedId);
                    filenum = "5";
                }
            }
        });

        finishbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Change task = new Change();
                task.execute(grade,classss,filenum,getday.getText().toString());
            }
        });
    }

    class Change extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = progressDialog.show(ChangeWord.this, "Please Wait", null,
                    true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response - " + result);

//                int length = temp.length;
//                int start = 0;

            if (result.equals("ok") == true) {
                   AlertDialog.Builder dialog = new AlertDialog.Builder(ChangeWord.this);
                dialog.setTitle("변경 완료")
                        .setMessage("변경 완료되었습니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(),ManageResult.class);
                                startActivity(intent);
                            }
                        }).create().show();
            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ChangeWord.this);
                dialog.setTitle("송신 에러")
                        .setMessage("에러 코드 : " + result)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = "http://ec2-13-125-229-159.ap-northeast-2.compute.amazonaws.com/changeword.php";
            String postParameters = "Grade=" + params[0] + "&Class=" + params[1] + "&File="+ params[2] + "&Day="+params[3];
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
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
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
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

       Intent intent = new Intent(getApplicationContext(),ManageResult.class);
       startActivity(intent);
    }
}
