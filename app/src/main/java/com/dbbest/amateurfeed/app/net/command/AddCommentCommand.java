package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.response.NewsResponseModel;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.model.AuthToken;
import com.dbbest.amateurfeed.model.CommentModel;
import com.dbbest.amateurfeed.model.FeedCommentModel;
import com.dbbest.amateurfeed.model.TagModel;
import com.dbbest.amateurfeed.ui.fragments.EditItemDetailFragment;

import static android.R.attr.data;
import static com.dbbest.amateurfeed.ui.fragments.EditItemDetailFragment.DETAIL_FRAGMENT_COMMENT;

public class AddCommentCommand extends Command {
    private FeedCommentModel mFeedCommentModel;

    public AddCommentCommand(int postId, String body, int parentCommentId) {
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
        Log.i(DETAIL_FRAGMENT_COMMENT, "Start execute Add Comment Command: \n" + mFeedCommentModel.toString());
        RestApiClient apiClient = App.getApiFactory().restClient();
        AuthToken authToken = new AuthToken();
        ResponseWrapper<Object> response = apiClient.postComment(authToken.bearer(), mFeedCommentModel);
        if (response != null) {
            if (response.isSuccessful()) {

                notifySuccess(Bundle.EMPTY);

            } else {


                notifyError(Bundle.EMPTY);

            }

        } else {


            notifyError(Bundle.EMPTY);

        }


    }

    public static final Parcelable.Creator<AddCommentCommand> CREATOR = new Parcelable.Creator<AddCommentCommand>() {
        @Override
        public AddCommentCommand createFromParcel(Parcel source) {
            return new AddCommentCommand(source);
        }

        @Override
        public AddCommentCommand[] newArray(int size) {
            return new AddCommentCommand[size];
        }
    };
}
