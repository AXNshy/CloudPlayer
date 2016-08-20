package com.axnshy.cloudmusic.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.axnshy.cloudmusic.FilesRead.MusicInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by axnshy on 16/5/24.
 */
public class SelectInfoDao {

    private static final String TABLE_MUSIC = "music_info";
    private Context mContext;

    public void saveMusicInfo(ArrayList<MusicInfo> list) {
        SQLiteDatabase db = DatabaseHelper.getInstance(mContext);
        for (MusicInfo music : list) {
            ContentValues cv = new ContentValues();
            //cv.put("resourceId", music._id);
            cv.put("album", music.album);
            cv.put("duration", music.duration);
            cv.put("musicName", music.musicName);
            cv.put("artist", music.artist);
            cv.put("data",music.data);
            cv.put("size",music.size);
            cv.put("displayName",music.displayName);
            db.insert(TABLE_MUSIC, null, cv);
        }
        db.close();
    }

    private List<MusicInfo> parseCursor(Cursor cursor) {
        List<MusicInfo> list = new ArrayList<MusicInfo>();
        while(cursor.moveToNext()) {
            MusicInfo music = new MusicInfo();
            //music._id = cursor.getInt(cursor.getColumnIndex("_id"));
            //music.songId = cursor.getInt(cursor.getColumnIndex("songid"));
            music.album = cursor.getString(cursor.getColumnIndex("album"));
            music.duration = cursor.getInt(cursor.getColumnIndex("duration"));
            music.musicName = cursor.getString(cursor.getColumnIndex("musicname"));
            music.artist = cursor.getString(cursor.getColumnIndex("artist"));
            music.data = cursor.getString(cursor.getColumnIndex("data"));
            music.size=cursor.getInt(cursor.getColumnIndex("size"));
            music.displayName = cursor.getString(cursor.getColumnIndex("displayName"));
            list.add(music);
        }
        cursor.close();
        return list;
    }

    public List<MusicInfo> getMusicInfo() {
        SQLiteDatabase db = DatabaseHelper.getInstance(mContext);
        String sql = "select * from " + TABLE_MUSIC;

        return parseCursor(db.rawQuery(sql, null));
    }


}
