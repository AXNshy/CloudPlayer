package com.axnshy.cloudmusic.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
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
 * Created by axnshy on 16/8/7.
 */
public class MusicPlayAdapter extends BaseAdapter {
    private ArrayList<MusicInfo> mList;
    private Context context;

    public MusicPlayAdapter(Context context, ArrayList<MusicInfo> mList){
        this.context=context;
        this.mList=mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (mList == null) {
            return null;
        }

        final ViewHolder holder;
        final MusicInfo music = mList.get(i);
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.custom_list_item, null);
            holder = new ViewHolder();
            holder.musicCover = (ImageView) convertView.findViewById(R.id.iv_current_musicAlbumCover);
            holder.musicTitle = (TextView) convertView.findViewById(R.id.tv_current_musicName);
            holder.musicArtist = (TextView) convertView.findViewById(R.id.tv_current_musicArtist);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bitmap bm = BitmapFactory.decodeFile((String)msg.obj);
                BitmapDrawable bmpDraw = new BitmapDrawable(bm);
                holder.musicCover.setImageDrawable(bmpDraw);
            }
        };
        holder.musicCover.setBackgroundResource(R.drawable.h1);
        holder.musicTitle.setText(music.getMusicName());
        if (music.getMusicArtist().length() > 20) {
            holder.musicArtist.setText(music.getMusicArtist().substring(1, 15) + "......");
        } else {
            holder.musicArtist.setText(music.getMusicArtist());
        }
        return convertView;
    }
    private class ViewHolder{
        ImageView musicCover;
        TextView musicTitle;
        TextView musicArtist;
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };
}
