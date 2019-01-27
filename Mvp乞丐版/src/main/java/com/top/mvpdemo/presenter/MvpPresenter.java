package com.top.mvpdemo.presenter;

import com.top.mvpdemo.model.MvpCallback;
import com.top.mvpdemo.model.MvpModel;
import com.top.mvpdemo.view.MvpView;

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
