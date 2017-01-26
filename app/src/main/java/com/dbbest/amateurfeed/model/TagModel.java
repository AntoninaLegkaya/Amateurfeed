package com.dbbest.amateurfeed.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by antonina on 26.01.17.
 */

public class TagModel {
    @SerializedName("id")
    private int mId;

    @SerializedName("name")
    private String mName;


    public TagModel(int id, String name) {
        mId = id;
        mName = name;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    @Override
    public String toString() {
        return "TagModel{" +
                "mId=" + mId +
                ", mName='" + mName +
                '}';
    }
}
