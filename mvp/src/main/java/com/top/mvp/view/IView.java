package com.top.mvp.view;

/**
 * 作者：ProZoom
 * 时间：2018/3/14
 * 描述：View层接口---执行各种UI操作，定义的方法主要是给Presenter中来调用的
 */
public interface IView {
    void showLoadingProgress(String message);

    void showData(String text);
}
