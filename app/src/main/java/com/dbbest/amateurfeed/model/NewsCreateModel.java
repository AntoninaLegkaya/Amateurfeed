package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antonina on 26.01.17.
 */

public class NewsCreateModel implements Parcelable {

    @SerializedName("title")
    private String mTitle;

    @SerializedName("text")
    private String mText;

    @SerializedName("image")
    private String mImage;

    @SerializedName("tags")
    private List<TagModel> mTags = new ArrayList<>();

    public String getTitle() {
        return mTitle;
    }

    public String getText() {
        return mText;
    }

    public String getImage() {
        return mImage;
    }

    public List<TagModel> getTags() {
        return mTags;
    }

    public NewsCreateModel(String title, String text, String image, List<TagModel> tags) {

        mTitle = title;
        mText = text;
        mImage = image;
        mTags = tags;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    private NewsCreateModel(Parcel in) {
        mTitle = in.readString();
        mText = in.readString();
        mImage = in.readString();
        mTags = new ArrayList<TagModel>();
        in.readTypedList(mTags, TagModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mText);
        dest.writeString(mImage);
        dest.writeTypedList(mTags);
    }


    public static final Creator<NewsCreateModel> CREATOR = new Creator<NewsCreateModel>() {
        @Override
        public NewsCreateModel createFromParcel(Parcel in) {
            return new NewsCreateModel(in);
        }

        @Override
        public NewsCreateModel[] newArray(int size) {
            return new NewsCreateModel[size];
        }
    };


    @Override
    public String toString() {
        String tagsNames = new String();
        if (mTags != null) {
            for (TagModel model : mTags) {
                tagsNames = tagsNames + model.getName() + " ";

            }
        }

        return "NewsCreateModel{"
                + "mTitle=" + mTitle + '\n'
                + "mText=" + mText + '\n'
                + "mImage=" + mImage + '\n'
                + "mTags=" + tagsNames + '\n'
                + '}';
    }
}
