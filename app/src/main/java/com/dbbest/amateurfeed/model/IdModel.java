package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by antonina on 10.02.17.
 */

public class IdModel implements Parcelable {
    @SerializedName("id")
    private long  _ID;

    public IdModel(Parcel in) {
        _ID = in.readInt();
    }

    public long get_ID() {
        return _ID;
    }

    public IdModel(long id) {
        _ID=id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_ID);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<IdModel> CREATOR = new Parcelable.Creator<IdModel>() {
        @Override
        public IdModel createFromParcel(Parcel in) {
            return new IdModel(in);
        }

        @Override
        public IdModel[] newArray(int size) {
            return new IdModel[size];
        }
    };
}