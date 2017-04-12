package com.dbbest.amateurfeed.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.presenter.PasswordResetPresenter;
import com.dbbest.amateurfeed.ui.navigator.UIDialogNavigation;
import com.dbbest.amateurfeed.ui.navigator.UiActivityNavigation;
import com.dbbest.amateurfeed.view.ResetPasswordView;


public class ResetPasswordActivity extends AppCompatActivity implements ResetPasswordView {

  private PasswordResetPresenter presenter;
  private AppCompatEditText emailEdit;
  private Button resetBtn;
  private DialogFragment progressDialog;
  private TextView loginScreenLink;
  private TextView signUpLink;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reset_password);
    presenter = new PasswordResetPresenter();
    emailEdit = (AppCompatEditText) findViewById(R.id.text_login);
    resetBtn = (Button) findViewById(R.id.button_reset_password);
    resetBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        presenter.resetPassword(emailEdit.getText().toString());
      }
    });
    loginScreenLink = (TextView) findViewById(R.id.text_login_link);
    loginScreenLink.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
        startActivity(UiActivityNavigation.startActivity(ResetPasswordActivity.this));
      }
    });
    signUpLink = (TextView) findViewById(R.id.text_sign_up_link);
    signUpLink.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (view.getId() == R.id.text_sign_up_link) {
          finish();
          startActivity(UiActivityNavigation.registerActivity(ResetPasswordActivity.this));
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
  public void showSuccessDialog() {
    UIDialogNavigation.showWarningDialog(R.string.info_dialog_reset_password)
        .show(getSupportFragmentManager(), "info");
  }

  @Override
  public void showErrorDialog() {
    UIDialogNavigation.showWarningDialog(R.string.error_dialog_reset_password)
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
    UIDialogNavigation.showWarningDialog(R.string.no_internet_connection)
        .show(getSupportFragmentManager(), "warn");
  }
}
