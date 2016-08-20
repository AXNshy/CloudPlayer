package com.axnshy.cloudmusic;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.axnshy.cloudmusic.DBHelper.MusicInfoDao;
import com.axnshy.cloudmusic.FilesRead.MusicInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by axnshy on 16/4/19.
 */
public class PlayerService extends Service implements Config, MediaPlayer.OnPreparedListener {


    private MyObservable mObservable;

    private static final String TAG = "CloudMusic";
    public static int PlayerState = 1;
    public static int MediaPlayer_PLAY = 2;
    public static int MediaPlayer_PAUSE = 1;


    public int repeatTag = 0;
    public int shuffleTag = 0;
    private ArrayList<MusicInfo> mList;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: {
                    mList = (ArrayList<MusicInfo>) msg.obj;
                    currentMusic = mList.get(0);
                    Log.e("TAG", "List 以获取");
                    mPlayer = createMediaPlayer();
                    if (mPlayer != null) {
                        mPlayer.setOnPreparedListener(PlayerService.this);
                    }
                    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    break;
                }
                case 0:
                    Log.e("TAG", " --------------- currentMusic -------------" + currentMusic);
                    mObservable.notifyChanged(currentMusic);
                    break;

            }
        }
    };
        /*
        * 当前播放资源的绝对路径
        * */
        private String uri;

    /*
    * 当前播放资源的Id
    *
    * */

        public MusicInfo currentMusic;

        private MediaPlayer mPlayer;


        //private Context context;
        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            Log.d(LOG_TAG, "onBind");
            return mBinder;
        }

        @Override
        public boolean onUnbind(Intent intent) {
            Log.d(LOG_TAG, "onUnbind");
            return super.onUnbind(intent);
        }

        public static final String LOG_TAG = "LOG_TAG";

        private MyBinder mBinder = new MyBinder();

        @Override
        public void onPrepared(MediaPlayer mp) {

        }

        public class MyBinder extends Binder {

            public PlayerService getService() {
                return PlayerService.this;
            }
        }

        @Override
        public void onCreate() {
            super.onCreate();
            mObservable = new MyObservable();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            //super.onStartCommand();
            //初始化media player
            initEvent();
            return START_STICKY;
        }

        public PlayerService() {
            super();
        }


        private void initEvent() {
            //getList(PlayerService.this);
        }

        public void getList(final Context context) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = handler.obtainMessage(1, MusicInfoDao.getAllMusic(context));
                    handler.sendMessage(message);
                }
            }).start();
        }

        public ArrayList<MusicInfo> getMyList() {
            return mList;
        }

        public MusicInfo getPreviousMusic() {
            int location = getMyList().indexOf(currentMusic);
            if (location == 0) {
                return getMyList().get(getMyList().size() - 1);
            } else
                return getMyList().get(location - 1);
        }

        public MusicInfo getNextMusic() {
            int location = getMyList().indexOf(currentMusic);
            if (location == getMyList().size() - 1) {
                return getMyList().get(0);
            } else
                return getMyList().get(location + 1);
        }

        public int getPlayerState() {
            if (PlayerState > 0)
                return PlayerState;
            return -1;
        }

        public void setPlayerState(int state) {
            PlayerState = state;
        }


        public void setPlayerList(ArrayList<MusicInfo> list) {
            this.mList = list;
            currentMusic = mList.get(0);
        }

        public MediaPlayer getPlayerInstance() {
            if (mPlayer != null)
                return mPlayer;
            mPlayer = createMediaPlayer();
            return mPlayer;
        }

        public void play(MusicInfo music) {
            getPlayerInstance();
            getPlayerState();
            try {
                mPlayer.reset();
                mPlayer.setDataSource(music.data);
                mPlayer.prepare();
                mPlayer.start();
                currentMusic = music;
                PlayerState = MediaPlayer_PLAY;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void pause() {
            getPlayerInstance().pause();
            //handler.sendMessage(handler.obtainMessage(0, currentMusic));
        }

        private MediaPlayer createMediaPlayer() {
                MediaPlayer mediaPlayer = new MediaPlayer();
                if (repeatTag == 0)
                    mediaPlayer.setLooping(false);
                if (repeatTag == 1)
                    mediaPlayer.setLooping(true);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        try {
                            if (shuffleTag == 0) {
                                MusicInfo music = mList.get(mList.indexOf(currentMusic)+1);
                                mp.reset();
                                mp.setDataSource(music.data);
                                mp.prepare();
                                mp.start();
                                currentMusic = music;
                            } else {
                                Double double1 = Math.random();
                                int random = (int) (double1 * mList.size());
                                MusicInfo music = mList.get(random);
                                mp.reset();
                                mp.setDataSource(music.data);
                                mp.prepare();
                                mp.start();
                                currentMusic = music;
                            }
                            PlayerState = MediaPlayer_PLAY;
                            handler.sendMessage(handler.obtainMessage(0, currentMusic));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
                return mediaPlayer;
        }

        /**
         * 添加观察者
         * @param observer
         */
        public void addObserver(Observer observer) {
            mObservable.addObserver(observer);
        }

        public class MyObservable extends Observable {
            public void notifyChanged(Object object) {
                this.setChanged();
                this.notifyObservers();
            }
        }
    }
