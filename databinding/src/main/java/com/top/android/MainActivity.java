package com.top.android;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.top.android.bean.UserBean;
import com.top.android.databinding.ActivityMainBinding;

/**
 * 作者：李阳
 * 时间：2019/1/14
 * 描述：
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private UserBean userBean;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //绑定变量
        userBean = new UserBean("张三",26,"18012092538",0,"http://www.baidu.com");

        //1.数据绑定
        //bean绑定
        mBinding.setUser(userBean);
        //数组绑定
        String[] arrays={"字符串1","字符串2"};
        //mBinding.setArray(arrays);


        //2.事件方法绑定

    }
}
