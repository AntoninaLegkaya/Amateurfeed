package com.dbbest.amateurfeed.app.net.command;

import static com.dbbest.amateurfeed.ui.fragments.EditItemDetailFragment.DETAIL_FRAGMENT_COMMENT;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.data.FeedContract;
import com.dbbest.amateurfeed.data.FeedProvider;
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
  private FeedCommentModel mFeedCommentModel;

  public AddCommentCommand(int postId, String body, Integer parentCommentId) {
    mFeedCommentModel = new FeedCommentModel(postId, body, parentCommentId);
  }

  public AddCommentCommand(Parcel in) {
    super(in);
    mFeedCommentModel = in.readParcelable(FeedCommentModel.class.getClassLoader());
  }

  @Override
  public void writeToParcel(int flags, Parcel dest) {
    dest.writeParcelable(mFeedCommentModel, flags);
  }

  @Override
  public void execute() {
    Log.i(DETAIL_FRAGMENT_COMMENT,
        "Start execute Add Comment Command: \n" + mFeedCommentModel.toString());
    RestApiClient apiClient = App.getApiFactory().restClient();
    AuthToken authToken = new AuthToken();
    ResponseWrapper<Object> response = apiClient.postComment(authToken.bearer(), mFeedCommentModel);
    if (response != null) {
      if (response.isSuccessful()) {
        Bundle bundle = new Bundle();
        bundle.putInt("post_id", mFeedCommentModel.getPostId());
        bundle.putString("body", mFeedCommentModel.getBody());
        bundle.putSerializable("parentComment_id", mFeedCommentModel.getParentCommentId());
        notifySuccess(bundle);
      } else {
        notifyError(Bundle.EMPTY);
      }
    } else {
      notifyError(Bundle.EMPTY);
    }
  }

}
