package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.response.NewsResponseModel;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.model.AuthToken;
import com.dbbest.amateurfeed.model.NewsCreateModel;
import com.dbbest.amateurfeed.model.TagModel;
import java.util.ArrayList;


public class AddNewItemNewsCommand extends Command {

  public static final Creator<AddNewItemNewsCommand> CREATOR = new Creator<AddNewItemNewsCommand>() {
    @Override
    public AddNewItemNewsCommand createFromParcel(Parcel source) {
      return new AddNewItemNewsCommand(source);
    }

    @Override
    public AddNewItemNewsCommand[] newArray(int size) {
      return new AddNewItemNewsCommand[size];
    }
  };
  public static final String NEWS_ID = "newsId";
  private final String TAG = AddNewItemNewsCommand.class.getName();
  private ArrayList<TagModel> tagModels = new ArrayList<>();
  private String title;
  private String text;
  private String image;

  public AddNewItemNewsCommand(ArrayList<TagModel> tagModels, String title, String text,
      String image
  ) {
    this.tagModels = tagModels;
    this.title = title;
    this.text = text;
    this.image = image;
    for (TagModel model : tagModels) {
      Log.i(TAG, "What you give NewsUpdateModel Tag: " + model.getName() + '\n');
    }
  }

  public AddNewItemNewsCommand(Parcel in) {
    super(in);
    title = in.readString();
    text = in.readString();
    image = in.readString();
    tagModels = new ArrayList<>();
    in.readTypedList(tagModels, TagModel.CREATOR);
  }

  @Override
  public void writeToParcel(int flags, Parcel dest) {
    dest.writeString(title);
    dest.writeString(text);
    dest.writeString(image);
    dest.writeTypedList(tagModels);

  }

  @Override
  public void execute() {

    RestApiClient apiClient = App.getApiFactory().restClient();
    AuthToken authToken = new AuthToken();
    NewsCreateModel mNewsCreateModel = new NewsCreateModel(title, text, image, tagModels);
    Log.i(TAG, "Edit News " + "\n" +
        "Title: " + mNewsCreateModel.getTitle() + '\n' +
        "Description: " + mNewsCreateModel.getText() + '\n' +
        "Image: " + mNewsCreateModel.getImage());
    ArrayList<TagModel> tags = mNewsCreateModel.getTags();
    for (TagModel model : tags) {
      Log.i(TAG, "Tag: " + model.getName() + '\n');
    }

    ResponseWrapper<NewsResponseModel> response = apiClient
        .addNewNews(authToken.bearer(), mNewsCreateModel);
    if (response != null) {
      if (response.isSuccessful() && response.data() != null) {
        NewsResponseModel data = response.data();
        Log.i(TAG, "Updated  Item By ID:  " + data.getId());
        Bundle bundle = new Bundle();
        bundle.putInt(NEWS_ID, data.getId());
        notifySuccess(bundle);

      } else {
        notifyError(Bundle.EMPTY);
      }
    } else {
      notifyError(Bundle.EMPTY);
    }
  }
}

