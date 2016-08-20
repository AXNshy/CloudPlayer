package com.axnshy.cloudmusic.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.axnshy.cloudmusic.FilesRead.ListsInfo;
import com.axnshy.cloudmusic.FilesRead.MusicInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by axnshy on 16/8/9.
 */
public class ListsInfoDao {
    private static  ListsInfoDao mDao;

    public List<ListsInfo> mList;

    public ListsInfoDao() {
    }

    public static ListsInfoDao getDAO() {
        if(mDao!=null)
        return mDao;
        return new ListsInfoDao();
    }

    public List<ListsInfo> getListsList(Context context) {
        SQLiteDatabase db = DatabaseHelper.getHelper(context).getWritableDatabase();
        mList = new ArrayList<>();
        int i = 0;
        Cursor cursor = db.query(DatabaseHelper.TABLE_LISTS, new String[]{"listId", "listName", "listLength", "listAvatar"}, null, null, null, null, null);
        if(cursor!=null&&cursor.moveToFirst()) {
            do{
                ListsInfo list = new ListsInfo();
                list.setListId(cursor.getInt(cursor.getColumnIndexOrThrow("listId")));
                list.setListName(cursor.getString(cursor.getColumnIndexOrThrow("listName")));
                list.setListCount(cursor.getInt(cursor.getColumnIndexOrThrow("listLength")));
                list.setBackgroundPath(cursor.getString(cursor.getColumnIndexOrThrow("listAvatar")));
                mList.add(i++, list);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return mList;
    }

    public void addMusicList(Context context, String trim) {
        this.addMusicList(context, trim, null);
    }

    public void addMusicList(Context context, String trim, String picPath) {
        SQLiteDatabase db = DatabaseHelper.getHelper(context).getWritableDatabase();
        Log.w("TAG","db.isOpen()--------------------------------"+db.isOpen()+"pic Path -----  "+picPath);
//        db.beginTransaction();
        ContentValues value = new ContentValues();
        value.put("listName", trim);
        value.put("listAvatar",picPath);
        value.put("listLength", 0);
        db.insert(DatabaseHelper.TABLE_LISTS, null, value);
        db.close();
    }

    public void addMusicToList(Context context, MusicInfo musicInfo, int listId){
        SQLiteDatabase db = DatabaseHelper.getHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        int musicId=musicInfo.get_id();
        values.put("musicId",musicId);
        values.put("listId",listId);
        db.insert(DatabaseHelper.TABLE_MUSIC_LIST_RELATIONSHIP,null,values);
        db.execSQL("update "+DatabaseHelper.TABLE_LISTS+" set listLength = listLength+1 where listId = "+listId);
        db.close();
    }

    public boolean removeMusic(Context context,int musicid,int listid){
        SQLiteDatabase db =DatabaseHelper.getHelper(context).getWritableDatabase();
        db.execSQL("delete from "+DatabaseHelper.TABLE_MUSIC_LIST_RELATIONSHIP+" where musicId= "+musicid+" and listId = "+listid);
        db.close();
        return true;
    }

    public boolean deleteMusic(Context context,int musicid){
        SQLiteDatabase db =DatabaseHelper.getHelper(context).getWritableDatabase();
        db.execSQL("delete from "+DatabaseHelper.TABLE_MUSIC+" where musicId= "+musicid);
        db.close();
        return true;
    }
}
