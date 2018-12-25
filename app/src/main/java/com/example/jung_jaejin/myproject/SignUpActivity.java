package com.example.jung_jaejin.myproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SignUpActivity extends AppCompatActivity {
    Button Next;
    EditText Name;
    EditText Phone;
    RadioGroup rg0;
    RadioGroup rg1;
    RadioGroup rg2;
    String getname;
    String getphone;
    String getgrade = "선택안함";
    String getclass = "선택안함";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Name = (EditText)findViewById(R.id.editTextName);
        Phone = (EditText)findViewById(R.id.editTextHP);
        Next = (Button)findViewById(R.id.next);
        rg0 = (RadioGroup)findViewById(R.id.group0);
        rg1 = (RadioGroup)findViewById(R.id.group1);
        rg2 = (RadioGroup)findViewById(R.id.group2);




        rg0.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.middle1)
                {
                    rg1.clearCheck();
                    rg0.check(checkedId);
                    getgrade = "middle1";
                }
                if(checkedId == R.id.middle2)
                {
                    rg1.clearCheck();
                    rg0.check(checkedId);
                    getgrade = "middle2";
                }
                if(checkedId == R.id.middle3)
                {
                    rg1.clearCheck();
                    rg0.check(checkedId);
                    getgrade = "middle3";
                }

            }
        });
        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.high1)
                {rg0.clearCheck();
                rg1.check(checkedId);
                getgrade = "high1";}
                if(checkedId == R.id.high2)
                {rg0.clearCheck();
                    rg1.check(checkedId);
                    getgrade = "high2";}
                if(checkedId == R.id.high3)
                {rg0.clearCheck();
                    rg1.check(checkedId);
                    getgrade = "high3";}
            }
        });
        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.Sclass)
                {
                    getclass = "S";
                }
                if(checkedId == R.id.Aclass)
                {
                    getclass = "A";
                }
                if(checkedId == R.id.Bclass)
                {
                    getclass = "B";
                }
            }
        });

        Next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){

                getname = Name.getText().toString();
                getphone = Phone.getText().toString();
                if(getname.replace(" ","").equals("")==true||
                        getphone.replace(" ","").equals("")||
                        getgrade.equals("선택안함")||getclass.equals("선택안함")){

                    AlertDialog.Builder dialog = new AlertDialog.Builder(SignUpActivity.this);
                    dialog.setTitle("주의!")
                            .setMessage("모두 작성해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create().show();
                }
                else{
                     Intent intent = new Intent(getApplicationContext(),Signup2.class);
                     intent.putExtra("이름",getname);
                     intent.putExtra("핸드폰",getphone);
                     intent.putExtra("학년",getgrade);
                     intent.putExtra("반",getclass);
                     startActivity(intent);
                }
            }
        });
    }
}
