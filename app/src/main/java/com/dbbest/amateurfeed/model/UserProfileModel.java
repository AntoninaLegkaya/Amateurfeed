package com.dbbest.amateurfeed.model;

import com.google.gson.annotations.SerializedName;


public class UserProfileModel {


  @SerializedName("fullName")
  private String fullName;

  @SerializedName("email")
  private String mEmail;

  @SerializedName("image")
  private String mImage;

  @SerializedName("skype")
  private String mSkype;

  @SerializedName("address")
  private String mAddress;

  @SerializedName("job")
  private String mJob;

  @SerializedName("phone")
  private String mPhone;

  public UserProfileModel(String fullName, String email, String image, String skype, String address,
      String job, String phone) {
    this.fullName = fullName;
    mEmail = email;
    mImage = image;
    mSkype = skype;
    mAddress = address;
    mJob = job;
    mPhone = phone;
  }

  public String getFullName() {
    return fullName;
  }

  public String getEmail() {
    return mEmail;
  }

  public String getImage() {
    return mImage;
  }

  public String getSkype() {
    return mSkype;
  }

  public String getAddress() {
    return mAddress;
  }

  public String getJob() {
    return mJob;
  }

  public String getPhone() {
    return mPhone;
  }
}
