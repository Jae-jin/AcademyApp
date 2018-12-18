package com.example.jung_jaejin.myproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultPage extends AppCompatActivity {
    private Button result;
    private int NumOfRight;
    private int NumOfProblem;
    private int IsPass;
    private TextView howmuch;
    private TextView passorfail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);
        Intent intent = getIntent();

        NumOfRight = intent.getIntExtra("numOfRight", 0);
        NumOfProblem = intent.getIntExtra("numOfProblem",120);
        IsPass = intent.getIntExtra("pass",0);


        howmuch = (TextView) findViewById(R.id.Howmuch);
        passorfail = (TextView) findViewById(R.id.PassOrFail);
        result = (Button)findViewById(R.id.result);

        if(IsPass == 1){
            howmuch.setText(NumOfRight + "/" + NumOfProblem);
            passorfail.setText("PASS");
            result.setText("완료");
        }
        else{
            howmuch.setText(NumOfRight + "/" + NumOfProblem);
            passorfail.setText("Fail");
            result.setText("다시하기");
        }
        result.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),month.class);
                startActivity(intent);
            }
        });
    }
}
