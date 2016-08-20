package com.axnshy.cloudmusic.Json;


import com.axnshy.cloudmusic.User;

/**
 * Created by axnshy on 16/7/22.
 */
public class User_BasicJson {
    private String login_state;

    private User user_basic;

    public User_BasicJson(){

    }

    public String getLogin_state() {
        return login_state;
    }

    public User getUser_basic() {
        return user_basic;
    }

    public void setLogin_state(String login_state) {
        this.login_state = login_state;
    }


    public void setUser_basic(User user_basic) {
        this.user_basic = user_basic;
    }
}
