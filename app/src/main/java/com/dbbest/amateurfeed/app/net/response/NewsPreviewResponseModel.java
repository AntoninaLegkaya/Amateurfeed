package com.dbbest.amateurfeed.app.net.response;

import com.dbbest.amateurfeed.model.TagModel;
import com.dbbest.amateurfeed.model.UserFeedCommentModel;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.bind.ArrayTypeAdapter;

import java.util.ArrayList;

/**
 * Created by antonina on 26.01.17.
 */

public class NewsPreviewResponseModel {
    @SerializedName("id")
    private int mId;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("text")
    private String mText;

    @SerializedName("tags")
    private ArrayList<TagModel> mTags;

    @SerializedName("likes")
    private int mLikes;

    @SerializedName("isLike")
    private boolean mIsLike;

    @SerializedName("author")
    private String mAuthor;

    @SerializedName("authorImage")
    private String mAuthorImage;

    @SerializedName("createDate")
    private String mCreateDate;

    @SerializedName("image")
    private String mImage;

    @SerializedName("isMy")
    private boolean mIsMy;

    @SerializedName("comments")
    private ArrayList<UserFeedCommentModel> mComments;

    public NewsPreviewResponseModel(int id, String title, String text, ArrayList<TagModel> tags, int likes, boolean isLike, String author, String authorImage, String createDate, String image, boolean isMy, ArrayList<UserFeedCommentModel> comments) {
        mId = id;
        mTitle = title;
        mText = text;
        mTags = tags;
        mLikes = likes;
        mIsLike = isLike;
        mAuthor = author;
        mAuthorImage = authorImage;
        mCreateDate = createDate;
        mImage = image;
        mIsMy = isMy;
        mComments = comments;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getText() {
        return mText;
    }

    public ArrayList<TagModel> getTags() {
        return mTags;
    }

    public int getLikes() {
        return mLikes;
    }

    public boolean isLike() {
        return mIsLike;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getAuthorImage() {
        return mAuthorImage;
    }

    public String getCreateDate() {
        return mCreateDate;
    }

    public String getImage() {
        return mImage;
    }

    public boolean isMy() {
        return mIsMy;
    }

    public ArrayList<UserFeedCommentModel> getComments() {
        return mComments;
    }
}
