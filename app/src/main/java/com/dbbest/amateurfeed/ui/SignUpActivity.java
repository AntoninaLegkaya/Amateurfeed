package com.dbbest.amateurfeed.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.presenter.SignUpPresenter;
import com.dbbest.amateurfeed.ui.util.UiActivityNavigation;
import com.dbbest.amateurfeed.view.SignUpView;

/**
 * Created by antonina on 19.01.17.
 */

public class SignUpActivity extends AppCompatActivity implements SignUpView {

    private SignUpPresenter mPresenter;
    private TextView mLoginScreenLink;
    private TextView mResetPswLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        mPresenter = new SignUpPresenter();
        mLoginScreenLink = (TextView) findViewById(R.id.login_link);
        mLoginScreenLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(UiActivityNavigation.startActivity(SignUpActivity.this));
            }
        });
        mResetPswLink = (TextView) findViewById(R.id.reset_link);
        mResetPswLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.reset_link) {
                    startActivity(UiActivityNavigation.resetPassActivity(SignUpActivity.this));
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
