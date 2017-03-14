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
  private int mId;
  @SerializedName("name")
  private String mName;
  @SerializedName("isAdmin")
  private boolean mIsAdmin;
  @SerializedName("image")
  private String mImage;
  @SerializedName("email")
  private String mEmail;

  public UserFeedCreatorExpand(String name, int id, boolean isAdmin, String email, String image) {
    mId = id;
    mName = name;
    mIsAdmin = isAdmin;
    mImage = image;
    mEmail = email;
  }

  protected UserFeedCreatorExpand(Parcel in) {
    mId = in.readInt();
    mName = in.readString();
    mIsAdmin = in.readByte() != 0;
    mImage = in.readString();
    mEmail = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(mId);
    dest.writeString(mName);
    dest.writeByte((byte) (mIsAdmin ? 1 : 0));
    dest.writeString(mImage);
    dest.writeString(mEmail);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public int getId() {
    return mId;
  }

  public String getName() {
    return mName;
  }

  public boolean isAdmin() {
    return mIsAdmin;
  }

  public String getImage() {
    return mImage;
  }

  public String getEmail() {
    return mEmail;
  }

  @Override
  public String toString() {
    return "UserFeedCreator {" +
        "mId=" + mId +
        ", mName='" + mName + '\n' +
        ", mIsAdmin='" + mIsAdmin + '\n' +
        ", mIsAdmin='" + mIsAdmin + '\n' +
        ", mEmail='" + mEmail + '\n' +
        '}';
  }
}
