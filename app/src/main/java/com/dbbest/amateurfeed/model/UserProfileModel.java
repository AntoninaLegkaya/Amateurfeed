package com.dbbest.amateurfeed.model;

import com.google.gson.annotations.SerializedName;


public class UserProfileModel {


  @SerializedName("fullName")
  private String fullName;

  @SerializedName("email")
  private String email;

  @SerializedName("image")
  private String image;

  @SerializedName("skype")
  private String skype;

  @SerializedName("address")
  private String address;

  @SerializedName("job")
  private String job;

  @SerializedName("phone")
  private String phone;

  public UserProfileModel(String fullName, String email, String image, String skype, String address,
      String job, String phone) {
    this.fullName = fullName;
    this.email = email;
    this.image = image;
    this.skype = skype;
    this.address = address;
    this.job = job;
    this.phone = phone;
  }

  public String getFullName() {
    return fullName;
  }

  public String getEmail() {
    return email;
  }

  public String getImage() {
    return image;
  }

  public String getSkype() {
    return skype;
  }

  public String getAddress() {
    return address;
  }

  public String getJob() {
    return job;
  }

  public String getPhone() {
    return phone;
  }
}
