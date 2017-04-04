package com.dbbest.amateurfeed.presenter;

import android.common.framework.Presenter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import com.dbbest.amateurfeed.app.net.command.Command;
import com.dbbest.amateurfeed.app.net.command.CommandResultReceiver;
import com.dbbest.amateurfeed.app.net.command.UpdateSettingsCommand;
import com.dbbest.amateurfeed.view.SplashView;

public class SplashPresenter extends Presenter<SplashView> implements
    CommandResultReceiver.CommandListener {

  private static final int CODE_PUSH_NOTIFIER = 0;
  private static final String TAG = SplashPresenter.class.getName();

  private CommandResultReceiver resultReceiver;

  @Override
  protected void onAttachView(@NonNull SplashView view) {
    super.onAttachView(view);
    if (resultReceiver == null) {
      resultReceiver = new CommandResultReceiver();
    }
    resultReceiver.setListener(this);
  }

  @Override
  protected void onDetachView(@NonNull SplashView view) {
    super.onDetachView(view);
    if (resultReceiver != null) {
      resultReceiver.setListener(null);
    }

  }

  @Override
  public void onSuccess(int code, Bundle data) {
    if (getView() != null) {
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

  public void updateUserSettings(boolean enablePush) {
    Command command = new UpdateSettingsCommand(enablePush);
    command.send(CODE_PUSH_NOTIFIER, resultReceiver);

  }
}
