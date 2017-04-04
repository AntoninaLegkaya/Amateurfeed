package com.dbbest.amateurfeed.app.net.response;

import com.google.gson.annotations.SerializedName;


public class LoginResponseModel {

  @SerializedName("userId")
  private int userId;

  @SerializedName("access_token")
  private String accessToken;

  @SerializedName("userName")
  private String userName;

  @SerializedName("role")
  private String role;

  @SerializedName("profileImage")
  private String profileImage;


  public LoginResponseModel(int userId, String accessToken, String userName, String role, String profileImage) {
    this.userId = userId;
    this.accessToken = accessToken;
    this.userName = userName;
    this.role = role;
    this.profileImage = profileImage;
  }

  @Override
  public String toString() {
    return "RegistrationResponse{" +
        "userId=" + userId +
        ", accessToken='" + accessToken + '\'' +
        ", userName='" + userName + '\'' +
        ", profileImage='" + profileImage + '\'' +
        '}';
  }

  public int getUserId() {
    return userId;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getUserName() {
    return userName;
  }

  public String getRole() {
    return role;
  }

  public String getProfileImage() {
    return profileImage;
  }
}
