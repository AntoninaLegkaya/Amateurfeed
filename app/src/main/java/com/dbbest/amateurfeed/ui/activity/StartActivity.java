package com.dbbest.amateurfeed.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.dbbest.amateurfeed.BuildConfig;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.presenter.StartPresenter;
import com.dbbest.amateurfeed.ui.navigator.UIDialogNavigation;
import com.dbbest.amateurfeed.ui.navigator.UiActivityNavigation;
import com.dbbest.amateurfeed.view.StartView;

public class StartActivity extends AppCompatActivity implements StartView {

  private StartPresenter presenter;
  private AppCompatEditText loginEdit;
  private AppCompatEditText passwordEdit;
  private DialogFragment progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login);
    presenter = new StartPresenter();
    loginEdit = (AppCompatEditText) findViewById(R.id.text_login);
    passwordEdit = (AppCompatEditText) findViewById(R.id.text_password);
    Button loginBtn = (Button) findViewById(R.id.button_login);
    final String deviceId = Settings.Secure.ANDROID_ID;
    loginBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        presenter.login(loginEdit.getText().toString(), passwordEdit.getText().toString(),
            deviceId, getString(R.string.os_device), BuildConfig.OPEN_FIREBASE_API_KEY);
      }
    });
//    CallbackManager callbackManager = CallbackManager.Factory.create();
    TextView signUpLink = (TextView) findViewById(R.id.text_sign_up_link);
    signUpLink.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (view.getId() == R.id.text_sign_up_link) {
          finish();
          startActivity(UiActivityNavigation.registerActivity(StartActivity.this));
        }
      }
    });
    TextView resetPswLink = (TextView) findViewById(R.id.text_reset_link);
    resetPswLink.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (view.getId() == R.id.text_reset_link) {
          finish();
          startActivity(UiActivityNavigation.resetPassActivity(StartActivity.this));
        }
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
    finish();
    startActivity(UiActivityNavigation.homeActivity(StartActivity.this));
  }

  @Override
  public void showErrorIncorrectPassword() {
    UIDialogNavigation.showWarningDialog(R.string.incorrectPassword)
        .show(getSupportFragmentManager(), "warn");
  }

  @Override
  public void requestPermission(int code, @NonNull String... permissions) {
    if (permissions.length > 0) {
      ActivityCompat.requestPermissions(this, permissions, code);
    }
  }

  @Override
  public void showErrorConnectionDialog() {
    UIDialogNavigation.showWarningDialog(R.string.no_internet_connection)
        .show(getSupportFragmentManager(), "warn");
  }

  @Override
  public void showErrorLoginDialog() {
    UIDialogNavigation.showWarningDialog(R.string.incorrectLoginDialog)
        .show(getSupportFragmentManager(), "warn");
  }

  @NonNull
  @Override
  public Context getContext() {
    return this;
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    presenter.onPermissionsRequestResult(requestCode, grantResults);
  }
}
