package com.axnshy.cloudmusic.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.axnshy.cloudmusic.FilesRead.MusicInfo;
import com.axnshy.cloudmusic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by axnshy on 16/5/21.
 */
public class MyAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<MusicInfo> mList;
    private OnItemMenuClickListener mLister;
    private boolean[] isPlaying;
    //记录上一次播放位置
    int current;

    public MyAdapter(Context context, List<MusicInfo> list) {
        this.context = context;
         layoutInflater= LayoutInflater.from(context);
        if (list != null) {
            this.mList = (ArrayList<MusicInfo>) list;
        }
        initIsPlaying();
        mLister = (OnItemMenuClickListener) context;
//        System.out.println("mLister:" + mLister + "Activity:" + activity);
    }

    private void initIsPlaying() {
        isPlaying=new boolean[mList.size()];
    }


    class ViewHolder {
        ImageView playIndicator;
        TextView musicName;
        TextView musicArtist;
        ImageView itemMenu;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        if (mList == null) {
            return null;
        }
        final ViewHolder holder;
        final MusicInfo music = mList.get(position);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.custom_list_item, null);
            holder = new ViewHolder();
            holder.musicName = (TextView) convertView.findViewById(R.id.id_music_name);
            holder.musicArtist = (TextView) convertView.findViewById(R.id.id_music_artist);
            holder.itemMenu = (ImageView) convertView.findViewById(R.id.iv_item_menu);
            holder.playIndicator = (ImageView) convertView.findViewById(R.id.iv_play_indicator);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.musicName.setText(music.getMusicName());
        if (music.getMusicArtist().length() > 20) {
            holder.musicArtist.setText(music.getMusicArtist().substring(1, 15) + "......");
        } else {
            holder.musicArtist.setText(music.getMusicArtist());
        }
        if (isPlaying[position]) {
            holder.playIndicator.setVisibility(View.VISIBLE);
        } else
            holder.playIndicator.setVisibility(View.GONE);
        holder.itemMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLister.showItemMenu(music);
            }
        });
        return convertView;
    }

    public interface OnItemMenuClickListener {
        public void showItemMenu(MusicInfo music);
    }

    public void setIsPlaying( int position) {
        this.isPlaying[position] = true;
        this.isPlaying[current]=false;
        current = position;
    }
}
