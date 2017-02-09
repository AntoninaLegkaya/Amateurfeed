package com.dbbest.amateurfeed.view;

import android.common.framework.IView;
import android.support.annotation.NonNull;

/**
 * Created by antonina on 20.01.17.
 */

public interface HomeView extends IView {
    void showSuccessDialog();

    void showErrorConnectionDialog();

    void showErrorDialog();

    void showProgressDialog();

    void dismissProgressDialog();

    void navigateToScreen();

}
