package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.request.ResetRequestPasswordModel;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;


public class ResetPasswordCommand extends Command {

  public static final Parcelable.Creator<ResetPasswordCommand> CREATOR = new Parcelable.Creator<ResetPasswordCommand>() {
    @Override
    public ResetPasswordCommand createFromParcel(Parcel source) {
      return new ResetPasswordCommand(source);
    }

    @Override
    public ResetPasswordCommand[] newArray(int size) {
      return new ResetPasswordCommand[size];
    }
  };
  private final ResetRequestPasswordModel mResetRequest;

  public ResetPasswordCommand(String email) {
    mResetRequest = new ResetRequestPasswordModel(email);
  }

  @Override
  public void writeToParcel(int flags, Parcel dest) {
    dest.writeParcelable(mResetRequest, flags);
  }

  @Override
  public void execute() {

    RestApiClient apiClient = App.getApiFactory().restClient();
    ResponseWrapper<Object> response = apiClient.forgotPassword(mResetRequest);
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

  protected ResetPasswordCommand(Parcel in) {

    super(in);
    mResetRequest = in.readParcelable(ResetRequestPasswordModel.class.getClassLoader());
  }
}
