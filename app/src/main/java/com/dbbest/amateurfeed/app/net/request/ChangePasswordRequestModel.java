package com.dbbest.amateurfeed.app.net.request;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;


public class ChangePasswordRequestModel implements Parcelable {

  public static final Parcelable.Creator<ChangePasswordRequestModel> CREATOR =
      new Parcelable.Creator<ChangePasswordRequestModel>() {
    @Override
    public ChangePasswordRequestModel createFromParcel(Parcel source) {
      return new ChangePasswordRequestModel(source);
    }

    @Override
    public ChangePasswordRequestModel[] newArray(int size) {
      return new ChangePasswordRequestModel[size];
    }
  };
  @SerializedName("currentPassword")
  private String mCurrentPassword;
  @SerializedName("password")
  private String mPassword;
  @SerializedName("confirmPassword")
  private String mConfirmPassword;


  public ChangePasswordRequestModel(String currentPassword, String password,
      String confirmPassword) {
    mCurrentPassword = currentPassword;
    mConfirmPassword = confirmPassword;
    mPassword = password;

  }

  private ChangePasswordRequestModel(Parcel in) {
    mCurrentPassword = in.readString();
    mPassword = in.readString();
    mConfirmPassword = in.readString();

  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mCurrentPassword);
    dest.writeString(mPassword);
    dest.writeString(mConfirmPassword);

  }
}
