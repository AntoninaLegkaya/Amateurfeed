package com.dbbest.amateurfeed.utils.intarface;

import com.dbbest.amateurfeed.presenter.HomePresenter;
import com.dbbest.amateurfeed.presenter.PasswordResetPresenter;
import com.dbbest.amateurfeed.presenter.SignUpPresenter;
import com.dbbest.amateurfeed.presenter.StartPresenter;

/**
 * Created by antonina on 20.01.17.
 */

public interface IPresenterFactory {
    StartPresenter startPresenter();
    SignUpPresenter signUpPresenter();
    PasswordResetPresenter passwordResetPresenter();
    HomePresenter homePresenter();

}
