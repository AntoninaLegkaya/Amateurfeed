package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;


public class TagModel implements Parcelable {

  public static final Parcelable.Creator<TagModel> CREATOR = new Parcelable.Creator<TagModel>() {
    @Override
    public TagModel createFromParcel(Parcel source) {
      return new TagModel(source);
    }

    @Override
    public TagModel[] newArray(int size) {
      return new TagModel[size];
    }
  };
  @SerializedName("id")
  private int id;
  @SerializedName("name")
  private String name;

  public TagModel(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public TagModel(Parcel in) {
    id = in.readInt();
    name = in.readString();
  }

  @Override
  public String toString() {
    return "TagModel{" +
        "id=" + id +
        ", name='" + name +
        '}';
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {

    dest.writeInt(id);
    dest.writeString(name);
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
