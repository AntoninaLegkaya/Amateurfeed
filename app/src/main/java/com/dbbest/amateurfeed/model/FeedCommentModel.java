package com.dbbest.amateurfeed.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by antonina on 26.01.17.
 */

public class FeedCommentModel {

    @SerializedName("postId")
    private int mPostId;

    @SerializedName("body")
    private String mBody;

    @SerializedName("parentCommentId")
    private int mParentCommentId;

    public int getPostId() {
        return mPostId;
    }

    public String getBody() {
        return mBody;
    }

    public int getParentCommentId() {
        return mParentCommentId;
    }

    public FeedCommentModel(int postId, String body, int parentCommentId) {

        mPostId = postId;
        mBody = body;
        mParentCommentId = parentCommentId;
    }
}
