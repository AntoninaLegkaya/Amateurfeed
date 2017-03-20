package com.dbbest.amateurfeed.app.net.request;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class LoginRequestModel implements Parcelable {

  public static final Parcelable.Creator<LoginRequestModel> CREATOR =
      new Parcelable.Creator<LoginRequestModel>() {
        @Override
        public LoginRequestModel createFromParcel(Parcel source) {
          return new LoginRequestModel(source);
        }
        @Override
        public LoginRequestModel[] newArray(int size) {
          return new LoginRequestModel[size];
        }
      };
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
  @SerializedName("osType")
  private String mOsType;
  @SerializedName("deviceToken")
  private String mDeviceToken;


  public LoginRequestModel(String email, String password, String deviceId, String osType,
      String deviceToken) {
    mEmail = email;
    mPassword = password;
    mDeviceId = deviceId;
    mOsType = osType;
    mDeviceToken = deviceToken;

  }

  private LoginRequestModel(Parcel in) {
    mEmail = in.readString();
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
    dest.writeString(mPassword);
    dest.writeString(mDeviceId);
    dest.writeString(mOsType);
    dest.writeString(mDeviceToken);

  }
}