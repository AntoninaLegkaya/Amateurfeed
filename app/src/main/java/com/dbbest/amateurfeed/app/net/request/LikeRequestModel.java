package com.dbbest.amateurfeed.app.net.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by antonina on 09.02.17.
 */

public class LikeRequestModel implements Parcelable {
    @SerializedName("like")
    private int isLike;


    public LikeRequestModel(int isLike) {

        this.isLike = isLike;

    }


    private LikeRequestModel(Parcel in) {
        isLike = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(isLike);

    }

    public static final Parcelable.Creator<LikeRequestModel> CREATOR = new Parcelable.Creator<LikeRequestModel>() {
        @Override
        public LikeRequestModel createFromParcel(Parcel source) {
            return new LikeRequestModel(source);
        }

        @Override
        public LikeRequestModel[] newArray(int size) {
            return new LikeRequestModel[size];
        }
    };
}