package com.axnshy.cloudmusic.Fragment;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.axnshy.cloudmusic.Activity.MusicListActivity;
import com.axnshy.cloudmusic.Activity.MusicPlayingActivity;
import com.axnshy.cloudmusic.Adapter.MyAdapter;
import com.axnshy.cloudmusic.Config;
import com.axnshy.cloudmusic.DBHelper.MusicInfoDao;
import com.axnshy.cloudmusic.FilesRead.ListsInfo;
import com.axnshy.cloudmusic.FilesRead.MusicInfo;
import com.axnshy.cloudmusic.PlayerService;
import com.axnshy.cloudmusic.R;
import com.axnshy.cloudmusic.Service.Service;

import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by axnshy on 16/5/21.
 */
@ContentView(R.layout.list_fragment)
public class List_Fragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private MusicListActivity activity;
    private View view;
    private ArrayList<MusicInfo> mList;
    private MyAdapter mAdapter;
    private ListView mListView;
    private PlayerService mService;
    private List<ListsInfo> ListsList;
    private int listPositon;
    private SwipeRefreshLayout refreshLayout;
    TextView listCountTx;
    TextView listNameTx;
    TextView listCreatorTx;
    ImageView listAvatarImg;
    ImageView listCreatorAvatarImg;

    int curMusicposition;
    // 定义ServiceConnection
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 通过定义的Binder来获取Service实例来供使用
            mService = ((PlayerService.MyBinder) service).getService();
            Log.w(PlayerService.LOG_TAG, "Activity onServiceConnected");
            Log.w("TAG", "Service   -------------------------   " + mService);
            initView();
            initList();

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
        view = inflater.inflate(R.layout.list_fragment, container, false);
        Bundle bundle = getArguments();
        listPositon = bundle.getInt(Config.LIST);
        ListsList = bundle.getParcelableArrayList("ListsList");
        return view;
    }

    private void initList() {
        // mList=new ArrayList<>();
        Log.e("TAG", "Service   --------------------------    " + mService);
        mList = MusicInfoDao.getMusicList(view.getContext(), ListsList.get(listPositon).getListId());
        mAdapter = new MyAdapter(view.getContext(), mList);
        System.out.println("mAdapter" + mAdapter);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        // bindService
        Intent intent = new Intent(view.getContext(), PlayerService.class);
        view.getContext().bindService(intent, conn, Context.BIND_AUTO_CREATE);
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
        listCountTx = (TextView) view.findViewById(R.id.tv_list_count);
        listCreatorTx = (TextView) view.findViewById(R.id.tv_list_creator_name);
        listNameTx = (TextView) view.findViewById(R.id.tv_list_name);
        listAvatarImg = (ImageView) view.findViewById(R.id.iv_list_avator);
        listCreatorAvatarImg = (ImageView) view.findViewById(R.id.iv_list_creator_avatar);
        mListView = (ListView) view.findViewById(R.id.lv_fragment_musicList);
        mListView.setOnItemClickListener(this);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout_custom);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        });
        if (ListsList.get(listPositon).getBackgroundPath() != null) {
            listAvatarImg.setImageDrawable(Drawable.createFromPath(ListsList.get(listPositon).getBackgroundPath()));
        } else {
            listAvatarImg.setImageResource(R.drawable.h1);
        }
        listCountTx.setText(ListsList.get(listPositon).getListCount() + "");
        listNameTx.setText(ListsList.get(listPositon).getListName());
        listCreatorTx.setText(ListsList.get(listPositon).getUserId() + "");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View mview, int position, long id) {
        //判断Service是否存在
        if (!Service.isMyServiceRunning(view.getContext(), PlayerService.class)) {
            Intent serviceintent = new Intent(view.getContext(), PlayerService.class);
            view.getContext().startService(serviceintent);
            Log.e("TAGS", "MediaPlayerService does not existed");
        }
//        mService.setPlayerList(mList);
//        mService.play(mList.get(position));
        mService.setPlayerList(mList);
        mService.play(mList.get(position));
        mAdapter.setIsPlaying(position);
        mAdapter.notifyDataSetChanged();
//        setPlayIndicator(position);
        //mListener.updateToolbar(mService.getMyList().get(position).musicName);
        Intent intent = new Intent(view.getContext(), MusicPlayingActivity.class);
        startActivity(intent);
    }


}
