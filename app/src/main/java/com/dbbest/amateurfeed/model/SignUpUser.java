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

  private String mUserEmail;
  private String mUserFullName;
  private String mAddress;
  private String mPassword;
  private String mDeviceId;
  private String mOsType;
  private String mDeviceToken;

  public SignUpUser(String userEmail, String userFullName, String adress, String password,
      String deviceId, String osType, String deviceToken) {
    mUserEmail = userEmail;
    mUserFullName = userFullName;
    mAddress = adress;
    mPassword = password;
    mDeviceId = deviceId;
    mOsType = osType;
    mDeviceToken = deviceToken;
  }

  public SignUpUser() {
    SharedPreferences preferences = preferences();
    mUserEmail = preferences.getString(KEY_EMAIL, null);
    mUserFullName = preferences.getString(KEY_FULL_NAME, null);
    mAddress = preferences.getString(KEY_ADDRESS, null);
    mPassword = preferences.getString(KEY_PASSWORD, null);
    mDeviceId = preferences.getString(KEY_DEVICE_ID, null);
    mOsType = preferences.getString(KEY_OS_TYPE, null);
    mDeviceToken = preferences.getString(KEY_DEVICE_TOKEN, null);
  }

  private SharedPreferences preferences() {
    return App.instance().getSharedPreferences(CURRENT_USER_FILE, Context.MODE_PRIVATE);
  }

  public String getUserEmail() {
    return mUserEmail;
  }

  public void setUserEmail(String userEmail) {
    mUserEmail = userEmail;
    preferences().edit().putString(KEY_EMAIL, mUserEmail).apply();
  }

  public String getUserFullName() {
    return mUserFullName;
  }

  public void setUserFullName(String userFullName) {
    mUserFullName = userFullName;
    preferences().edit().putString(KEY_FULL_NAME, mUserFullName).apply();
  }

  public String getAddress() {
    return mAddress;
  }

  public void setAddress(String address) {
    mAddress = address;
    preferences().edit().putString(KEY_ADDRESS, mAddress).apply();
  }

  public String getPassword() {
    return mPassword;
  }

  public void setPassword(String password) {
    mPassword = password;
    preferences().edit().putString(KEY_PASSWORD, mPassword).apply();
  }

  public String getDeviceId() {
    return mDeviceId;
  }

  public void setDeviceId(String deviceId) {
    mDeviceId = deviceId;
    preferences().edit().putString(KEY_DEVICE_ID, mDeviceId).apply();
  }

  public String getOsType() {
    return mOsType;
  }

  public void setOsType(String osType) {
    mOsType = osType;
    preferences().edit().putString(KEY_OS_TYPE, mOsType).apply();
  }

  public String getDeviceToken() {
    return mDeviceToken;
  }

  public void setDeviceToken(String deviceToken) {
    mDeviceToken = deviceToken;
    preferences().edit().putString(KEY_DEVICE_TOKEN, mDeviceToken).apply();
  }

  @Override
  public void purge() {
    preferences().edit().clear().apply();
    mUserEmail = null;
    mUserFullName = null;
    mAddress = null;
    mPassword = null;
    mDeviceId = null;
    mOsType = null;
    mDeviceToken = null;

  }

  @Override
  public boolean isValid() {
    return !TextUtils.isEmpty(mUserEmail) && !TextUtils.isEmpty(mUserFullName) && !TextUtils
        .isEmpty(mAddress) && !TextUtils.isEmpty(mPassword) &&
        !TextUtils.isEmpty(mDeviceId) && !TextUtils.isEmpty(mOsType) && !TextUtils
        .isEmpty(mDeviceToken);
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
        + "mUserEmail=" + mUserEmail
        + ", mUserFullName='" + mUserFullName + '\''
        + ", mAddress='" + mAddress + '\''
        + ", mPassword='" + mPassword + '\''
        + ", mDeviceId='" + mDeviceId + '\''
        + ", mOsType='" + mOsType + '\''
        + ", mDeviceToken='" + mDeviceToken + '\''
        + '}';
  }
}
