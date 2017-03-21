package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.model.AuthToken;

public class LogoutCommand extends Command {

  public static final Parcelable.Creator<LogoutCommand> CREATOR =
      new Parcelable.Creator<LogoutCommand>() {
        @Override
        public LogoutCommand createFromParcel(Parcel source) {
          return new LogoutCommand(source);
        }

        @Override
        public LogoutCommand[] newArray(int size) {
          return new LogoutCommand[size];
        }
      };

  public LogoutCommand() {
  }

  protected LogoutCommand(Parcel in) {
    super(in);
  }

  @Override
  public void writeToParcel(int flags, Parcel dest) {
  }

  @Override
  public void execute() {
    RestApiClient apiClient = App.getApiFactory().restClient();
    AuthToken authToken = new AuthToken();
    ResponseWrapper<Object> response = apiClient.logout(authToken.bearer());
    if (response != null) {
      if (response.isSuccessful()) {
        notifySuccess(Bundle.EMPTY);
      } else {
        notifyError(Bundle.EMPTY);
      }
    } else {
      notifyError(Bundle.EMPTY);
    }

  }
}