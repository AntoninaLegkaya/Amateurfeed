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
  private String email;
  @SerializedName("password")
  private String password;
  @SerializedName("longitude")
  private double longitude;
  @SerializedName("latitude")
  private double latitude;
  @SerializedName("deviceId")
  private String deviceId;
  @SerializedName("osType")
  private String osType;
  @SerializedName("deviceToken")
  private String deviceToken;


  public LoginRequestModel(String email, String password, String deviceId, String osType,
      String deviceToken) {
    this.email = email;
    this.password = password;
    this.deviceId = deviceId;
    this.osType = osType;
    this.deviceToken = deviceToken;

  }

  private LoginRequestModel(Parcel in) {
    email = in.readString();
    password = in.readString();
    deviceId = in.readString();
    osType = in.readString();
    deviceToken = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(email);
    dest.writeString(password);
    dest.writeString(deviceId);
    dest.writeString(osType);
    dest.writeString(deviceToken);

  }

  public String getDeviceId() {
    return deviceId;
  }

  public String getOsType() {
    return osType;
  }

  public String getDeviceToken() {
    return deviceToken;
  }
}