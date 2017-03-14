package com.dbbest.amateurfeed.app.net.response;

import com.google.gson.annotations.SerializedName;


public class NewsResponseModel {

  @SerializedName("newsId")
  private int mId;

  public NewsResponseModel(int id) {
    mId = id;
  }

  public int getId() {
    return mId;
  }
}
