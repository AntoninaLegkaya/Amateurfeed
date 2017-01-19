package com.dbbest.amateurfeed.app.net.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by antonina on 19.01.17.
 */

public class ResetRequest implements Parcelable {

    @SerializedName("email")
    private String mEmail;


    public ResetRequest(String email) {
        mEmail = email;

    }


    private ResetRequest(Parcel in) {
        mEmail = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mEmail);

    }

    public static final Parcelable.Creator<ResetRequest> CREATOR = new Parcelable.Creator<ResetRequest>() {
        @Override
        public ResetRequest createFromParcel(Parcel source) {
            return new ResetRequest(source);
        }

        @Override
        public ResetRequest[] newArray(int size) {
            return new ResetRequest[size];
        }
    };
}
