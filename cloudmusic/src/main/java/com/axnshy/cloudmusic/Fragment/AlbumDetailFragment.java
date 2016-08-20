package com.axnshy.cloudmusic.Fragment;

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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.axnshy.cloudmusic.FilesRead.MusicInfo;
import com.axnshy.cloudmusic.PlayerService;
import com.axnshy.cloudmusic.R;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by axnshy on 16/7/31.
 */
public class AlbumDetailFragment extends Fragment implements Observer {
    View view;
    private ImageView musicCover;
    private TextView musicTitle;
    private TextView musicArtist;
MusicInfo mMusicInfo;

    //绑定service与activity
    private PlayerService mService;

    // 定义ServiceConnection
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 通过定义的Binder来获取Service实例来供使用
            mService = ((PlayerService.MyBinder) service).getService();
            Log.w(PlayerService.LOG_TAG, "Activity onServiceConnected");
            mService.addObserver(AlbumDetailFragment.this);
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
        Intent intent = new Intent(view.getContext(), PlayerService.class);
        view.getContext().bindService(intent, conn, Context.BIND_AUTO_CREATE);
        Log.w(PlayerService.LOG_TAG, "Activity bindService");
        // updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 进行unbind
        view.getContext().unbindService(conn);
        Log.w(PlayerService.LOG_TAG, "Activity unbindService");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.artwork, container);
        initView();
        Bundle bundle=getArguments();
        mMusicInfo=bundle.getParcelable("MusicInfo");
        return view;
    }

    private void initView() {
        musicCover = (ImageView) view.findViewById(R.id.iv_current_musicAlbumCover);
        musicArtist = (TextView) view.findViewById(R.id.tv_current_musicArtist);
        musicTitle = (TextView) view.findViewById(R.id.tv_current_musicName);
    }

    @Override
    public void update(Observable observable, Object o) {
        musicTitle.setText(mService.currentMusic.getMusicName());
        musicArtist.setText(mService.currentMusic.getMusicArtist());
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bitmap bm = BitmapFactory.decodeFile((String) msg.obj);
            BitmapDrawable bmpDraw = new BitmapDrawable(bm);
            musicCover.setImageDrawable(bmpDraw);
        }
    };
}
