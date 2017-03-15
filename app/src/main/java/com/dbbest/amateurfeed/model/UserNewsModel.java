package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;


public class UserNewsModel implements Parcelable {

  public static final Creator<UserNewsModel> CREATOR = new Creator<UserNewsModel>() {
    @Override
    public UserNewsModel createFromParcel(Parcel in) {
      return new UserNewsModel(in);
    }

    @Override
    public UserNewsModel[] newArray(int size) {
      return new UserNewsModel[size];
    }
  };

  @SerializedName("id")
  private int mId;

  @SerializedName("title")
  private String mTitle;

  @SerializedName("updateDate")
  private String mUpdateDate;


  @SerializedName("status")
  private String mStatus;

  @SerializedName("image")
  private String mImage;

  @SerializedName("likes")
  private int mLikes;

  public UserNewsModel(int id, String title, String updateDate, String status, String image,
      int likes) {
    mId = id;
    mTitle = title;
    mUpdateDate = updateDate;
    mStatus = status;
    mImage = image;
    mLikes = likes;
  }

  private UserNewsModel(Parcel in) {
    mId = in.readInt();
    mTitle = in.readString();
    mUpdateDate = in.readString();
    mStatus = in.readString();
    mImage = in.readString();
    mLikes = in.readInt();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(mId);
    dest.writeString(mTitle);
    dest.writeString(mUpdateDate);
    dest.writeString(mStatus);
    dest.writeString(mImage);
    dest.writeInt(mLikes);
  }

  public int getId() {
    return mId;
  }

  public String getTitle() {
    return mTitle;
  }

  public String getUpdateDate() {
    return mUpdateDate;
  }

  public String getStatus() {
    return mStatus;
  }

  public String getImage() {
    return mImage;
  }

  public int getLikes() {
    return mLikes;
  }

  @Override
  public int describeContents() {
    return 0;
  }


}
