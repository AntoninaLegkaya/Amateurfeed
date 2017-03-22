package com.dbbest.amateurfeed.view;

import android.common.framework.IView;

public interface ChangePasswordView extends IView {

  void showSuccessChangePasswordDialog();

  void showErrorChangePasswordDialog();

  void navigateToPreferenceScreen();
}
