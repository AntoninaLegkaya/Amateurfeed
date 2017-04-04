package com.dbbest.amateurfeed.app.net.request;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;


public class RegistrationFaceBookRequestModel implements Parcelable {

  public static final Creator<RegistrationFaceBookRequestModel> CREATOR =
      new Parcelable.Creator<RegistrationFaceBookRequestModel>() {
        @Override
        public RegistrationFaceBookRequestModel createFromParcel(Parcel source) {
          return new RegistrationFaceBookRequestModel(source);
        }

        @Override
        public RegistrationFaceBookRequestModel[] newArray(int size) {
          return new RegistrationFaceBookRequestModel[size];
        }
      };
  @SerializedName("code")
  private String code;
  @SerializedName("longitude")
  private double longitude;
  @SerializedName("latitude")
  private double latitude;
  @SerializedName("deviceId")
  private String deviceId;
  @SerializedName("osType")
  private int osType;
  @SerializedName("deviceToken")
  private String deviceToken;


  public RegistrationFaceBookRequestModel(String code, double longitude, double latitude) {
    this.code = code;
    this.longitude = longitude;
    this.latitude = latitude;

  }

  private RegistrationFaceBookRequestModel(Parcel in) {
    code = in.readString();
    longitude = in.readDouble();
    latitude = in.readDouble();

  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(code);
    dest.writeDouble(longitude);
    dest.writeDouble(latitude);
  }
}
