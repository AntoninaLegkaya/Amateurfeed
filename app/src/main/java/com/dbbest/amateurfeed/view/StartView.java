package com.dbbest.amateurfeed.view;

import android.common.framework.IView;
import android.support.annotation.NonNull;

/**
 * Created by antonina on 19.01.17.
 */

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

    void showPasswordResetSuccess();

    void requestPermission(int code, @NonNull String... permissions);

    void showSuccessRegistrationDialog();

    void onSocialLoginCompleted();

    void showErrorIncorrectPassword();

    void showBannedAccountError();
}
