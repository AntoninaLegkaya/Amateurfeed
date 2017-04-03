package com.dbbest.amateurfeed.view;

import android.common.framework.IView;


public interface HomeView extends IView {

  void showSuccessDialog();

  void showErrorConnectionDialog();

  void showErrorDialog();

  void showProgressDialog();

  void dismissProgressDialog();

  void navigateToScreen();

  void showSuccessLikeDialog();

  void showErrorLikeDialog();

  void showSuccessDeleteDialog();

  void showErrorDeleteDialog();

  void updateColumnLikeInBd();

  void refreshFragmentFeedLoader();

}
