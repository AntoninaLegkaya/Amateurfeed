package com.dbbest.amateurfeed.app.net.response;

import com.google.gson.annotations.SerializedName;


public class NewsResponseModel {

  @SerializedName("newsId")
  private int id;

  public NewsResponseModel(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }
}
