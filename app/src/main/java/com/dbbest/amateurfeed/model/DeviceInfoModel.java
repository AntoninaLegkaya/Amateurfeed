package com.dbbest.amateurfeed.model;

import com.google.gson.annotations.SerializedName;


public class DeviceInfoModel {

  @SerializedName("osTypr")
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

  public String getOsType() {
    return mOsType;
  }

  public String getDeviceToken() {
    return mDeviceToken;
  }

  public String getDeviceId() {
    return mDeviceId;
  }
}
