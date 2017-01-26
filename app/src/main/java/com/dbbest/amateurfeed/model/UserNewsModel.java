package com.dbbest.amateurfeed.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by antonina on 26.01.17.
 */

public class UserNewsModel {

    @SerializedName("id")
    private int mId;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("updateDate")
    private String mUpdateDate;


    @SerializedName("status")
    private String mStatus;

    @SerializedName("image")
    private String mImage;

    @SerializedName("likes")
    private int mLikes;

    public UserNewsModel(int id, String title, String updateDate, String status, String image, int likes) {
        mId = id;
        mTitle = title;
        mUpdateDate = updateDate;
        mStatus = status;
        mImage = image;
        mLikes = likes;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUpdateDate() {
        return mUpdateDate;
    }

    public String getStatus() {
        return mStatus;
    }

    public String getImage() {
        return mImage;
    }

    public int getLikes() {
        return mLikes;
    }
}
