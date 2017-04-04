package com.dbbest.amateurfeed.presenter;

import android.common.framework.Presenter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import com.dbbest.amateurfeed.app.net.command.Command;
import com.dbbest.amateurfeed.app.net.command.CommandResultReceiver;
import com.dbbest.amateurfeed.app.net.command.LogoutCommand;
import com.dbbest.amateurfeed.app.net.command.UpdateSettingsCommand;
import com.dbbest.amateurfeed.view.PreferenceView;

public class PreferencePresenter extends Presenter<PreferenceView> implements
    CommandResultReceiver.CommandListener {

  private static final int CODE_LOGOUT = 0;
  private static final int CODE_PUSH_NOTIFIER = 1;
  private static final String TAG = PreferencePresenter.class.getName();

  private CommandResultReceiver resultReceiver;

  @Override
  protected void onAttachView(@NonNull PreferenceView view) {
    if (resultReceiver == null) {
      resultReceiver = new CommandResultReceiver();
    }
    resultReceiver.setListener(this);
  }

  @Override
  protected void onDetachView(@NonNull PreferenceView view) {
    if (resultReceiver != null) {
      resultReceiver.setListener(null);
    }
  }

  @Override
  public void onSuccess(int code, Bundle data) {
    if (getView() != null) {
      if (code == CODE_LOGOUT) {
        getView().navigateToStartScreen();
      }
      if (code == CODE_PUSH_NOTIFIER) {
        Log.i(TAG, "Push notification enable from Server!");
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
    logoutCommand.send(CODE_LOGOUT, resultReceiver);
  }

  public void updateUserSettings(boolean enablePush) {
    Command command = new UpdateSettingsCommand(enablePush);
    command.send(CODE_PUSH_NOTIFIER, resultReceiver);

  }
}
