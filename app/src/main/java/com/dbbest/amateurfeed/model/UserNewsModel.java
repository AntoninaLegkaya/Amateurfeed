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
  private int id;

  @SerializedName("title")
  private String title;

  @SerializedName("updateDate")
  private String updateDate;


  @SerializedName("status")
  private String status;

  @SerializedName("image")
  private String image;

  @SerializedName("likes")
  private int likes;

  public UserNewsModel(int id, String title, String updateDate, String status, String image,
      int likes) {
    this.id = id;
    this.title = title;
    this.updateDate = updateDate;
    this.status = status;
    this.image = image;
    this.likes = likes;
  }

  private UserNewsModel(Parcel in) {
    id = in.readInt();
    title = in.readString();
    updateDate = in.readString();
    status = in.readString();
    image = in.readString();
    likes = in.readInt();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeString(title);
    dest.writeString(updateDate);
    dest.writeString(status);
    dest.writeString(image);
    dest.writeInt(likes);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public int getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getUpdateDate() {
    return updateDate;
  }

  public String getStatus() {
    return status;
  }

  public String getImage() {
    return image;
  }

  public int getLikes() {
    return likes;
  }


}
