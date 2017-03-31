package com.dbbest.amateurfeed.app.net.command;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.data.FeedContract;
import com.dbbest.amateurfeed.data.FeedContract.UserNewsEntry;
import com.dbbest.amateurfeed.model.AuthToken;
import com.dbbest.amateurfeed.model.TagModel;
import com.dbbest.amateurfeed.model.UserFeedCommentModel;
import com.dbbest.amateurfeed.model.UserNewsModel;
import java.util.ArrayList;
import java.util.Vector;

public class GetMyNewsCommand extends Command {

  public static final Parcelable.Creator<GetMyNewsCommand> CREATOR = new Parcelable.Creator<GetMyNewsCommand>() {
    @Override
    public GetMyNewsCommand createFromParcel(Parcel source) {
      return new GetMyNewsCommand(source);
    }

    @Override
    public GetMyNewsCommand[] newArray(int size) {
      return new GetMyNewsCommand[size];
    }
  };
  private String TAG = GetMyNewsCommand.class.getName();

  private GetMyNewsCommand(Parcel in) {
    super(in);
  }

  public GetMyNewsCommand() {
  }

  @Override
  public void writeToParcel(int flags, Parcel dest) {

  }

  @Override
  public void execute() {
    RestApiClient apiClient = App.getApiFactory().restClient();
    AuthToken authToken = new AuthToken();
    ResponseWrapper<ArrayList<UserNewsModel>> response = apiClient.getMyNews(authToken.bearer());
    if (response != null) {
      if (response.isSuccessful() && response.data() != null) {

        App.instance().getContentResolver().delete(UserNewsEntry.CONTENT_URI, null, null);
        ArrayList<UserNewsModel> userNewsModels = response.data();
        Bundle bundle = new Bundle();
        if (userNewsModels != null) {
          bundle.putParcelableArrayList("list", userNewsModels);

          // Insert the new news information into the database
          Vector<ContentValues> cVVector = new Vector<ContentValues>(userNewsModels.size());

          int i = 0;
          for (UserNewsModel preview : userNewsModels) {
            // These are the values that will be collected.

            int _id;
            String mTitle;
            String mUpdateDate;
            String mStatus;
            String mImage;
            int mLikes;

            ArrayList<TagModel> mTags;
            int _idTag;
            String mNameTag;

            ArrayList<UserFeedCommentModel> mComments;

            _id = preview.getId();
            mTitle = preview.getTitle();
            mUpdateDate = preview.getUpdateDate();
            mStatus = preview.getStatus();
            mImage = preview.getImage();
            mLikes = preview.getLikes();

            ContentValues previewValues = new ContentValues();

            previewValues.put(UserNewsEntry._ID, _id);
            previewValues.put(UserNewsEntry.COLUMN_TITLE, mTitle);
            previewValues.put(UserNewsEntry.COLUMN_UPDATE_DATE, mUpdateDate);
            previewValues.put(UserNewsEntry.COLUMN_STATUS, mStatus);
            if (mImage == null) {
              previewValues.put(UserNewsEntry.COLUMN_IMAGE, "");
            } else {
              previewValues.put(UserNewsEntry.COLUMN_IMAGE, mImage);
            }
            previewValues.put(UserNewsEntry.COLUMN_LIKES, mLikes);
            cVVector.add(previewValues);
            i = i + 1;
          }

          // add to  preview table
          if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            App.instance().getContentResolver()
                .bulkInsert(FeedContract.UserNewsEntry.CONTENT_URI, cvArray);
          }
          Log.i(TAG, "Insert  My News Complete. " + cVVector.size() + " Inserted");
          notifySuccess(bundle);
        } else {
          notifyError(Bundle.EMPTY);
        }
      }
    } else {
      notifyError(Bundle.EMPTY);
    }

  }
}
