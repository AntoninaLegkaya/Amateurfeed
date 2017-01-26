package com.dbbest.amateurfeed.app.net.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by antonina on 19.01.17.
 */

@SuppressWarnings("unused")
public class RegistrationFaceBookRequestModel implements Parcelable {

    @SerializedName("code")
    private String mCode;

    @SerializedName("longitude")
    private double mLongitude;

    @SerializedName("latitude")
    private double mLatitude;

    @SerializedName("deviceId")
    private String mDeviceId;

    @SerializedName("osType")
    private int mOsType;

    @SerializedName("deviceToken")
    private String mDeviceToken;

    public RegistrationFaceBookRequestModel(String code, double longitude, double latitude) {
        mCode = code;
        mLongitude = longitude;
        mLatitude = latitude;

    }


    private RegistrationFaceBookRequestModel(Parcel in) {
        mCode = in.readString();
        mLongitude = in.readDouble();
        mLatitude = in.readDouble();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCode);
        dest.writeDouble(mLongitude);
        dest.writeDouble(mLatitude);
    }

    public static final Creator<RegistrationFaceBookRequestModel> CREATOR = new Parcelable.Creator<RegistrationFaceBookRequestModel>() {
        @Override
        public RegistrationFaceBookRequestModel createFromParcel(Parcel source) {
            return new RegistrationFaceBookRequestModel(source);
        }

        @Override
        public RegistrationFaceBookRequestModel[] newArray(int size) {
            return new RegistrationFaceBookRequestModel[size];
        }
    };
}
