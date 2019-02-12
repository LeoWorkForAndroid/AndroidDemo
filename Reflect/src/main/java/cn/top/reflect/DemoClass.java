package cn.top.reflect;

import android.util.Log;

/**
 * 作者：李阳
 * 时间：2019/1/30
 * 描述：
 */
public class DemoClass {

    private static final String TAG = "DemoClass";

    //私有实例变量
    private int a;

    //公有实例变量
    public int aa;

    //(静态)类变量
    static int aaa;


    public DemoClass() {
        Log.i(TAG, "DemoClass()");
    }


    public DemoClass(int a) {
        this.a = a;
    }

    //公有方法
    public void method() {
        Log.i(TAG, "public void method()");

    }

    //私有方法
    private void method2() {
        Log.i(TAG, "private void method2()");

    }

        //私有方法
    protected void method3() {
        Log.i(TAG, "protected void method3()");

    }

}
