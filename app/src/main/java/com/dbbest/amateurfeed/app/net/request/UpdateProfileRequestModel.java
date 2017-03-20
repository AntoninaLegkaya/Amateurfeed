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
  private String mEmail;
  @SerializedName("image")
  private String mImage;
  @SerializedName("phone")
  private String mPhone;
  @SerializedName("job")
  private String mJob;

  public UpdateProfileRequestModel(String fullName, String email, String image, String phone,
      String job) {
    this.fullName = fullName;
    mEmail = email;
    mImage = image;
    mPhone = phone;
    mJob = job;
  }

  protected UpdateProfileRequestModel(Parcel in) {
    fullName = in.readString();
    mEmail = in.readString();
    mImage = in.readString();
    mPhone = in.readString();
    mJob = in.readString();
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

  public String getPhone() {
    return mPhone;
  }

  public String getJob() {
    return mJob;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(fullName);
    dest.writeString(mEmail);
    dest.writeString(mImage);
    dest.writeString(mPhone);
    dest.writeString(mJob);
  }
}
