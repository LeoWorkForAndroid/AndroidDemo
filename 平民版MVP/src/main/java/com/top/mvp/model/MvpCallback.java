package com.top.mvp.model;

/**
 * 作者：ProZoom
 * 时间：2018/3/14
 * 描述：
 */
public interface MvpCallback {


    void onSuccess(String data);

    void onFailure(String msg);

    void onError();

    void onComplete();

}
