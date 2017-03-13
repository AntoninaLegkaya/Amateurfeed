package com.dbbest.amateurfeed.app.net.request;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;


public class LikeModel implements Parcelable {

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<LikeModel> CREATOR = new Parcelable.Creator<LikeModel>() {
    @Override
    public LikeModel createFromParcel(Parcel in) {
      return new LikeModel(in);
    }

    @Override
    public LikeModel[] newArray(int size) {
      return new LikeModel[size];
    }
  };
  @SerializedName("isLike")
  private boolean isLike;

  public LikeModel(boolean isLike) {
    this.isLike = isLike;
  }

  protected LikeModel(Parcel in) {
    isLike = in.readByte() != 0x00;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeByte((byte) (isLike ? 0x01 : 0x00));
  }

  public boolean isLike() {
    return isLike;
  }
}
