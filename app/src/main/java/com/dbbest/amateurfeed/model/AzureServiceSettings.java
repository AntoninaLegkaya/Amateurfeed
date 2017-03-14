package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class AzureServiceSettings implements Parcelable {


  public static final Parcelable.Creator<AzureServiceSettings> CREATOR =
      new Parcelable.Creator<AzureServiceSettings>() {

        @Override
        public AzureServiceSettings createFromParcel(Parcel in) {
          return new AzureServiceSettings(in);
        }

        @Override
        public AzureServiceSettings[] newArray(int size) {
          return new AzureServiceSettings[size];
        }
      };
  @SerializedName("accountName")
  private String mAccountName;
  @SerializedName("accountKey")
  private String mAccountKey;
  @SerializedName("storageUrl")
  private String mStorageUrl;
  @SerializedName("containerName")
  private String mContainerName;

  protected AzureServiceSettings(Parcel in) {
    mAccountName = in.readString();
    mAccountKey = in.readString();
    mStorageUrl = in.readString();
    mContainerName = in.readString();
  }

  public AzureServiceSettings(String accountName, String accountKey, String storageUrl,
      String containerName) {
    mAccountName = accountName;
    mAccountKey = accountKey;
    mStorageUrl = storageUrl;
    mContainerName = containerName;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {

    dest.writeString(mAccountName);
    dest.writeString(mAccountKey);
    dest.writeString(mStorageUrl);
    dest.writeString(mContainerName);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public String getAccountName() {
    return mAccountName;
  }

  public String getAccountKey() {
    return mAccountKey;
  }

  public String getStorageUrl() {
    return mStorageUrl;
  }

  public String getContainerName() {
    return mContainerName;
  }
}




