package com.axnshy.cloudmusic.Fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.axnshy.cloudmusic.Activity.MusicListActivity;
import com.axnshy.cloudmusic.Adapter.RecyclerViewAdapter;
import com.axnshy.cloudmusic.Config;
import com.axnshy.cloudmusic.DBHelper.ListsInfoDao;
import com.axnshy.cloudmusic.FilesRead.ListsInfo;
import com.axnshy.cloudmusic.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by axnshy on 16/5/20.
 */
@ContentView(R.layout.home_top_fragment)
public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private List<ListsInfo> mListsList;
    private LinearLayout linearLayout1;
    private TextView txtNum1;

    private int screenWidth;
    private int screenHeight;

    @ViewInject(R.id.recycler_music_list)
    private RecyclerView recyclerView;
    @ViewInject(R.id.tv_addlist)
    private TextView newMusicListTx;
    private RecyclerViewAdapter mRecyclerAdapter;

    private View view;

    private LayoutInflater inflater;

    private AlertDialog dialog;

    private String AvatarPath = null;

    private ImageView showAvatar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_top_fragment, container, false);
        this.inflater = inflater;
        initView();
        initData();
        return view;
    }


    private void initView() {
        linearLayout1 = (LinearLayout) view.findViewById(R.id.id_top_home_linearlayout1);
        txtNum1 = (TextView) view.findViewById(R.id.tv_music_count1);
        linearLayout1.setOnClickListener(this);
        newMusicListTx = (TextView) view.findViewById(R.id.tv_addlist);
        newMusicListTx.setOnClickListener(this);
        txtNum1.setText(" 个");
        txtNum1.setTextColor(Color.parseColor("#FFFFFF"));
        mListsList = new ArrayList<>();

    }

    @Override
    public void onClick(View v) {
        int viewID = v.getId();
        switch (viewID) {
            case R.id.id_top_home_linearlayout1: {
                Intent intent = new Intent(view.getContext(), MusicListActivity.class);
                intent.putExtra(Config.LIST, -1);
                intent.putParcelableArrayListExtra("ListsList", (ArrayList<? extends Parcelable>) mListsList);
                startActivity(intent);
                break;
            }
            case R.id.tv_addlist:
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.dialog_add_music_list, null);
                builder.setView(layout);
                dialog = builder.create();
                final EditText inputListName = (EditText) layout.findViewById(R.id.et_input_music_list_name);
                TextView cancelTx = (TextView) layout.findViewById(R.id.tv_cancel);
                TextView confirm = (TextView) layout.findViewById(R.id.tv_confirm);
                final Button addAvatar = (Button) layout.findViewById(R.id.btn_select_background_from_lib);
                showAvatar = (ImageView) layout.findViewById(R.id.iv_show_list_avatar);

                cancelTx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ListsInfoDao.getDAO().addMusicList(view.getContext(), inputListName.getText().toString().trim(), AvatarPath);
                                Message msg = Message.obtain();
                                msg.obj = new ListsInfo(inputListName.getText().toString(), 0, AvatarPath);
                                msg.what = 1;
                                mHandler.sendMessage(msg);
                                dialog.dismiss();
                            }
                        }).start();
                    }
                });
                addAvatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addAvatar.setVisibility(View.INVISIBLE);
                        Intent picture = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(picture, 101);//101:从图库选择
                    }
                });
                dialog.show();
        }
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendMessage(mHandler.obtainMessage(0, ListsInfoDao.getDAO().getListsList(view.getContext())));
            }
        }).start();
    }

    /*
    * http://blog.csdn.net/wangliblog/article/details/22501141
    * */
    public void getAndroiodScreenProperty() {
        WindowManager wm = (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;// 屏幕宽度（像素）
        screenHeight = dm.heightPixels;
        /*int width = dm.widthPixels;// 屏幕宽度（像素）
        int height = dm.heightPixels; // 屏幕高度（像素）*/
        /*float density = dm.density;//屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;//屏幕密度dpi（120 / 160 / 240）
        //屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        screenWidth = (int) (width / density);//屏幕宽度(dp)
        screenHeight = (int) (height / density);//屏幕高度(dp)*/
        Log.e("TAG", screenWidth + "======" + screenHeight);
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mListsList.add((ListsInfo) msg.obj);
                    mRecyclerAdapter.notifyItemChanged(mListsList.size() - 1);
                    break;
                case 0:
                    if (recyclerView == null)
                        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_music_list);
                    mListsList = (List<ListsInfo>) msg.obj;
//                    mListsList.add(0,new ListsInfo());
                    getAndroiodScreenProperty();
                    mRecyclerAdapter = new RecyclerViewAdapter(view.getContext(), mListsList, screenWidth, screenHeight);
                    mRecyclerAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            int _id = mListsList.get(position).getListId();
                            Intent intent = new Intent(view.getContext(), MusicListActivity.class);
                            intent.putExtra(Config.LIST, position);
                            intent.putParcelableArrayListExtra("ListsList", (ArrayList<? extends Parcelable>) mListsList);
                            startActivity(intent);
                        }

                        @Override
                        public void onItemLongClick(View view, int position) {

                        }
                    });
                    recyclerView.setAdapter(mRecyclerAdapter);
                    GridLayoutManager manager = new GridLayoutManager(view.getContext(), 3);
                    recyclerView.setLayoutManager(manager);
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = view.getContext().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            AvatarPath = picturePath;
            showAvatar.setImageURI(Uri.parse(AvatarPath));
//            Uri destination = Uri.fromFile(new File(view.getContext().getCacheDir(), "cropped"));
//            /*destinationFileName=picturePath+"destination";*/
//            UCrop.of(Uri.parse(picturePath),destination)
//                    .withAspectRatio(1,1)
//                    .withMaxResultSize(view.getWidth()*2/3, view.getHeight()*2/3)
//                    .start(getActivity());
//          //  Crop.of(Uri.parse(picturePath), destination).asSquare().start(getActivity());

        }
    }
}
