package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.dbbest.amateurfeed.app.net.request.LoginRequestModel;
import com.google.gson.annotations.SerializedName;

/**
 * Created by antonina on 09.02.17.
 */

public class NewsRequestModel implements Parcelable {


    @SerializedName("offset")
    private int mOffset;

    @SerializedName("count")
    private int mCount;

    @SerializedName("deviceToken")
    private String mAccessToken;

    public int getOffset() {
        return mOffset;
    }

    public int getCount() {
        return mCount;
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public NewsRequestModel(int offset, int count, String accessToken) {
        mOffset = offset;
        mCount = count;
        mAccessToken=accessToken;

    }


    private NewsRequestModel(Parcel in) {
        mOffset = in.readInt();
        mCount = in.readInt();
        mAccessToken=in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mOffset);
        dest.writeInt(mCount);
        dest.writeString(mAccessToken);

    }

    public static final Parcelable.Creator<NewsRequestModel> CREATOR = new Parcelable.Creator<NewsRequestModel>() {
        @Override
        public NewsRequestModel createFromParcel(Parcel source) {
            return new NewsRequestModel(source);
        }

        @Override
        public NewsRequestModel[] newArray(int size) {
            return new NewsRequestModel[size];
        }
    };
}
