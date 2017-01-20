package com.dbbest.amateurfeed.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.EditText;

import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.presenter.StartPresenter;
import com.dbbest.amateurfeed.ui.util.UiActivityNavigation;
import com.dbbest.amateurfeed.view.StartView;
import com.facebook.login.widget.LoginButton;

public class StartActivity extends AppCompatActivity implements StartView, View.OnClickListener {

    private StartPresenter mPresenter;

    private LoginButton mFacebookLoginButton;
    private AppCompatEditText mLoginEdit;
    private AppCompatEditText mPasswordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mPresenter = new StartPresenter();

        mLoginEdit = (AppCompatEditText) findViewById(R.id.login);
        mPasswordEdit = (AppCompatEditText) findViewById(R.id.password);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login_button) {

            mPresenter.login(mLoginEdit.getText().toString(), mPasswordEdit.getText().toString(), null, null, null);

        } else if (view.getId() == R.id.sign_up_button) {
            Intent intent = UiActivityNavigation.registerActivity(StartActivity.this);
            startActivity(intent);
        }

        //TODO Implement Facebook button login
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
    public void showErrorConnectionDialog() {

    }

    @Override
    public void showErrorLoginDialog() {

    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void dismissProgressDialog() {

    }

    @Override
    public void navigateToHomeScreen() {

    }

    @Override
    public void showPasswordResetSuccess() {

    }

    @Override
    public void requestPermission(int code, @NonNull String... permissions) {

    }

    @Override
    public void showSuccessRegistrationDialog() {

    }

    @Override
    public void onSocialLoginCompleted() {

    }

    @Override
    public void showErrorIncorrectPassword() {

    }

    @Override
    public void showBannedAccountError() {

    }

    @NonNull
    @Override
    public Context getContext() {
        return null;
    }


}
