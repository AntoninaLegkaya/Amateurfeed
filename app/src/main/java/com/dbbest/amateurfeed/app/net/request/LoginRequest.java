package com.dbbest.amateurfeed.app.net.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by antonina on 19.01.17.
 */

@SuppressWarnings("unused")
public class LoginRequest implements Parcelable {

    @SerializedName("email")
    private String mEmail;

    @SerializedName("password")
    private String mPassword;

    @SerializedName("longitude")
    private double mLongitude;

    @SerializedName("latitude")
    private double mLatitude;


    public LoginRequest(String email, String password, double longitude, double latitude) {
        mEmail = email;
        mPassword = password;
        mLongitude = longitude;
        mLatitude = latitude;
    }


    private LoginRequest(Parcel in) {
        mEmail = in.readString();
        mPassword = in.readString();
        mLongitude = in.readDouble();
        mLatitude = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mEmail);
        dest.writeString(mPassword);
        dest.writeDouble(mLongitude);
        dest.writeDouble(mLatitude);
    }

    public static final Parcelable.Creator<LoginRequest> CREATOR = new Parcelable.Creator<LoginRequest>() {
        @Override
        public LoginRequest createFromParcel(Parcel source) {
            return new LoginRequest(source);
        }

        @Override
        public LoginRequest[] newArray(int size) {
            return new LoginRequest[size];
        }
    };
}