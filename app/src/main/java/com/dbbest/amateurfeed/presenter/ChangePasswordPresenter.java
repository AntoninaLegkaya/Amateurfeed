package com.dbbest.amateurfeed.presenter;

import android.common.framework.Presenter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.dbbest.amateurfeed.app.net.command.ChangePasswordCommand;
import com.dbbest.amateurfeed.app.net.command.Command;
import com.dbbest.amateurfeed.app.net.command.CommandResultReceiver;
import com.dbbest.amateurfeed.view.ChangePasswordView;

public class ChangePasswordPresenter extends Presenter<ChangePasswordView> implements
    CommandResultReceiver.CommandListener {

  private static final int CODE_CHANGE_PASSWORD = 0;

  private CommandResultReceiver resetReceiver;

  @Override
  protected void onAttachView(@NonNull ChangePasswordView view) {
    if (resetReceiver == null) {
      resetReceiver = new CommandResultReceiver();
    }
    resetReceiver.setListener(this);
  }

  @Override
  protected void onDetachView(@NonNull ChangePasswordView view) {
    if (resetReceiver != null) {
      resetReceiver.setListener(null);
    }
  }

  @Override
  public void onSuccess(int code, Bundle data) {
    if (getView() != null) {
      if (code == CODE_CHANGE_PASSWORD) {
        getView().showSuccessChangePasswordDialog();
        getView().navigateToPreferenceScreen();
      }
    }
  }

  @Override
  public void onFail(int code, Bundle data) {
    if (code == CODE_CHANGE_PASSWORD) {
      if (getView() != null) {
        getView().showErrorChangePasswordDialog();
      }
    }
  }

  @Override
  public void onProgress(int code, Bundle data, int progress) {

  }

  public void changePassword(String currentPassword, String password, String confirmPassword) {
    Command changePasswordCommand = new ChangePasswordCommand(currentPassword, password,
        confirmPassword);
    changePasswordCommand.send(CODE_CHANGE_PASSWORD, resetReceiver);
  }
}
