package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.model.AuthToken;
import com.dbbest.amateurfeed.model.DeviceInfoModel;

public class UpdateDeviceInfoCommand extends Command {

  public static final Parcelable.Creator<UpdateDeviceInfoCommand> CREATOR =
      new Parcelable.Creator<UpdateDeviceInfoCommand>() {
        @Override
        public UpdateDeviceInfoCommand createFromParcel(Parcel source) {
          return new UpdateDeviceInfoCommand(source);
        }

        @Override
        public UpdateDeviceInfoCommand[] newArray(int size) {
          return new UpdateDeviceInfoCommand[size];
        }
      };
  private DeviceInfoModel mDeviceInfoModel;

  public UpdateDeviceInfoCommand(String osType, String deviceToken, String deviceId) {
    mDeviceInfoModel = new DeviceInfoModel(osType, deviceToken, deviceId);
  }

  protected UpdateDeviceInfoCommand(Parcel in) {
    super(in);
    mDeviceInfoModel = in.readParcelable(DeviceInfoModel.class.getClassLoader());
  }

  @Override
  public void writeToParcel(int flags, Parcel dest) {
    dest.writeParcelable(mDeviceInfoModel, flags);
  }

  @Override
  public void execute() {

    RestApiClient apiClient = App.getApiFactory().restClient();
    AuthToken authToken = new AuthToken();
    ResponseWrapper<Object> response = apiClient
        .updateDeviceInfo(authToken.bearer(), mDeviceInfoModel);
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
