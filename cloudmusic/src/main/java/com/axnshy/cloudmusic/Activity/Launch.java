package com.axnshy.cloudmusic.Activity;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.axnshy.cloudmusic.Adapter.DrawMenuAdapter;
import com.axnshy.cloudmusic.DBHelper.MusicInfoDao;
import com.axnshy.cloudmusic.Fragment.DrawerHeaderSignIn;
import com.axnshy.cloudmusic.Fragment.DrawerHeaderSignOut;
import com.axnshy.cloudmusic.Fragment.Home_Fragment;
import com.axnshy.cloudmusic.Fragment.List_Fragment;
import com.axnshy.cloudmusic.PlayerService;
import com.axnshy.cloudmusic.R;
import com.axnshy.cloudmusic.Service.Service;
import com.axnshy.cloudmusic.User;
import com.soundcloud.android.crop.Crop;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.Observable;
import java.util.Observer;

import cn.bmob.v3.BmobUser;

@ContentView(R.layout.activity_launch)
public class Launch extends BaseActivity implements Toolbar.OnMenuItemClickListener, List_Fragment.OnItemClickListener, Observer, View.OnClickListener, AdapterView.OnItemClickListener {
    private ActionBarDrawerToggle toggle;
    private DrawerLayout mDrawerLayout;
    private Fragment home;
    private Fragment drawerHeader;
    private Toolbar mToolbar;
    private ImageView repeatImg;
    private ImageView previousImg;
    private ImageView playImg;
    private ImageView nextImg;
    private ImageView shuffleImg;
    @ViewInject(R.id.iv_user_avatar)
    private ImageView mDrawerImg;
    @ViewInject(R.id.tv_username)
    private TextView view_username;
    private boolean PlayerBarToken;
    @ViewInject(R.id.music_playerBarInApp)
    private LinearLayout playerBarLayout;
    @ViewInject(R.id.lv_drawer_menu)
    private ListView drawMenu;

    //绑定service与activity

    //绑定service与activity
    private PlayerService mService;

    // 定义ServiceConnection
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 通过定义的Binder来获取Service实例来供使用
            mService = ((PlayerService.MyBinder) service).getService();
            Log.w(PlayerService.LOG_TAG, "Activity onServiceConnected");
            mService.addObserver(Launch.this);

            updateUI();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            // 当Service被意外销毁时
            Log.w(PlayerService.LOG_TAG, "Activity onServiceDisconnected");
        }
    };

    @Override
    public void onResume() {
        super.onResume();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initEvent();
        launchService();
    }

    private void initView() {
        PlayerBarToken = true;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_top);
        repeatImg = (ImageView) findViewById(R.id.iv_media_repeat);
        previousImg = (ImageView) findViewById(R.id.iv_media_previous);
        playImg = (ImageView) findViewById(R.id.iv_media_play);
        nextImg = (ImageView) findViewById(R.id.iv_media_next);
        shuffleImg = (ImageView) findViewById(R.id.iv_media_shuffle);
        playerBarLayout = (LinearLayout) findViewById(R.id.music_playerBarInApp);
        mToolbar.setTitle("");
        mToolbar.setSubtitle("");
        setSupportActionBar(mToolbar);
        toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        home = new Home_Fragment();
        if (BmobUser.getCurrentUser(User.class) != null) {
            drawerHeader = new DrawerHeaderSignIn();
        } else
            drawerHeader = new DrawerHeaderSignOut();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.id_home_container, home).commit();
        fragmentManager.beginTransaction().replace(R.id.frame_drawer_header, drawerHeader).commit();
        setUpDrawer();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        toggle.syncState();// 这个必须要，没有的话进去的默认是个箭头。。正常应该是三横杠的
        super.onPostCreate(savedInstanceState);
    }


    private void initEvent() {
        playImg.setOnClickListener(this);
        previousImg.setOnClickListener(this);
        nextImg.setOnClickListener(this);
        repeatImg.setOnClickListener(this);
        shuffleImg.setOnClickListener(this);
    }

    /*
    * 启动Service
    * */
    private void launchService() {
        startService(new Intent(Launch.this, PlayerService.class));
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.it_menu_scan: {
                MusicInfoDao.getAllMusic(this);
                Crop.pickImage(this);
                break;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
//        getMenuInflater().inflate(R.menu.drawer,navigationView.createContextMenu());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateToolbar(String string) {

    }

    @Override
    public void update(Observable observable, Object data) {
    }

    @Override
    public void onClick(View v) {
        //判断Service是否存在

        if (!Service.isMyServiceRunning(this, PlayerService.class)) {
            Intent serviceintent = new Intent(this, PlayerService.class);
            startService(serviceintent);
            Log.e("TAGS", "MediaPlayerService does not existed");
        } else {
            Log.e("TAGS", "MediaPlayerService existed");
        }
        switch (v.getId()) {
            case R.id.iv_media_repeat:
                if (mService.repeatTag == 0) {
                    repeatImg.setBackgroundResource(R.drawable.basecopy);
                    mService.repeatTag = 1;
                } else {
                    repeatImg.setBackgroundResource(R.drawable.base);
                    mService.repeatTag = 0;
                }
                break;
            case R.id.iv_media_shuffle:
                if (mService.shuffleTag == 0) {
                    shuffleImg.setBackgroundResource(R.drawable.basecopy);
                    mService.shuffleTag = 1;
                } else {
                    shuffleImg.setBackgroundResource(R.drawable.base);
                    mService.shuffleTag = 0;
                }
                break;
            case R.id.iv_media_previous:
                int location = mService.getMyList().indexOf(mService.currentMusic);
                if (location == 0) {
                    mService.play(mService.getMyList().get(mService.getMyList().size() - 1));
                } else
                    mService.play(mService.getMyList().get(location - 1));
                break;
            case R.id.iv_media_next:
                int location1 = mService.getMyList().indexOf(mService.currentMusic);
                if (location1 == mService.getMyList().size() - 1) {
                    mService.play(mService.getMyList().get(0));
                } else
                    mService.play(mService.getMyList().get(location1 + 1));
                break;
            case R.id.iv_media_play: {
                if (mService.getPlayerState() == mService.MediaPlayer_PLAY) {
                    mService.pause();
                    mService.setPlayerState(mService.MediaPlayer_PAUSE);
                } else {
                    mService.getPlayerInstance().start();
                    mService.setPlayerState(mService.MediaPlayer_PLAY);
                }
                break;
            }
        }
        updateUI();
    }

    private void updateUI() {
        //PlayerState代表播放状态
        switch (mService.getPlayerState()) {
            case 1: {
                playImg.setImageResource(R.drawable.play);
                break;
            }
            case 2: {
                playImg.setImageResource(R.drawable.pause);
                break;
            }
            case 3: {
                playImg.setImageResource(R.drawable.play);
                break;
            }
        }
        if (PlayerBarToken == true && PlayerService.PlayerState == PlayerService.MediaPlayer_PAUSE) {
            ObjectAnimator.ofFloat(playerBarLayout, "translationY", 0F, 180F).setDuration(0).start();
            PlayerBarToken = false;
        }
    }

    private void setUpDrawer() {
//        LayoutInflater inflater = LayoutInflater.from(this);
//        drawMenu.addHeaderView(inflater.inflate(R.layout.drawer_listview_header, drawMenu, false));
        drawMenu.setAdapter(new DrawMenuAdapter(this));
        drawMenu.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position) {
            case 0:
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(Launch.this, AccountManageActivity.class));
                startActivity(intent);
                Toast.makeText(Launch.this, position + "", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(Launch.this, position + "", Toast.LENGTH_SHORT).show();
                System.exit(0);
                break;
        }
    }
}
