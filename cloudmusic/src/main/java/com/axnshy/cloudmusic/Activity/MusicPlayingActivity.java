package com.axnshy.cloudmusic.Activity;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.axnshy.cloudmusic.FilesRead.MusicInfo;
import com.axnshy.cloudmusic.PlayerService;
import com.axnshy.cloudmusic.R;
import com.axnshy.cloudmusic.Service.Service;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by axnshy on 16/7/29.
 */
@ContentView(R.layout.music_play)
public class MusicPlayingActivity extends BaseActivity implements Observer {

    @ViewInject(R.id.toolbar_top)
    private Toolbar toolbar;
    @ViewInject(R.id.iv_back)
    private ImageView returnImg;
    @ViewInject(R.id.iv_toolbar_right)
    private ImageView shareImg;
    @ViewInject(R.id.tv_toolbar_title)
    private TextView toolbar_title;
    @ViewInject(R.id.progressbar_player_status)
    private SeekBar playStatusBar;
    @ViewInject(R.id.tv_musicPlay_currentLocation)
    private TextView playStatusCurrent;
    @ViewInject(R.id.tv_musicPlay_totalTime)
    private TextView playStatusTotal;
    @ViewInject(R.id.iv_repeat_play)
    private ImageView musicRepeatImg;
    @ViewInject(R.id.iv_previous_play)
    private ImageView musicPrevImg;
    @ViewInject(R.id.iv_play)
    private ImageView musicPlayImg;
    @ViewInject(R.id.iv_next_play)
    private ImageView musicNextImg;
    @ViewInject(R.id.iv_shuffle_play)
    private ImageView musicShuffleImg;
    @ViewInject(R.id.iv_current_musicAlbumCover)
    private ImageView artWorkImg;
    @ViewInject(R.id.tv_current_musicName)
    private TextView CurrentMusicNameTx;
    @ViewInject(R.id.tv_current_musicArtist)
    private TextView CurrentArtistTx;

    private Context context;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    playStatusBar.setProgress((int) msg.obj);
                    playStatusCurrent.setText(MusicInfo.parseMusicData(mService.getPlayerInstance().getCurrentPosition()));
                    break;
                case 1:
                    Bitmap bm = BitmapFactory.decodeFile((String) msg.obj);
                    BitmapDrawable bmpDraw = new BitmapDrawable(bm);
                    artWorkImg.setImageDrawable(bmpDraw);
                    break;
            }
        }
    };

    @Event(value = {R.id.iv_toolbar_right,R.id.iv_back, R.id.iv_toolbar_right, R.id.iv_repeat_play, R.id.iv_previous_play, R.id.iv_play, R.id.iv_next_play, R.id.iv_shuffle_play})
    private void OnClicked(View view) {
        //判断Service是否存在

        if (!Service.isMyServiceRunning(context, PlayerService.class)) {
            Intent serviceintent = new Intent(context, PlayerService.class);
            startService(serviceintent);
            Log.e("TAGS", "MediaPlayerService does not existed");
        } else {
            Log.e("TAGS", "MediaPlayerService existed");

        }
        switch (view.getId()) {
            case R.id.iv_repeat_play:
                if (mService.repeatTag == 0) {
                    musicRepeatImg.setBackgroundResource(R.drawable.basecopy);
                    mService.repeatTag = 1;
                } else {
                    musicRepeatImg.setBackgroundResource(R.drawable.base);
                    mService.repeatTag = 0;
                }
                break;
            case R.id.iv_shuffle_play:
                if (mService.shuffleTag == 0) {
                    musicShuffleImg.setBackgroundResource(R.drawable.basecopy);
                    mService.shuffleTag = 1;
                } else {
                    musicShuffleImg.setBackgroundResource(R.drawable.base);
                    mService.shuffleTag = 0;
                }
                break;
            case R.id.iv_previous_play:
                int location = mService.getMyList().indexOf(mService.currentMusic);
                if (location == 0) {
                    mService.play(mService.getMyList().get(mService.getMyList().size() - 1));
                } else
                    mService.play(mService.getMyList().get(location - 1));
                break;
            case R.id.iv_next_play:
                int location1 = mService.getMyList().indexOf(mService.currentMusic);
                if (location1 == mService.getMyList().size() - 1) {
                    mService.play(mService.getMyList().get(0));
                } else
                    mService.play(mService.getMyList().get(location1 + 1));
                break;
            case R.id.iv_play: {
                if (mService.getPlayerState() == mService.MediaPlayer_PLAY) {
                    mService.pause();
                    mService.setPlayerState(mService.MediaPlayer_PAUSE);
                } else {
                    mService.getPlayerInstance().start();
                    mService.setPlayerState(mService.MediaPlayer_PLAY);
                }
                break;
            }
            case R.id.iv_back:
                onBackPressed();
                break;

        }
        updateUI();
    }

    //绑定service与activity
    private PlayerService mService;

    // 定义ServiceConnection
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 通过定义的Binder来获取Service实例来供使用
            mService = ((PlayerService.MyBinder) service).getService();
            updateUI();
            Log.w(PlayerService.LOG_TAG, "Activity onServiceConnected");
            mService.addObserver(MusicPlayingActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            // 当Service被意外销毁时
            Log.w(PlayerService.LOG_TAG, "Activity onServiceDisconnected");
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        // bindService
        Intent intent = new Intent(this, PlayerService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        Log.w(PlayerService.LOG_TAG, "Activity bindService");
        // updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 进行unbind
        unbindService(conn);
        Log.w(PlayerService.LOG_TAG, "Activity unbindService");
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initView();

    }

    private void initView() {
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);
        shareImg.setImageResource(R.drawable.ic_share_white_48dp);
        returnImg.setImageResource(R.drawable.ic_chevron_left_white_48dp);
    }

    @Override
    public void update(Observable observable, Object o) {

        updateUI();
    }

    private void updateUI() {
        //PlayerState代表播放状态
        switch (mService.getPlayerState()) {
            case 1: {
                musicPlayImg.setImageResource(R.drawable.play);
                break;
            }
            case 2: {
                musicPlayImg.setImageResource(R.drawable.pause);
                break;
            }
            case 3: {
                musicPlayImg.setImageResource(R.drawable.play);
                break;
            }
        }
        CurrentMusicNameTx.setText(mService.currentMusic.getMusicName());
        CurrentArtistTx.setText(mService.currentMusic.getMusicArtist());
        playStatusBar.setMax((int) (mService.currentMusic.duration / 1000));
        Log.w("TAG", "TotalProgress-----------" + mService.currentMusic.duration / 1000);
        playStatusTotal.setText(MusicInfo.parseMusicData(mService.currentMusic.duration));
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mService.getPlayerState() == mService.MediaPlayer_PLAY) {
                    Message msg = Message.obtain();
                    int progress = mService.getPlayerInstance().getCurrentPosition() / 1000;
                    msg.obj = progress;
                    Log.w("TAG", "progress-----------" + progress);
                    msg.what = 0;
                    handler.sendMessage(msg);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }


/*
    private static void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }*/
}
