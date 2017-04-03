package com.dbbest.amateurfeed.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;


public class UserFeedCommentModel {

  @SerializedName("id")
  private int id;

  @SerializedName("postId")
  private int postId;

  @SerializedName("body")
  private String body;

  @SerializedName("parentCommentId")
  private int parentCommentId;

  @SerializedName("creator")
  private UserFeedCreator mCreator;

  @SerializedName("createDate")
  private String createdate;

  @SerializedName("children")
  private ArrayList<UserFeedCommentModel> children;


  public UserFeedCommentModel(int id, int postId, String body, int parentCommentId,
      UserFeedCreator creator, String createDate, ArrayList<UserFeedCommentModel> children) {
    this.id = id;
    this.postId = postId;
    this.body = body;
    this.parentCommentId = parentCommentId;
    mCreator = creator;
    createdate = createDate;
    this.children = children;
  }

  public int getId() {
    return id;
  }

  public int getPostId() {
    return postId;
  }

  public String getBody() {
    return body;
  }

  public int getParentCommentId() {
    return parentCommentId;
  }

  public UserFeedCreator getCreator() {
    return mCreator;
  }

  public String getCreateDate() {
    return createdate;
  }

  public ArrayList<UserFeedCommentModel> getChildren() {
    return children;
  }
}
