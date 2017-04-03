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
  private String currentPassword;
  @SerializedName("password")
  private String password;
  @SerializedName("confirmPassword")
  private String confirmPassword;


  public ChangePasswordRequestModel(String currentPassword, String password,
      String confirmPassword) {
    this.currentPassword = currentPassword;
    this.confirmPassword = confirmPassword;
    this.password = password;

  }

  private ChangePasswordRequestModel(Parcel in) {
    currentPassword = in.readString();
    password = in.readString();
    confirmPassword = in.readString();

  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(currentPassword);
    dest.writeString(password);
    dest.writeString(confirmPassword);

  }
}
