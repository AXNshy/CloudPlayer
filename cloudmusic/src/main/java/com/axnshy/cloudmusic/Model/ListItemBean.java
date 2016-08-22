package com.axnshy.cloudmusic.Model;

import android.annotation.SuppressLint;

import com.axnshy.cloudmusic.FilesRead.MusicInfo;

/**
 * Created by axnshy on 16/8/22.
 */
@SuppressLint("ParcelCreator")
public class ListItemBean extends MusicInfo{
    private boolean isplaying;

    public void setPlaying(boolean isplaying) {
        this.isplaying = isplaying;
    }

    public boolean isPlaying(){
        return isplaying;
    }
}
