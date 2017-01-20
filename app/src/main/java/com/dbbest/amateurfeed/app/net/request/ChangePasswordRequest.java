package com.dbbest.amateurfeed.app.net.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by antonina on 20.01.17.
 */

public class ChangePasswordRequest implements Parcelable {

    @SerializedName("currentPassword")
    private String mCurrentPassword;

    @SerializedName("password")
    private String mPassword;

    @SerializedName("confirmPassword")
    private String mConfirmPassword;


    public ChangePasswordRequest(String currentPassword, String password, String confirmPassword) {
        mCurrentPassword = currentPassword;
        mConfirmPassword = confirmPassword;
        mPassword = password;

    }


    private ChangePasswordRequest(Parcel in) {
        mCurrentPassword = in.readString();
        mPassword = in.readString();
        mConfirmPassword = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCurrentPassword);
        dest.writeString(mPassword);
        dest.writeString(mConfirmPassword);

    }

    public static final Parcelable.Creator<ChangePasswordRequest> CREATOR = new Parcelable.Creator<ChangePasswordRequest>() {
        @Override
        public ChangePasswordRequest createFromParcel(Parcel source) {
            return new ChangePasswordRequest(source);
        }

        @Override
        public ChangePasswordRequest[] newArray(int size) {
            return new ChangePasswordRequest[size];
        }
    };
}
