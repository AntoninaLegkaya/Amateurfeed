package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;


public class DeviceInfoModel implements Parcelable {

  @SerializedName("osType")
  private String mOsType;

  @SerializedName("deviceToken")
  private String mDeviceToken;

  @SerializedName("deviceId")
  private String mDeviceId;

  public DeviceInfoModel(String osType, String deviceToken, String deviceId) {
    mOsType = osType;
    mDeviceToken = deviceToken;
    mDeviceId = deviceId;
  }

  protected DeviceInfoModel(Parcel in) {
    mOsType = in.readString();
    mDeviceToken = in.readString();
    mDeviceId = in.readString();
  }

  public static final Creator<DeviceInfoModel> CREATOR = new Creator<DeviceInfoModel>() {
    @Override
    public DeviceInfoModel createFromParcel(Parcel in) {
      return new DeviceInfoModel(in);
    }

    @Override
    public DeviceInfoModel[] newArray(int size) {
      return new DeviceInfoModel[size];
    }
  };

  public String getOsType() {
    return mOsType;
  }

  public String getDeviceToken() {
    return mDeviceToken;
  }

  public String getDeviceId() {
    return mDeviceId;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mOsType);
    dest.writeString(mDeviceToken);
    dest.writeString(mDeviceId);
  }
}
