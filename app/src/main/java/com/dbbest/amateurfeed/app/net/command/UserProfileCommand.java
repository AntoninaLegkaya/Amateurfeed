package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.app.storage.processor.DataProcessor;
import com.dbbest.amateurfeed.app.storage.processor.UserPreferences;
import com.dbbest.amateurfeed.model.UserProfileModel;

public class UserProfileCommand extends Command {

  public static final Parcelable.Creator<UserProfileCommand> CREATOR =
      new Parcelable.Creator<UserProfileCommand>() {
        @Override
        public UserProfileCommand createFromParcel(Parcel source) {
          return new UserProfileCommand(source);
        }

        @Override
        public UserProfileCommand[] newArray(int size) {
          return new UserProfileCommand[size];
        }
      };
  private String TAG = UserProfileCommand.class.getName();

  public UserProfileCommand() {
  }

  private UserProfileCommand(Parcel in) {
    super(in);
  }

  @Override
  public void writeToParcel(int flags, Parcel dest) {
  }

  @Override
  public void execute() {

    RestApiClient apiClient = App.getApiFactory().restClient();
    DataProcessor dataProcessor = new DataProcessor();
    ResponseWrapper<UserProfileModel> response = apiClient
        .getUserInfo(dataProcessor.authToken().bearer());
    if (response != null) {
      if (response.isSuccessful() && response.data() != null) {
        UserProfileModel profileModel = response.data();
        UserPreferences.setCredentials(profileModel.getFullName(), profileModel.getEmail(),
            profileModel.getImage(),
            profileModel.getSkype(), profileModel.getAddress(), profileModel.getJob(),
            profileModel.getPhone());
        notifySuccess(Bundle.EMPTY);
        UserPreferences userPreferences = new UserPreferences();
        Log.i(TAG, userPreferences.toString());
      } else {
        notifyError(Bundle.EMPTY);
      }
    } else {
      notifyError(Bundle.EMPTY);
    }

  }
}