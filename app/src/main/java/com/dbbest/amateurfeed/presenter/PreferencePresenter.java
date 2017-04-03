package com.dbbest.amateurfeed.presenter;

import android.common.framework.Presenter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.dbbest.amateurfeed.app.net.command.Command;
import com.dbbest.amateurfeed.app.net.command.CommandResultReceiver;
import com.dbbest.amateurfeed.app.net.command.LogoutCommand;
import com.dbbest.amateurfeed.view.PreferenceView;

public class PreferencePresenter extends Presenter<PreferenceView> implements
    CommandResultReceiver.CommandListener {

  private static final int CODE_LOGOUT = 0;

  private CommandResultReceiver resetReceiver;

  @Override
  protected void onAttachView(@NonNull PreferenceView view) {
    if (resetReceiver == null) {
      resetReceiver = new CommandResultReceiver();
    }
    resetReceiver.setListener(this);
  }

  @Override
  protected void onDetachView(@NonNull PreferenceView view) {
    if (resetReceiver != null) {
      resetReceiver.setListener(null);
    }
  }

  @Override
  public void onSuccess(int code, Bundle data) {
    if (getView() != null) {
      if (code == CODE_LOGOUT) {
        getView().navigateToStartScreen();
      }
    }
  }

  @Override
  public void onFail(int code, Bundle data) {

  }

  @Override
  public void onProgress(int code, Bundle data, int progress) {

  }

  public void logout() {
    Command logoutCommand = new LogoutCommand();
    logoutCommand.send(CODE_LOGOUT, resetReceiver);
  }
}
