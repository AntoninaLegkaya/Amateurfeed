package com.dbbest.amateurfeed.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.presenter.StartPresenter;
import com.dbbest.amateurfeed.ui.util.UIDialogNavigation;
import com.dbbest.amateurfeed.ui.util.UiActivityNavigation;
import com.dbbest.amateurfeed.utils.Utils;
import com.dbbest.amateurfeed.view.StartView;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class StartActivity extends AppCompatActivity implements StartView {

    private StartPresenter mPresenter;

    private LoginButton mFacebookLoginButton;
    private CallbackManager mCallbackManager;
    private AppCompatEditText mLoginEdit;
    private AppCompatEditText mPasswordEdit;
    private Button mLoginBtn;
    private TextView mSignUpLink;
    private TextView mResetPswLink;
    private DialogFragment mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mPresenter = new StartPresenter();

        mLoginEdit = (AppCompatEditText) findViewById(R.id.login);
        mPasswordEdit = (AppCompatEditText) findViewById(R.id.password);
        mLoginBtn = (Button) findViewById(R.id.login_button);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mPresenter.login(mLoginEdit.getText().toString(), mPasswordEdit.getText().toString(), null, null, null);
            }
        });
        mCallbackManager = CallbackManager.Factory.create();
        mFacebookLoginButton = (LoginButton) findViewById(R.id.login_fb_button);
        mFacebookLoginButton.setReadPermissions("email");
        mFacebookLoginButton.setReadPermissions("public_profile");
        mFacebookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        mSignUpLink = (TextView) findViewById(R.id.sign_up_link);
        mSignUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.sign_up_link) {

                    startActivity(UiActivityNavigation.registerActivity(StartActivity.this));


                }
            }
        });

        mResetPswLink = (TextView) findViewById(R.id.reset_link);
        mResetPswLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.reset_link) {

                    startActivity(UiActivityNavigation.resetPassActivity(StartActivity.this));


                }
            }
        });
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

        UIDialogNavigation.showWarningDialog(R.string.emptyEmailError).show(getSupportFragmentManager(), "warn");

    }

    @Override
    public void showEmailValidationError() {
        UIDialogNavigation.showWarningDialog(R.string.incorrectEmail).show(getSupportFragmentManager(), "warn");

    }

    @Override
    public void showEmptyPasswordError() {
        UIDialogNavigation.showWarningDialog(R.string.emptyPasswordError).show(getSupportFragmentManager(), "warn");
    }

    @Override
    public void showPasswordLengthValidationError() {
        UIDialogNavigation.showWarningDialog(R.string.lengthPasswordError).show(getSupportFragmentManager(), "warn");

    }

    @Override
    public void showPasswordValidationError() {
        UIDialogNavigation.showWarningDialog(R.string.characterPasswordError).show(getSupportFragmentManager(), "warn");
    }

    @Override
    public void showProgressDialog() {
        mProgressDialog = UIDialogNavigation.showProgressDialog();
        mProgressDialog.show(getSupportFragmentManager(), "progress");

    }

    @Override
    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }

    }

    @Override
    public void navigateToHomeScreen() {
        startActivity(UiActivityNavigation.homeActivity(StartActivity.this));
    }

    @Override
    public void showErrorIncorrectPassword() {
        UIDialogNavigation.showWarningDialog(R.string.incorrectPassword).show(getSupportFragmentManager(), "warn");

    }

    @Override
    public void showErrorConnectionDialog() {

        UIDialogNavigation.showWarningDialog(R.string.no_internet_connection).show(getSupportFragmentManager(), "warn");

    }

    @Override
    public void showErrorLoginDialog() {
        UIDialogNavigation.showWarningDialog(R.string.incorrectLoginDialog).show(getSupportFragmentManager(), "warn");

    }


    @NonNull
    @Override
    public Context getContext() {
        return this;
    }


}
