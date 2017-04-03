package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;


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
  private String title;
  @SerializedName("text")
  private String text;
  @SerializedName("image")
  private String image;
  @SerializedName("tags")
  private ArrayList<TagModel> tags = new ArrayList<>();

  public NewsUpdateModel(ArrayList<TagModel> tags, String title, String text, String image) {

    this.title = title;
    this.text = text;
    this.image = image;
    this.tags = tags;
  }

  private NewsUpdateModel(Parcel in) {
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
