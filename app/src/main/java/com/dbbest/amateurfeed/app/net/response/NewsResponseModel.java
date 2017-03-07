package com.dbbest.amateurfeed.app.net.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by antonina on 26.01.17.
 */

public class NewsResponseModel {

    @SerializedName("newsId")
    private int mId;

    public int getId() {
        return mId;
    }

    public NewsResponseModel(int id) {
        mId = id;
    }
}
