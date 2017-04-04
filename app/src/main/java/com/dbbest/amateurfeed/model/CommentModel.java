package com.dbbest.amateurfeed.model;

import com.google.gson.annotations.SerializedName;


public class CommentModel {

  @SerializedName("id")
  private int id;

  @SerializedName("postId")
  private int postId;

  @SerializedName("body")
  private String body;

  @SerializedName("parentCommentId")
  private int parentCommentId;

  @SerializedName("creator")
  private int creator;

  @SerializedName("createDate")
  private String createDate;

  public CommentModel(int id, int postId, int creator, String body, int parentCommentId,
      String createDate) {
    this.id = id;
    this.postId = postId;
    this.body = body;
    this.parentCommentId = parentCommentId;
    this.creator = creator;
    this.createDate = createDate;
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

  public int getCreator() {
    return creator;
  }

  public String getCreatedate() {
    return createDate;
  }
}
