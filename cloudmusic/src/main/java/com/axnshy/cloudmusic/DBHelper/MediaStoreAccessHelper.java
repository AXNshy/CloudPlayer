package com.axnshy.cloudmusic.DBHelper;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class MediaStoreAccessHelper {


	public static Cursor getAlbumInfomation(Context context,int album_id){
		String strAlbums = "content://media/external/album_art";
		ContentResolver contentResolver = context.getContentResolver();
		Uri uri = Uri.parse(strAlbums);
		String selection = MediaStore.Audio.AlbumColumns.ALBUM_ID+ "="+album_id;
		return contentResolver.query(uri,null,selection,null,null);
	}
	
	/**
	 * public static Cursor getAllSongs(Context context,String[] projection,String sortOrder)
	 *
	 * Queries MediaStore and returns a cursor with all songs.
	 */
	public static Cursor getAllSongUri(Context context,

									   String sortOrder) {

		ContentResolver contentResolver = context.getContentResolver();
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";

		return contentResolver.query(uri,null,selection,null,sortOrder);
	}

}
