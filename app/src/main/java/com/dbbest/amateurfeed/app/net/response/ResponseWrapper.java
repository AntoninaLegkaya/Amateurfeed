package com.dbbest.amateurfeed.app.net.response;


import com.google.gson.annotations.SerializedName;

public class ResponseWrapper<D> {

  @SerializedName("statusCode")
  private int mStatusCode;

  @SerializedName("success")
  private boolean mSuccess;

  @SerializedName("errorMessage")
  private String mErrorMessage;

  @SerializedName("data")
  private D mData;

  public ResponseWrapper(int statusCode, boolean success, String errorMessage, D data) {
    mStatusCode = statusCode;
    mSuccess = success;
    mErrorMessage = errorMessage;
    mData = data;
  }

  public int code() {
    return mStatusCode;
  }

  public boolean isSuccessful() {
    return mSuccess;
  }

  public String message() {
    return mErrorMessage;
  }

  public D data() {
    return mData;
  }

  @Override
  public String toString() {
    return "ResponseWrapper{" +
        "mStatusCode=" + mStatusCode +
        ", mSuccess=" + mSuccess +
        ", mErrorMessage='" + mErrorMessage + '\'' +
        ", mData=" + mData +
        '}';
  }
}
