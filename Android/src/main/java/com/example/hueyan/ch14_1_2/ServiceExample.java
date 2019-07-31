package com.example.hueyan.ch14_1_2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ServiceExample extends Activity {
    Button myButton;
    EditText customer_id;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customer_id = (EditText) findViewById(R.id.editText);
        myButton = (Button) findViewById(R.id.Button01);
        myButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if ( myButton.getText().equals("背景執行") )
                {
                    myButton.setText("停止背景執行");
                    // 透過Intent指定要啟動的Service
                    Intent i = new Intent(ServiceExample.this, MainActivity.class);
                    i.putExtra("key1", customer_id.getText().toString());
                    startService(i);
                }
                else
                {
                    myButton.setText("背景執行");
                    // 透過Intent指定要停止的Service
                    Intent i = new Intent(ServiceExample.this, MainActivity.class);
                    stopService(i);
                }
            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent();
            intent.setClass(ServiceExample.this, Main2Activity.class);//回到選單頁
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
