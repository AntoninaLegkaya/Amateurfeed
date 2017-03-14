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

  private int mUserId;
  private String mUserName;
  private String mRole;
  private String mProfileImage;

  public CurrentUser() {
    SharedPreferences preferences = preferences();
    mUserId = preferences.getInt(KEY_ID, -1);
    mUserName = preferences.getString(KEY_NAME, null);
    mRole = preferences.getString(KEY_ROLE, null);
    mProfileImage = preferences.getString(KEY_PROFILE_IMAGE, null);


  }

  public int id() {
    return mUserId;
  }

  public void setId(int userId) {
    mUserId = userId;
    preferences().edit().putInt(KEY_ID, userId).apply();
  }

  public String name() {
    return mUserName;
  }

  public void setName(String userName) {
    mUserName = userName;
    preferences().edit().putString(KEY_NAME, userName).apply();
  }

  public String role() {
    return mRole;
  }

  public void setRole(String role) {
    mRole = role;
    preferences().edit().putString(KEY_ROLE, role).apply();
  }

  public String profileImage() {
    return mProfileImage;
  }

  public void setProfileImage(String profileImage) {
    mProfileImage = profileImage;
    preferences().edit().putString(KEY_PROFILE_IMAGE, profileImage).apply();
  }


  @Override
  public void purge() {
    preferences().edit().clear().apply();
    mUserId = -1;
    mUserName = null;
    mRole = null;
    mProfileImage = null;

  }

  @Override
  public boolean isValid() {
    return mUserId != -1 && !TextUtils.isEmpty(mUserName) && !TextUtils.isEmpty(mRole);
  }

  @Override
  public void subscribeChanges(OnRecordChangeListener<CurrentUser> listener) {
    throw new NotImplementedException("subscribe changes");
  }

  @Override
  public void unsubscribeChanges() {
    throw new NotImplementedException("unsubscribe changes");
  }

  private SharedPreferences preferences() {
    return App.instance().getSharedPreferences(CURRENT_USER_FILE, Context.MODE_PRIVATE);
  }

  @Override
  public String toString() {
    return "CurrentUser{"
        + "mUserId=" + mUserId
        + ", mUserName='" + mUserName + '\''
        + ", mRole='" + mRole + '\''
        + ", mProfileImage='" + mProfileImage + '\''
        + '}';
  }
}
