package com.top.start;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private String packname="com.top.screenrecording";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void start(View view) {
        Intent intent = new Intent();
        //第一种方式
        ComponentName cn = new ComponentName("com.top.screenrecording", "com.top.screenrecording.MainActivity");
        try {
            intent.setComponent(cn);
            //第二种方式
            //intent.setClassName("com.example.fm", "com.example.fm.MainFragmentActivity");
            intent.putExtra("test", "intent1");
            startActivity(intent);
        } catch (Exception e) {
            //TODO  可以在这里提示用户没有安装应用或找不到指定Activity，或者是做其他的操作
            Log.e("screenrecording",e.toString());
        }
    }
}
