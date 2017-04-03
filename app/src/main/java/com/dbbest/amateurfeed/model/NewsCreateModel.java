package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;


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
  private String title;
  @SerializedName("text")
  private String text;
  @SerializedName("image")
  private String image;
  @SerializedName("tags")
  private ArrayList<TagModel> tags = new ArrayList<>();

  public NewsCreateModel(String title, String text, String image, ArrayList<TagModel> tags) {

    this.title = title;
    this.text = text;
    this.image = image;
    this.tags = tags;
  }

  private NewsCreateModel(Parcel in) {
    title = in.readString();
    text = in.readString();
    image = in.readString();
    tags = new ArrayList<TagModel>();
    in.readTypedList(tags, TagModel.CREATOR);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(title);
    dest.writeString(text);
    dest.writeString(image);
    dest.writeTypedList(tags);
  }

  public String getTitle() {
    return title;
  }

  public String getText() {
    return text;
  }

  public String getImage() {
    return image;
  }

  public ArrayList<TagModel> getTags() {
    return tags;
  }

}
