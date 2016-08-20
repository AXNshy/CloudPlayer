package com.axnshy.cloudmusic.FilesRead;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by axnshy on 16/5/22.
 */
public class AlbumInfo implements Parcelable{

    private int album_id;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator<AlbumInfo> CREATOR = new Creator<AlbumInfo>(){

        @Override
        public AlbumInfo createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public AlbumInfo[] newArray(int size) {
            return new AlbumInfo[0];
        }
    };

}
