package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;


public class NewsUpdateModel implements Parcelable {

  public static final Parcelable.Creator<NewsUpdateModel> CREATOR =
      new Parcelable.Creator<NewsUpdateModel>() {
        @Override
        public NewsUpdateModel createFromParcel(Parcel in) {
          return new NewsUpdateModel(in);
        }

        @Override
        public NewsUpdateModel[] newArray(int size) {
          return new NewsUpdateModel[size];
        }
      };
  @SerializedName("title")
  private String mTitle;
  @SerializedName("text")
  private String mText;
  @SerializedName("image")
  private String mImage;
  @SerializedName("tags")
  private List<TagModel> mTags = new ArrayList<>();

  public NewsUpdateModel(List<TagModel> tags, String title, String text, String image) {

    mTitle = title;
    mText = text;
    mImage = image;
    mTags = tags;
  }

  private NewsUpdateModel(Parcel in) {
    mTitle = in.readString();
    mText = in.readString();
    mImage = in.readString();
    mTags = new ArrayList<TagModel>();
    in.readTypedList(mTags, TagModel.CREATOR);
  }

  public String getTitle() {
    return mTitle;
  }

  public String getText() {
    return mText;
  }

  public String getImage() {
    return mImage;
  }

  public List<TagModel> getTags() {
    return mTags;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mTitle);
    dest.writeString(mText);
    dest.writeString(mImage);
    dest.writeTypedList(mTags);
  }
}
