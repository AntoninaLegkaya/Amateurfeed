package com.dbbest.amateurfeed.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by antonina on 26.01.17.
 */

public class UserFeedCreator {
    @SerializedName("id")
    private int mId;

    @SerializedName("name")
    private String mName;


    @SerializedName("isAdmin")
    private boolean mIsAdmin;

    @SerializedName("image")
    private String mImage;

    public UserFeedCreator(int id, String name, boolean isAdmin, String image) {
        mId = id;
        mName = name;
        mIsAdmin = isAdmin;
        mImage = image;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public boolean isAdmin() {
        return mIsAdmin;
    }

    public String getImage() {
        return mImage;
    }


    @Override
    public String toString() {
        return "UserFeedCreator {" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mIsAdmin='" + mIsAdmin + '\'' +
                ", mIsAdmin='" + mIsAdmin + '\'' +
        '}';
    }

}
