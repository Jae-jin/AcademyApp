package com.example.jung_jaejin.myproject;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import static android.speech.tts.TextToSpeech.ERROR;

public class Review extends AppCompatActivity {

    private TextView word;
    private TextView mean;
    private TextView numofword;
    private TextToSpeech tts;
    private ArrayList<String> wordlist = new ArrayList<>();//영어단어 집어 넣는 리스트
    private ArrayList<String> meanlist = new ArrayList<>();//영어의미 집어 넣는 리스트
    private int i = 0;//
    private int limittime = 300;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

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
        });//영어 단어 소리 내기 위한 객체 생성
        try{
            StringBuffer data = new StringBuffer();
            FileInputStream fis = openFileInput("_data.txt");
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fis));
            String str = buffer.readLine();
            while(str != null){
                data.append(str+"\n");
                Log.d("TEST  ",str);
                String[] array = str.split(" ");
                wordlist.add(array[0]);
                meanlist.add(array[1]);
                Log.d("word:",array[0]);
                Log.d("mean:",array[1]);
                str = buffer.readLine();
            }
            buffer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Message message = handler.obtainMessage(1);
        handler.sendMessageDelayed(message, 1000);//1초 대기

    }
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case 1:
                    Message message = handler.obtainMessage(2);
                    handler.sendMessageDelayed(message, 2000);//2초 있다가 단어를 불러온다.

                    break;
                case 2:
                    if(i<wordlist.size()) {//단어 아직 남았을 때
                        word.setText(wordlist.get(i));
                        mean.setText("");//뜻칸 비워두고
                        numofword.setText(i + 1 + "/" + wordlist.size());//i는 몇 번째 단어인지
                        Message message1 = handler.obtainMessage(3);
                        tts.speak(wordlist.get(i),TextToSpeech.QUEUE_FLUSH,null);//음성 발사
                        handler.sendMessageDelayed(message1, 2000);//2초 있다가 뜻까지 같이 표시한다.
                    }
                    else {//단어 다 봤을 경우
                        removeMessages(3);
                        Intent intent = new Intent(getApplicationContext(),Starttest_review.class);//다음 화면으로 넘어간다.
                        startActivity(intent);
                    }
                    break;

                case 3:
                    word.setText(wordlist.get(i));
                    mean.setText(meanlist.get(i));//뜻표시
                    numofword.setText(i + 1 + "/" + wordlist.size());
                    tts.speak(wordlist.get(i),TextToSpeech.QUEUE_FLUSH,null);
                    Message message2 = handler.obtainMessage(2);
                    i++;//다음 단어로~
                    handler.sendMessageDelayed(message2, 2000);//2초 있다가 다음 단어 표시

                    break;
            }
        }
    };
}
