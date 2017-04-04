package com.dbbest.amateurfeed.view;

import android.common.framework.IView;
import android.support.annotation.NonNull;


public interface StartView extends IView {

  void showEmptyEmailError();

  void showEmailValidationError();

  void showEmptyPasswordError();

  void showPasswordLengthValidationError();

  void showPasswordValidationError();

  void showErrorConnectionDialog();

  void showErrorLoginDialog();

  void showProgressDialog();

  void dismissProgressDialog();

  void navigateToHomeScreen();

  void showErrorIncorrectPassword();

  void requestPermission(int code, @NonNull String... permissions);

}
