package com.dbbest.amateurfeed.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by antonina on 26.01.17.
 */

public class UnreadMessageCounterModel {

    @SerializedName("counter")
    private int mCounter;

    public int getCounter() {
        return mCounter;
    }

    public UnreadMessageCounterModel(int counter) {

        mCounter = counter;
    }
}
