package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class Dictionary implements Parcelable {

  public static final Creator<Dictionary> CREATOR = new Creator<Dictionary>() {
    @Override
    public Dictionary createFromParcel(Parcel in) {
      return new Dictionary(in);
    }

    @Override
    public Dictionary[] newArray(int size) {
      return new Dictionary[size];
    }
  };
  @SerializedName("users")
  private ArrayList<UserFeedCreatorExpand> users = new ArrayList<>();
  ;
  @SerializedName("news")
  private ArrayList<NewsModel> news = new ArrayList<>();

  public Dictionary(ArrayList<UserFeedCreatorExpand> users, ArrayList<NewsModel> news) {
    this.users = users;
    this.news = news;
  }

  public Dictionary(Parcel in) {
    users = new ArrayList<UserFeedCreatorExpand>();
    in.readTypedList(users, UserFeedCreatorExpand.CREATOR);
    news = new ArrayList<NewsModel>();
    in.readTypedList(news, NewsModel.CREATOR);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeTypedList(news);
    dest.writeTypedList(users);
  }

  public ArrayList<UserFeedCreatorExpand> getUsers() {
    return users;
  }

  public ArrayList<NewsModel> getNews() {
    return news;
  }
}
