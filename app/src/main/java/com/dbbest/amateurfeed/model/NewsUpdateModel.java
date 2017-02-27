package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by antonina on 26.01.17.
 */

public class NewsUpdateModel implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


        dest.writeString(mTitle);
        dest.writeString(mText);
        dest.writeString(mImage);
        dest.writeList(mTags);
    }

    public NewsUpdateModel(Parcel in) {
        mTitle = in.readString();
        mText = in.readString();
        this.mTags = new ArrayList<TagModel>();
        in.readList(this.mTags, TagModel.class.getClassLoader());
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<NewsUpdateModel> CREATOR = new Parcelable.Creator<NewsUpdateModel>() {
        @Override
        public NewsUpdateModel createFromParcel(Parcel in) {
            return new NewsUpdateModel(in);
        }

        @Override
        public NewsUpdateModel[] newArray(int size) {
            return new NewsUpdateModel[size];
        }
    };
}
