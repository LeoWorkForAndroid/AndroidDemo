package com.top.screenrecording.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.top.screenrecording.MainActivity;

/**
 * 作者：李阳
 * 时间：2019/2/25
 * 描述：
 */
public class SecretCodeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent activityIntent = new Intent(context, MainActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(activityIntent);
    }
}
