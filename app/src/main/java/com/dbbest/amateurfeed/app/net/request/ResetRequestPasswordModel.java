package com.dbbest.amateurfeed.app.net.request;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class ResetRequestPasswordModel implements Parcelable {

  public static final Parcelable.Creator<ResetRequestPasswordModel> CREATOR =
      new Parcelable.Creator<ResetRequestPasswordModel>() {
    @Override
    public ResetRequestPasswordModel createFromParcel(Parcel source) {
      return new ResetRequestPasswordModel(source);
    }

    @Override
    public ResetRequestPasswordModel[] newArray(int size) {
      return new ResetRequestPasswordModel[size];
    }
  };
  @SerializedName("email")
  private String mEmail;


  public ResetRequestPasswordModel(String email) {
    mEmail = email;

  }

  private ResetRequestPasswordModel(Parcel in) {
    mEmail = in.readString();

  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mEmail);

  }
}
