package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.response.NewsResponseModel;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.model.AuthToken;
import com.dbbest.amateurfeed.model.NewsUpdateModel;
import com.dbbest.amateurfeed.model.TagModel;
import java.util.ArrayList;

public class EditNewsCommand extends Command {

  public static final Creator<EditNewsCommand> CREATOR = new Creator<EditNewsCommand>() {
    @Override
    public EditNewsCommand createFromParcel(Parcel source) {
      return new EditNewsCommand(source);
    }

    @Override
    public EditNewsCommand[] newArray(int size) {
      return new EditNewsCommand[size];
    }
  };
  private String TAG = EditNewsCommand.class.getName();
  private ArrayList<TagModel> mTagModels = new ArrayList<>();
  private String mTitle;
  private String mText;
  private String mImage;
  private int mId;

  public EditNewsCommand(ArrayList<TagModel> tagModels, String title, String text, String image,
      int id) {
    mId = id;
    mTagModels = tagModels;
    mTitle = title;
    mText = text;
    mImage = image;
    for (TagModel model : tagModels) {
      Log.i(TAG, "What you give NewsUpdateModel Tag: " + model.getName() + '\n');
    }
  }

  public EditNewsCommand(Parcel in) {
    super(in);
    mId = in.readInt();
    mTitle = in.readString();
    mText = in.readString();
    mImage = in.readString();
    mTagModels = new ArrayList<TagModel>();
    in.readTypedList(mTagModels, TagModel.CREATOR);
  }

  @Override
  public void writeToParcel(int flags, Parcel dest) {
    dest.writeInt(mId);
    dest.writeString(mTitle);
    dest.writeString(mText);
    dest.writeString(mImage);
    dest.writeTypedList(mTagModels);

  }

  @Override
  public void execute() {

    RestApiClient apiClient = App.getApiFactory().restClient();
    AuthToken authToken = new AuthToken();
    NewsUpdateModel mNewsUpdateModel = new NewsUpdateModel(mTagModels, mTitle, mText, mImage);
    Log.i(TAG, "Edit News [_ID]: " + mId + "\n" +
        "Title: " + mNewsUpdateModel.getTitle() + '\n' +
        "Description: " + mNewsUpdateModel.getText() + '\n' +
        "Image: " + mNewsUpdateModel.getImage());
    ArrayList<TagModel> tags = mNewsUpdateModel.getTags();
    for (TagModel model : tags) {
      Log.i(TAG, "Tag: " + model.getName() + '\n');
    }

    ResponseWrapper<NewsResponseModel> response = apiClient
        .editNews(authToken.bearer(), mNewsUpdateModel, mId);
    if (response != null) {
      if (response.isSuccessful() && response.data() != null) {
        NewsResponseModel data = response.data();
        Log.i(TAG, "Updated  Item By ID:  " + data.getId());
        Bundle bundle = new Bundle();
        bundle.putParcelable("model", mNewsUpdateModel);
        notifySuccess(bundle);

      } else {
        notifyError(Bundle.EMPTY);
      }
    } else {
      notifyError(Bundle.EMPTY);
    }
  }
}