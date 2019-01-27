package cn.top.reflect;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Activity activity=new Activity();
        Class clazz = activity.getClass();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String key = field.getName();
            Log.e("reflectdemo",key);
           // PropertyDescriptor descriptor = new PropertyDescriptor(key, clazz);
           // Method method = descriptor.getReadMethod();
            //Object value = method.invoke(person);

            //System.out.println(key + ":" + value);

        }
    }
}
