package com.dbbest.amateurfeed.model;

import com.google.gson.annotations.SerializedName;


public class UserSettingsModel {

  @SerializedName("enablePush")
  private boolean enablePush;

  public UserSettingsModel(boolean enablePush) {
    this.enablePush = enablePush;
  }

  public boolean isEnablePush() {
    return enablePush;
  }
}
