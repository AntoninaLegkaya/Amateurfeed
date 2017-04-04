package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;


public class DeviceInfoModel implements Parcelable {

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
  @SerializedName("osType")
  private String osType;
  @SerializedName("deviceToken")
  private String deviceToken;
  @SerializedName("deviceId")
  private String deviceId;

  public DeviceInfoModel(String osType, String deviceToken, String deviceId) {
    this.osType = osType;
    this.deviceToken = deviceToken;
    this.deviceId = deviceId;
  }

  private DeviceInfoModel(Parcel in) {
    osType = in.readString();
    deviceToken = in.readString();
    deviceId = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(osType);
    dest.writeString(deviceToken);
    dest.writeString(deviceId);
  }

  public String getOsType() {
    return osType;
  }

  public String getDeviceToken() {
    return deviceToken;
  }

  public String getDeviceId() {
    return deviceId;
  }
}
