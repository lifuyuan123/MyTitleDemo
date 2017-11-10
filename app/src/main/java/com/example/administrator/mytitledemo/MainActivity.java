package com.example.administrator.mytitledemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tv_1,tv_2,tv_3,tv_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_1= (TextView) findViewById(R.id.tv_1);
        tv_2= (TextView) findViewById(R.id.tv_2);
        tv_3= (TextView) findViewById(R.id.tv_3);
        tv_4= (TextView) findViewById(R.id.tv_4);

        tv_1.setOnClickListener(this);
        tv_2.setOnClickListener(this);
        tv_3.setOnClickListener(this);
        tv_4.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_1://放大效果
                startActivity(new Intent(MainActivity.this,Main1Activity.class));
                break;
            case R.id.tv_2://移动效果
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
                break;
            case R.id.tv_3://沉浸式
                startActivity(new Intent(MainActivity.this,Main3Activity.class));
                break;
            case R.id.tv_4:
                startActivity(new Intent(MainActivity.this,Main4Activity.class));
                break;
        }
    }
}
