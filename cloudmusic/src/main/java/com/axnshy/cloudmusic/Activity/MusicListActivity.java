package com.axnshy.cloudmusic.Activity;


import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.axnshy.cloudmusic.Adapter.MyAdapter;
import com.axnshy.cloudmusic.Config;
import com.axnshy.cloudmusic.DBHelper.ListsInfoDao;
import com.axnshy.cloudmusic.DBHelper.MusicInfoDao;
import com.axnshy.cloudmusic.Dialog.CollectDialog;
import com.axnshy.cloudmusic.FilesRead.ListsInfo;
import com.axnshy.cloudmusic.FilesRead.MusicInfo;
import com.axnshy.cloudmusic.PlayerService;
import com.axnshy.cloudmusic.R;
import com.axnshy.cloudmusic.Service.Service;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by axnshy on 16/5/20.
 */
@ContentView(R.layout.list_activity)
public class MusicListActivity extends BaseActivity implements View.OnClickListener, java.util.Observer, MyAdapter.OnItemMenuClickListener, AdapterView.OnItemClickListener {


    //    private LinearLayout musicListLayout;
    private ImageView repeatImg;
    private ImageView previousImg;
    private ImageView playImg;
    private ImageView nextImg;
    private ImageView shuffleImg;
    private Context mContext;
    private Toolbar toolbar;
    private ImageView returnImg;
    private TextView listTitle;
    @ViewInject(R.id.layout_coordinator)
    private CoordinatorLayout coordinator;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView collapsingImage;
    private ListView mListView;
    private TextView musicNameTx;
    private TextView commentCount;
    private TextView singerName;
    private TextView albumName;
    private LinearLayout nextLayout;
    private LinearLayout collectLayout;
    private LinearLayout downloadLayout;
    private LinearLayout commentLayout;
    private LinearLayout shareLayout;
    private LinearLayout deleteLayout;
    private LinearLayout singerLayout;
    private LinearLayout albumLayout;
    private LinearLayout playerBarLayout;

    private BottomSheetDialog bottomSheetDialog;

    WindowManager.LayoutParams params;
    Window window;

    //    private boolean PlayerBarToken;
    //绑定service与activity
    private PlayerService mService;

    List<ListsInfo> ListsList;
    int ListPosition;
    private List<MusicInfo> mList;
    private MyAdapter mAdapter;
    ListsInfo currentList;

    // 定义ServiceConnection
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 通过定义的Binder来获取Service实例来供使用
            mService = ((PlayerService.MyBinder) service).getService();
            Log.w(PlayerService.LOG_TAG, "Activity onServiceConnected");
            mService.addObserver(MusicListActivity.this);
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


    private int selectItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        Intent intent = getIntent();
        ListsList = intent.getParcelableArrayListExtra("ListsList");
        ListPosition = intent.getIntExtra(Config.LIST, -1);

        mContext = this;
        initView();
        initEvent();
        initListData();
//        PlayerBarToken=true;
    }

    private void initListData() {
        Observable.just(ListPosition)
                .subscribeOn(Schedulers.io())
                .map(new Func1<Integer, List<MusicInfo>>() {
                    @Override
                    public List<MusicInfo> call(Integer integer) {
                        if (integer >= 0)
                            return MusicInfoDao.getDAO().getMusicList(MusicListActivity.this, ListsList.get(integer).getListId());
                        else
                            return MusicInfoDao.getAllMusic(MusicListActivity.this);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<MusicInfo>>() {
                    @Override
                    public void call(List<MusicInfo> musicInfos) {
                        mList = musicInfos;
                        mAdapter = new MyAdapter(MusicListActivity.this, mList);
                        mListView.setAdapter(mAdapter);
                    }
                });
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void initView() {
        coordinator = (CoordinatorLayout) findViewById(R.id.layout_coordinator);
//        musicListLayout = (LinearLayout) coordinator.findViewById(R.id.layout_list_activity);
        returnImg = (ImageView) coordinator.findViewById(R.id.iv_back);
        repeatImg = (ImageView) coordinator.findViewById(R.id.iv_media_repeat);
        previousImg = (ImageView) coordinator.findViewById(R.id.iv_media_previous);
        playImg = (ImageView) coordinator.findViewById(R.id.iv_media_play);
        nextImg = (ImageView) coordinator.findViewById(R.id.iv_media_next);
        shuffleImg = (ImageView) coordinator.findViewById(R.id.iv_media_shuffle);
        toolbar = (Toolbar) coordinator.findViewById(R.id.toolbar_list_fragment);
        listTitle = (TextView) coordinator.findViewById(R.id.tv_toolbar_title);
        playerBarLayout = (LinearLayout) findViewById(R.id.music_playerBarInApp);
        mListView = (ListView) coordinator.findViewById(R.id.lv_music_list);
        mListView.setOnItemClickListener(this);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);

//        musicListLayout.setVisibility(View.VISIBLE);
        Log.w("TAG", "musicname-----------" + musicNameTx + "comment------------ " + commentCount);
        /*if (ListPosition > -1) {
            List_Fragment fragment = new List_Fragment();

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_list_fragment
                    , fragment).commit();
            Bundle bundle = new Bundle();
            if (ListPosition >= 0) {
                String name = ListsList.get(ListPosition).getListName();
                listTitle.setText(name);
            }
            bundle.putParcelableArrayList("ListsList", (ArrayList<? extends Parcelable>) ListsList);
            bundle.putInt(Config.LIST, ListPosition);
            fragment.setArguments(bundle);
        } else {
            SystemListFragment fragment = new SystemListFragment();

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_list_fragment
                    , fragment).commit();
            Bundle bundle = new Bundle();
            if (ListPosition >= 0) {
                String name = ListsList.get(ListPosition).getListName();
                listTitle.setText(name);
            }
            bundle.putParcelableArrayList("ListsList", (ArrayList<? extends Parcelable>) ListsList);
            fragment.setArguments(bundle);
        }*/
    }

    private void initEvent() {
        playImg.setOnClickListener(this);
        returnImg.setOnClickListener(this);
        previousImg.setOnClickListener(this);
        nextImg.setOnClickListener(this);
        repeatImg.setOnClickListener(this);
        shuffleImg.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        //判断Service是否存在

        if (!Service.isMyServiceRunning(mContext, PlayerService.class)) {
            Intent serviceintent = new Intent(mContext, PlayerService.class);
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

                mService.play(mService.getPreviousMusic());
                break;
            case R.id.iv_media_next:
                mService.play(mService.getNextMusic());
                break;
            case R.id.iv_media_play: {
                if (mService.getPlayerState() == PlayerService.MediaPlayer_PAUSE && mService.getMyList() == null) {
                    mService.setPlayerList(MusicInfoDao.getAllMusic(this));
                }
//                if(CurrentListPosition<0){
//                    mService.setPlayerList(MusicInfoDao.getAllMusic(this));
//                }
                if (mService.getPlayerState() == mService.MediaPlayer_PLAY) {
                    mService.pause();
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

    private void updateUI() {
        //PlayerState代表播放状态
        switch (mService.getPlayerState()) {
            case 1: {
                playImg.setImageResource(R.drawable.play);
                //Toast.makeText(context, "music is prepared", Toast.LENGTH_SHORT).show();
                break;
            }
            case 2: {
                playImg.setImageResource(R.drawable.pause);
                //Toast.makeText(context, "music is playing", Toast.LENGTH_SHORT).show();
                break;
            }
            case 3: {
                playImg.setImageResource(R.drawable.play);
                //Toast.makeText(context, "music is paused", Toast.LENGTH_SHORT).show();
                break;

            }
        }
    }


    @Override
    public void update(java.util.Observable observable, Object data) {
        updateUI();
    }

    @Override
    public void showItemMenu(final MusicInfo music) {
        bottomSheetDialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.item_menu, null);
        musicNameTx = (TextView) view.findViewById(R.id.tv_item_menu_music_name);
        commentCount = (TextView) view.findViewById(R.id.tv_item_menu_commentCount);
        singerName = (TextView) view.findViewById(R.id.tv_item_menu_singer);
        albumName = (TextView) view.findViewById(R.id.tv_item_menu_album);
        nextLayout = (LinearLayout) view.findViewById(R.id.menu_item_next);
        collectLayout = (LinearLayout) view.findViewById(R.id.menu_item_collect);
        downloadLayout = (LinearLayout) view.findViewById(R.id.menu_item_download);
        commentLayout = (LinearLayout) view.findViewById(R.id.menu_item_comment);
        shareLayout = (LinearLayout) view.findViewById(R.id.menu_item_share);
        deleteLayout = (LinearLayout) view.findViewById(R.id.menu_item_delete);
        singerLayout = (LinearLayout) view.findViewById(R.id.menu_item_singer);
        albumLayout = (LinearLayout) view.findViewById(R.id.menu_item_album);
        bottomSheetDialog.setContentView(view);
        musicNameTx.setText(music.getMusicName());
        commentCount.setText("(" + music.size + ")");
        singerName.setText(music.getMusicArtist());
        albumName.setText(music.album);
        deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ListPosition < 0) {
                    //从SQLite数据库中删除该音乐
                    int id = music.get_id();
                    ListsInfoDao.getDAO().deleteMusic(mContext, id);
                    bottomSheetDialog.dismiss();
                }
                if (ListPosition >= 0) {
                    //仅从该表中移除
                    int id = ListsList.get(ListPosition).getListId();
                    int musicid = music.get_id();
                    ListsInfoDao.getDAO().removeMusic(mContext, musicid, id);
                    bottomSheetDialog.dismiss();
                }
            }
        });
        collectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectDialog dialog = new CollectDialog(mContext, ListsList, music);
                bottomSheetDialog.dismiss();
                dialog.show();

            }
        });
        bottomSheetDialog.show();
        Log.w("TAG", "it's a interface callback in MyAdapter");
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (!Service.isMyServiceRunning(view.getContext(), PlayerService.class)) {
            Intent serviceintent = new Intent(view.getContext(), PlayerService.class);
            view.getContext().startService(serviceintent);
            Log.e("TAGS", "MediaPlayerService does not existed");
        }
        mService.setPlayerList((ArrayList<MusicInfo>) mList);
        mService.play(mList.get(position));
        mAdapter.setIsPlaying(position);
        mAdapter.notifyDataSetChanged();
    }
}