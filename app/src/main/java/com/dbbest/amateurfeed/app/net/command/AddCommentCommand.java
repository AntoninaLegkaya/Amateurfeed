package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.model.AuthToken;
import com.dbbest.amateurfeed.model.FeedCommentModel;

public class AddCommentCommand extends Command {

  public static final Parcelable.Creator<AddCommentCommand> CREATOR =
      new Parcelable.Creator<AddCommentCommand>() {
        @Override
        public AddCommentCommand createFromParcel(Parcel source) {
          return new AddCommentCommand(source);
        }

        @Override
        public AddCommentCommand[] newArray(int size) {
          return new AddCommentCommand[size];
        }
      };
  public static final String POST_ID = "post_id";
  public static final String BODY = "body";
  public static final String PARENT_COMMENT_ID = "parentComment_id";
  private FeedCommentModel feedCommentModel;

  public AddCommentCommand(int postId, String body, Integer parentCommentId) {
    feedCommentModel = new FeedCommentModel(postId, body, parentCommentId);
  }

  private AddCommentCommand(Parcel in) {
    super(in);
    feedCommentModel = in.readParcelable(FeedCommentModel.class.getClassLoader());
  }

  @Override
  public void writeToParcel(int flags, Parcel dest) {
    dest.writeParcelable(feedCommentModel, flags);
  }

  @Override
  public void execute() {
    RestApiClient apiClient = App.getApiFactory().restClient();
    AuthToken authToken = new AuthToken();
    ResponseWrapper<Object> response = apiClient.postComment(authToken.bearer(), feedCommentModel);
    if (response != null) {
      if (response.isSuccessful()) {
        Bundle bundle = new Bundle();
        bundle.putInt(POST_ID, feedCommentModel.getPostId());
        bundle.putString(BODY, feedCommentModel.getBody());
        bundle.putSerializable(PARENT_COMMENT_ID, feedCommentModel.getParentCommentId());
        notifySuccess(bundle);
      } else {
        notifyError(Bundle.EMPTY);
      }
    } else {
      notifyError(Bundle.EMPTY);
    }
  }

}
