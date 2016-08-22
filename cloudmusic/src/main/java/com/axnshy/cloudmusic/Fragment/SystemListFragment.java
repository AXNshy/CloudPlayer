package com.axnshy.cloudmusic.Fragment;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.axnshy.cloudmusic.Activity.MusicListActivity;
import com.axnshy.cloudmusic.Adapter.MyAdapter;
import com.axnshy.cloudmusic.DBHelper.MusicInfoDao;
import com.axnshy.cloudmusic.FilesRead.ListsInfo;
import com.axnshy.cloudmusic.FilesRead.MusicInfo;
import com.axnshy.cloudmusic.PlayerService;
import com.axnshy.cloudmusic.R;
import com.axnshy.cloudmusic.Service.Service;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by axnshy on 16/8/12.
 */
@ContentView(R.layout.custom_list_fragment)
public class SystemListFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private MusicListActivity activity;
    private View view;
    private ArrayList<MusicInfo> mList;
    private MyAdapter mAdapter;
    @ViewInject(R.id.lv_fragment_musicList)
    private ListView mListView;
    private PlayerService mService;
    private List<ListsInfo> ListsList;
    private int listId;
    int curMusicposition;
//    @ViewInject(R.id.refreshLayout_local)
//    private SwipeRefreshLayout refreshLayout;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mList = (ArrayList<MusicInfo>) msg.obj;
                    Log.w("TAG", "List   --------------------    " + (mList.size()));
                    mAdapter = new MyAdapter(getActivity(), mList);
                    System.out.println("mAdapter" + mAdapter);
                    mListView.setAdapter(mAdapter);
                    break;
            }
        }
    };

    // 定义ServiceConnection
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 通过定义的Binder来获取Service实例来供使用
            mService = ((PlayerService.MyBinder) service).getService();
            Log.w(PlayerService.LOG_TAG, "Activity onServiceConnected");
            Log.w("TAG", "Service   -------------------------   " + mService);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            // 当Service被意外销毁时
            Log.w(PlayerService.LOG_TAG, "Activity onServiceDisconnected");
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.custom_list_fragment, container, false);
        Bundle bundle = getArguments();
        ListsList = bundle.getParcelableArrayList("ListsList");
        initView();
        initList();
        return view;
    }

    private void initList() {
        // mList=new ArrayList<>();
        Log.e("TAG", "Service   --------------------------    " + mService);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = MusicInfoDao.getAllMusic(view.getContext());
                handler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public void onStart() {
        super.onStart();
        // bindService
        Intent intent = new Intent(getActivity(), PlayerService.class);
        getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
        Log.w(PlayerService.LOG_TAG, "Activity bindService");
    }


    @Override
    public void onPause() {
        super.onPause();
        // 进行unbind
        view.getContext().unbindService(conn);
        Log.w(PlayerService.LOG_TAG, "Activity unbindService");
    }

    private void initView() {
//        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout_local);
        /*refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        });*/
        mListView = (ListView) ((CoordinatorLayout)getActivity().findViewById(R.id.layout_coordinator)).findViewById(R.id.lv_fragment_musicList);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View mview, int position, long id) {
        //判断Service是否存在
        if (!Service.isMyServiceRunning(view.getContext(), PlayerService.class)) {
            Intent serviceintent = new Intent(view.getContext(), PlayerService.class);
            view.getContext().startService(serviceintent);
            Log.e("TAGS", "MediaPlayerService does not existed");
        }
        mService.setPlayerList(mList);
        mService.play(mList.get(position));
        mAdapter.setIsPlaying(position);
        mAdapter.notifyDataSetChanged();
//        setPlayIndicator(position);
        //设置播放Flag

        /*//mListener.updateToolbar(mService.getMyList().get(position).musicName);
        Intent intent = new Intent(view.getContext(), MusicPlayingActivity.class);
        startActivity(intent);*/
    }
}
