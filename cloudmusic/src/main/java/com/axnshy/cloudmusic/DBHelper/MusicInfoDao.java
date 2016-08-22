/**
 * Created by axnshy on 16/5/20.
 */
package com.axnshy.cloudmusic.DBHelper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;

import com.axnshy.cloudmusic.Config;
import com.axnshy.cloudmusic.FilesRead.MusicInfo;

import java.util.ArrayList;
import java.util.List;

public class MusicInfoDao implements Config {

    private static final String TABLE_MUSIC = "music_info";

    private static MusicInfoDao mDao;

    public static MusicInfoDao getDAO() {
        if (mDao != null)
            return mDao;
        return new MusicInfoDao();
    }

    public void saveMusicInfo(List<MusicInfo> list, Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context);
        for (MusicInfo music : list) {
            ContentValues cv = new ContentValues();
            cv.put(MusicInfo.KEY_ID, music.musicId);
            cv.put(MusicInfo.KEY_ALBUM, music.album);
            cv.put(MusicInfo.KEY_DURATION, music.duration);
            cv.put(MusicInfo.KEY_MUSIC_NAME, music.musicName);
            cv.put(MusicInfo.KEY_ARTIST, music.artist);
            cv.put(MusicInfo.KEY_DATA, music.data);
            cv.put(MusicInfo.KEY_SIZE, music.size);

            db.insert(TABLE_MUSIC, null, cv);
        }
    }



    public static void addToList(SQLiteDatabase database, ArrayList<MusicInfo> list, String ListName) {
        for (MusicInfo music : list) {
            int musicId = music.musicId;
            database.execSQL("insert into " + ListName + " (musicID) values " + musicId);
        }
    }

    public static ArrayList<MusicInfo> getListDet(Context context, String listTableName) {
        ArrayList<MusicInfo> musicList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.getInstance(context);
        Cursor cursor = sqLiteDatabase.rawQuery("select music_info._id,music_info.musicName,music_info.album,music_info.artist,music_info.displayName,music_info.publish,music_info.duration,music_info.size,music_info.data from music_info left join" + listTableName + "on music_info._id=" + listTableName + "._id", null);
        if(cursor!=null&&cursor.moveToFirst()) {
            do{
                MusicInfo music = new MusicInfo();
                music.musicId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                music.musicName = cursor.getString(cursor.getColumnIndexOrThrow("musicName"));
                music.album = cursor.getString(cursor.getColumnIndexOrThrow("album"));
                music.artist = cursor.getString(cursor.getColumnIndexOrThrow("artist"));
                music.displayName = cursor.getString(cursor.getColumnIndexOrThrow("displayName"));
                music.publish = cursor.getString(cursor.getColumnIndexOrThrow("publish"));
                music.duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
                music.size = cursor.getInt(cursor.getColumnIndexOrThrow("size"));
                music.data = cursor.getString(cursor.getColumnIndexOrThrow("data"));
                if (music.duration >= 1 * 1000 * 60 && music.size > 1 * 1024 * 1024) {
                    musicList.add(music);
                }
            }while (cursor.moveToNext());
        }
        return musicList;
    }

    public static ArrayList<MusicInfo> getAllMusic(Context context) {
        ArrayList<MusicInfo> mList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.getHelper(context).getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_MUSIC, null, null, null, null, null, null);
        if(cursor!=null&&cursor.moveToFirst()) {
            do {
                MusicInfo musicInfo = new MusicInfo();
                musicInfo.musicId = cursor.getInt(cursor.getColumnIndexOrThrow("musicId"));
                musicInfo.data = cursor.getString(cursor.getColumnIndexOrThrow("data"));
                musicInfo.artist = cursor.getString(cursor.getColumnIndexOrThrow("artist"));
                musicInfo.album = cursor.getString(cursor.getColumnIndexOrThrow("album"));
                musicInfo.musicName = cursor.getString(cursor.getColumnIndexOrThrow("musicName"));
                musicInfo.duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
                musicInfo.size = cursor.getInt(cursor.getColumnIndexOrThrow("size"));
                musicInfo.publish = cursor.getString(cursor.getColumnIndexOrThrow("year"));
                if (musicInfo.duration >= 1 * 1000 * 60 && musicInfo.size > 1 * 1024 * 1024) {
                    mList.add(musicInfo);
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return mList;
    }

    /*
    * 根据列表ID获取列表实例
    * */
    public ArrayList<MusicInfo> getMusicList(Context context, int listId) {
        if (listId >= 0) {
            ArrayList<MusicInfo> list = new ArrayList<>();
            SQLiteDatabase sqLiteDatabase = DatabaseHelper.getHelper(context).getWritableDatabase();
            Cursor cursor1 = sqLiteDatabase.rawQuery("select * from " + DatabaseHelper.TABLE_MUSIC + " where musicId in ( select musicId from " + DatabaseHelper.TABLE_MUSIC_LIST_RELATIONSHIP
                    + " where listId=" + listId + ") ", null);
            if (cursor1 != null && cursor1.moveToFirst()) {
                do {
                    MusicInfo music = new MusicInfo();
                    music.musicId = cursor1.getInt(cursor1.getColumnIndexOrThrow("musicId"));
                    music.musicName = cursor1.getString(cursor1.getColumnIndexOrThrow("musicName"));
                    music.album = cursor1.getString(cursor1.getColumnIndexOrThrow("album"));
                    music.artist = cursor1.getString(cursor1.getColumnIndexOrThrow("artist"));
                    music.publish = cursor1.getString(cursor1.getColumnIndexOrThrow("year"));
                    music.duration = cursor1.getInt(cursor1.getColumnIndexOrThrow("duration"));
                    music.size = cursor1.getInt(cursor1.getColumnIndexOrThrow("size"));
                    music.data = cursor1.getString(cursor1.getColumnIndexOrThrow("data"));
                    music.category = cursor1.getInt(cursor1.getColumnIndexOrThrow("category"));
                    music.filetype = cursor1.getInt(cursor1.getColumnIndexOrThrow("filetype"));
                    if (music.duration >= 1 * 1000 * 60 && music.size > 1 * 1024 * 1024) {
                        list.add(music);
                    }
                } while (cursor1.moveToNext());
            }
//            cursor.close();
            cursor1.close();
            return list;
        } else {
            return getAllMusic(context);
        }
    }


    /**
     * 功能 通过album_id查找 album_art 如果找不到返回null
     *
     * @return album_art
     */
/*
    public static String getAlbumArt(Context context, int album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cur = context.getContentResolver().query(
                Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)),
                projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        if (album_art == null) {
            return null;
        }
        return album_art;
    }*/

    public static void scanMusic(Context context) {
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.getHelper(context).getWritableDatabase();
        Cursor cursor = MediaStoreAccessHelper.getAllSongUri(context, null);
        if(cursor!=null&&cursor.moveToFirst()) {
           do{
                ContentValues values = new ContentValues();
                values.put("data", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                values.put("artist", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                values.put("album", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
                values.put("musicName", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                values.put("duration", cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                values.put("size", cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                values.put("year", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR)));
                sqLiteDatabase.insert(DatabaseHelper.TABLE_MUSIC, null, values);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
    }

    /**
     *
     * @param context
     * @param albumid
     * @return
     */
    private static String getAlbumArt(Context context,int albumid) {
        String strAlbums = "content://media/external/audio/albums";
        String[] projection = new String[] {android.provider.MediaStore.Audio.AlbumColumns.ALBUM_ART };
        Cursor cur = context.getContentResolver().query(
                Uri.parse(strAlbums + "/" + Integer.toString(albumid)),
                projection, null, null, null);
        String strPath = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            strPath = cur.getString(0);
        }
        cur.close();
        cur = null;
        return strPath;
    }
}
