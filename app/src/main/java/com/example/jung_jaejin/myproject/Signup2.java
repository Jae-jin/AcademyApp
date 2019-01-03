package com.example.jung_jaejin.myproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.log4j.chainsaw.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.regex.Pattern;

public class Signup2 extends AppCompatActivity {
    private Button complete;
    private String getname;
    private String getphone;
    private String getgrade;
    private String getclass;
    private EditText Id;
    private EditText Password;
    private String getid;
    private String getpassword;

    private static String TAG = "Signup2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
        Intent intent = getIntent();

        Id = (EditText) findViewById(R.id.idsign);
        Password = (EditText) findViewById(R.id.passwordsign);
        Id.setFilters(new InputFilter[] {filterAlphaNum});
        Password.setFilters(new InputFilter[] {filterAlphaNum});
        complete = (Button) findViewById(R.id.complete);

        getname = intent.getStringExtra("이름");
        getphone = intent.getStringExtra("핸드폰");
        getgrade = intent.getStringExtra("학년");
        getclass = intent.getStringExtra("반");

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getid = Id.getText().toString();
                getpassword = Password.getText().toString();
                if (getid.replace(" ", "").equals("") == true ||
                        getpassword.replace(" ", "").equals("")) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(Signup2.this);
                    dialog.setTitle("주의!")
                            .setMessage("모두 작성해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create().show();
                }

                else{

                InsertData task = new InsertData();
                task.execute(getid,getpassword,getname,getphone,getgrade,getclass);
                }
            }
        });
    }


//public class registDB extends AsyncTask<Void,Integer,Void> {
//        @Override
//    protected Void doInBackground(Void... unused) {
//            String param = "Id="+getid + "&Pw="+getpassword + "&Name=" + getname
//                    + "&Phone" + getphone + "&Grade" + getphone + "&Class" + getclass+"";
//            try{
//                URL url = new URL("http://ymc7737.cafe24.com/appmake/student.php");
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
//                conn.setRequestMethod("POST");
//                conn.setDoInput(true);
//                conn.connect();
//
//                OutputStream outs = conn.getOutputStream();
//                outs.write(param.getBytes("UTF-8"));
//                outs.flush();
//                outs.close();
//
//                InputStream is = null;
//                BufferedReader in = null;
//                String data = "";
//
//                is = conn.getInputStream();
//                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
//                String line = null;
//                StringBuffer buff = new StringBuffer();
//                while ( ( line = in.readLine() ) != null )
//                {
//                    buff.append(line + "\n");
//                }
//                data = buff.toString().trim();
//                Log.e("RECV DATA",data);
//
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//}


    class InsertData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            progressDialog = progressDialog.show(Signup2.this, "Please Wait",null,
                    true, true);
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG,"POST response - " + result);
            if(result.equals("success") == true){
                AlertDialog.Builder dialog = new AlertDialog.Builder(Signup2.this);
                dialog.setTitle("가입 성공!")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                            }
                        }).create().show();
            }
            else if(result.equals("1062")==true){
                AlertDialog.Builder dialog = new AlertDialog.Builder(Signup2.this);
                dialog.setTitle("가입 실패!")
                        .setMessage("ID 중복")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
            }
            else{
                AlertDialog.Builder dialog = new AlertDialog.Builder(Signup2.this);
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
            String serverURL = "http://ec2-13-125-229-159.ap-northeast-2.compute.amazonaws.com/student.php";
            String postParameters = "Id=" + params[0] + "&Pw=" + params[1] + "&Name=" + params[2] + "&Phone=" + params[3] + "&Grade=" +
                    params[4] + "&Class=" + params[5];
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
    protected InputFilter filterAlphaNum = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");
            if(!ps.matcher(source).matches()){
                return "";
            }
            return null;
        }
    };

}