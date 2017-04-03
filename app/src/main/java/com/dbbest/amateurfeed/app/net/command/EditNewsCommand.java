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
  private final String TAG = EditNewsCommand.class.getName();
  private ArrayList<TagModel> mTagModels = new ArrayList<>();
  private String title;
  private String text;
  private String image;
  private int id;

  public EditNewsCommand(ArrayList<TagModel> tagModels, String title, String text, String image,
      int id) {
    this.id = id;
    mTagModels = tagModels;
    this.title = title;
    this.text = text;
    this.image = image;
    for (TagModel model : tagModels) {
      Log.i(TAG, "What you give NewsUpdateModel Tag: " + model.getName() + '\n');
    }
  }

  public EditNewsCommand(Parcel in) {
    super(in);
    id = in.readInt();
    title = in.readString();
    text = in.readString();
    image = in.readString();
    mTagModels = new ArrayList<>();
    in.readTypedList(mTagModels, TagModel.CREATOR);
  }

  @Override
  public void writeToParcel(int flags, Parcel dest) {
    dest.writeInt(id);
    dest.writeString(title);
    dest.writeString(text);
    dest.writeString(image);
    dest.writeTypedList(mTagModels);
  }

  @Override
  public void execute() {
    RestApiClient apiClient = App.getApiFactory().restClient();
    AuthToken authToken = new AuthToken();
    NewsUpdateModel mNewsUpdateModel = new NewsUpdateModel(mTagModels, title, text, image);
    Log.i(TAG, "Edit News [_ID]: " + id + "\n" +
        "Title: " + mNewsUpdateModel.getTitle() + '\n' +
        "Description: " + mNewsUpdateModel.getText() + '\n' +
        "Image: " + mNewsUpdateModel.getImage());
    ArrayList<TagModel> tags = mNewsUpdateModel.getTags();
    for (TagModel model : tags) {
      Log.i(TAG, "Tag: " + model.getName() + '\n');
    }
    ResponseWrapper<NewsResponseModel> response = apiClient
        .editNews(authToken.bearer(), mNewsUpdateModel, id);
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