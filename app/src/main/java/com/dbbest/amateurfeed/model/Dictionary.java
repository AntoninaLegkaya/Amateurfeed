package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Dictionary implements Parcelable {
    @SerializedName("users")
    private ArrayList<UserFeedCreatorExpand> mUsers = new ArrayList<>();
    ;
    @SerializedName("news")
    private ArrayList<News> mNews = new ArrayList<>();
    ;

    public Dictionary(ArrayList<UserFeedCreatorExpand> users, ArrayList<News> news) {
        mUsers = users;
        mNews = news;
    }

    public Dictionary(Parcel in) {

        mUsers = new ArrayList<UserFeedCreatorExpand>();
        in.readTypedList(mUsers, UserFeedCreatorExpand.CREATOR);
        mNews = new ArrayList<News>();
        in.readTypedList(mNews, News.CREATOR);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mNews);
        dest.writeTypedList(mUsers);
    }

    public static final Creator<Dictionary> CREATOR = new Creator<Dictionary>() {
        @Override
        public Dictionary createFromParcel(Parcel in) {
            return new Dictionary(in);
        }

        @Override
        public Dictionary[] newArray(int size) {
            return new Dictionary[size];
        }
    };

    public ArrayList<UserFeedCreatorExpand> getUsers() {
        return mUsers;
    }

    public ArrayList<News> getNews() {
        return mNews;
    }
}
