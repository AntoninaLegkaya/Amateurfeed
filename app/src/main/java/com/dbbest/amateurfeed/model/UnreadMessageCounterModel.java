package com.dbbest.amateurfeed.model;

import com.google.gson.annotations.SerializedName;


public class UnreadMessageCounterModel {

  @SerializedName("counter")
  private int mCounter;

  public UnreadMessageCounterModel(int counter) {

    mCounter = counter;
  }

  public int getCounter() {
    return mCounter;
  }
}
