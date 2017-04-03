package com.dbbest.amateurfeed.app.net.request;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;


public class UpdateProfileRequestModel implements Parcelable {

  public static final Creator<UpdateProfileRequestModel> CREATOR = new Creator<UpdateProfileRequestModel>() {
    @Override
    public UpdateProfileRequestModel createFromParcel(Parcel in) {
      return new UpdateProfileRequestModel(in);
    }

    @Override
    public UpdateProfileRequestModel[] newArray(int size) {
      return new UpdateProfileRequestModel[size];
    }
  };
  @SerializedName("fullName")
  private String fullName;
  @SerializedName("email")
  private String email;
  @SerializedName("image")
  private String image;
  @SerializedName("phone")
  private String phone;
  @SerializedName("job")
  private String job;

  public UpdateProfileRequestModel(String fullName, String email, String image, String phone,
      String job) {
    this.fullName = fullName;
    this.email = email;
    this.image = image;
    this.phone = phone;
    this.job = job;
  }

  private UpdateProfileRequestModel(Parcel in) {
    fullName = in.readString();
    email = in.readString();
    image = in.readString();
    phone = in.readString();
    job = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(fullName);
    dest.writeString(email);
    dest.writeString(image);
    dest.writeString(phone);
    dest.writeString(job);
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

  public String getPhone() {
    return phone;
  }

  public String getJob() {
    return job;
  }
}
