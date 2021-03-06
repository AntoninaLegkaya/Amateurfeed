package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.request.RegistrationFaceBookRequestModel;
import com.dbbest.amateurfeed.app.net.request.RegistrationRequestModel;
import com.dbbest.amateurfeed.app.net.response.LoginResponseModel;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.model.AuthToken;

public class RegistrationFacebookCommand extends Command {

  public static final Creator<RegistrationFacebookCommand> CREATOR =
      new Creator<RegistrationFacebookCommand>() {
        @Override
        public RegistrationFacebookCommand createFromParcel(Parcel source) {
          return new RegistrationFacebookCommand(source);
        }

        @Override
        public RegistrationFacebookCommand[] newArray(int size) {
          return new RegistrationFacebookCommand[size];
        }
      };
  private final static String KEY_IS_COMPLETED_PROFILE = "profile_completed";
  private final RegistrationFaceBookRequestModel registrationFacebookRequest;

  public static boolean grabIsProfileCompleted(@NonNull Bundle bundle) {
    return bundle.getBoolean(KEY_IS_COMPLETED_PROFILE, false);
  }

  public RegistrationFacebookCommand(String code, double longitude, double latitude) {
    registrationFacebookRequest = new RegistrationFaceBookRequestModel(code, longitude, latitude);
  }

  private RegistrationFacebookCommand(Parcel in) {
    super(in);
    registrationFacebookRequest = in
        .readParcelable(RegistrationRequestModel.class.getClassLoader());
  }

  @Override
  public void writeToParcel(int flags, Parcel dest) {
    dest.writeParcelable(registrationFacebookRequest, flags);
  }

  @Override
  public void execute() {
    RestApiClient apiClient = App.getApiFactory().restClient();
    ResponseWrapper<LoginResponseModel> response = apiClient
        .registrationFacebook(registrationFacebookRequest);
    if (response != null) {
      if (response.isSuccessful() && response.data() != null) {
        LoginResponseModel data = response.data();
        AuthToken authToken = new AuthToken();
        authToken.update(data.getAccessToken());
      } else {
        Bundle bundle = getStatusBundle(response);
        notifyError(bundle);
      }
    }

  }
}