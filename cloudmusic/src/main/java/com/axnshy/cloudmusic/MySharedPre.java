package com.axnshy.cloudmusic;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by axnshy on 16/5/22.
 */
public class MySharedPre {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String REPEATTAG = "repeatTag";
    public static final String SHUFFLETAG = "shuffleTag";

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    //0:blue   1:red
    public static int APPTheme = 0;

    public static int APPTheme_BLUE = 0;

    public static String BLUE = "#4374f5";
    public static String RED = "#FA6E86";

    public static int APPTheme_RED = 1;


    public static final String APP_COLOR = "app_color";


    public static final String FIRST = "first_enter";

    public static SharedPreferences getSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(Config.USER_PREFER, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        return sharedPreferences;
    }

    public static void setAppTheme(Context context, String color) {
        getSharedPreferences(context);
        editor.putString(APP_COLOR, color);
        editor.commit();
        if (APPTheme == APPTheme_BLUE) {
            APPTheme = APPTheme_RED;
        } else {
            APPTheme = APPTheme_BLUE;
        }
    }

    public static String getAPPTheme(Context context) {
        getSharedPreferences(context);
        String color = sharedPreferences.getString(APP_COLOR, "NO");
        if (color == "NO") {
            if (color == "#FA6E86") {
                APPTheme = 1;
            } else
                APPTheme = 0;

        }
        return color;
    }


    public static void updateCurrentUser(Context context, String username, String password) {
        getSharedPreferences(context);
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.commit();
    }

    public static void unRegisterUser(Context context) {
        getSharedPreferences(context);
        editor.remove(USERNAME);
        editor.remove(PASSWORD);
        editor.commit();
    }

    public static void setRepeatAndShuffleTag(Context context, int repeatTag, int shuffleTag) {
        getSharedPreferences(context);
        editor.putInt(REPEATTAG, repeatTag);
        editor.putInt(SHUFFLETAG, shuffleTag);
        editor.commit();
    }

    public static void setOpenFLAG(Context context) {
        getSharedPreferences(context);
        editor.putInt(FIRST, 1);
        editor.commit();
    }

    public static int getOpenFlag(Context context){
        getSharedPreferences(context);
       return sharedPreferences.getInt(FIRST,0);

    }

}
