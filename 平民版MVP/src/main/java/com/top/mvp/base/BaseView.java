package com.top.mvp.base;

import android.content.Context;

/**
 * 作者：ProZoom
 * 时间：2018/3/14
 * 描述：
 */
public interface BaseView {

    void showLoading();

    void hideLoading();

    void showToast(String msg);

    void showError();

    Context getContext();
}
