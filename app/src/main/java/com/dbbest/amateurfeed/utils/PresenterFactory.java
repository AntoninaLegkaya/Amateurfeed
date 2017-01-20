package com.dbbest.amateurfeed.utils;

import com.dbbest.amateurfeed.presenter.HomePresenter;
import com.dbbest.amateurfeed.presenter.PasswordResetPresenter;
import com.dbbest.amateurfeed.presenter.SignUpPresenter;
import com.dbbest.amateurfeed.presenter.StartPresenter;
import com.dbbest.amateurfeed.utils.intarface.IPresenterFactory;

/**
 * Created by antonina on 20.01.17.
 */

public class PresenterFactory implements IPresenterFactory {
    @Override
    public StartPresenter startPresenter() {
        return new StartPresenter();
    }

    @Override
    public SignUpPresenter signUpPresenter() {
        return new SignUpPresenter();
    }

    @Override
    public PasswordResetPresenter passwordResetPresenter() {
        return new PasswordResetPresenter();
    }

    @Override
    public HomePresenter homePresenter() {
        return new HomePresenter();
    }
}
