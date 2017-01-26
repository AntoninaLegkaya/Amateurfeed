package com.dbbest.amateurfeed.app.net.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by antonina on 19.01.17.
 */

@SuppressWarnings("unused")
public class RegistrationRequestModel implements Parcelable {

    @SerializedName("email")
    private String mEmail;

    @SerializedName("fullName")
    private String mFullName;

    @SerializedName("phone")
    private String mPhone;

    @SerializedName("address")
    private String mAddress;

    @SerializedName("password")
    private String mPassword;

    @SerializedName("deviceId")
    private String mDeviceId;

    //    'Ios','Android','Unknown',
    @SerializedName("osType")
    private String mOsType;

    @SerializedName("deviceToken")
    private String mDeviceToken;


    public RegistrationRequestModel(String email, String fullName, String phone, String address, String password, String deviceId, String osType, String deviceToken) {
        mEmail = email;
        mFullName = fullName;
        mPhone = phone;
        mAddress = address;
        mPassword = password;
        mDeviceId = deviceId;
        mOsType = osType;
        mDeviceToken = deviceToken;
    }



    private RegistrationRequestModel(Parcel in) {
        mEmail = in.readString();
        mFullName = in.readString();
        mPhone = in.readString();
        mAddress = in.readString();
        mPassword = in.readString();
        mDeviceId = in.readString();
        mOsType = in.readString();
        mDeviceToken = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mEmail);
        dest.writeString(mFullName);
        dest.writeString(mPhone);
        dest.writeString(mAddress);
        dest.writeString(mPassword);
        dest.writeString(mDeviceId);
        dest.writeString(mOsType);
        dest.writeString(mDeviceToken);
    }

    public static final Parcelable.Creator<RegistrationRequestModel> CREATOR = new Parcelable.Creator<RegistrationRequestModel>() {
        @Override
        public RegistrationRequestModel createFromParcel(Parcel source) {
            return new RegistrationRequestModel(source);
        }

        @Override
        public RegistrationRequestModel[] newArray(int size) {
            return new RegistrationRequestModel[size];
        }
    };
}
