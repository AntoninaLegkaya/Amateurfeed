package com.dbbest.amateurfeed.app.net.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by antonina on 19.01.17.
 */

@SuppressWarnings("unused")
public class RegistrationRequest implements Parcelable {

    @SerializedName("email")
    private String mEmail;

    @SerializedName("password")
    private String mPassword;

    @SerializedName("firstName")
    private String mFirstName;

    @SerializedName("lastName")
    private String mLastName;

    @SerializedName("longitude")
    private double mLongitude;

    @SerializedName("latitude")
    private double mLatitude;


    public RegistrationRequest(String email, String password, double longitude, double latitude) {
        mEmail = email;
        mPassword = password;
        mLongitude = longitude;
        mLatitude = latitude;
    }

    public RegistrationRequest(String email, String firstName, String lastName, String password) {
        mEmail = email;
        mPassword = password;
        mFirstName = firstName;
        mLastName = lastName;
    }


    private RegistrationRequest(Parcel in) {
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
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
        dest.writeDouble(mLongitude);
        dest.writeDouble(mLatitude);
    }

    public static final Parcelable.Creator<RegistrationRequest> CREATOR = new Parcelable.Creator<RegistrationRequest>() {
        @Override
        public RegistrationRequest createFromParcel(Parcel source) {
            return new RegistrationRequest(source);
        }

        @Override
        public RegistrationRequest[] newArray(int size) {
            return new RegistrationRequest[size];
        }
    };
}
