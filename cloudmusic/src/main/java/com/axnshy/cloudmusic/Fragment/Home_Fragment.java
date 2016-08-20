package com.axnshy.cloudmusic.Fragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.axnshy.cloudmusic.PlayerService;
import com.axnshy.cloudmusic.R;
import com.axnshy.cloudmusic.Service.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by axnshy on 16/4/18.
 */
public class Home_Fragment extends Fragment implements View.OnClickListener {

    //private ExpandableListView mExpandableListView;
    View view;
    private List<String> topList = null;
    private List<String> defaultChildList = null;
    private Map<String, String> map;
    private Context context;


    //绑定service与activity
    private PlayerService mService;

    // 定义ServiceConnection
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 通过定义的Binder来获取Service实例来供使用
            mService = ((PlayerService.MyBinder) service).getService();
            Log.w(PlayerService.LOG_TAG, "Activity onServiceConnected");
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
        Intent intent = new Intent(context, PlayerService.class);
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
        Log.w(PlayerService.LOG_TAG, "Activity bindService");
    }

    @Override
    public void onStop() {
        super.onStop();
        // 进行unbind
        context.unbindService(conn);
        Log.w(PlayerService.LOG_TAG, "Activity unbindService");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home, container, false);
        context = view.getContext();
        initView();
        return view;
    }


    private void initView() {
        HomeFragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.id_home_content
                , fragment).commit();
    }

    @Override
    public void onClick(View v) {
        //判断Service是否存在
        if (!Service.isMyServiceRunning(context, PlayerService.class)) {
            Intent serviceintent = new Intent(context, PlayerService.class);
            context.startService(serviceintent);
            Log.e("TAGS", "MediaPlayerService does not existed");
        } else {
            Log.e("TAGS", "MediaPlayerService existed");

        }
    }
}
