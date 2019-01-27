package com.top.mvpdemo.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.top.mvpdemo.R;
import com.top.mvpdemo.presenter.MvpPresenter;

/**
 * 作者：ProZoom
 * 时间：2018/3/14
 * 描述：
 */
public class MainActivity extends AppCompatActivity implements MvpView {

    private ProgressDialog mProgressDialog;

    private TextView tv;

    private MvpPresenter mvpPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv=findViewById(R.id.tv);

        mProgressDialog=new ProgressDialog(this);

        mProgressDialog.setCancelable(false);

        mProgressDialog.setMessage("Loading......");




        mvpPresenter=new MvpPresenter(this);
    }




    @Override
    public void showLoading() {
        if(!mProgressDialog.isShowing()){
            mProgressDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if(mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showData(String data) {

        tv.setText(data);
    }

    @Override
    public void showFailureMessage(String msg) {

        tv.setText(msg);
    }

    @Override
    public void showErrorMessage() {

        tv.setText("网络请求数据出现异常");
    }

    public void getData(View view) {

        mvpPresenter.getData("Normal");
    }

    public void getDataForFailure(View view) {

        mvpPresenter.getData("Failure");
    }

    public void getDataForError(View view) {

        mvpPresenter.getData("Error");
    }
}
