package com.dbbest.amateurfeed.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by antonina on 26.01.17.
 */

public class UserFeedCommentModel {

    @SerializedName("id")
    private int mId;

    @SerializedName("postId")
    private int mPostId;

    @SerializedName("body")
    private String mBody;

    @SerializedName("parentCommentId")
    private int mParentCommentId;

    @SerializedName("creator")
    private UserFeedCreator mCreator;

    @SerializedName("createDate")
    private String mCreatedate;

    @SerializedName("children")
    private ArrayList<UserFeedCommentModel> mChildren;


    public UserFeedCommentModel(int id, int postId, String body, int parentCommentId, UserFeedCreator creator, String createdate, ArrayList<UserFeedCommentModel> children) {
        mId = id;
        mPostId = postId;
        mBody = body;
        mParentCommentId = parentCommentId;
        mCreator = creator;
        mCreatedate = createdate;
        mChildren = children;
    }

    public int getId() {
        return mId;
    }

    public int getPostId() {
        return mPostId;
    }

    public String getBody() {
        return mBody;
    }

    public int getParentCommentId() {
        return mParentCommentId;
    }

    public UserFeedCreator getCreator() {
        return mCreator;
    }

    public String getCreatedate() {
        return mCreatedate;
    }

    public ArrayList<UserFeedCommentModel> getChildren() {
        return mChildren;
    }
}
