package com.top.android.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * 作者：李阳
 * 时间：2019/1/14
 * 描述：
 *
 * @author Administrator
 */
public class UserBean extends BaseObservable{


    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private int age;


    public UserBean(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Bindable
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    @Override
    public String toString() {
        return "UserBean{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
