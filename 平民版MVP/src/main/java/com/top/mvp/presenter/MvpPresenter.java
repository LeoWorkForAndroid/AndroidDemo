package com.top.mvp.presenter;

import com.top.mvp.model.MvpCallback;
import com.top.mvp.model.MvpModel;
import com.top.mvp.view.MvpView;


/**
 * 作者：ProZoom
 * 时间：2018/3/14
 * 描述：
 */
public class MvpPresenter {

    private MvpView mView;

    public MvpPresenter(MvpView mView) {
        this.mView = mView;
    }


    /**
     * 关联view
     *
     * @param v
     */
    public void attachView(MvpView v) {
        this.mView = v;
    }

    /**
     * 取消关联view，一般在destory里调用
     */
    public void detachView() {
        this.mView = null;
    }

    /**
     * 是否与View建立连接
     * 每次调用业务请求的时候都要出先调用方法检查是否与View建立连接
     */
    public boolean isViewAttached() {
        return mView != null;
    }

    public void getData(String param) {

        mView.showLoading();

        MvpModel.getNetData(param, new MvpCallback() {
            @Override
            public void onSuccess(String data) {
                mView.showData(data);
            }

            @Override
            public void onFailure(String msg) {
                mView.showFailureMessage(msg);
            }

            @Override
            public void onError() {
                mView.showErrorMessage();
            }

            @Override
            public void onComplete() {
                mView.hideLoading();
            }
        });
    }
}
