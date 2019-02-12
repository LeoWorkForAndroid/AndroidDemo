package cn.top.reflect;


import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //获取Activity类的demo
        DemoClass activity = new DemoClass();

        //1.获取Class对象的方式
        Class clazz = activity.getClass();
        Log.e(TAG, clazz.getName());

       /* Class clazz2 = Activity.class;
        Log.e(TAG, "" + (clazz).equals(clazz2));


        try {
            Class clazz3 = Class.forName("android.app.Activity");
            Log.e(TAG, "" + (clazz).equals(clazz3));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());

        }*/


        //1.获取构造方法并使用
        //获取公有构造方法
        Constructor[] conArray = clazz.getConstructors();
        for (Constructor c : conArray) {
            Log.e(TAG, "公有构造方法: " + c);
        }

        //获取所有构造方法
        conArray = clazz.getDeclaredConstructors();
        for (Constructor c : conArray) {
            Log.e(TAG, "所有构造方法: " + c);

        }

        //获取公有，无参的构造函数
        try {
            Constructor constructor = clazz.getConstructor(null);
            Log.e(TAG, "公有，无参构造方法: " + constructor);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        //获取私有构造方法
        try {
            Constructor declaredAnnotation = clazz.getDeclaredConstructor(char.class);
            Log.e(TAG, "获取私有构造方法:" + declaredAnnotation);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


        //3.获取成员变量并使用

/*
 * 获取成员变量并调用：
 *
 * 1.批量的
 * 		1).Field[] getFields():获取所有的"公有字段"
 * 		2).Field[] getDeclaredFields():获取所有字段，包括：私有、受保护、默认、公有；
 * 2.获取单个的：
 * 		1).public Field getField(String fieldName):获取某个"公有的"字段；
 * 		2).public Field getDeclaredField(String fieldName):获取某个字段(可以是私有的)
 *
 * 	 设置字段的值：
 * 		Field --> public void set(Object obj,Object value):
 * 					参数说明：
 * 					1.obj:要设置的字段所在的对象；
 * 					2.value:要为字段设置的值；
 *
 */


        //获取所有公有字段
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String key = field.getName();
            Log.e(TAG, fields.length+"获取所有公有字段:" + key);
        }

        //获取所有字段（包括私有，受保护，默认）
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            String key = field.getName();
            Log.e(TAG, declaredFields.length+"获取所有字段（包括私有，受保护，默认）:" + key);
        }



    }
}
