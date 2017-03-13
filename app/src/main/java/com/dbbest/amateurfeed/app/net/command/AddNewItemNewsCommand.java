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
import com.dbbest.amateurfeed.model.NewsCreateModel;
import com.dbbest.amateurfeed.model.TagModel;
import java.util.List;


public class AddNewItemNewsCommand extends Command {

  public static final Parcelable.Creator<AddNewItemNewsCommand> CREATOR = new Parcelable.Creator<AddNewItemNewsCommand>() {
    @Override
    public AddNewItemNewsCommand createFromParcel(Parcel source) {
      return new AddNewItemNewsCommand(source);
    }

    @Override
    public AddNewItemNewsCommand[] newArray(int size) {
      return new AddNewItemNewsCommand[size];
    }
  };
  private NewsCreateModel mNewsCreateModel;
  private String TAG = AddNewItemNewsCommand.class.getName();

  public AddNewItemNewsCommand(String title, String text, String image, List<TagModel> tags) {
    mNewsCreateModel = new NewsCreateModel(title, text, image, tags);
  }

  public AddNewItemNewsCommand(Parcel in) {
    super(in);
    mNewsCreateModel = in.readParcelable(NewsCreateModel.class.getClassLoader());
  }

  @Override
  public void writeToParcel(int flags, Parcel dest) {
    dest.writeParcelable(mNewsCreateModel, flags);
  }

  @Override
  public void execute() {
    Log.i(TAG, "Start execute Add News Item Command: \n" + mNewsCreateModel.toString());
    RestApiClient apiClient = App.getApiFactory().restClient();
    AuthToken authToken = new AuthToken();
    ResponseWrapper<NewsResponseModel> response = apiClient
        .addNewNews(authToken.bearer(), mNewsCreateModel);
    if (response != null) {
      if (response.isSuccessful() && response.data() != null) {
        NewsResponseModel data = response.data();
        Log.i(TAG, "Created New Item By ID: " + data.getId());
        Bundle bundle = new Bundle();
        bundle.putInt("newsId", data.getId());
        notifySuccess(bundle);

      } else {

        notifyError(Bundle.EMPTY);
      }

    } else {

      notifyError(Bundle.EMPTY);
    }

  }
}

