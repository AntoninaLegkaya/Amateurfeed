package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.request.RegistrationRequestModel;
import com.dbbest.amateurfeed.app.net.response.LoginResponseModel;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.model.AuthToken;


public class RegistrationCommand extends Command {

  public static final Parcelable.Creator<RegistrationCommand> CREATOR =
      new Parcelable.Creator<RegistrationCommand>() {
        @Override
        public RegistrationCommand createFromParcel(Parcel source) {
          return new RegistrationCommand(source);
        }

        @Override
        public RegistrationCommand[] newArray(int size) {
          return new RegistrationCommand[size];
        }
      };
  private final RegistrationRequestModel registrationRequest;
  private final String TAG = RegistrationCommand.class.getName();

  public RegistrationCommand(String email, String fullName, String phone, String address,
      String password, String deviceId, String osType, String deviceToken) {
    registrationRequest = new RegistrationRequestModel(email, fullName, phone, address, password,
        deviceId, osType, deviceToken);
  }

  private RegistrationCommand(Parcel in) {
    super(in);
    registrationRequest = in.readParcelable(RegistrationRequestModel.class.getClassLoader());
  }

  @Override
  public void writeToParcel(int flags, Parcel dest) {
    dest.writeParcelable(registrationRequest, flags);
  }

  @Override
  public void execute() {
    RestApiClient apiClient = App.getApiFactory().restClient();
    ResponseWrapper<LoginResponseModel> response = apiClient.registration(registrationRequest);
    if (response != null) {
      if (response.isSuccessful() && response.data() != null) {

        LoginResponseModel data = response.data();
        AuthToken authToken = new AuthToken();
        authToken.update(data.getAccessToken());
        authToken.updateFcmToken(registrationRequest.getDeviceToken());
        authToken.updateDeviceId(registrationRequest.getDeviceId());
        authToken.updateDeviceOs(registrationRequest.getOsType());
        Log.i(TAG, authToken.toString());
        notifySuccess(Bundle.EMPTY);
      } else {
        notifyError(Bundle.EMPTY);
      }
    } else {
      notifyError(Bundle.EMPTY);
    }
  }
}
