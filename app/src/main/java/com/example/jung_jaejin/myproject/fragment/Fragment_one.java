package com.example.jung_jaejin.myproject.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;


public class Fragment_one extends Fragment {
    ScalableLayout s2;
    View v1;
    private Button checkall;
    private TextView[] mText;
    private Button changebutton;
    private CheckBox[] mCheck;
    private ArrayList<String> idlist = new ArrayList<>();
    private ArrayList<String> namelist = new ArrayList<>();
    private ArrayList<Integer> checklist = new ArrayList<>();
    private static String TAG = "Fragment_one";

    public Fragment_one() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v1 = inflater.inflate(R.layout.fragment_fragment_one, container, false);
        s2 = (ScalableLayout) v1.findViewById(R.id.scale11);
        checkall = (Button)v1.findViewById(R.id.checkall);
        changebutton = (Button)v1.findViewById(R.id.changebutton);
        changebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = {"중1S","중1A","중1B","중1C","중2S","중2A","중2B", "중3S","중3A","중3B", "고1S","고1A","고1B", "고2S","고2A","고2B", "고3S","고3A","고3B"};
                final String[] grade = {"중1","중1","중1","중1","중2","중2","중2", "중3","중3","중3", "고1","고1","고1", "고2","고2","고2", "고3","고3","고3"};
                final String[] classarray = {"S","A","B","C","S","A","B", "S","A","B", "S","A","B", "S","A","B", "S","A","B"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(v1.getContext());
                dialog.setTitle("옮길 반을 고르세요.")
                        .setSingleChoiceItems(items,
                                0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                            for (int i = 0; i < checklist.size(); i++) {
                                                Change task = new Change();
                                                task.execute(idlist.get(i), grade[which], classarray[which]);
                                            }
                                            Intent intent = new Intent(v1.getContext(), ManageResult.class);
                                            startActivity(intent);
                                            checklist.clear();
                                            dialog.dismiss();



                                    }
                                });
                if (mCheck.length != 0) {
                    for (int i = 0; i < mCheck.length; i++) {
                        if (mCheck[i].isChecked()==true) {
                            checklist.add(i);
                        }
                    }
                    dialog.create();
                    dialog.show();
                }
            }
        });
        checkall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<mCheck.length;i++){
                    mCheck[i].setChecked(true);
                }
            }
        });
        Student task = new Student();
        task.execute(ManageResult.gradee,ManageResult.classs);
        return v1;
    }


    class Student extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = progressDialog.show(v1.getContext(), "Please Wait", null,
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
                    for (int i = 0; i < length; i++) {
                        String getarray1 = temp[i].split(",")[0];
                        String getarray2 = temp[i].split(",")[1];

                        idlist.add(getarray1);
                        namelist.add(getarray2);
                    }
                    mCheck = new CheckBox[length];
                    mText = new TextView[length];
                    for (int i = 0; i < length; i++) {
                        TextView idbutton = new TextView(v1.getContext());
                        mText[i] = idbutton;
                        mText[i].setTag(i);
                        CheckBox idcheck = new CheckBox(v1.getContext()) ;
                        mCheck[i] = idcheck;
                        s2.addView(idbutton, 0, i * 100, 250, 150);
                        s2.setScale_TextSize(idbutton, 50);
                        idbutton.setText(namelist.get(i));
                        s2.addView(idcheck,500,i*100,100,100);
                    }
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v1.getContext());
                    dialog.setTitle("송신 에러")
                            .setMessage("에러 코드 : " + result)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create().show();
                }
//                Toast.makeText(v1.getContext(),"체크 박스 개수 : "+ mCheck.length,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = "http://ec2-13-125-229-159.ap-northeast-2.compute.amazonaws.com/getstudent.php";
            String postParameters = "Grade=" + params[0] + "&Class=" + params[1];
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
    class Change extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = progressDialog.show(v1.getContext(), "Please Wait", null,
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
//                    for (int i = 0; i < length; i++) {
//                        String getarray1 = temp[i].split(",")[0];
//                        String getarray2 = temp[i].split(",")[1];
//
//                        idlist.add(getarray1);
//                        namelist.add(getarray2);
//                    }
//
//                    mText = new TextView[length + 1];
//                    for (int i = 0; i < length; i++) {
//                        TextView idbutton = new TextView(v1.getContext());
//
//                        mText[i] = idbutton;
//                        mText[i].setTag(i);
//                        s2.addView(idbutton, 0, i * 100, 250, 150);
//                        s2.setScale_TextSize(idbutton, 50);
//
//                        idbutton.setText(namelist.get(i));
//                    }
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v1.getContext());
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
            String serverURL = "http://ec2-13-125-229-159.ap-northeast-2.compute.amazonaws.com/changeclass.php";
            String postParameters = "Id=" + params[0] + "&Grade=" + params[1] + "&Class="+ params[2];
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
}