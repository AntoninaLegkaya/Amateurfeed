package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class TagModel implements Parcelable {
    @SerializedName("id")
    private int mId;

    @SerializedName("name")
    private String mName;


    public TagModel(int id, String name) {
        mId = id;
        mName = name;
    }

    public TagModel(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


        dest.writeLong(mId);
        dest.writeString(mName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TagModel> CREATOR = new Parcelable.Creator<TagModel>() {
        @Override
        public TagModel createFromParcel(Parcel in) {
            return new TagModel(in);
        }

        @Override
        public TagModel[] newArray(int size) {
            return new TagModel[size];
        }
    };
}
