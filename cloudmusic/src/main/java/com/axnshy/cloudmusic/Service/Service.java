package com.axnshy.cloudmusic.Service;


import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by axnshy on 16/7/30.
 */
public class Service{
    /*
    *判断service有没有运行
    * */
    public static boolean isMyServiceRunning(Context context,Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



}
