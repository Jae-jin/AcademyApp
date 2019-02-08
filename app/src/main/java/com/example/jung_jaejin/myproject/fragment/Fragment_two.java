package com.example.jung_jaejin.myproject.fragment;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Toast;

import com.example.jung_jaejin.myproject.ManageResult;
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


public class Fragment_two extends Fragment  {
    ScalableLayout s1;
    View v;
    private int j;
    private int gradelimit;
    private int getfilenum;
    private Button[] mButton;
    private ArrayList<String> idlist = new ArrayList<>();
    private ArrayList<String> namelist = new ArrayList<>();
    private ArrayList<Integer> gradelist= new ArrayList<>();
    private ArrayList<Integer> gradeweeklist= new ArrayList<>();
    private ArrayList<Integer> grademonthlist= new ArrayList<>();
    private static String TAG = "Fragment_two";
    public Fragment_two() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancestate){
        for(int i = 0; i<330;i++)
        {
            gradelist.add(-1);
        }
        for(int i = 0; i<105;i++)
        {
            gradeweeklist.add(-1);
        }
        for(int i = 0; i<5;i++)
        {
            grademonthlist.add(-1);
        }
        v = inflater.inflate(R.layout.fragment_fragment_two,container,false);
        s1 = (ScalableLayout)v.findViewById(R.id.scale);
        Student task = new Student();
        task.execute(ManageResult.gradee,ManageResult.classs);
        return v;
    }
    public void refresh(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.detach(this).attach(this).commit();
    }



    class Student extends AsyncTask<String, Void, String> implements View.OnClickListener {
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
            if(main.length !=1 ) {
                String temp[] = main[1].split("&");
                int length = temp.length-1;
                int start = 0;
                getfilenum = Integer.parseInt(temp[length]);
                if (main[0].equals("ok") == true) {
                    for (int i = 0; i < length; i++) {
                        String getarray1 = temp[i].split(",")[0];
                        String getarray2 = temp[i].split(",")[1];

                        idlist.add(getarray1);
                        namelist.add(getarray2);
                    }
                    mButton = new Button[length + 1];
                    for (int i = 0; i < length; i++) {
                        Button idbutton = new Button(v.getContext());
                        mButton[i] = idbutton;
                        mButton[i].setOnClickListener(this);
                        mButton[i].setTag(i);
                        s1.addView(idbutton, 0, i * 160, 250, 140);
                        s1.setScale_TextSize(idbutton, 50);
                        idbutton.setText(namelist.get(i));
                    }
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                    dialog.setTitle("송신 에러")
                            .setMessage("에러 코드 : " + result)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create().show();
                }

            }
        }

        @Override
        protected String doInBackground(String... params){
            String serverURL = "http://ec2-13-125-229-159.ap-northeast-2.compute.amazonaws.com/getstudent.php";
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

        @Override
        public void onClick(View v1) {
            Button newButton = (Button) v1;

            for(Button tempButton : mButton){
                if(tempButton == newButton)
                {
                    refresh();
                    for(int i = 0; i<330;i++)
                    {
                        gradelist.set(i,-1);
                    }
                    int position = (Integer)v1.getTag();
                    Getgradecl task1 = new Getgradecl();
                    task1.execute(idlist.get(position));
                    Getgradecweek task2 = new Getgradecweek();
                    task2.execute(idlist.get(position));
                    Getgradecmonth task3 = new Getgradecmonth();
                    task3.execute(idlist.get(position));
                }
            }

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
            if(main.length != 1){
            String temp[] = main[1].split("&");
            int length = temp.length;
            int start = 0;

            if(main[0].equals("ok") == true){
                if(length != 0) {
                    for (int i = 0; i < length; i++) {

                        String getarray1 = temp[i].split(",")[0];
                        String getarray2 = temp[i].split(",")[1];
                        String getarray3 = temp[i].split(",")[2];
                        int qq = Integer.parseInt(getarray1);
                        int qqq = Integer.parseInt(getarray2);
                        int qqqq = Integer.parseInt(getarray3);
                        while (qqq != start) {
                            start++;
                        }

                        gradelist.set(5 * start + qqqq - 1, qq);

                    }

                    switch (getfilenum){
                        case 1:
                            gradelimit = 36;
                            break;
                        case 2:
                            gradelimit = 28;
                            break;
                        case 3:
                            gradelimit = 41;
                            break;
                        case 4:
                            gradelimit = 66;
                            break;

                    }
                    for (int tr = 0; tr < gradelimit+1; tr++) {

                        for (int tv = 0; tv < 6; tv++) {
                            if (tr == 0 && tv == 0) {

                                TextView textv = new TextView(v.getContext());
                                textv.setBackgroundResource(R.drawable.edge);
                                s1.addView(textv, 250, tr * 100, 140, 100);

                            } else if (tr == 0 && tv != 0) {
                                TextView textv = new TextView(v.getContext());
                                s1.addView(textv, 390+120*(tv-1), tr * 100, 120, 100);
                                textv.setBackgroundResource(R.drawable.edge);
                                s1.setScale_TextSize(textv, 50);
                                if (tv == 1) {
                                    textv.setText("1st");
                                    textv.setGravity(Gravity.CENTER);
                                }

                                if (tv == 2) {
                                    textv.setText("2nd");
                                    textv.setGravity(Gravity.CENTER);
                                }
                                if (tv == 3) {
                                    textv.setText("3rd");
                                    textv.setGravity(Gravity.CENTER);
                                }
                                if (tv == 4) {
                                    textv.setText("4th");
                                    textv.setGravity(Gravity.CENTER);
                                }
                                if (tv == 5) {
                                    textv.setText("5th");
                                    textv.setGravity(Gravity.CENTER);
                                }

                            } else if (tr != 0 && tv == 0) {
                                TextView textv = new TextView(v.getContext());
                                textv.setBackgroundResource(R.drawable.edge);
                                s1.addView(textv, 250, tr * 100, 140, 100);
                                s1.setScale_TextSize(textv, 36);
                                textv.setText("Day" + tr);

                                textv.setGravity(Gravity.CENTER);
                            } else {
                                TextView textv = new TextView(v.getContext());
                                textv.setBackgroundResource(R.drawable.edge);
                                s1.addView(textv, 390+120*(tv-1), tr * 100, 120, 100);
                                s1.setScale_TextSize(textv, 50);
                                if(gradelist.get(5*(tr-1)+tv-1) == -1){
                                    textv.setText("");
                                    textv.setGravity(Gravity.CENTER);
                                }
                                else {
                                    textv.setText("" + gradelist.get(5 * (tr - 1) + tv - 1));
                                    textv.setGravity(Gravity.CENTER);
                                }
                            }
                        }

                    }
                }}

                else{



            }
            }

            else{
                switch (getfilenum){
                    case 1:
                        gradelimit = 36;
                        break;
                    case 2:
                        gradelimit = 28;
                        break;
                    case 3:
                        gradelimit = 41;
                        break;
                    case 4:
                        gradelimit = 66;
                        break;

                }
                for (int tr = 0; tr < gradelimit+1; tr++) {

                    for (int tv = 0; tv < 6; tv++) {
                        if (tr == 0 && tv == 0) {

                            TextView textv = new TextView(v.getContext());
                            textv.setBackgroundResource(R.drawable.edge);
                            s1.addView(textv, 250, tr * 100, 140, 100);

                        } else if (tr == 0 && tv != 0) {
                            TextView textv = new TextView(v.getContext());
                            textv.setBackgroundResource(R.drawable.edge);
                            s1.addView(textv, 390+120*(tv-1), tr * 100, 120, 100);
                            s1.setScale_TextSize(textv, 50);
                            if (tv == 1) {
                                textv.setText("1st");
                                textv.setGravity(Gravity.CENTER);
                            }

                            if (tv == 2) {
                                textv.setText("2nd");
                                textv.setGravity(Gravity.CENTER);
                            }
                            if (tv == 3) {
                                textv.setText("3rd");
                                textv.setGravity(Gravity.CENTER);
                            }
                            if (tv == 4) {
                                textv.setText("4th");
                                textv.setGravity(Gravity.CENTER);
                            }
                            if (tv == 5) {
                                textv.setText("5th");
                                textv.setGravity(Gravity.CENTER);
                            }

                        } else if (tr != 0 && tv == 0) {
                            TextView textv = new TextView(v.getContext());
                            textv.setBackgroundResource(R.drawable.edge);
                            s1.addView(textv, 250, tr * 100, 140, 100);
                            s1.setScale_TextSize(textv, 36);
                            textv.setText("Day" + tr);
                            textv.setGravity(Gravity.CENTER);
                        } else {
                            TextView textv = new TextView(v.getContext());
                            textv.setBackgroundResource(R.drawable.edge);
                            s1.addView(textv, 390+120*(tv-1), tr * 100, 120, 100);
                            s1.setScale_TextSize(textv, 50);
                            if(gradelist.get(5*(tr-1)+tv-1) == -1){
                                textv.setText("");
                                textv.setGravity(Gravity.CENTER);
                            }
                            else {
                                textv.setText("" + gradelist.get(5 * (tr - 1) + tv - 1));
                                textv.setGravity(Gravity.CENTER);
                            }
                        }
                    }

                }
            }


        }
        @Override
        protected String doInBackground(String... strings) {
            String serverURL = "http://ec2-13-125-229-159.ap-northeast-2.compute.amazonaws.com/getgrade.php";
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

    class Getgradecweek extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            progressDialog = progressDialog.show(v.getContext(), "Please Wait", null,
                    true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response - " + result);
            String main[] = result.split("Q");
            if (main.length != 1) {
                String temp[] = main[1].split("&");
                int length = temp.length;
                int start = 0;

                if (main[0].equals("ok") == true) {
                    if (length != 0) {
                        for (int i = 0; i < length; i++) {

                            String getarray1 = temp[i].split(",")[0];
                            String getarray2 = temp[i].split(",")[1];
                            String getarray3 = temp[i].split(",")[2];
                            int qq = Integer.parseInt(getarray1);
                            int qqq = Integer.parseInt(getarray2);
                            int qqqq = Integer.parseInt(getarray3);
                            while (qqq != start) {
                                start++;
                            }
                            if(start <= 21) {
                                gradeweeklist.set(5 * (start / 3) + qqqq - 1, qq);
                            }

                        }

                        switch (getfilenum) {
                            case 1:
                                gradelimit = 36;
                                break;
                            case 2:
                                gradelimit = 28;
                                break;
                            case 3:
                                gradelimit = 41;
                                break;
                            case 4:
                                gradelimit = 66;
                                break;
                        }
                        for (int tr = 0; tr < (gradelimit / 3) + 1; tr++) {
                            for (int tv = 0; tv < 6; tv++) {
                                if (tr == 0 && tv == 0) {

                                    TextView textv = new TextView(v.getContext());
                                    textv.setBackgroundResource(R.drawable.edge);
                                    s1.addView(textv, 250, gradelimit * 100 + 200 + tr * 100, 140, 100);

                                } else if (tr == 0 && tv != 0) {
                                    TextView textv = new TextView(v.getContext());
                                    textv.setBackgroundResource(R.drawable.edge);
                                    s1.addView(textv, 250 + 120 * tv, gradelimit * 100 + 200 + tr * 100, 120, 100);
                                    s1.setScale_TextSize(textv, 50);
                                    if (tv == 1) {
                                        textv.setText("1st");
                                        textv.setGravity(Gravity.CENTER);
                                    }
                                    if (tv == 2) {
                                        textv.setText("2nd");
                                        textv.setGravity(Gravity.CENTER);
                                    }
                                    if (tv == 3) {
                                        textv.setText("3rd");
                                        textv.setGravity(Gravity.CENTER);
                                    }
                                    if (tv == 4) {
                                        textv.setText("4th");
                                        textv.setGravity(Gravity.CENTER);
                                    }
                                    if (tv == 5) {
                                        textv.setText("5th");
                                        textv.setGravity(Gravity.CENTER);
                                    }

                                } else if (tr != 0 && tv == 0) {
                                    TextView textv = new TextView(v.getContext());
                                    textv.setBackgroundResource(R.drawable.edge);
                                    s1.addView(textv, 250, gradelimit * 100 + 200 + tv * 100, 140, 100);
                                    s1.setScale_TextSize(textv, 30);
                                    textv.setText("Week" + tr);
                                    textv.setGravity(Gravity.CENTER);
                                } else {
                                    TextView textv = new TextView(v.getContext());
                                    textv.setBackgroundResource(R.drawable.edge);
                                    s1.addView(textv, 250 + 120 * tv, gradelimit * 100 + 200 + tv * 100, 120, 100);
                                    s1.setScale_TextSize(textv, 50);
                                    if(gradeweeklist.get((tr - 1) + tv - 1) != -1) {
                                        textv.setText("" + gradeweeklist.get(5 * (tr - 1) + tv - 1));
                                        textv.setGravity(Gravity.CENTER);
                                    }
                                    else{
                                        textv.setText("");
                                        textv.setGravity(Gravity.CENTER);
                                    }
                                }
                            }

                        }
                    }
                }
            } else {
                switch (getfilenum) {
                    case 1:
                        gradelimit = 36;
                        break;
                    case 2:
                        gradelimit = 28;
                        break;
                    case 3:
                        gradelimit = 41;
                        break;
                    case 4:
                        gradelimit = 66;
                        break;
                }
                for (int tr = 0; tr < (gradelimit / 3) + 1; tr++) {
                    for (int tv = 0; tv < 6; tv++) {
                        if (tr == 0 && tv == 0) {

                            TextView textv = new TextView(v.getContext());
                            textv.setBackgroundResource(R.drawable.edge);
                            s1.addView(textv, 250, gradelimit * 100 + 200 + tr * 100, 140, 100);

                        } else if (tr == 0 && tv != 0) {
                            TextView textv = new TextView(v.getContext());
                            textv.setBackgroundResource(R.drawable.edge);
                            s1.addView(textv, 390+120*(tv-1), gradelimit * 100 + 200 + tr * 100, 120, 100);
                            s1.setScale_TextSize(textv, 50);
                            if (tv == 1) {
                                textv.setText("1st");
                                textv.setGravity(Gravity.CENTER);
                            }
                            if (tv == 2) {
                                textv.setText("2nd");
                                textv.setGravity(Gravity.CENTER);
                            }
                            if (tv == 3) {
                                textv.setText("3rd");
                                textv.setGravity(Gravity.CENTER);
                            }
                            if (tv == 4) {
                                textv.setText("4th");
                                textv.setGravity(Gravity.CENTER);
                            }
                            if (tv == 5) {
                                textv.setText("5th");
                                textv.setGravity(Gravity.CENTER);
                            }

                        } else if (tr != 0 && tv == 0) {
                            TextView textv = new TextView(v.getContext());
                            textv.setBackgroundResource(R.drawable.edge);
                            s1.addView(textv, 250, gradelimit * 100 + 200 + tr * 100, 140, 100);
                            s1.setScale_TextSize(textv, 30);
                            textv.setText("Week" + tr);
                            textv.setGravity(Gravity.CENTER);
                        } else {
                            TextView textv = new TextView(v.getContext());
                            textv.setBackgroundResource(R.drawable.edge);
                            s1.addView(textv, 390+120*(tv-1), gradelimit * 100 + 200 + tr * 100, 120, 100);
                            s1.setScale_TextSize(textv, 50);
                            if(gradeweeklist.get((tr - 1) + tv - 1) != -1) {
                                textv.setText("" + gradeweeklist.get(5 * (tr - 1) + tv - 1));
                                textv.setGravity(Gravity.CENTER);
                            }
                            else{
                                textv.setText("");
                                textv.setGravity(Gravity.CENTER);
                            }
                        }
                    }

                }
            }
        }
        @Override
        protected String doInBackground(String... strings) {
            String serverURL = "http://ec2-13-125-229-159.ap-northeast-2.compute.amazonaws.com/getweekgrade.php";
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
    class Getgradecmonth extends AsyncTask<String, Void, String>{
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
            if(main.length != 1) {
                String temp[] = main[1].split("&");
                int length = temp.length;
                int start = 0;

                if (main[0].equals("ok") == true) {
                    if (length != 0) {
                        for (int i = 0; i < length; i++) {

                            String getarray1 = temp[i].split(",")[0];
                            String getarray2 = temp[i].split(",")[1];
                            int qq = Integer.parseInt(getarray1);
                            int qqq = Integer.parseInt(getarray2);
                            while (qqq != start && start <= 70) {
                                start++;
                            }

                            grademonthlist.set((start / 12) - 1, qq);

                        }

                        switch (getfilenum) {
                            case 1:
                                gradelimit = 36;
                                break;
                            case 2:
                                gradelimit = 28;
                                break;
                            case 3:
                                gradelimit = 41;
                                break;
                            case 4:
                                gradelimit = 66;
                                break;

                        }
                        if (gradelimit / 12 >= 1) {
                            for (int tr = 0; tr < (gradelimit / 12) + 1; tr++) {

                                for (int tv = 0; tv < 2; tv++) {
                                    if (tr == 0 && tv == 0) {

                                        TextView textv = new TextView(v.getContext());
                                        textv.setBackgroundResource(R.drawable.edge);
                                        s1.addView(textv, 250, gradelimit * 100 + 390 + gradelimit / 3 * 100 + tr * 100, 140, 100);

                                    } else if (tr == 0 && tv != 0) {
                                        TextView textv = new TextView(v.getContext());
                                        textv.setBackgroundResource(R.drawable.edge);
                                        s1.addView(textv, 390+120*(tv-1), gradelimit * 100 + 390 + gradelimit / 3 * 100 + tr * 100, 120, 100);
                                        s1.setScale_TextSize(textv, 50);
                                        if (tv == 1) {
                                            textv.setText("1st");
                                            textv.setGravity(Gravity.CENTER);
                                        }

                                    } else if (tr != 0 && tv == 0) {
                                        TextView textv = new TextView(v.getContext());
                                        textv.setBackgroundResource(R.drawable.edge);
                                        s1.addView(textv, 250, gradelimit * 100 + 390 + gradelimit / 3 * 100 + tr * 100, 140, 100);
                                        s1.setScale_TextSize(textv, 30);
                                        textv.setText("Month" + tr);
                                        textv.setGravity(Gravity.CENTER);
                                    } else {
                                        TextView textv = new TextView(v.getContext());
                                        textv.setBackgroundResource(R.drawable.edge);
                                        s1.addView(textv, 390+120*(tv-1), gradelimit * 100 + 390 + gradelimit / 3 * 100 + tr * 100, 120, 100);
                                        s1.setScale_TextSize(textv, 30);
                                        if(gradeweeklist.get((tr - 1) + tv - 1) != -1) {
                                            textv.setText("" + gradeweeklist.get((tr - 1) + tv - 1));
                                            textv.setGravity(Gravity.CENTER);
                                        }
                                        else{
                                            textv.setText("");
                                            textv.setGravity(Gravity.CENTER);
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
            else{
                switch (getfilenum) {
                    case 1:
                        gradelimit = 36;
                        break;
                    case 2:
                        gradelimit = 28;
                        break;
                    case 3:
                        gradelimit = 41;
                        break;
                    case 4:
                        gradelimit = 66;
                        break;

                }
                if (gradelimit / 12 >= 1) {
                    for (int tr = 0; tr < (gradelimit / 12) + 1; tr++) {

                        for (int tv = 0; tv < 2; tv++) {
                            if (tr == 0 && tv == 0) {

                                TextView textv = new TextView(v.getContext());
                                textv.setBackgroundResource(R.drawable.edge);
                                s1.addView(textv, 250, gradelimit * 100 + 390 + gradelimit / 3 * 100 + tr * 100, 140, 100);

                            } else if (tr == 0 && tv != 0) {
                                TextView textv = new TextView(v.getContext());
                                textv.setBackgroundResource(R.drawable.edge);
                                s1.addView(textv, 390+120*(tv-1), gradelimit * 100 + 390 + gradelimit / 3 * 100 + tr * 100, 120, 100);
                                s1.setScale_TextSize(textv, 50);
                                if (tv == 1) {
                                    textv.setText("1st");
                                    textv.setGravity(Gravity.CENTER);
                                }

                            } else if (tr != 0 && tv == 0) {
                                TextView textv = new TextView(v.getContext());
                                textv.setBackgroundResource(R.drawable.edge);
                                s1.addView(textv, 250, gradelimit * 100 + 390 + gradelimit / 3 * 100 + tr * 100, 140, 100);
                                s1.setScale_TextSize(textv, 30);
                                textv.setText("Month" + tr);
                                textv.setGravity(Gravity.CENTER);
                            } else {
                                TextView textv = new TextView(v.getContext());
                                textv.setBackgroundResource(R.drawable.edge);
                                s1.addView(textv, 390+120*(tv-1), gradelimit * 100 + 390 + gradelimit / 3 * 100 + tr * 100, 120, 100);
                                s1.setScale_TextSize(textv, 50);
                                if(gradeweeklist.get((tr - 1) + tv - 1) != -1) {
                                    textv.setText("" + gradeweeklist.get((tr - 1) + tv - 1));
                                    textv.setGravity(Gravity.CENTER);
                                }
                                else{
                                    textv.setText("");
                                    textv.setGravity(Gravity.CENTER);
                                }
                            }
                        }
                    }
                }

        }}
        @Override
        protected String doInBackground(String... strings) {
            String serverURL = "http://ec2-13-125-229-159.ap-northeast-2.compute.amazonaws.com/getmonthgrade.php";
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
