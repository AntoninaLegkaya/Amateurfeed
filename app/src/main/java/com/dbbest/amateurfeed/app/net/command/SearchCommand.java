package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.model.Dictionary;


public class SearchCommand extends Command {

  public static final Parcelable.Creator<SearchCommand> CREATOR = new Parcelable.Creator<SearchCommand>() {
    @Override
    public SearchCommand createFromParcel(Parcel source) {
      return new SearchCommand(source);
    }

    @Override
    public SearchCommand[] newArray(int size) {
      return new SearchCommand[size];
    }
  };
  private String mSearchParam;

  public SearchCommand(String searchParam) {
    mSearchParam = searchParam;
  }

  private SearchCommand(Parcel in) {
    super(in);
    mSearchParam = in.readString();
  }

  @Override
  public void writeToParcel(int flags, Parcel dest) {
    dest.writeString(mSearchParam);
  }

  @Override
  public void execute() {
    RestApiClient apiClient = App.getApiFactory().restClient();
    ResponseWrapper<Dictionary> response = apiClient.searchNews(mSearchParam);
    if (response != null) {
      if (response.isSuccessful() && response.data() != null) {
        Bundle bundle = new Bundle();
        Dictionary dictionary = response.data();
        if (dictionary != null) {
          bundle.putParcelable("dictionary", dictionary);
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