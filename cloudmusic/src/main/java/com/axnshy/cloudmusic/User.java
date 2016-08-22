package com.axnshy.cloudmusic;


import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by axnshy on 16/5/25.
 */
public class User extends BmobUser {



    private String nickName;

    private int gender;

    private BmobFile avatar;

    private int age;

    private String qq;

    private Date birthday;

    private String address;

    public User(){
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public Date getBirthday() {
        return birthday;
    }

    public int getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getNickName() {
        return nickName;
    }

    public BmobFile getAvatar() {
        return avatar;
    }

    public String getQq() {
        return qq;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }
}
