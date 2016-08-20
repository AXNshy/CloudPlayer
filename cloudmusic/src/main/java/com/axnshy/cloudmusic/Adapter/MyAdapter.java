package com.axnshy.cloudmusic.Adapter;


import android.app.Activity;
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

/**
 * Created by axnshy on 16/5/21.
 */
public class MyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MusicInfo> List;
    private OnItemMenuClickListener mLister;

    public MyAdapter(Context context, Activity activity, ArrayList<MusicInfo> object) {
        this.context = context;
        if (object != null) {
            List = object;
        }
        mLister = (OnItemMenuClickListener) activity;
        System.out.println("mLister:" + mLister + "Activity:" + activity);
    }


    class ViewHolder {
        ImageView playIndicator;
        TextView musicName;
        TextView musicArtist;
        ImageView itemMenu;
    }

    @Override
    public int getCount() {
        return List.size();
    }

    @Override
    public Object getItem(int position) {
        return List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        if (List == null) {
            return null;
        }
        final ViewHolder holder;
        final MusicInfo music = List.get(position);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.custom_list_item, null);
            holder = new ViewHolder();
            holder.musicName = (TextView) convertView.findViewById(R.id.id_music_name);
            holder.musicArtist = (TextView) convertView.findViewById(R.id.id_music_artist);
            holder.itemMenu = (ImageView) convertView.findViewById(R.id.iv_item_menu);
            holder.playIndicator= (ImageView) convertView.findViewById(R.id.iv_play_indicator);
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
        holder.playIndicator.setVisibility(View.INVISIBLE);
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
}
