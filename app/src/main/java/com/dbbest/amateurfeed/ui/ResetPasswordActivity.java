package com.dbbest.amateurfeed.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.presenter.PasswordResetPresenter;
import com.dbbest.amateurfeed.ui.util.UIDialogNavigation;
import com.dbbest.amateurfeed.ui.util.UiActivityNavigation;
import com.dbbest.amateurfeed.utils.Utils;
import com.dbbest.amateurfeed.view.ResetPasswordView;


public class ResetPasswordActivity extends AppCompatActivity implements ResetPasswordView {
    private PasswordResetPresenter mPresenter;

    private AppCompatEditText mEmailEdit;
    private Button mResetBtn;
    private DialogFragment mProgressDialog;
    private TextView mLoginScreenLink;
    private TextView mSignUpLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);
        mPresenter = new PasswordResetPresenter();

        mEmailEdit = (AppCompatEditText) findViewById(R.id.login);
        mResetBtn = (Button) findViewById(R.id.reset_button);
        mResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.resetPassword(mEmailEdit.getText().toString());
            }
        });
        mLoginScreenLink = (TextView) findViewById(R.id.login_link);
        mLoginScreenLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(UiActivityNavigation.startActivity(ResetPasswordActivity.this));

            }
        });
        mSignUpLink = (TextView) findViewById(R.id.sign_up_link);
        mSignUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.sign_up_link) {

                    startActivity(UiActivityNavigation.registerActivity(ResetPasswordActivity.this));

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
    public void showSuccessDialog() {

        UIDialogNavigation.showWarningDialog(R.string.info_dialog_reset_password).show(getSupportFragmentManager(), "info");

    }

    @Override
    public void showErrorDialog() {
        UIDialogNavigation.showWarningDialog(R.string.error_dialog_reset_password).show(getSupportFragmentManager(), "warn");
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
    public void navigateToStartScreen() {
        startActivity(UiActivityNavigation.startActivity(ResetPasswordActivity.this));
    }

    @NonNull
    @Override
    public Context getContext() {

        return this;
    }

    @Override
    public void showErrorConnectionDialog() {

        UIDialogNavigation.showWarningDialog(R.string.no_internet_connection).show(getSupportFragmentManager(), "warn");

    }
}
