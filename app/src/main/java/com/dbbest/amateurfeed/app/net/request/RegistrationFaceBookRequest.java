package com.dbbest.amateurfeed.app.net.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by antonina on 19.01.17.
 */

@SuppressWarnings("unused")
public class RegistrationFaceBookRequest implements Parcelable {

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

    public RegistrationFaceBookRequest(String code, double longitude, double latitude) {
        mCode = code;
        mLongitude = longitude;
        mLatitude = latitude;

    }


    private RegistrationFaceBookRequest(Parcel in) {
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

    public static final Creator<RegistrationFaceBookRequest> CREATOR = new Parcelable.Creator<RegistrationFaceBookRequest>() {
        @Override
        public RegistrationFaceBookRequest createFromParcel(Parcel source) {
            return new RegistrationFaceBookRequest(source);
        }

        @Override
        public RegistrationFaceBookRequest[] newArray(int size) {
            return new RegistrationFaceBookRequest[size];
        }
    };
}
