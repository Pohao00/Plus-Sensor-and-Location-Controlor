package com.example.hueyan.ch14_1_2;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends ActionBarActivity {
    Button backButton;
    Button Button2;
    Button Button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        backButton = (Button) findViewById(R.id.button);
        Button2 = (Button) findViewById(R.id.button2);
        Button3 = (Button) findViewById(R.id.button3);

        backButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Main2Activity.this, ServiceExample.class);//啟動第二頁
                startActivity(intent);
                finish();
            }
        });
        Button2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Main2Activity.this, Service2Example.class);//啟動第二頁
                startActivity(intent);
                finish();
            }
        });
        Button3.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Main2Activity.this, Main3Activity.class);//啟動第二頁
                startActivity(intent);
                finish();
            }
        });
    }
}
