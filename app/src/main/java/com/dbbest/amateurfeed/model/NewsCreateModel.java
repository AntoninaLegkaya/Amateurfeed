package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;


public class NewsCreateModel implements Parcelable {

  public static final Creator<NewsCreateModel> CREATOR = new Creator<NewsCreateModel>() {
    @Override
    public NewsCreateModel createFromParcel(Parcel in) {
      return new NewsCreateModel(in);
    }

    @Override
    public NewsCreateModel[] newArray(int size) {
      return new NewsCreateModel[size];
    }
  };
  @SerializedName("title")
  private String mTitle;
  @SerializedName("text")
  private String mText;
  @SerializedName("image")
  private String mImage;
  @SerializedName("tags")
  private ArrayList<TagModel> mTags = new ArrayList<>();

  public NewsCreateModel(String title, String text, String image, ArrayList<TagModel> tags) {

    mTitle = title;
    mText = text;
    mImage = image;
    mTags = tags;
  }

  private NewsCreateModel(Parcel in) {
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

  public ArrayList<TagModel> getTags() {
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

//    @Override
//    public String toString() {
//        String tagsNames = new String();
//        if (mTags != null) {
//            for (TagModel model : mTags) {
//                tagsNames = tagsNames + model.getName() + " ";
//
//            }
//        }
//
//        return "NewsCreateModel{"
//                + "mTitle=" + mTitle + '\n'
//                + "mText=" + mText + '\n'
//                + "mImage=" + mImage + '\n'
//                + "mTags=" + tagsNames + '\n'
//                + '}';
//    }
}
