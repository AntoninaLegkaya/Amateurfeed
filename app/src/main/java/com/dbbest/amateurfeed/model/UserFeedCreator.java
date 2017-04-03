package com.dbbest.amateurfeed.model;

import com.google.gson.annotations.SerializedName;


public class UserFeedCreator {

  @SerializedName("id")
  private int id;

  @SerializedName("name")
  private String name;


  @SerializedName("isAdmin")
  private boolean isAdmin;

  @SerializedName("image")
  private String image;

  public UserFeedCreator(int id, String name, boolean isAdmin, String image) {
    this.id = id;
    this.name = name;
    this.isAdmin = isAdmin;
    this.image = image;
  }

  @Override
  public String toString() {
    return "UserFeedCreator {" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", isAdmin='" + isAdmin + '\'' +
        ", isAdmin='" + isAdmin + '\'' +
        '}';
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public boolean isAdmin() {
    return isAdmin;
  }

  public String getImage() {
    return image;
  }

}
