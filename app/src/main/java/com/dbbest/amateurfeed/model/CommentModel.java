package com.dbbest.amateurfeed.model;

import com.google.gson.annotations.SerializedName;


public class CommentModel {

  @SerializedName("id")
  private int mId;

  @SerializedName("postId")
  private int mPostId;

  @SerializedName("body")
  private String mBody;

  @SerializedName("parentCommentId")
  private int mParentCommentId;

  @SerializedName("creator")
  private int mCreator;

  @SerializedName("createDate")
  private String mCreateDate;

  public CommentModel(int id, int postId, int creator, String body, int parentCommentId,
      String createDate) {
    mId = id;
    mPostId = postId;
    mBody = body;
    mParentCommentId = parentCommentId;
    mCreator = creator;
    mCreateDate = createDate;
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

  public int getCreator() {
    return mCreator;
  }

  public String getCreatedate() {
    return mCreateDate;
  }
}
