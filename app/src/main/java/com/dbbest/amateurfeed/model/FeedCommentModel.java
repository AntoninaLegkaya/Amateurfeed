package com.dbbest.amateurfeed.model;

import com.google.gson.annotations.SerializedName;

public class FeedCommentModel {

    @SerializedName("postId")
    private int mPostId;

    @SerializedName("body")
    private String mBody;

    @SerializedName("parentCommentId")
    private int mParentCommentId;

    public FeedCommentModel(int postId, String body, int parentCommentId) {
        mPostId = postId;
        mBody = body;
        mParentCommentId = parentCommentId;
    }
}
