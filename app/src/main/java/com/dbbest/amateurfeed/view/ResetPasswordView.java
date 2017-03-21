package com.dbbest.amateurfeed.view;

import android.common.framework.IView;


public interface ResetPasswordView extends IView {

  void showEmptyEmailError();

  void showEmailValidationError();

  void showSuccessDialog();

  void showErrorDialog();

  void showProgressDialog();

  void dismissProgressDialog();

  void showErrorConnectionDialog();

  void navigateToStartScreen();
}
