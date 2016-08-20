/**
 * Created by axnshy on 16/5/20.
 */
package com.axnshy.cloudmusic.FilesRead;


import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class MusicInfo implements Parcelable {


    public static final String KEY_ID = "musicId";

    public static final String KEY_ALBUM = "album";

    public static final String KEY_YEAR = "publishDate";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_SIZE = "size";
    public static final String KEY_MUSIC_NAME = "musicName";
    public static final String KEY_ARTIST = "artist";
    public static final String KEY_DATA = "data";
    public static final String KEY_FILETYPE = "filetype";
    public static final String KEY_CATEGORY = "category";


    /**
     * 数据库中的_id
     */
    public int musicId = -1;
    public String album;
    public int duration;
    public String musicName;
    public String artist;
    public String data;
    //歌曲文件名称
    public String displayName;
    //发布日期
    public String publish;
    //文件大小
    public int size;
    //  0    1    2    3    4    5
    public int category;
    //  0    1    2    3    4    5
    public int filetype;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ID, musicId);
        bundle.putString(KEY_ALBUM, album);
        bundle.putInt(KEY_DURATION, duration);
        bundle.putString(KEY_MUSIC_NAME, musicName);
        bundle.putString(KEY_ARTIST, artist);
        bundle.putString(KEY_YEAR, publish);
        bundle.putString(KEY_DATA, data);
        bundle.putInt(KEY_SIZE, size);
        bundle.putInt(KEY_CATEGORY, category);
        bundle.putInt(KEY_FILETYPE, filetype);
        dest.writeBundle(bundle);
    }

    public static final Creator<MusicInfo> CREATOR = new Creator<MusicInfo>() {

        @Override
        public MusicInfo createFromParcel(Parcel source) {
            MusicInfo music = new MusicInfo();
            Bundle bundle = source.readBundle();
            music.musicId = bundle.getInt(KEY_ID);
            music.duration = bundle.getInt(KEY_DURATION);
            music.musicName = bundle.getString(KEY_MUSIC_NAME);
            music.artist = bundle.getString(KEY_ARTIST);
            music.data = bundle.getString(KEY_DATA);
            music.publish = bundle.getString(KEY_YEAR);
            music.size = bundle.getInt(KEY_SIZE);
            music.album = bundle.getString(KEY_ALBUM);
            music.filetype = bundle.getInt(KEY_FILETYPE);
            music.category = bundle.getInt(KEY_CATEGORY);
            return music;
        }

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };

    public String getMusicName() {
        return musicName;
    }

    public String getMusicArtist() {
        return artist;
    }

    public long getMusicDuration() {
        return duration;
    }

    public String getUri() {
        return data;
    }

    public int get_id() {
        return musicId;
    }

    public String getMusicTimeTotal() {
        int head = (int) duration / 60000;
        int foot = (int) (duration % 60000) / 1000;
        return head + ":" + foot;
    }

    public static String parseMusicData(long time) {
        int head = (int) time / 60000;
        int foot = (int) (time % 60000) / 1000;
        return head + ":" + foot;
    }


}