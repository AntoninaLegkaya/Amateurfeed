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

    @SerializedName("deviceId")
    private String mDeviceId;

    //    'Ios','Android','Unknown',
    @SerializedName("osType")
    private String mOsType;

    @SerializedName("deviceToken")
    private String mDeviceToken;


    public LoginRequest(String email, String password,String deviceId, String osType,String deviceToken) {
        mEmail = email;
        mPassword = password;
        mDeviceId = deviceId;
        mOsType = osType;
        mDeviceToken=deviceToken;

    }


    private LoginRequest(Parcel in) {
        mEmail = in.readString();
        mPassword = in.readString();
        mDeviceId = in.readString();
        mOsType = in.readString();
        mDeviceToken=in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mEmail);
        dest.writeString(mPassword);
        dest.writeString(mDeviceId);
        dest.writeString(mOsType);
        dest.writeString(mDeviceToken);

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