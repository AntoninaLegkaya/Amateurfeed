package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.model.AuthToken;
import com.dbbest.amateurfeed.model.TagModel;
import java.util.ArrayList;

public class CheckTagCommand extends Command {

  public static final String TAG_MODEL = "tagModel";
  public static final Parcelable.Creator<CheckTagCommand> CREATOR = new Parcelable.Creator<CheckTagCommand>() {
    @Override
    public CheckTagCommand createFromParcel(Parcel source) {
      return new CheckTagCommand(source);
    }

    @Override
    public CheckTagCommand[] newArray(int size) {
      return new CheckTagCommand[size];
    }
  };
  private String TAG = CheckTagCommand.class.getName();
  private String mTagName;

  public CheckTagCommand(String tag) {
    mTagName = tag;
  }

  private CheckTagCommand(Parcel in) {
    super(in);
    mTagName = in.readString();
  }

  @Override
  public void writeToParcel(int flags, Parcel dest) {
    dest.writeString(mTagName);
  }

  @Override
  public void execute() {

    RestApiClient apiClient = App.getApiFactory().restClient();
    AuthToken authToken = new AuthToken();

    ResponseWrapper<ArrayList<TagModel>> response = apiClient
        .checkTagName(authToken.bearer(), mTagName);
    if (response != null) {
      if (response.isSuccessful() && response.data() != null) {
        ArrayList<TagModel> data = response.data();
        Bundle bundle = new Bundle();
        boolean flag = false;
        for (TagModel tag : data) {
          if (mTagName.equals(tag.getName())) {
            Log.i(TAG, "You check tag: " + mTagName + ": this new tag for news");
            flag = true;
            bundle.putParcelable(TAG_MODEL, new TagModel(tag.getId(), tag.getName()));
          }

        }
        if (flag) {
          notifySuccess(bundle);
        } else {
          notifyError(bundle);
        }

      } else {

        notifyError(Bundle.EMPTY);

      }

    } else {

      notifyError(Bundle.EMPTY);

    }


  }
}