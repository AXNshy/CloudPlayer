/**
 * Created by axnshy on 16/5/20.
 */
package com.axnshy.cloudmusic.FilesRead;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * Created by axnshy on 16/5/20.
 */
public class FileList extends ArrayList {


    public FileList(Context context) {
        getFileList(context);
    }

   /* private static Context getContext(){
        return this.context;
    }*/
    public static ArrayList<MusicInfo> getFileList(Context context){
       /* if(mFolderInfoDao == null) {
            mFolderInfoDao = new FolderInfoDao(context);
        }
        SPStorage sp = new SPStorage(context);*/
        Uri uri = MediaStore.Files.getContentUri("external");
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        int i = 0;
        int cursorCount = cursor.getCount();
        if (cursorCount >0 )
        {
            cursor.moveToFirst();
            while (i < cursorCount)
            {
                //歌曲ID：MediaStore.Audio.Media._ID
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));


                //歌曲的名称 ：MediaStore.Audio.Media.TITL
                String tilte = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));

                //歌曲的专辑名：MediaStore.Audio.Media.ALBUM
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));

                //歌曲的歌手名： MediaStore.Audio.Media.ARTIST
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));

                //歌曲文件的全路径 ：MediaStore.Audio.Media.DATA
                String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

                //歌曲文件的名称：MediaStroe.Audio.Media.DISPLAY_NAME

                String display_name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));

                //歌曲文件的发行日期：MediaStore.Audio.Media.YEAR
                String year = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR));


                //歌曲的总播放时长 ：MediaStore.Audio.Media.DURATION
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

                //歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));

                if(url.toLowerCase().indexOf("指定的歌曲路径") > 0)
                {
                    //DatabaseHelper.getInstance(context).execSQL("");
                }
                i++;
                cursor.moveToNext();
            }
            cursor.close();
        }
        return null;
    }
}
