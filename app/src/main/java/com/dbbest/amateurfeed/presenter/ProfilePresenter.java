package com.dbbest.amateurfeed.presenter;

import android.common.framework.Presenter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.dbbest.amateurfeed.app.net.command.Command;
import com.dbbest.amateurfeed.app.net.command.CommandResultReceiver;
import com.dbbest.amateurfeed.app.net.command.GetUserNewsCommand;
import com.dbbest.amateurfeed.view.ProfileView;


public class ProfilePresenter extends Presenter<ProfileView> implements
    CommandResultReceiver.CommandListener {

  private static final int CODE_GET_MY_NEWS = 0;
  private CommandResultReceiver resultReceiver;

  @Override
  protected void onAttachView(@NonNull ProfileView view) {
    if (resultReceiver == null) {
      resultReceiver = new CommandResultReceiver();
    }
    resultReceiver.setListener(this);
  }

  @Override
  protected void onDetachView(@NonNull ProfileView view) {
    if (resultReceiver != null) {
      resultReceiver.setListener(null);
    }
  }

  @Override
  public void onSuccess(int code, Bundle data) {
    if (getView() != null) {
      if (code == CODE_GET_MY_NEWS) {
        if (data != null) {
          getView().initLoader(data);
        }
      }
    }
  }

  @Override
  public void onFail(int code, Bundle data) {
  }

  @Override
  public void onProgress(int code, Bundle data, int progress) {
  }

  public void getNews() {
    Command command = new GetUserNewsCommand();
    command.send(CODE_GET_MY_NEWS, resultReceiver);
  }

}

