package com.top.screenrecording;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.top.screenrecording.service.ScreenRecordService;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import kr.co.namee.permissiongen.PermissionGen;

public class MainActivity extends Activity {


    private static String TAG = "ScreenRecording";


    private String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
            Manifest.permission.CAMERA};
    private BroadcastReceiver endReceiver;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*        //设置1像素
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);*/

        // initBroadcastReceiver();

        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏

        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        int i = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            for (String permission : permissions) {

                i++;
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, permission + "未授权");
                    //没有权限提示授权
                    ActivityCompat.requestPermissions(this, permissions, 211);
                } else {
                    Log.i(TAG, permission + "已经授权了!");
                    if (i == permissions.length) {
                        startScreenRecord();
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //checkScreen();
        //finish();
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case 211:
                switch (permissions[0]) {
                    case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                        //权限1
                        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            boolean b = shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (!b) {
                                Log.i(TAG, "再次申请授权!");
                            }
                        } else {
                            Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case Manifest.permission.READ_EXTERNAL_STORAGE:
                        //权限2
                        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            boolean b = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                            if (!b) {
                                Log.i(TAG, "再次申请授权!");
                            }
                        } else {
                            Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS:
                        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            boolean b = shouldShowRequestPermissionRationale(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
                            if (!b) {
                                Log.i(TAG, "再次申请授权!");
                            }
                        } else {
                            Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case Manifest.permission.CAMERA:
                        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            boolean b = shouldShowRequestPermissionRationale(Manifest.permission.CAMERA);
                            if (!b) {
                                Log.i(TAG, "再次申请授权!");
                            }
                        } else {
                            Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                //获得录屏权限，启动Service进行录制
                Intent intent = new Intent(MainActivity.this, ScreenRecordService.class);
                intent.putExtra("resultCode", resultCode);
                intent.putExtra("resultData", data);
                startService(intent);
                //Toast.makeText(this, "录屏开始", Toast.LENGTH_SHORT).show();
                //showOrHide();
            } else {
                //Toast.makeText(this, "录屏失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //start screen record
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startScreenRecord() {
        //Manages the retrieval of certain types of MediaProjection tokens.
        MediaProjectionManager mediaProjectionManager =
                (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        //Returns an Intent that must passed to startActivityForResult() in order to start screen capture.
        Intent permissionIntent = mediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(permissionIntent, 1000);
    }

    //stop screen record.
    private void stopScreenRecord() {
        Intent service = new Intent(this, ScreenRecordService.class);
        stopService(service);
    }


    private void initBroadcastReceiver() {
        // Listen for broadcasts
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mScreenOffReceiver, intentFilter);
    }

    private final BroadcastReceiver mScreenOffReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                Log.i(TAG, "screen on");
                startScreenRecord();
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                Log.i(TAG, "screen off");
                stopScreenRecord();
            }
        }
    };


    private void showOrHide() {
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(this, MainActivity.class);
        int res = packageManager.getComponentEnabledSetting(componentName);
        if (res == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
                || res == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            // 隐藏应用图标
            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        } else {
            // 显示应用图标
            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                    PackageManager.DONT_KILL_APP);
        }
    }


}
