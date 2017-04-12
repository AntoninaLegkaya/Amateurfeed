package com.dbbest.amateurfeed.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.dbbest.amateurfeed.BuildConfig;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.presenter.SignUpPresenter;
import com.dbbest.amateurfeed.ui.navigator.UIDialogNavigation;
import com.dbbest.amateurfeed.ui.navigator.UiActivityNavigation;
import com.dbbest.amateurfeed.view.SignUpView;


public class SignUpActivity extends AppCompatActivity implements SignUpView {

  private SignUpPresenter presenter;
  private DialogFragment progressDialog;
  private AppCompatEditText emailEditText;
  private AppCompatEditText nameEditText;
  private AppCompatEditText phoneEditText;
  private AppCompatEditText passwordEditText;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up);
    final String deviceId = Settings.Secure.ANDROID_ID;
    presenter = new SignUpPresenter();
    emailEditText = (AppCompatEditText) findViewById(R.id.text_email);
    nameEditText = (AppCompatEditText) findViewById(R.id.text_full_name);
    phoneEditText = (AppCompatEditText) findViewById(R.id.text_phone);
    passwordEditText = (AppCompatEditText) findViewById(R.id.text_password);
    TextView loginScreenLink = (TextView) findViewById(R.id.text_login_link);
    loginScreenLink.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
        startActivity(UiActivityNavigation.startActivity(SignUpActivity.this));
      }
    });
    TextView resetPswLink = (TextView) findViewById(R.id.text_reset_link);
    resetPswLink.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (view.getId() == R.id.text_reset_link) {
          finish();
          startActivity(UiActivityNavigation.resetPassActivity(SignUpActivity.this));
        }
      }
    });
    Button signUpButton = (Button) findViewById(R.id.button_sign_up);
    signUpButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        presenter
            .registration(emailEditText.getText().toString(), nameEditText.getText().toString(),
                phoneEditText.getText().toString(), null, passwordEditText.getText().toString(),
                deviceId, getString(R.string.os_device), BuildConfig.OPEN_FIREBASE_API_KEY);
      }
    });

  }

  @Override
  protected void onStart() {
    super.onStart();
    presenter.attachView(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
    presenter.detachView();
  }

  @Override
  public void showEmptyEmailError() {
    UIDialogNavigation.showWarningDialog(R.string.emptyEmailError)
        .show(getSupportFragmentManager(), "warn");
  }

  @Override
  public void showEmailValidationError() {
    UIDialogNavigation.showWarningDialog(R.string.incorrectEmail)
        .show(getSupportFragmentManager(), "warn");
  }

  @Override
  public void showEmptyPasswordError() {
    UIDialogNavigation.showWarningDialog(R.string.emptyPasswordError)
        .show(getSupportFragmentManager(), "warn");
  }

  @Override
  public void showEmptyFullNameError() {
    UIDialogNavigation.showWarningDialog(R.string.emptyNameError)
        .show(getSupportFragmentManager(), "warn");
  }

  @Override
  public void showFullNameValidationError() {
    UIDialogNavigation.showWarningDialog(R.string.incorrectName)
        .show(getSupportFragmentManager(), "warn");
  }

  @Override
  public void showPhoneValidationError() {
    UIDialogNavigation.showWarningDialog(R.string.incorrectPhone)
        .show(getSupportFragmentManager(), "warn");
  }

  @Override
  public void showPasswordLengthValidationError() {
    UIDialogNavigation.showWarningDialog(R.string.lengthPasswordError)
        .show(getSupportFragmentManager(), "warn");
  }

  @Override
  public void showPasswordValidationError() {
    UIDialogNavigation.showWarningDialog(R.string.characterPasswordError)
        .show(getSupportFragmentManager(), "warn");
  }


  @Override
  public void showSuccessDialog() {
    UIDialogNavigation.showWarningDialog(R.string.success_registration)
        .show(getSupportFragmentManager(), "warn");
  }

  @Override
  public void showErrorConnectionDialog() {
    UIDialogNavigation.showWarningDialog(R.string.no_internet_connection)
        .show(getSupportFragmentManager(), "warn");
  }

  @Override
  public void showErrorRegistrationDialog() {
    UIDialogNavigation.showWarningDialog(R.string.error_registration)
        .show(getSupportFragmentManager(), "warn");
  }

  @Override
  public void showProgressDialog() {
    progressDialog = UIDialogNavigation.showProgressDialog();
    progressDialog.show(getSupportFragmentManager(), "progress");
  }

  @Override
  public void dismissProgressDialog() {
    if (progressDialog != null) {
      progressDialog.dismiss();
    }
  }

  @Override
  public void navigateToHomeScreen() {
    startActivity(UiActivityNavigation.homeActivity(SignUpActivity.this));
  }

  @NonNull
  @Override
  public Context getContext() {
    return this;
  }
}
