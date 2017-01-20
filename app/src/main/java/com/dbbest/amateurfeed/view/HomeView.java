package com.dbbest.amateurfeed.view;

import android.common.framework.IView;
import android.support.annotation.NonNull;

/**
 * Created by antonina on 20.01.17.
 */

public interface HomeView extends IView {
    void requestPermission(int code, @NonNull String... permissions);

    void onLoginSuccess();

    void onLoginError();

}
