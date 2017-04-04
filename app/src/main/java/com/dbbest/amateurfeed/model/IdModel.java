package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;


public class IdModel implements Parcelable {

  public static final Parcelable.Creator<IdModel> CREATOR = new Parcelable.Creator<IdModel>() {
    @Override
    public IdModel createFromParcel(Parcel in) {
      return new IdModel(in);
    }

    @Override
    public IdModel[] newArray(int size) {
      return new IdModel[size];
    }
  };
  @SerializedName("id")
  private long id;

  public IdModel(Parcel in) {
    id = in.readInt();
  }

  public IdModel(long id) {
    this.id = id;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeLong(id);
  }

  public long getId() {
    return id;
  }
}