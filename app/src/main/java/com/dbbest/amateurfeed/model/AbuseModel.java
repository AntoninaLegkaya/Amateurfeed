package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;


public class AbuseModel implements Parcelable {

  public static final Creator<AbuseModel> CREATOR = new Creator<AbuseModel>() {
    @Override
    public AbuseModel createFromParcel(Parcel in) {
      return new AbuseModel(in);
    }

    @Override
    public AbuseModel[] newArray(int size) {
      return new AbuseModel[size];
    }
  };
  @SerializedName("newsId")
  private int newsId;
  @SerializedName("comment")
  private String comment;

  public AbuseModel(int newsId, String comment) {

    this.newsId = newsId;
    this.comment = comment;
  }

  private AbuseModel(Parcel in) {
    newsId = in.readInt();
    comment = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(newsId);
    dest.writeString(comment);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public int getNewsId() {
    return newsId;
  }

  public String getComment() {
    return comment;
  }
}
