package com.axnshy.cloudmusic.Activity;

import android.app.Application;

import com.axnshy.cloudmusic.DBHelper.MusicInfoDao;
import com.axnshy.cloudmusic.MySharedPre;
import com.axnshy.cloudmusic.User;

import org.xutils.BuildConfig;
import org.xutils.x;

/**
 * Created by axnshy on 16/8/5.
 */
public class MyApp extends Application {

    public User mUser;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);


        if (MySharedPre.getOpenFlag(this) == 0) {
            scanLocal();
            MySharedPre.setOpenFLAG(this);
        }
    }

    /*
   * 扫描本地音乐文件
   * */
    private void scanLocal() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MusicInfoDao.scanMusic(getApplicationContext());
            }
        }).start();

    }
}
