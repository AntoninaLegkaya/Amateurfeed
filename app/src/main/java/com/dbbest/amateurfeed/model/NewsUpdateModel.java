package com.dbbest.amateurfeed.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by antonina on 26.01.17.
 */

public class NewsUpdateModel {
    @SerializedName("title")
    private String mTitle;

    @SerializedName("text")
    private String mText;

    @SerializedName("image")
    private String mImage;

    @SerializedName("tags")
    private ArrayList<TagModel> mTags;

    public String getTitle() {
        return mTitle;
    }

    public String getText() {
        return mText;
    }

    public String getImage() {
        return mImage;
    }

    public ArrayList<TagModel> getTags() {
        return mTags;
    }

    public NewsUpdateModel(String title, String text, String image, ArrayList<TagModel> tags) {

        mTitle = title;
        mText = text;
        mImage = image;
        mTags = tags;
    }
}
