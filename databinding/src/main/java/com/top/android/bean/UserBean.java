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

    private String phone;

    private int gender;

    private String imgUrl;


    public  UserBean(String name, int age, String phone, int gender, String imgUrl) {
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.gender = gender;
        this.imgUrl = imgUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
