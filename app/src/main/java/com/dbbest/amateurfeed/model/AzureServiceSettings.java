package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import static com.dbbest.amateurfeed.R.string.comment;

public class AzureServiceSettings implements Parcelable {


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

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(mAccountName);
        dest.writeString(mAccountKey);
        dest.writeString(mStorageUrl);
        dest.writeString(mContainerName);
    }

    public AzureServiceSettings(String accountName, String accountKey, String storageUrl, String containerName) {
        mAccountName = accountName;
        mAccountKey = accountKey;
        mStorageUrl = storageUrl;
        mContainerName = containerName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<AzureServiceSettings> CREATOR = new Parcelable.Creator<AzureServiceSettings>() {

        @Override
        public AzureServiceSettings createFromParcel(Parcel in) {
            return new AzureServiceSettings(in);
        }

        @Override
        public AzureServiceSettings[] newArray(int size) {
            return new AzureServiceSettings[size];
        }
    };

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




