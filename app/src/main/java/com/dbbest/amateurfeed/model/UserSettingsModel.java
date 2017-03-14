package com.dbbest.amateurfeed.model;

import com.google.gson.annotations.SerializedName;


public class UserSettingsModel {

  @SerializedName("enablePush")
  private boolean mEnablePush;

  public UserSettingsModel(boolean enablePush) {
    mEnablePush = enablePush;
  }

  public boolean isEnablePush() {
    return mEnablePush;
  }
}
