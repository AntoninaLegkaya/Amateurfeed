package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;


public class NewsRequestModel implements Parcelable {


  public static final Parcelable.Creator<NewsRequestModel> CREATOR =
      new Parcelable.Creator<NewsRequestModel>() {
        @Override
        public NewsRequestModel createFromParcel(Parcel source) {
          return new NewsRequestModel(source);
        }

        @Override
        public NewsRequestModel[] newArray(int size) {
          return new NewsRequestModel[size];
        }
      };
  @SerializedName("offset")
  private int offset;
  @SerializedName("count")
  private int count;
  @SerializedName("deviceToken")
  private String accessToken;

  public NewsRequestModel(int offset, int count, String accessToken) {
    this.offset = offset;
    this.count = count;
    this.accessToken = accessToken;

  }

  private NewsRequestModel(Parcel in) {
    offset = in.readInt();
    count = in.readInt();
    accessToken = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(offset);
    dest.writeInt(count);
    dest.writeString(accessToken);

  }

  public int getOffset() {
    return offset;
  }

  public int getCount() {
    return count;
  }

  public String getAccessToken() {
    return accessToken;
  }
}
