package com.dbbest.amateurfeed.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;


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


  public UserFeedCommentModel(int id, int postId, String body, int parentCommentId,
      UserFeedCreator creator, String createDate, ArrayList<UserFeedCommentModel> children) {
    mId = id;
    mPostId = postId;
    mBody = body;
    mParentCommentId = parentCommentId;
    mCreator = creator;
    mCreatedate = createDate;
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

  public String getCreateDate() {
    return mCreatedate;
  }

  public ArrayList<UserFeedCommentModel> getChildren() {
    return mChildren;
  }
}
