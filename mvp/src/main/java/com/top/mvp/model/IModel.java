package com.top.mvp.model;

/**
 * 作者：ProZoom
 * 时间：2018/3/14
 * 描述：Model层接口---实现该接口的类负责实际的获取数据操作，如数据库读取、网络加载
 */
public interface IModel {

    void getData(Model.LoadDataCallback callback);

}
