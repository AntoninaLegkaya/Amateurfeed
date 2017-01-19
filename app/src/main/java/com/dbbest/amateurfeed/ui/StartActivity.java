package com.dbbest.amateurfeed.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.view.StartView;

public class StartActivity extends AppCompatActivity  implements StartView{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
