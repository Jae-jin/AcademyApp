package com.example.jung_jaejin.myproject;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import static android.speech.tts.TextToSpeech.ERROR;


public class Study1 extends AppCompatActivity {
    private TextView word;
    private TextView mean;
    private TextView numofword;
    private TextToSpeech tts;
    private ArrayList<String> wordlist = new ArrayList<>();
    private ArrayList<String> meanlist = new ArrayList<>();
    private int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study1);

        word = (TextView) findViewById(R.id.word);
        mean = (TextView) findViewById(R.id.mean);
        numofword = (TextView) findViewById(R.id.NumOfWord);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR){
                    tts.setLanguage(Locale.ENGLISH);
                }
            }
        });
        try {
            AssetManager am = getAssets();
            InputStream is = am.open("English1.xls");
            Workbook wb = Workbook.getWorkbook(is);
            if(wb != null) {
                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                if(sheet != null) {

                    int colTotal = sheet.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet.getColumn(colTotal - 1).length;

                    StringBuilder sb;
                    for (int row = rowIndexStart; row < rowTotal; row++) {

                        String getword = sheet.getCell(0, row).getContents();
                        String getmean = sheet.getCell(1, row).getContents();
                        wordlist.add(getword);
                        meanlist.add(getmean);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

        Message message = handler.obtainMessage(1);
        handler.sendMessageDelayed(message, 1000);

    }
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case 1:
                        Message message = handler.obtainMessage(2);
                        handler.sendMessageDelayed(message, 2000);

                    break;
                case 2:
                        if(i<wordlist.size()) {
                            word.setText(wordlist.get(i));
                            mean.setText("");
                            numofword.setText(i + 1 + "/" + wordlist.size());
                            Message message1 = handler.obtainMessage(3);
                            tts.speak(wordlist.get(i),TextToSpeech.QUEUE_FLUSH,null);
                            handler.sendMessageDelayed(message1, 2000);
                        }
                    else {
                            removeMessages(3);
                            Intent intent = new Intent(getApplicationContext(),Starttest.class);
                            startActivity(intent);
                        }
                            break;

                case 3:
                            word.setText(wordlist.get(i));
                            mean.setText(meanlist.get(i));
                            numofword.setText(i + 1 + "/" + wordlist.size());
                            tts.speak(wordlist.get(i),TextToSpeech.QUEUE_FLUSH,null);
                            Message message2 = handler.obtainMessage(2);
                            i++;
                            handler.sendMessageDelayed(message2, 2000);

                    break;
            }
        }
    };
}
