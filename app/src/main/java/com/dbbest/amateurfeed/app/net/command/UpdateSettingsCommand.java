package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.model.AuthToken;
import com.dbbest.amateurfeed.model.UserSettingsModel;

public class UpdateSettingsCommand extends Command {

  public static final Parcelable.Creator<UpdateSettingsCommand> CREATOR = new Parcelable.Creator<UpdateSettingsCommand>() {
    @Override
    public UpdateSettingsCommand createFromParcel(Parcel source) {
      return new UpdateSettingsCommand(source);
    }

    @Override
    public UpdateSettingsCommand[] newArray(int size) {
      return new UpdateSettingsCommand[size];
    }
  };
  private UserSettingsModel enablePushModel;

  public UpdateSettingsCommand(boolean isPush) {
    enablePushModel = new UserSettingsModel(isPush);
  }

  private UpdateSettingsCommand(Parcel in) {
    super(in);
    enablePushModel = in.readParcelable(UserSettingsModel.class.getClassLoader());
  }

  @Override
  public void writeToParcel(int flags, Parcel dest) {
    dest.writeParcelable(enablePushModel, flags);
  }

  @Override
  public void execute() {
    RestApiClient apiClient = App.getApiFactory().restClient();
    AuthToken authToken = new AuthToken();
    ResponseWrapper<Object> response = apiClient.updateSettings(authToken.bearer(), enablePushModel);
    if (response != null) {
      if (response.isSuccessful()) {
        Bundle bundle = new Bundle();
        notifySuccess(bundle);
      } else {
        notifyError(Bundle.EMPTY);
      }
    } else {
      notifyError(Bundle.EMPTY);
    }
  }
}
