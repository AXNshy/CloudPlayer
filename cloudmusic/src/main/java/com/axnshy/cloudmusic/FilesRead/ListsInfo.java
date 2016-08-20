package com.axnshy.cloudmusic.FilesRead;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by axnshy on 16/8/9.
 */
public class ListsInfo implements Parcelable {
    private int listId;
    private String listName;
    private int listCount;
    private String backgroundPath;
    private int userId;

    public ListsInfo() {
    }

    public ListsInfo(String listName, int listCount,String backgroundPath) {
        this.listName = listName;
        this.listCount = listCount;
        this.backgroundPath=backgroundPath;
        this.userId=-1;
    }

    protected ListsInfo(Parcel in) {
        listId = in.readInt();
        listName = in.readString();
        listCount = in.readInt();
        backgroundPath = in.readString();
    }

    public static final Creator<ListsInfo> CREATOR = new Creator<ListsInfo>() {
        @Override
        public ListsInfo createFromParcel(Parcel in) {
            ListsInfo list = new ListsInfo();
            list.setListId(in.readInt());
            list.setListName(in.readString());
            list.setListCount(in.readInt());
            list.setBackgroundPath(in.readString());
            list.setUserId(in.readInt());
            return list;
        }

        @Override
        public ListsInfo[] newArray(int size) {
            return new ListsInfo[size];
        }
    };

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public void setListCount(int listCount) {
        this.listCount = listCount;
    }

    public void setBackgroundPath(String backgroundPath) {
        this.backgroundPath = backgroundPath;
    }

    public int getListId() {
        return listId;
    }

    public String getListName() {
        return listName;
    }

    public int getListCount() {
        return listCount;
    }

    public String getBackgroundPath() {
        return backgroundPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(listId);
        parcel.writeString(listName);
        parcel.writeInt(listCount);
        parcel.writeString(backgroundPath);
        parcel.writeInt(userId);
    }


}
