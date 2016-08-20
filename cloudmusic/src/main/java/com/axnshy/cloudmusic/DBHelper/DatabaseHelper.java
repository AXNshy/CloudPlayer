/**
 * Created by axnshy on 16/5/20.
 */
package com.axnshy.cloudmusic.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static SQLiteDatabase mDb;
    private static DatabaseHelper mHelper;
    public static final int DB_VERSION = 3;
    public static final String DB_NAME = "music_player";
    public static final String TABLE_MUSIC = "music_info";
    public static final String TABLE_USER = "user_info";
    public static final String TABLE_LISTS = "music_list_info";
    public static final String TABLE_MUSIC_LIST_RELATIONSHIP = "music_list_relationship";


    public static SQLiteDatabase getInstance(Context context) {
        if (mDb == null) {
            System.out.println("SQLiteDatabase is null");
            mDb = getHelper(context).getWritableDatabase();
        } else
            System.out.println("SQLiteDatabase is not null");
        return mDb;
    }

    public synchronized static DatabaseHelper getHelper(Context context) {
        if (mHelper == null) {
            mHelper = new DatabaseHelper(context);
        }
        return mHelper;
    }

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    /*
    * 在SQLite数据库中创建5个表:music_info album_info artist_info folder_info favorite_infos
    * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "
                + TABLE_MUSIC
                + " (musicId INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "musicName varchar(40),data varchar(100),album varchar(40),artist varchar(40)," +
                "size integer,duration integer,category integer,year varchar(10),filetype integer)");
        db.execSQL("create table "
                + TABLE_LISTS
                + " (listId INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " listName varchar(45),listLength integer DEFAULT 0,listAvatar VARCHAR(100))");
        db.execSQL("create table "
                + TABLE_MUSIC_LIST_RELATIONSHIP
                + "(musicId integer REFERENCES " + TABLE_MUSIC + "(_id) ON UPDATE CASCADE," +
                "listId integer REFERENCES "+TABLE_LISTS +"(listId) ON UPDATE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MUSIC);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTS);

            onCreate(db);
        }
    }

    public void deleteTables(Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MUSIC, null, null);
        db.delete(TABLE_USER, null, null);
        db.delete(TABLE_LISTS, null, null);
    }

}
