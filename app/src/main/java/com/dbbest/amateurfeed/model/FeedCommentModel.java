package com.dbbest.amateurfeed.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class FeedCommentModel implements Parcelable {

  public static final Creator<FeedCommentModel> CREATOR = new Creator<FeedCommentModel>() {
    @Override
    public FeedCommentModel createFromParcel(Parcel in) {
      return new FeedCommentModel(in);
    }

    @Override
    public FeedCommentModel[] newArray(int size) {
      return new FeedCommentModel[size];
    }
  };
  @SerializedName("postId")
  private int postId;
  @SerializedName("body")
  private String body;
  @SerializedName("parentCommentId")
  private Integer parentCommentId;


  public FeedCommentModel(int postId, String body, Integer parentCommentId) {
    this.postId = postId;
    this.body = body;
    this.parentCommentId = parentCommentId;
  }

  private FeedCommentModel(Parcel in) {
    postId = in.readInt();
    body = in.readString();
    parentCommentId = (Integer) in.readSerializable();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(postId);
    dest.writeString(body);
    dest.writeSerializable(parentCommentId);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public String toString() {

    return "FeedCommentModel{"
        + "postId=" + postId + '\n'
        + "body=" + body + '\n'
        + "parentCommentId=" + parentCommentId
        + '}';
  }

  public int getPostId() {
    return postId;
  }

  public String getBody() {
    return body;
  }

  public Integer getParentCommentId() {
    return parentCommentId;
  }
}
