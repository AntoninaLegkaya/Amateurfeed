package com.dbbest.amateurfeed.app.net.command;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.request.ResetRequestPasswordModel;
import com.dbbest.amateurfeed.app.net.response.LoginResponseModel;
import com.dbbest.amateurfeed.app.net.response.NewsPreviewResponseModel;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.data.FeedContract;
import com.dbbest.amateurfeed.model.AuthToken;
import com.dbbest.amateurfeed.model.CurrentUser;
import com.dbbest.amateurfeed.model.NewsRequestModel;
import com.dbbest.amateurfeed.model.TagModel;
import com.dbbest.amateurfeed.model.UserFeedCommentModel;
import com.dbbest.amateurfeed.model.UserNewsModel;
import com.dbbest.amateurfeed.utils.Utils;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by antonina on 08.02.17.
 */

public class GetNewsCommand extends Command {
    private NewsRequestModel mNewsRequestModel;


    public GetNewsCommand(int offset, int count) {
        Log.i(Utils.TAG_LOG, "Create get News command");
        AuthToken authToken = new AuthToken();
        mNewsRequestModel = new NewsRequestModel(offset, count, authToken.bearer());
        Log.i(Utils.TAG_LOG, "Get News Command: AuthToken: " + mNewsRequestModel.getAccessToken());
        Log.i(Utils.TAG_LOG, "Get News Command: Offset: " + mNewsRequestModel.getOffset());
        Log.i(Utils.TAG_LOG, "Get News Command: Count: " + mNewsRequestModel.getCount());
    }

    private GetNewsCommand(Parcel in) {
        super(in);
        mNewsRequestModel = in.readParcelable(NewsRequestModel.class.getClassLoader());
    }


    @Override
    public void execute() {
        Log.i(Utils.TAG_LOG, "Execute get News command");

        //TODO RestApiClient
        RestApiClient apiClient = App.getApiFactory().restClient();

        ResponseWrapper<ArrayList<NewsPreviewResponseModel>> response = apiClient.getNews(mNewsRequestModel.getAccessToken(), mNewsRequestModel.getOffset(), mNewsRequestModel.getCount());

        if (response != null) {

            if (response.isSuccessful() && response.data() != null) {

                ArrayList<NewsPreviewResponseModel> data = response.data();
                // Insert the new weather information into the database
                Vector<ContentValues> cVVector = new Vector<ContentValues>(data.size());


                int i = 0;
                for (NewsPreviewResponseModel preview : data) {
                    // These are the values that will be collected.

                    int post_id;
                    String mTitle;
                    String mText;
                    ArrayList<TagModel> mTags;
                    int mLikes;
                    boolean mIsLike;
                    String mAuthor;
                    String mAuthorImage;
                    String mCreateDate;
                    String mImage;
                    boolean mIsMy;
                    ArrayList<UserFeedCommentModel> mComments;

                    mTitle = preview.getTitle();
                    mText = preview.getText();
                    mTags = preview.getTags();
                    mLikes = preview.getLikes();
                    mIsLike = preview.isLike();
                    mAuthor = preview.getAuthor();
                    mAuthorImage = preview.getAuthorImage();
                    mCreateDate = preview.getCreateDate();
                    mImage = preview.getImage();
                    mIsMy = preview.isMy();


                    ContentValues previewValues = new ContentValues();

                    previewValues.put(FeedContract.PreviewEntry.COLUMN_TITLE, mTitle);
                    previewValues.put(FeedContract.PreviewEntry.COLUMN_TEXT, mText);
                    previewValues.put(FeedContract.PreviewEntry.COLUMN_LIKES, mLikes);
                    if (mIsLike) {

                        previewValues.put(FeedContract.PreviewEntry.COLUMN_IS_LIKE, 1);

                    } else {

                        previewValues.put(FeedContract.PreviewEntry.COLUMN_IS_LIKE, 0);
                    }

                    previewValues.put(FeedContract.PreviewEntry.COLUMN_AUTHOR, mAuthor);

                    if (mAuthorImage == null) {
                        previewValues.put(FeedContract.PreviewEntry.COLUMN_AUTHOR_IMAGE, "");
                    } else {
                        previewValues.put(FeedContract.PreviewEntry.COLUMN_AUTHOR_IMAGE, mAuthorImage);
                    }
                    previewValues.put(FeedContract.PreviewEntry.COLUMN_CREATE_DATE, mCreateDate);

                    if (mImage == null) {
                        previewValues.put(FeedContract.PreviewEntry.COLUMN_IMAGE, "");
                    } else {
                        previewValues.put(FeedContract.PreviewEntry.COLUMN_IMAGE, mImage);
                    }

                    if (mIsMy) {
                        previewValues.put(FeedContract.PreviewEntry.COLUMN_IS_MY, 1);
                    } else {
                        previewValues.put(FeedContract.PreviewEntry.COLUMN_IS_MY, 0);
                    }


                    cVVector.add(previewValues);
                    i = i + 1;
                }

                // add to database
                if (cVVector.size() > 0) {
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    App.instance().getContentResolver().bulkInsert(FeedContract.PreviewEntry.CONTENT_URI, cvArray);


                }

                Log.i(Utils.TAG_LOG, "Get News Complete. " + cVVector.size() + " Inserted");


                notifySuccess(Bundle.EMPTY);

            } else {


                notifyError(Bundle.EMPTY);

            }


        } else {
            notifyError(Bundle.EMPTY);
        }

    }


    public static final Parcelable.Creator<GetNewsCommand> CREATOR = new Parcelable.Creator<GetNewsCommand>() {
        @Override
        public GetNewsCommand createFromParcel(Parcel source) {
            return new GetNewsCommand(source);
        }

        @Override
        public GetNewsCommand[] newArray(int size) {
            return new GetNewsCommand[size];
        }
    };

    @Override
    public void writeToParcel(int flags, Parcel dest) {
        dest.writeParcelable(mNewsRequestModel, flags);
    }

}
