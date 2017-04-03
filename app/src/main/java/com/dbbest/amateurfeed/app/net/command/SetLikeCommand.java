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
  private final long id;
  private LikeModel likeRequestModel;

  public SetLikeCommand(long id, boolean isLike) {
    this.id = id;
    likeRequestModel = new LikeModel(isLike);
  }

  private SetLikeCommand(Parcel in) {
    super(in);
    likeRequestModel = in.readParcelable(LikeModel.class.getClassLoader());
    id = in.readInt();
  }

  @Override
  public void writeToParcel(int flags, Parcel dest) {
    dest.writeParcelable(likeRequestModel, flags);
    dest.writeLong(id);
  }

  @Override
  public void execute() {
    AuthToken authToken = new AuthToken();
    RestApiClient apiClient = App.getApiFactory().restClient();
    ResponseWrapper<Object> response = apiClient.isLike(authToken.bearer(), id, likeRequestModel);
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
