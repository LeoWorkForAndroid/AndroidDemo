package com.top.screenrecording.service;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.top.screenrecording.MainActivity;

public class BootBroadcastReceiver extends BroadcastReceiver {


    private static String TAG="ScreenRecording";

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent){
        Log.i("ScreenRecording","机器开机了,需要启动ScreenRecordService录屏服务！");
        Intent service = new Intent(context, MainActivity.class);
        service.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(service);
    }
}
