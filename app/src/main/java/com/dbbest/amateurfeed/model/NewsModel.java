package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class NewsModel implements Parcelable {

  public static final Creator<NewsModel> CREATOR = new Creator<NewsModel>() {
    @Override
    public NewsModel createFromParcel(Parcel in) {
      return new NewsModel(in);
    }

    @Override
    public NewsModel[] newArray(int size) {
      return new NewsModel[size];
    }
  };
  @SerializedName("id")
  private int mId;
  @SerializedName("name")
  private String mName;

  public NewsModel(int id, String name) {
    mId = id;
    mName = name;
  }

  public NewsModel(Parcel in) {
    mId = in.readInt();
    mName = in.readString();
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

  public int getId() {
    return mId;
  }

  public String getName() {
    return mName;
  }
}
