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
  private String accountName;
  @SerializedName("accountKey")
  private String accountKey;
  @SerializedName("storageUrl")
  private String storageUrl;
  @SerializedName("containerName")
  private String containerName;

  public AzureServiceSettings(String accountName, String accountKey, String storageUrl,
      String containerName) {
    this.accountName = accountName;
    this.accountKey = accountKey;
    this.storageUrl = storageUrl;
    this.containerName = containerName;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {

    dest.writeString(accountName);
    dest.writeString(accountKey);
    dest.writeString(storageUrl);
    dest.writeString(containerName);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public String getAccountName() {
    return accountName;
  }

  public String getAccountKey() {
    return accountKey;
  }

  public String getStorageUrl() {
    return storageUrl;
  }

  public String getContainerName() {
    return containerName;
  }

  protected AzureServiceSettings(Parcel in) {
    accountName = in.readString();
    accountKey = in.readString();
    storageUrl = in.readString();
    containerName = in.readString();
  }
}




