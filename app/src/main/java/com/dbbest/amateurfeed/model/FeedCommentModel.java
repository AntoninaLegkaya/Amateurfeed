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
  private int mPostId;
  @SerializedName("body")
  private String mBody;
  @SerializedName("parentCommentId")
  private Integer mParentCommentId;


  public FeedCommentModel(int postId, String body, Integer parentCommentId) {
    mPostId = postId;
    mBody = body;
    mParentCommentId = parentCommentId;
  }

  protected FeedCommentModel(Parcel in) {
    mPostId = in.readInt();
    mBody = in.readString();
    mParentCommentId = (Integer) in.readSerializable();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(mPostId);
    dest.writeString(mBody);
    dest.writeSerializable(mParentCommentId);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public int getPostId() {
    return mPostId;
  }

  public String getBody() {
    return mBody;
  }

  public Integer getParentCommentId() {
    return mParentCommentId;
  }


  @Override
  public String toString() {

    return "FeedCommentModel{"
        + "mPostId=" + mPostId + '\n'
        + "mBody=" + mBody + '\n'
        + "mParentCommentId=" + mParentCommentId
        + '}';
  }
}
