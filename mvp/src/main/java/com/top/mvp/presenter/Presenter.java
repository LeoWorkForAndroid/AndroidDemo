package com.top.mvp.presenter;

import com.top.mvp.model.Model;
import com.top.mvp.view.IView;

/**
 * 作者：ProZoom
 * 时间：2018/3/14
 * 描述：
 */
public class Presenter implements IPresenter, Model.LoadDataCallback {

    private final IView mView;
    private final Model mModel;

    public Presenter(IView view) {
        mView = view;
        mModel = new Model();

    }

    @Override
    public void loadData() {
        mView.showLoadingProgress("加载数据中");
        mModel.getData(Presenter.this);
    }

    @Override
    public void success(String data) {
        mView.showData(data);
    }

    @Override
    public void failure() {

    }
}
