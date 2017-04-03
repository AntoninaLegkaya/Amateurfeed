package com.dbbest.amateurfeed.app.net.response;


import com.google.gson.annotations.SerializedName;

public class ResponseWrapper<D> {

  @SerializedName("statusCode")
  private int statusCode;

  @SerializedName("success")
  private boolean success;

  @SerializedName("errorMessage")
  private String errorMessage;

  @SerializedName("data")
  private D data;

  public ResponseWrapper(int statusCode, boolean success, String errorMessage, D data) {
    this.statusCode = statusCode;
    this.success = success;
    this.errorMessage = errorMessage;
    this.data = data;
  }

  @Override
  public String toString() {
    return "ResponseWrapper{" +
        "statusCode=" + statusCode +
        ", success=" + success +
        ", errorMessage='" + errorMessage + '\'' +
        ", data=" + data +
        '}';
  }

  public int code() {
    return statusCode;
  }

  public boolean isSuccessful() {
    return success;
  }

  public String message() {
    return errorMessage;
  }

  public D data() {
    return data;
  }
}
