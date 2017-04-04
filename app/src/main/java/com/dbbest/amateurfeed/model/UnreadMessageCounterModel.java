package com.dbbest.amateurfeed.model;

import com.google.gson.annotations.SerializedName;


public class UnreadMessageCounterModel {

  @SerializedName("counter")
  private int counter;

  public UnreadMessageCounterModel(int counter) {

    this.counter = counter;
  }

  public int getCounter() {
    return counter;
  }
}
