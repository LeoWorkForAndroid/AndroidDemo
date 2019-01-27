package com.top.mvp.model;

/**
 * 作者：ProZoom
 * 时间：2018/3/14
 * 描述：
 */
public class Model implements IModel {

    @Override
    public void getData(final LoadDataCallback callback) {
        //数据获取操作，如数据库查询、网络加载等
        new Thread() {
            @Override
            public void run() {
                try {
                    //模拟耗时操作
                    Thread.sleep(3000);
                    //获取到了数据
                    String data = "我是获取到的数据";
                    //将获取的数据通过接口反馈出去
                    callback.success(data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //获取数据失败的回调
                    callback.failure();
                }
            }
        }.start();
    }

    public interface LoadDataCallback {


        void success(String taskId);

        void failure();
    }
}
