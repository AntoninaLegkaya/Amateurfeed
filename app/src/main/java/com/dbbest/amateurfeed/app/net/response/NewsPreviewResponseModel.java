package com.dbbest.amateurfeed.app.net.response;

import com.dbbest.amateurfeed.model.TagModel;
import com.dbbest.amateurfeed.model.UserFeedCommentModel;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class NewsPreviewResponseModel {

  @SerializedName("id")
  private int id;

  @SerializedName("title")
  private String title;

  @SerializedName("text")
  private String text;

  @SerializedName("tags")
  private ArrayList<TagModel> tags;

  @SerializedName("likes")
  private int likes;

  @SerializedName("isLike")
  private boolean isLike;

  @SerializedName("author")
  private String author;

  @SerializedName("authorImage")
  private String authorImage;

  @SerializedName("createDate")
  private String createDate;

  @SerializedName("image")
  private String image;

  @SerializedName("isMy")
  private boolean isMy;

  @SerializedName("comments")
  private ArrayList<UserFeedCommentModel> comments;

  public NewsPreviewResponseModel(int id, String title, String text, ArrayList<TagModel> tags,
      int likes, boolean isLike, String author, String authorImage, String createDate, String image,
      boolean isMy, ArrayList<UserFeedCommentModel> comments) {
    this.id = id;
    this.title = title;
    this.text = text;
    this.tags = tags;
    this.likes = likes;
    this.isLike = isLike;
    this.author = author;
    this.authorImage = authorImage;
    this.createDate = createDate;
    this.image = image;
    this.isMy = isMy;
    this.comments = comments;
  }

  public int getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getText() {
    return text;
  }

  public ArrayList<TagModel> getTags() {
    return tags;
  }

  public int getLikes() {
    return likes;
  }

  public boolean isLike() {
    return isLike;
  }

  public String getAuthor() {
    return author;
  }

  public String getAuthorImage() {
    return authorImage;
  }

  public String getCreateDate() {
    return createDate;
  }

  public String getImage() {
    return image;
  }

  public boolean isMy() {
    return isMy;
  }

  public ArrayList<UserFeedCommentModel> getComments() {
    return comments;
  }
}
