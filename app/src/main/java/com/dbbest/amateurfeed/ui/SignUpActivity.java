package com.dbbest.amateurfeed.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.presenter.HomePresenter;
import com.dbbest.amateurfeed.presenter.SignUpPresenter;
import com.dbbest.amateurfeed.view.SignUpView;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

/**
 * Created by antonina on 19.01.17.
 */

public class SignUpActivity extends AppCompatActivity implements SignUpView {

    private SignUpPresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_ap_activity);
        mPresenter = new SignUpPresenter();

    }


    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.attachView(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.detachView();
    }

    @Override
    public void showEmptyEmailError() {

    }

    @Override
    public void showEmailValidationError() {

    }

    @Override
    public void showEmptyPasswordError() {

    }

    @Override
    public void showPasswordLengthValidationError() {

    }

    @Override
    public void showPasswordValidationError() {

    }

    @Override
    public void showEmptyConfirmPasswordError() {

    }

    @Override
    public void showConfirmPasswordValidationError() {

    }

    @Override
    public void showSuccessDialog() {

    }

    @Override
    public void showErrorConnectionDialog() {

    }

    @Override
    public void showErrorRegistrationDialog() {

    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void dismissProgressDialog() {

    }

    @NonNull
    @Override
    public Context getContext() {
        return null;
    }
}
