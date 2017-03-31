package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class News implements Parcelable {

  public static final Creator<News> CREATOR = new Creator<News>() {
    @Override
    public News createFromParcel(Parcel in) {
      return new News(in);
    }

    @Override
    public News[] newArray(int size) {
      return new News[size];
    }
  };
  @SerializedName("id")
  private int mId;
  @SerializedName("name")
  private String mName;

  public News(int id, String name) {
    mId = id;
    mName = name;
  }

  public News(Parcel in) {
    mId = in.readInt();
    mName = in.readString();
  }

  public int getId() {
    return mId;
  }

  public String getName() {
    return mName;
  }

  @Override
  public String toString() {
    return "News {" +
        "mId=" + mId +
        ", mName='" + mName +
        '}';
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(mId);
    dest.writeString(mName);
  }
}
