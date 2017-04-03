package com.dbbest.amateurfeed.model;

import android.common.util.TextUtils;
import android.content.Context;
import android.content.SharedPreferences;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.ui.navigator.NotImplementedException;


public class SignUpUser implements ActiveRecord<SignUpUser> {

  private static final String CURRENT_USER_FILE = ".user";
  private static final String KEY_EMAIL = "USER_EMAIL";
  private static final String KEY_FULL_NAME = "USER_FULL_NAME";
  private static final String KEY_ADDRESS = "USER_ADDRESS";
  private static final String KEY_PASSWORD = "USER_PASSWORD";
  private static final String KEY_DEVICE_ID = "DEVICE_ID";
  private static final String KEY_OS_TYPE = "USER_OS_TYPE";
  private static final String KEY_DEVICE_TOKEN = "DEVICE_TOKEN";

  private String userEmail;
  private String userFullName;
  private String address;
  private String password;
  private String deviceId;
  private String osType;
  private String deviceToken;

  public SignUpUser(String userEmail, String userFullName, String adress, String password,
      String deviceId, String osType, String deviceToken) {
    this.userEmail = userEmail;
    this.userFullName = userFullName;
    address = adress;
    this.password = password;
    this.deviceId = deviceId;
    this.osType = osType;
    this.deviceToken = deviceToken;
  }

  public SignUpUser() {
    SharedPreferences preferences = preferences();
    userEmail = preferences.getString(KEY_EMAIL, null);
    userFullName = preferences.getString(KEY_FULL_NAME, null);
    address = preferences.getString(KEY_ADDRESS, null);
    password = preferences.getString(KEY_PASSWORD, null);
    deviceId = preferences.getString(KEY_DEVICE_ID, null);
    osType = preferences.getString(KEY_OS_TYPE, null);
    deviceToken = preferences.getString(KEY_DEVICE_TOKEN, null);
  }

  @Override
  public void purge() {
    preferences().edit().clear().apply();
    userEmail = null;
    userFullName = null;
    address = null;
    password = null;
    deviceId = null;
    osType = null;
    deviceToken = null;

  }

  @Override
  public boolean isValid() {
    return !TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userFullName) && !TextUtils
        .isEmpty(address) && !TextUtils.isEmpty(password) &&
        !TextUtils.isEmpty(deviceId) && !TextUtils.isEmpty(osType) && !TextUtils
        .isEmpty(deviceToken);
  }

  @Override
  public void subscribeChanges(OnRecordChangeListener<SignUpUser> listener) {
    throw new NotImplementedException("subscribe changes");
  }

  @Override
  public void unsubscribeChanges() {
    throw new NotImplementedException("unsubscribe changes");
  }

  @Override
  public String toString() {
    return "CurrentUser{"
        + "userEmail=" + userEmail
        + ", userFullName='" + userFullName + '\''
        + ", address='" + address + '\''
        + ", password='" + password + '\''
        + ", deviceId='" + deviceId + '\''
        + ", osType='" + osType + '\''
        + ", deviceToken='" + deviceToken + '\''
        + '}';
  }

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
    preferences().edit().putString(KEY_EMAIL, this.userEmail).apply();
  }

  public String getUserFullName() {
    return userFullName;
  }

  public void setUserFullName(String userFullName) {
    this.userFullName = userFullName;
    preferences().edit().putString(KEY_FULL_NAME, this.userFullName).apply();
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
    preferences().edit().putString(KEY_ADDRESS, this.address).apply();
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
    preferences().edit().putString(KEY_PASSWORD, this.password).apply();
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
    preferences().edit().putString(KEY_DEVICE_ID, this.deviceId).apply();
  }

  public String getOsType() {
    return osType;
  }

  public void setOsType(String osType) {
    this.osType = osType;
    preferences().edit().putString(KEY_OS_TYPE, this.osType).apply();
  }

  public String getDeviceToken() {
    return deviceToken;
  }

  public void setDeviceToken(String deviceToken) {
    this.deviceToken = deviceToken;
    preferences().edit().putString(KEY_DEVICE_TOKEN, this.deviceToken).apply();
  }

  private SharedPreferences preferences() {
    return App.instance().getSharedPreferences(CURRENT_USER_FILE, Context.MODE_PRIVATE);
  }
}
