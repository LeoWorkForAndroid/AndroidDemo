package com.top.mvp.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.top.mvp.R;
import com.top.mvp.presenter.IPresenter;
import com.top.mvp.presenter.Presenter;

import java.lang.ref.WeakReference;


/**
 * 作者：ProZoom
 * 时间：2018/3/14
 * 描述：实现IView接口并实现各种UI操作的方法（其他的业务逻辑在Presenter中进行操作）
 */
public class MainActivity extends AppCompatActivity implements IView {

    private Button mBtnShowToast;
    private TextView mText;
    private MyHandler mHandler = new MyHandler(MainActivity.this);
    private IPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //实例化Presenter，并将实现了IView接口的类传入进去
        mPresenter = new Presenter(MainActivity.this);

        mBtnShowToast = (Button) findViewById(R.id.btn_show_toast);
        mText = (TextView) findViewById(R.id.tv);
        mBtnShowToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通过Presenter来实现业务逻辑操作，View层只负责UI相关操作
                mPresenter.loadData();
            }
        });
    }

    @Override
    public void showLoadingProgress(final String message) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mText.setText(message);
            }
        });
    }

    @Override
    public void showData(final String text) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mText.setText(text);
            }
        });
    }

    private class MyHandler extends Handler {

        //弱引用，防止内存泄露
        WeakReference<MainActivity> weakReference;

        public MyHandler(MainActivity activity) {
            this.weakReference = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    weakReference.get().mText.setText(msg.what);
                    break;
            }
        }
    }
}
