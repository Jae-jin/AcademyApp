package com.example.jung_jaejin.myproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class month extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView selectedText;
    Spinner spinner;
    String[] item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);

        spinner = (Spinner)findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);

        item = new String[]{"선택하세요","1주차(2018.09.18 ~2018.10.17","2주차(2018.10.18 ~2018.11.17","3주차(2018.11.18 ~2018.12.17"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedText.setText(item[position]);
        if(selectedText.getText().toString().equals("선택하세요")){
            selectedText.setText("");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        selectedText.setText("");
    }
}
