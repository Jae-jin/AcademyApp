package com.example.jung_jaejin.myproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

import com.example.jung_jaejin.myproject.adapter.MyPagerAdapter;

public class ManageResult extends AppCompatActivity {

    Button classselect;
    public static String gradee = "중1";
    public static String classs = "S";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_result);
        classselect = (Button)findViewById(R.id.buttonclass);
        classselect.setText(gradee + classs);
        //TabLayout
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Management"));
        tabs.addTab(tabs.newTab().setText("Result"));
        tabs.setTabGravity(tabs.GRAVITY_FILL);
        classselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });
        //어답터설정
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), 2);
        viewPager.setAdapter(myPagerAdapter);

        //탭메뉴를 클릭하면 해당 프래그먼트로 변경-싱크화
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
    }

    @Override
    protected Dialog onCreateDialog(int id){
        final String[] items = {"중1S","중1A","중1B","중1C","중2S","중2A","중2B", "중3S","중3A","중3B", "고1S","고1A","고1B", "고2S","고2A","고2B", "고3S","고3A","고3B"};
        final String[] grade = {"중1","중1","중1","중1","중2","중2","중2", "중3","중3","중3", "고1","고1","고1", "고2","고2","고2", "고3","고3","고3"};
        final String[] classarray = {"S","A","B","C","S","A","B", "S","A","B", "S","A","B", "S","A","B", "S","A","B"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ManageResult.this);
        builder.setTitle("보고싶은 반 선택");
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                    ManageResult.gradee = grade[which];
                    ManageResult.classs = classarray[which];
                    classselect.setText(items[which]);
                    Intent intent = new Intent(ManageResult.this,ManageResult.class);
                    startActivity(intent);
                    dialog.dismiss();

            }
        });
        return builder.create();
    }
    @Override
    public void onBackPressed() {

        // Alert을 이용해 종료시키기
        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(ManageResult.this);
        dialog  .setTitle("관리자 창 종료")
                .setMessage("로그인 화면으로 돌아가시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
    }
}
