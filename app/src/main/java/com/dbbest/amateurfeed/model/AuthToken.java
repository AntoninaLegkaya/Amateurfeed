package com.dbbest.amateurfeed.model;

import android.common.util.TextUtils;
import android.content.Context;
import android.content.SharedPreferences;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.ui.navigator.NotImplementedException;

public class AuthToken implements ActiveRecord<AuthToken> {

  private static final String AUTH_FILE_NAME = ".auth";
  private static final String KEY_AUTH_TOKEN = "AUTH_TOKEN";
  private static final String KEY_FCM_AUTH_TOKEN = "FCM_TOKEN";
  private static final String KEY_DEVICE_ID = "DEVICE_ID";
  private static final String KEY_DEVICE_OS = "DEVICE_OS";
  private static final String BEARER = "Bearer ";

  public AuthToken() {
  }

  @Override
  public void purge() {
    update(null);
  }

  @Override
  public boolean isValid() {
    String token = readToken();
    return !TextUtils.isEmpty(token);
  }

  @Override
  public void subscribeChanges(OnRecordChangeListener<AuthToken> listener) {
    throw new NotImplementedException("subscribe changes");
  }

  @Override
  public void unsubscribeChanges() {
    throw new NotImplementedException("subscribe changes");
  }

  @Override
  public String toString() {
    String token = readToken();
    String fcmToken = readFcmToken();
    String deviceID = readDeviceId();
    String deviceOs = readDeviceOs();
    return "AccessToken{"
        + "mToken='" + token + '\n'
        + "fcmToken='" + fcmToken + '\n'
        + "deviceID='" + deviceID + '\n'
        + "deviceOs='" + deviceOs + '\n'
        + '}';
  }

  public String value() {
    return readToken();
  }

  public String bearer() {
    String token = readToken();
    if (TextUtils.isEmpty(token)) {
      return null;
    }
    return BEARER + token;
  }

  public String getDeviceID() {

    return readDeviceId();
  }

  public String getDeviceOs() {

    return readDeviceOs();
  }

  public String getFcmToken() {

    return readFcmToken();
  }

  public void update(String token) {
    writeToken(token);
  }

  public void updateFcmToken(String token) {
    writeFcmToken(token);
  }

  public void updateDeviceId(String id) {
    writeDeviceId(id);
  }

  public void updateDeviceOs(String os) {
    writeDeviceOs(os);
  }

  private String readToken() {
    if (preferences() != null) {
      return preferences().getString(KEY_AUTH_TOKEN, null);
    }
    return null;
  }

  private void writeFcmToken(String value) {
    preferences().edit().putString(KEY_FCM_AUTH_TOKEN, value).apply();
  }


  private String readFcmToken() {
    if (preferences() != null) {
      return preferences().getString(KEY_FCM_AUTH_TOKEN, null);
    }
    return null;
  }

  private void writeDeviceId(String value) {
    preferences().edit().putString(KEY_DEVICE_ID, value).apply();
  }


  private String readDeviceId() {
    if (preferences() != null) {
      return preferences().getString(KEY_DEVICE_ID, null);
    }
    return null;
  }

  private void writeDeviceOs(String value) {
    preferences().edit().putString(KEY_DEVICE_OS, value).apply();
  }

  private String readDeviceOs() {
    if (preferences() != null) {
      return preferences().getString(KEY_DEVICE_OS, null);
    }
    return null;
  }

  private void writeToken(String value) {
    preferences().edit().putString(KEY_AUTH_TOKEN, value).apply();
  }

  private SharedPreferences preferences() {
    if (App.instance() != null) {
      return App.instance().getSharedPreferences(AUTH_FILE_NAME, Context.MODE_PRIVATE);
    }
    return null;

  }
}
