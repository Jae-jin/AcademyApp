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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.log4j.chainsaw.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView login;
    TextView signup;
    String getid;
    String getpass;
    EditText Id;
    EditText Password;
    Button gotoadminster;

    private static String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gotoadminster = (Button) findViewById(R.id.administerbutton);
        Id = (EditText)findViewById(R.id.signinid);
        Password = (EditText)findViewById(R.id.signinps);
        login = (TextView) findViewById(R.id.login);
        signup = (TextView) findViewById(R.id.signup);
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                getid = Id.getText().toString();
                getpass = Password.getText().toString();
                if(getid.replace(" ","").equals("")==true ||
                        getpass.replace(" ","").equals("")==true){
                    Toast.makeText(getApplicationContext(), "입력창이 비었습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Login task = new Login();
                    task.execute(getid,getpass);

                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
            }
        });

        gotoadminster.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),ManageResult.class);
                startActivity(intent);
            }
        });
    }
    class Login extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            progressDialog = progressDialog.show(MainActivity.this, "Please Wait", null,
                    true, true);
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response - " + result);
            String main[] = result.split(",");
            if(main[0].equals("ok") == true){

                Intent intent = new Intent(MainActivity.this, month.class);
                intent.putExtra("user_id", getid);
                intent.putExtra("grade",main[1]);
                intent.putExtra("class",main[2]);
                intent.putExtra("filenum",Integer.parseInt(main[3]));
                intent.putExtra("day",Integer.parseInt(main[4]));
                startActivity(intent);

            }
            else if(result.equals("none") == true){
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("로그인 실패!")
                        .setMessage("회원 정보가 일치하지않습니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
            }
            else{
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("가입 실패!")
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
            String serverURL = "http://ec2-13-125-229-159.ap-northeast-2.compute.amazonaws.com/login.php";
            String postParameters = "Id=" + params[0] + "&Pw=" + params[1];
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
    public void onBackPressed() {

        // Alert을 이용해 종료시키기
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog  .setTitle("종료 알림")
                .setMessage("어플을 종료하시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
    }
}
