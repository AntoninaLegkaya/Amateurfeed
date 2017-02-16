package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by antonina on 26.01.17.
 */

public class AbuseModel implements Parcelable {
    @SerializedName("newsId")
    private int mNewsId;

    @SerializedName("comment")
    private String mComment;

    protected AbuseModel(Parcel in) {
        mNewsId = in.readInt();
        mComment = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mNewsId);
        dest.writeString(mComment);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AbuseModel> CREATOR = new Creator<AbuseModel>() {
        @Override
        public AbuseModel createFromParcel(Parcel in) {
            return new AbuseModel(in);
        }

        @Override
        public AbuseModel[] newArray(int size) {
            return new AbuseModel[size];
        }
    };

    public int getNewsId() {
        return mNewsId;
    }

    public String getComment() {
        return mComment;
    }

    public AbuseModel(int newsId, String comment) {

        mNewsId = newsId;
        mComment = comment;
    }
}
