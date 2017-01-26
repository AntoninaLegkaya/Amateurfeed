package com.dbbest.amateurfeed.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by antonina on 26.01.17.
 */

public class AbuseModel {
    @SerializedName("newsId")
    private int mNewsId;

    @SerializedName("comment")
    private int mComment;

    public int getNewsId() {
        return mNewsId;
    }

    public int getComment() {
        return mComment;
    }

    public AbuseModel(int newsId, int comment) {

        mNewsId = newsId;
        mComment = comment;
    }
}
