package com.top.mvp.view;

/**
 * 作者：ProZoom
 * 时间：2018/3/14
 * 描述：
 */
public interface MvpView {


    void showLoading();

    void hideLoading();

    void showData(String data);

    void showFailureMessage(String msg);

    void showErrorMessage();
}
