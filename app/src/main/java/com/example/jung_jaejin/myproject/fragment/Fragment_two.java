package com.example.jung_jaejin.myproject.fragment;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.jung_jaejin.myproject.R;
import com.ssomai.android.scalablelayout.ScalableLayout;

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
import java.util.ArrayList;


public class Fragment_two extends Fragment implements View.OnClickListener {
    ScalableLayout s1;
    View v;
    private int check = 0;
    private int j;
    private Button[] mButton;
    private ArrayList<String> idlist = new ArrayList<>();
    private ArrayList<String> namelist = new ArrayList<>();
    private ArrayList<Integer> gradelist= new ArrayList<>();
    private ArrayList<Integer> timelist = new ArrayList<>();
    private ArrayList<Integer> maxlist = new ArrayList<>();
    private static String TAG = "Fragment_two";
    public Fragment_two() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancestate){
        for(int i = 0; i<180;i++)
        {
            gradelist.add(-1);
        }
        v = inflater.inflate(R.layout.fragment_fragment_two,container,false);
        s1 = (ScalableLayout)v.findViewById(R.id.scale);
        Student task = new Student();
        task.execute("middle1","S");
       
        for(int q = 0; q<mButton.length;q++)
        {
            mButton[q].setOnClickListener(this);
        }
        return v;
    }
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case 1:

                    break;

            }
        }
    };
    @Override
    public void onClick(View v) {
        Button newButton = (Button) v;

        for(Button tempButton : mButton){
            if(tempButton == newButton)
            {
                int position = (Integer)v.getTag();
                Getgradecl task1 = new Getgradecl();
                task1.execute(idlist.get(position));
            }
        }
    }


    class Student extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            progressDialog = progressDialog.show(v.getContext(), "Please Wait", null,
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

                    idlist.add(getarray1);
                    namelist.add(getarray2);
                }
                mButton = new Button[length+1];
                for(int i = 0;i<length;i++) {
                    Button idbutton = new Button(v.getContext());
                    mButton[i] = idbutton;
                    s1.addView(idbutton,0,i*100,250,150);
                    s1.setScale_TextSize(idbutton,50);
                    idbutton.setText(namelist.get(i));
                }
                check = 1;
            }

            else{
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
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
            String serverURL = "http://ymc7737.cafe24.com/appmake/getstudent.php";
            String postParameters = "Grade=" + params[0] + "&Class=" + params[1];
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

    class Getgradecl extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            progressDialog = progressDialog.show(v.getContext(), "Please Wait", null,
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
                        for(int tr = 0;tr<37 ;tr++){

           for(int tv= 0;tv<6;tv++)
           {
                if(tr == 0 && tv ==0)
                {
                    TextView textv = new TextView(v.getContext());
                    s1.addView(textv,250,tr*100,150,100);

                }

                else if(tr ==0 && tv !=0)
                {
                    TextView textv = new TextView(v.getContext());
                    s1.addView(textv,250+120*tv,tr*100,120,100);
                    s1.setScale_TextSize(textv,50);
                    if(tv == 1) {
                        textv.setText("1st");
                        textv.setGravity(Gravity.CENTER);
                    }

                    if(tv == 2) {
                        textv.setText("2nd");
                        textv.setGravity(Gravity.CENTER);
                    }
                    if(tv == 3) {
                        textv.setText("3rd");
                        textv.setGravity(Gravity.CENTER);
                    }
                    if(tv == 4) {
                        textv.setText("4th");
                        textv.setGravity(Gravity.CENTER);
                    }if(tv == 5) {
                    textv.setText("5th");
                    textv.setGravity(Gravity.CENTER);
                }

                }

                else if(tr !=0 && tv == 0)
                {
                    TextView textv = new TextView(v.getContext());
                    s1.addView(textv,250,tr*100,150,100);
                    s1.setScale_TextSize(textv,50);
                    textv.setText("Day"+ tr);
                    textv.setGravity(Gravity.CENTER);
                }
                else
                {
                    TextView textv = new TextView(v.getContext());
                    s1.addView(textv,250+120*tv,tr*100,120,100);
                    s1.setScale_TextSize(textv,50);
                    textv.setText(gradelist.get(5*(tr-1)+tv));
                    textv.setGravity(Gravity.CENTER);
                }
           }

        }

            }

            else{
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
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
        protected String doInBackground(String... strings) {
            String serverURL = "http://ymc7737.cafe24.com/appmake/getgrade.php";
            String postParameters = "Id=" + strings[0];
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
