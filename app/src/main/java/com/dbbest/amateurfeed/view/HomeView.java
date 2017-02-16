package com.dbbest.amateurfeed.view;

import android.common.framework.IView;
import android.support.annotation.NonNull;


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
