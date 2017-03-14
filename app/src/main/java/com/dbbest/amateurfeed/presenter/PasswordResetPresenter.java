package com.dbbest.amateurfeed.presenter;

import android.common.framework.Presenter;
import android.common.util.TextUtils;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.dbbest.amateurfeed.app.net.command.Command;
import com.dbbest.amateurfeed.app.net.command.CommandResultReceiver;
import com.dbbest.amateurfeed.app.net.command.ResetPasswordCommand;
import com.dbbest.amateurfeed.utils.Utils;
import com.dbbest.amateurfeed.view.ResetPasswordView;


public class PasswordResetPresenter extends Presenter<ResetPasswordView> implements
    CommandResultReceiver.CommandListener {

  private static final int CODE_RESET_PASSWORD = 0;

  private CommandResultReceiver mResetReceiver;

  public void resetPassword(String email) {

    if (getView() != null) {
      ResetPasswordView view = getView();

      if (TextUtils.isEmpty(email)) {
        view.showEmptyEmailError();
        return;
      } else if (!Utils.isEmailValid(email)) {
        view.showEmailValidationError();
        return;
      }

      view.showProgressDialog();
      Command command = new ResetPasswordCommand(email);
      command.send(CODE_RESET_PASSWORD, mResetReceiver);
    }
  }


  @Override
  protected void onAttachView(@NonNull ResetPasswordView view) {
    if (mResetReceiver == null) {
      mResetReceiver = new CommandResultReceiver();
    }
    mResetReceiver.setListener(this);
  }

  @Override
  protected void onDetachView(@NonNull ResetPasswordView view) {
    if (mResetReceiver != null) {
      mResetReceiver.setListener(null);
    }
  }


  @Override
  public void onSuccess(int code, Bundle data) {
    if (getView() != null) {
      getView().dismissProgressDialog();
      getView().showSuccessDialog();
      getView().navigateToStartScreen();
    }
  }

  @Override
  public void onFail(int code, Bundle data) {
    if (getView() != null) {
      getView().dismissProgressDialog();
      getView().showErrorDialog();
    }
  }

  @Override
  public void onProgress(int code, Bundle data, int progress) {
    if (getView() != null) {
      getView().dismissProgressDialog();
    }
  }
}
