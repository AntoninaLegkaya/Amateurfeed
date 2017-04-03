package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class UserFeedCreatorExpand implements Parcelable {

  public static final Creator<UserFeedCreatorExpand> CREATOR =
      new Creator<UserFeedCreatorExpand>() {
        @Override
        public UserFeedCreatorExpand createFromParcel(Parcel in) {
          return new UserFeedCreatorExpand(in);
        }

        @Override
        public UserFeedCreatorExpand[] newArray(int size) {
          return new UserFeedCreatorExpand[size];
        }
      };
  @SerializedName("id")
  private int id;
  @SerializedName("name")
  private String name;
  @SerializedName("isAdmin")
  private boolean isAdmin;
  @SerializedName("image")
  private String image;
  @SerializedName("email")
  private String email;

  public UserFeedCreatorExpand(String name, int id, boolean isAdmin, String email, String image) {
    this.id = id;
    this.name = name;
    this.isAdmin = isAdmin;
    this.image = image;
    this.email = email;
  }

  private UserFeedCreatorExpand(Parcel in) {
    id = in.readInt();
    name = in.readString();
    isAdmin = in.readByte() != 0;
    image = in.readString();
    email = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeString(name);
    dest.writeByte((byte) (isAdmin ? 1 : 0));
    dest.writeString(image);
    dest.writeString(email);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public String toString() {
    return "UserFeedCreator {" +
        "id=" + id +
        ", name='" + name + '\n' +
        ", isAdmin='" + isAdmin + '\n' +
        ", isAdmin='" + isAdmin + '\n' +
        ", email='" + email + '\n' +
        '}';
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public boolean isAdmin() {
    return isAdmin;
  }

  public String getImage() {
    return image;
  }

  public String getEmail() {
    return email;
  }
}
