package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;


public class UserSettingsModel implements Parcelable

{

  public static final Parcelable.Creator<UserSettingsModel> CREATOR = new Parcelable.Creator<UserSettingsModel>() {
    @Override
    public UserSettingsModel createFromParcel(Parcel in) {
      return new UserSettingsModel(in);
    }

    @Override
    public UserSettingsModel[] newArray(int size) {
      return new UserSettingsModel[size];
    }
  };
  @SerializedName("enablePush")
  private boolean enablePush;


  public UserSettingsModel(boolean isPush) {
    this.enablePush = isPush;
  }

  private UserSettingsModel(Parcel in) {
    enablePush = in.readByte() != 0x00;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeByte((byte) (enablePush ? 0x01 : 0x00));
  }

}
