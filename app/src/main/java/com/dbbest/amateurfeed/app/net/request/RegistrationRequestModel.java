package com.dbbest.amateurfeed.app.net.request;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class RegistrationRequestModel implements Parcelable {

  public static final Parcelable.Creator<RegistrationRequestModel> CREATOR =
      new Parcelable.Creator<RegistrationRequestModel>() {
        @Override
        public RegistrationRequestModel createFromParcel(Parcel source) {
          return new RegistrationRequestModel(source);
        }

        @Override
        public RegistrationRequestModel[] newArray(int size) {
          return new RegistrationRequestModel[size];
        }
      };
  @SerializedName("email")
  private String email;
  @SerializedName("fullName")
  private String fullName;
  @SerializedName("phone")
  private String phone;
  @SerializedName("address")
  private String address;
  @SerializedName("password")
  private String password;
  @SerializedName("deviceId")
  private String deviceId;
  @SerializedName("osType")
  private String osType;
  @SerializedName("deviceToken")
  private String deviceToken;


  public RegistrationRequestModel(String email, String fullName, String phone, String address,
      String password, String deviceId, String osType, String deviceToken) {
    this.email = email;
    this.fullName = fullName;
    this.phone = phone;
    this.address = address;
    this.password = password;
    this.deviceId = deviceId;
    this.osType = osType;
    this.deviceToken = deviceToken;
  }

  private RegistrationRequestModel(Parcel in) {
    email = in.readString();
    fullName = in.readString();
    phone = in.readString();
    address = in.readString();
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
    dest.writeString(fullName);
    dest.writeString(phone);
    dest.writeString(address);
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
