package com.dbbest.amateurfeed.presenter;

import android.common.framework.Presenter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.dbbest.amateurfeed.app.net.command.CommandResultReceiver;
import com.dbbest.amateurfeed.view.ChangePasswordView;

public class ChangePasswordPresenter extends Presenter<ChangePasswordView> implements
    CommandResultReceiver.CommandListener {

  private static final int CODE_CHANGE_PASSWORD = 0;

  private CommandResultReceiver mResetReceiver;


  @Override
  protected void onAttachView(@NonNull ChangePasswordView view) {
    if (mResetReceiver == null) {
      mResetReceiver = new CommandResultReceiver();
    }
    mResetReceiver.setListener(this);
  }

  @Override
  protected void onDetachView(@NonNull ChangePasswordView view) {
    if (mResetReceiver != null) {
      mResetReceiver.setListener(null);
    }
  }

  @Override
  public void onSuccess(int code, Bundle data) {

  }

  @Override
  public void onFail(int code, Bundle data) {

  }

  @Override
  public void onProgress(int code, Bundle data, int progress) {

  }
}
