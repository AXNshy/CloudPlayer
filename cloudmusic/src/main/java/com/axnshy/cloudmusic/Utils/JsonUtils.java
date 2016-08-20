package com.axnshy.cloudmusic.Utils;


import com.axnshy.cloudmusic.Json.User_BasicJson;
import com.axnshy.cloudmusic.User;
import com.google.gson.Gson;

/**
 * Created by axnshy on 2016/3/14.
 */
public class JsonUtils {

    protected String login_info;

    public void setLogin_info(String login_info) {
        this.login_info = login_info;
    }

    public String getLogin_info() {
        return login_info;
    }

    /*
    * 从json数据中获取登录信息
    * */
    public static String getLoginInfoFromJson(String jsonString) {
        User_BasicJson loginInfo=new Gson().fromJson(jsonString,User_BasicJson.class);
        JsonUtils jsonUtils = new JsonUtils();
        jsonUtils.setLogin_info(loginInfo.getLogin_state());
        return jsonUtils.getLogin_info();
    }

    /**
     * 把Json数据转化成一个User_Basic类对象
     */
    public static User parseJsonToUser_Basic(String jsonString) {
        if(jsonString == null) {
            return null;
        }

        User user=new Gson().fromJson(jsonString,User_BasicJson.class).getUser_basic();

        return user;
    }

}
