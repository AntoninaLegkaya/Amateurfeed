package com.dbbest.amateurfeed.app.net.command;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.request.LikeModel;
import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import com.dbbest.amateurfeed.app.net.retrofit.RestApiClient;
import com.dbbest.amateurfeed.model.AuthToken;

public class SetLikeCommand extends Command {

  public static final Parcelable.Creator<SetLikeCommand> CREATOR = new Parcelable.Creator<SetLikeCommand>() {
    @Override
    public SetLikeCommand createFromParcel(Parcel source) {
      return new SetLikeCommand(source);
    }

    @Override
    public SetLikeCommand[] newArray(int size) {
      return new SetLikeCommand[size];
    }
  };
  private final long mId;
  private LikeModel mLikeRequestModel;

  private SetLikeCommand(Parcel in) {
    super(in);
    mLikeRequestModel = in.readParcelable(LikeModel.class.getClassLoader());
    mId = in.readInt();
  }

  public SetLikeCommand(long id, boolean isLike) {
    mId = id;
    mLikeRequestModel = new LikeModel(isLike);
  }

  @Override
  public void writeToParcel(int flags, Parcel dest) {
    dest.writeParcelable(mLikeRequestModel, flags);
    dest.writeLong(mId);
  }

  @Override
  public void execute() {
    AuthToken authToken = new AuthToken();
    RestApiClient apiClient = App.getApiFactory().restClient();
    ResponseWrapper<Object> response = apiClient.isLike(authToken.bearer(), mId, mLikeRequestModel);
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
