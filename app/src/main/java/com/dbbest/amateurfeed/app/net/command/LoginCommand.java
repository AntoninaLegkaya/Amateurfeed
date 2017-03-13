package com.dbbest.amateurfeed.app.net.command;


import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.request.LoginRequestModel;
import com.dbbest.amateurfeed.app.net.request.RegistrationRequestModel;
import com.dbbest.amateurfeed.app.net.response.LoginResponseModel;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.model.AuthToken;
import com.dbbest.amateurfeed.model.CurrentUser;


public class LoginCommand extends Command {

  public static final Parcelable.Creator<LoginCommand> CREATOR = new Parcelable.Creator<LoginCommand>() {
    @Override
    public LoginCommand createFromParcel(Parcel source) {
      return new LoginCommand(source);
    }

    @Override
    public LoginCommand[] newArray(int size) {
      return new LoginCommand[size];
    }
  };
  private final LoginRequestModel mLoginRequest;
  private String TAG = LoginCommand.class.getName();

  public LoginCommand(String email, String password, String deviceId, String osType,
      String deviceToken) {
    mLoginRequest = new LoginRequestModel(email, password, deviceId, osType, deviceToken);
  }

  private LoginCommand(Parcel in) {
    super(in);
    mLoginRequest = in.readParcelable(RegistrationRequestModel.class.getClassLoader());
  }

  @Override
  public void writeToParcel(int flags, Parcel dest) {
    dest.writeParcelable(mLoginRequest, flags);
  }

  @Override
  public void execute() {
    RestApiClient apiClient = App.getApiFactory().restClient();
    ResponseWrapper<LoginResponseModel> response = apiClient.login(mLoginRequest);
    if (response != null) {
      if (response.isSuccessful() && response.data() != null) {
        LoginResponseModel data = response.data();
        AuthToken authToken = new AuthToken();
        authToken.update(data.getAccessToken());
        notifySuccess(Bundle.EMPTY);
        CurrentUser user = new CurrentUser();
        user.setId(data.getUserId());
        user.setName(data.getUserName());
        user.setRole(data.getRole());
        user.setProfileImage(data.getProfileImage());
      } else {
        notifyError(Bundle.EMPTY);
      }
    } else {
      notifyError(Bundle.EMPTY);
    }
  }
}
