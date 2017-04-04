package com.dbbest.amateurfeed.app.net.command;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.data.UserNewsEntry;
import com.dbbest.amateurfeed.model.AuthToken;
import com.dbbest.amateurfeed.model.UserFeedCommentModel;
import com.dbbest.amateurfeed.model.UserNewsModel;
import java.util.ArrayList;
import java.util.Vector;

public class GetUserNewsCommand extends Command {

  public static final Parcelable.Creator<GetUserNewsCommand> CREATOR = new Parcelable.Creator<GetUserNewsCommand>() {
    @Override
    public GetUserNewsCommand createFromParcel(Parcel source) {
      return new GetUserNewsCommand(source);
    }

    @Override
    public GetUserNewsCommand[] newArray(int size) {
      return new GetUserNewsCommand[size];
    }
  };
  private String TAG = GetUserNewsCommand.class.getName();

  public GetUserNewsCommand() {
  }

  private GetUserNewsCommand(Parcel in) {
    super(in);
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

        App.instance().getContentResolver().delete(com.dbbest.amateurfeed.data.UserNewsEntry.CONTENT_URI, null, null);
        ArrayList<UserNewsModel> userNewsModels = response.data();
        Bundle bundle = new Bundle();
        if (userNewsModels != null) {
          bundle.putParcelableArrayList("list", userNewsModels);

          // Insert the new news information into the database
          Vector<ContentValues> cVVector = new Vector<>(userNewsModels.size());

          int i = 0;
          for (UserNewsModel preview : userNewsModels) {
            // These are the values that will be collected.

            int _id;
            String title;
            String updateDate;
            String status;
            String image;
            int likes;

            ArrayList<UserFeedCommentModel> mComments;

            _id = preview.getId();
            title = preview.getTitle();
            updateDate = preview.getUpdateDate();
            status = preview.getStatus();
            image = preview.getImage();
            likes = preview.getLikes();

            ContentValues previewValues = new ContentValues();

            previewValues.put(UserNewsEntry._ID, _id);
            previewValues.put(UserNewsEntry.COLUMN_TITLE, title);
            previewValues.put(UserNewsEntry.COLUMN_UPDATE_DATE, updateDate);
            previewValues.put(UserNewsEntry.COLUMN_STATUS, status);
            if (image == null) {
              previewValues.put(UserNewsEntry.COLUMN_IMAGE, "");
            } else {
              previewValues.put(UserNewsEntry.COLUMN_IMAGE, image);
            }
            previewValues.put(UserNewsEntry.COLUMN_LIKES, likes);
            cVVector.add(previewValues);
            i = i + 1;
          }

          // add to  preview table
          if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            App.instance().getContentResolver()
                .bulkInsert(com.dbbest.amateurfeed.data.UserNewsEntry.CONTENT_URI, cvArray);
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
