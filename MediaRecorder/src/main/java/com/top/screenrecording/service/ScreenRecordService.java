package com.top.screenrecording.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.util.DisplayMetrics.DENSITY_LOW;

public class ScreenRecordService extends Service {

    private static String TAG = "ScreenRecording";

    private int resultCode;
    private Intent resultData = null;

    private MediaProjection mediaProjection = null;
    private MediaRecorder mediaRecorder = null;
    private VirtualDisplay virtualDisplay = null;

    private int mScreenWidth;
    private int mScreenHeight;
    private int mScreenDensity;

    private String filePathName;


    Handler mHandler = new Handler() {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 1111111:

                    //检索视频，五天以前的都删除
                    getAll("sdcard/xibei/");

                    Log.e(TAG, "再次录屏");
                    mediaProjection = createMediaProjection();
                    if (mediaProjection == null) {
                        Log.e(TAG, "mediaProjection is null!");
                    }

                    mediaRecorder = createMediaRecorder();

                    if (mediaRecorder == null) {
                        Log.e(TAG, "mediaRecorder is null!");
                    }

                    virtualDisplay = createVirtualDisplay();
                    if (virtualDisplay == null) {
                        Log.e(TAG, "virtualDisplay is null!");
                    }


                    mediaRecorder.start();
                    mediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onInfo(MediaRecorder mr, int what, int extra) {

                            if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                                Log.e("ScreenRecording", "Maximum Duration Reached");
                                if (virtualDisplay != null) {
                                    virtualDisplay.release();
                                    virtualDisplay = null;
                                }
                                if (mediaRecorder != null) {
                                    try {
                                        mediaRecorder.stop();
                                        mediaRecorder.reset();
                                    } catch (IllegalStateException e) {
                                        // TODO 如果当前java状态和jni里面的状态不一致，
                                        //e.printStackTrace();
                                        mediaRecorder = null;
                                        mediaRecorder = new MediaRecorder();
                                    }
                                    mediaRecorder.release();
                                    mediaRecorder = null;
                                }

                                if (mediaProjection != null) {
                                    mediaProjection.stop();

                                    mediaProjection = null;
                                }

                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                mHandler.sendEmptyMessage(1111111);
                            }

                        }
                    });

                    break;

            }

        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("ScreenRecording", "ScreenRecordService----------------onCreate");
        getScreenBaseInfo();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("ScreenRecording", "ScreenRecordService----------------onStartCommand");



        // Listen for broadcasts
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mScreenOffReceiver, intentFilter);

        try {
            resultCode = intent.getIntExtra("resultCode", -1);
            resultData = intent.getParcelableExtra("resultData");

            mediaProjection = createMediaProjection();
            if (mediaProjection == null) {
                Log.e(TAG, "mediaProjection is null!");
            }

            mediaRecorder = createMediaRecorder();
            if (mediaRecorder == null) {
                Log.e(TAG, "mediaRecorder is null!");
            }

            virtualDisplay = createVirtualDisplay();
            if (virtualDisplay == null) {
                Log.e(TAG, "virtualDisplay is null!");
            }

            mediaRecorder.start();

            mediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onInfo(MediaRecorder mr, int what, int extra) {

                    if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                        Log.e("ScreenRecording", "Maximum Duration Reached");
                        if (virtualDisplay != null) {
                            virtualDisplay.release();
                            virtualDisplay = null;
                        }
                        if (mediaRecorder != null) {
                            try {
                                mediaRecorder.stop();
                                mediaRecorder.reset();
                            } catch (IllegalStateException e) {
                                // TODO 如果当前java状态和jni里面的状态不一致，
                                //e.printStackTrace();
                                mediaRecorder = null;
                                mediaRecorder = new MediaRecorder();
                            }
                            mediaRecorder.release();
                            mediaRecorder = null;
                        }

                        if (mediaProjection != null) {
                            mediaProjection.stop();

                            mediaProjection = null;
                        }

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessage(1111111);


                    }

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.i("ScreenRecording", "ScreenRecordService----------------onBind");
        return null;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopScreenRecord();
        unregisterReceiver(mScreenOffReceiver);
    }

    //createMediaProjection
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MediaProjection createMediaProjection() {
        /**
         * Use with getSystemService(Class) to retrieve a MediaProjectionManager instance for
         * managing media projection sessions.
         */
        return ((MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE)).getMediaProjection(resultCode, resultData);
        /**
         * Retrieve the MediaProjection obtained from a succesful screen capture request.
         * Will be null if the result from the startActivityForResult() is anything other than RESULT_OK.
         */
    }

    private MediaRecorder createMediaRecorder() {

        Calendar calendar = Calendar.getInstance();

        //获取系统的日期
        //年
        int year = calendar.get(Calendar.YEAR);
        //月
        int month = calendar.get(Calendar.MONTH) + 1;
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //获取系统时间
        //小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //分钟
        int minute = calendar.get(Calendar.MINUTE);
        //秒
        int second = calendar.get(Calendar.SECOND);

        @SuppressLint("DefaultLocale") String dirname = year + String.format("%02d", month) + String.format("%02d", day);
        @SuppressLint("DefaultLocale") String name = dirname + String.format("%02d", hour) + String.format("%02d", minute) + String.format("%02d", second);


        String filedir = Environment.getExternalStorageDirectory() + File.separator + "xibei" + File.separator;


        String filedir2 = filedir + dirname + File.separator;

        filePathName = filedir2 + File.separator + "video" + name + "";

        File dir = new File(filedir);

        if (!dir.exists()) {
            dir.mkdir();
        } else {

        }

        File dir2 = new File(filedir2);

        if (!dir2.exists()) {
            dir2.mkdir();
        } else {

        }

        //Used to record audio and video. The recording control is based on a simple state machine.
        MediaRecorder mediaRecorder = new MediaRecorder();
        //Set the video source to be used for recording.
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        //Set the format of the output produced during recording.
        //3GPP media file format
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        //mediaRecorder.setCaptureRate(1);
        //Sets the video encoding bit rate for recording.
        //param:the video encoding bit rate in bits per second.
        mediaRecorder.setVideoEncodingBitRate(1024);
        //Sets the video encoder to be used for recording.
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        //Sets the width and height of the video to be captured.
        mediaRecorder.setVideoSize(mScreenWidth, mScreenHeight);


        //Sets the frame rate of the video to be captured.
        mediaRecorder.setVideoFrameRate(15);

        mediaRecorder.setMaxDuration(2 * 60000);

        //2.1M
        //mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_QVGA));

        //mediaRecorder.setMaxFileSize(1024 * 1024 * 3);


        try {
            //Pass in the file object to be written.
            mediaRecorder.setOutputFile(filePathName);
            //Prepares the recorder to begin capturing and encoding data.
            mediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediaRecorder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private VirtualDisplay createVirtualDisplay() {
        /**
         * name	String: The name of the virtual display, must be non-empty.This value must never be null.
         width	int: The width of the virtual display in pixels. Must be greater than 0.
         height	int: The height of the virtual display in pixels. Must be greater than 0.
         dpi	int: The density of the virtual display in dpi. Must be greater than 0.
         flags	int: A combination of virtual display flags. See DisplayManager for the full list of flags.
         surface	Surface: The surface to which the content of the virtual display should be rendered, or null if there is none initially.
         callback	VirtualDisplay.Callback: Callback to call when the virtual display's state changes, or null if none.
         handler	Handler: The Handler on which the callback should be invoked, or null if the callback should be invoked on the calling thread's main Looper.
         */
        /**
         * DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR
         * Virtual display flag: Allows content to be mirrored on private displays when no content is being shown.
         */
        return mediaProjection.createVirtualDisplay("mediaProjection", mScreenWidth, mScreenHeight, DENSITY_LOW,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mediaRecorder.getSurface(), null, null);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void stopScreenRecord() {
        if (virtualDisplay != null) {
            virtualDisplay.release();
            virtualDisplay = null;
        }
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
                mediaRecorder.reset();
            } catch (IllegalStateException e) {
                // TODO 如果当前java状态和jni里面的状态不一致，
                //e.printStackTrace();
                mediaRecorder = null;
                mediaRecorder = new MediaRecorder();
            }
            mediaRecorder.release();
            mediaRecorder = null;
        }

        if (mediaProjection != null) {
            mediaProjection.stop();

            mediaProjection = null;
        }
    }

    /**
     * 获取屏幕基本信息
     */
    private void getScreenBaseInfo() {
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);

        //A structure describing general information about a display, such as its size, density, and font scaling.
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
        mScreenDensity = metrics.densityDpi;
    }


    public void getAll(String path) {

        File file = new File(path);

        //判断是不是文件夹
        if (!file.isDirectory()) {

            Log.e(TAG, file.getName());
        } else {
            //是文件夹，便遍历出里面所有的文件（文件，文件夹）
            String[] fileslist = file.list();

            if (fileslist.length > 5) {

                int[] intArray = new int[fileslist.length];
                for (int i = 0; i < fileslist.length; i++) {
                    intArray[i] = Integer.parseInt(fileslist[i]);
                }

                // 用于记录数组最小值，初始值可以是数组中的任意一个值或者是Integer.MAX_VALUE
                int min = intArray[0];
                for (int i : intArray) {
                    if (i < min) {
                        min = i;
                    }
                }

                File file1 = new File("sdcard/xibei/" + String.valueOf(min));
                deleteDirWihtFile(file1);
            }

        }
    }

    private void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return;
        }
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                file.delete(); // 删除所有文件
            } else if (file.isDirectory()) {
                deleteDirWihtFile(file); // 递规的方式删除文件夹
            }
        }
        dir.delete();// 删除目录本身
    }


    /**
     * 按文件名排序
     *
     * @param filePath
     */
    private ArrayList<String> orderByName(String filePath) {
        ArrayList<String> FileNameList = new ArrayList<String>();
        File file = new File(filePath);
        File[] files = file.listFiles();
        List fileList = Arrays.asList(files);
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile()) {
                    return -1;
                }
                if (o1.isFile() && o2.isDirectory()) {
                    return 1;
                }
                return o1.getName().compareTo(o2.getName());
            }
        });
        for (File file1 : files) {
            if (file1.isDirectory()) {
                FileNameList.add(file1.getName());
            }
        }
        return FileNameList;
    }

    /**
     * 按文件修改时间排序
     *
     * @param filePath
     */
    public static ArrayList<String> orderByDate(String filePath) {
        ArrayList<String> FileNameList = new ArrayList<String>();
        File file = new File(filePath);
        File[] files = file.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                long diff = f1.lastModified() - f2.lastModified();
                if (diff > 0) {
                    return 1;
                } else if (diff == 0) {
                    return 0;
                } else {
                    return -1;// 如果 if 中修改为 返回-1 同时此处修改为返回 1 排序就会是递减
                }
            }

            @Override
            public boolean equals(Object obj) {
                return true;
            }

        });

        for (File file1 : files) {
            if (file1.isDirectory()) {
                FileNameList.add(file1.getName());
            }
        }
        return FileNameList;
    }


    private final BroadcastReceiver mScreenOffReceiver = new BroadcastReceiver() {
        @TargetApi(Build.VERSION_CODES.N)
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                Log.i(TAG, "screen on");

                mediaRecorder.resume();



            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                Log.i(TAG, "screen off");
                mediaRecorder.pause();
      /*          Intent intent1 = new Intent(getApplicationContext(), SinglePixelActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );


                //startActivity(intent1);*/
            }
        }
    };


}
