package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.request.UpdateProfileRequestModel;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.model.AuthToken;

public class UpdateUserProfileCommand extends Command {

  public static final Parcelable.Creator<UpdateUserProfileCommand> CREATOR =
      new Parcelable.Creator<UpdateUserProfileCommand>() {
        @Override
        public UpdateUserProfileCommand createFromParcel(Parcel source) {
          return new UpdateUserProfileCommand(source);
        }

        @Override
        public UpdateUserProfileCommand[] newArray(int size) {
          return new UpdateUserProfileCommand[size];
        }
      };
  private UpdateProfileRequestModel profileRequestModel;

  public UpdateUserProfileCommand(String name, String email, String image, String phone,
      String job) {
    profileRequestModel = new UpdateProfileRequestModel(name, email, image, phone, job);
  }

  private UpdateUserProfileCommand(Parcel in) {
    super(in);
    profileRequestModel = in.readParcelable(UpdateProfileRequestModel.class.getClassLoader());
  }

  @Override
  public void writeToParcel(int flags, Parcel dest) {
    dest.writeParcelable(profileRequestModel, flags);
  }

  @Override
  public void execute() {

    RestApiClient apiClient = App.getApiFactory().restClient();
    AuthToken authToken = new AuthToken();
    ResponseWrapper<Object> response = apiClient
        .updateUserProfile(authToken.bearer(), profileRequestModel);
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
