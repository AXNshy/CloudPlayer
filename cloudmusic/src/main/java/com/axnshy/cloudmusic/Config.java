package com.axnshy.cloudmusic;


/**
 * Created by axnshy on 16/5/15.
 */
public interface Config {

    public static final String BROADCAST_NAME = "com.axnshy.music.broadcast";
    public static final String SERVICE_NAME = "com.axnshy.music.service.MediaService";
    public static final String BROADCAST_QUERY_COMPLETE_NAME = "com.axnshy.music.querycomplete.broadcast";
    public static final String BROADCAST_CHANGEBG = "com.axnshy.music.changebg";
    public static final String BROADCAST_SHAKE = "com.axnshy.music.shake";

    public static final String MUSIC_LIST_ALL="com.axnshy.player.action.allmusic";
    public static final String MUSIC_LIST_FAVORITE="com.axnshy.player.action.favoritemusic";
    public static final String MUSIC_LIST_DOWNLOAD="com.axnshy.player.action.download";
    public static final String MUSIC_LIST_RECENT="com.axnshy.player.action.recentplay";


    public static final String PLAYERACTIVITY_UPDATE = "com.axnshy.action.PlayerActivityReceiver";
    public static final String PLAYERSERVICE_UPDATE = "com.axnshy.action.PlayerServiceReceiver";
    public static final String MUSIC_UPDATE = "com.axnshy.action.MusicReceiver";
    //com.ken.android.CloudMusic.
    public static final String ServiceAbsName="RadioPlayerService";

    public static final String URL="pathOfMusic";
    public static final String ID="resourceId";

    public static final String USER_PREFER="userPrefer";

    public static final String SERVICE_UPDATE="service_update";

    public static final String CIRCLE="circle";
    public static int CIRCLE_DE=1;
    public static final String PREVIOUS="previous";
    public static int PREVIOUS_DE=1;
    public static final String PLAY="play";
    public static int PLAY_DE=1;
    public static final String NEXT="next";
    public static int NEXT_DE=1;


    public static final boolean LOGIN_SUCCESS=true;
    public static final int LOGIN=1;

    public static final String WEB_SERVER_LOGIN="http://192.168.1.105:8080/CloudMusic/user/loginUser?";
    public static final String WEB_SERVER_REGISTER="http://192.168.1.105:8080/CloudMusic/user/registerUser?";
    public static final String WEB_SERVER_ADDMUSIC_COLLECT="http://192.168.1.105:8080/CloudMusic/user/addCollectMusic?";


/*
* 1: MUSIC_INFO
* */
    public static int MSG=-1;

    // 播放状态
    public static final int NOFILE = -1; // 无音乐文件
    public static final int MPS_INVALID = 0; // 当前音乐文件无效
    public static final int MPS_PREPARE = 1; // 准备就绪
    public static final int MPS_PLAYING = 2; // 播放中
    public static final int MPS_PAUSE = 3; // 暂停

    // 播放模式
    public static final int MPM_LIST_LOOP_PLAY = 0; // 列表循环
    public static final int MPM_ORDER_PLAY = 1; // 顺序播放
    public static final int MPM_RANDOM_PLAY = 2; // 随机播放
    public static final int MPM_SINGLE_LOOP_PLAY = 3; // 单曲循环


    //歌手和专辑列表点击都会进入MyMusic 此时要传递参数表明是从哪里进入的
    public static final String LIST = "list";
    public static final int START_FROM_LOCAL = 0;
    public static final int START_FROM_FAVORITE = 1;
    public static final int START_FROM_EAST = 2;
    public static final int START_FROM_ANIME = 3;

    public static int totalNum=0;
}
