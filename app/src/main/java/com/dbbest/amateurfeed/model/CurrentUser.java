package com.dbbest.amateurfeed.model;

import android.common.util.TextUtils;
import android.content.Context;
import android.content.SharedPreferences;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.ui.navigator.NotImplementedException;


public class CurrentUser implements ActiveRecord<CurrentUser> {

  private static final String CURRENT_USER_FILE = ".user";
  private static final String KEY_ID = "USER_ID";
  private static final String KEY_NAME = "USER_NAME";
  private static final String KEY_ROLE = "ROLE";
  private static final String KEY_PROFILE_IMAGE = "PROFILE_IMAGE";

  private int userId;
  private String userName;
  private String role;
  private String profileImage;

  public CurrentUser() {
    SharedPreferences preferences = preferences();
    userId = preferences.getInt(KEY_ID, -1);
    userName = preferences.getString(KEY_NAME, null);
    role = preferences.getString(KEY_ROLE, null);
    profileImage = preferences.getString(KEY_PROFILE_IMAGE, null);
  }

  @Override
  public void purge() {
    preferences().edit().clear().apply();
    userId = -1;
    userName = null;
    role = null;
    profileImage = null;

  }

  @Override
  public boolean isValid() {
    return userId != -1 && !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(role);
  }

  @Override
  public void subscribeChanges(OnRecordChangeListener<CurrentUser> listener) {
    throw new NotImplementedException("subscribe changes");
  }

  @Override
  public void unsubscribeChanges() {
    throw new NotImplementedException("unsubscribe changes");
  }

  @Override
  public String toString() {
    return "CurrentUser{"
        + "userId=" + userId
        + ", userName='" + userName + '\''
        + ", role='" + role + '\''
        + ", profileImage='" + profileImage + '\''
        + '}';
  }

  public int id() {
    return userId;
  }

  public void setId(int userId) {
    this.userId = userId;
    preferences().edit().putInt(KEY_ID, userId).apply();
  }

  public String name() {
    return userName;
  }

  public void setName(String userName) {
    this.userName = userName;
    preferences().edit().putString(KEY_NAME, userName).apply();
  }

  public String role() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
    preferences().edit().putString(KEY_ROLE, role).apply();
  }

  public String profileImage() {
    return profileImage;
  }

  public void setProfileImage(String profileImage) {
    this.profileImage = profileImage;
    preferences().edit().putString(KEY_PROFILE_IMAGE, profileImage).apply();
  }

  private SharedPreferences preferences() {
    return App.instance().getSharedPreferences(CURRENT_USER_FILE, Context.MODE_PRIVATE);
  }
}
