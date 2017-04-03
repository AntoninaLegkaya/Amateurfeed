package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.request.ChangePasswordRequestModel;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.model.AuthToken;

public class ChangePasswordCommand extends Command {

  public static final Parcelable.Creator<ChangePasswordCommand> CREATOR =
      new Parcelable.Creator<ChangePasswordCommand>() {
        @Override
        public ChangePasswordCommand createFromParcel(Parcel source) {
          return new ChangePasswordCommand(source);
        }

        @Override
        public ChangePasswordCommand[] newArray(int size) {
          return new ChangePasswordCommand[size];
        }
      };
  private ChangePasswordRequestModel requestModel;

  public ChangePasswordCommand(String currentPassword, String newPassword, String confirmPassword) {
    requestModel = new ChangePasswordRequestModel(currentPassword, newPassword, confirmPassword);
  }

  private ChangePasswordCommand(Parcel in) {
    super(in);
    requestModel = in.readParcelable(ChangePasswordRequestModel.class.getClassLoader());
  }

  @Override
  public void writeToParcel(int flags, Parcel dest) {
    dest.writeParcelable(requestModel, flags);
  }

  @Override
  public void execute() {
    AuthToken authToken = new AuthToken();
    RestApiClient apiClient = App.getApiFactory().restClient();
    ResponseWrapper<Object> response = apiClient.changePassword(authToken.bearer(), requestModel);
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
