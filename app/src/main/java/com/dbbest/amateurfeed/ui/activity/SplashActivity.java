package com.dbbest.amateurfeed.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.dbbest.amateurfeed.model.AuthToken;
import com.dbbest.amateurfeed.presenter.SplashPresenter;
import com.dbbest.amateurfeed.ui.navigator.UiActivityNavigation;
import com.dbbest.amateurfeed.utils.Utils;
import com.dbbest.amateurfeed.view.SplashView;


public class SplashActivity extends AppCompatActivity implements SplashView {

  private static final int FADE_DELAY = 500;
  private SplashPresenter presenter;

  private Handler mHandler = new Handler(new Handler.Callback() {
    @Override
    public boolean handleMessage(Message msg) {
      if (msg.what == 1) {
        nextScreen();
      }
      return true;
    }
  });

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    presenter = new SplashPresenter();
    presenter.updateUserSettings(Utils.checkNotificationPref());
  }


  @Override
  protected void onPostResume() {
    super.onPostResume();
    mHandler.sendEmptyMessageDelayed(1, FADE_DELAY);
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
  protected void onPause() {
    super.onPause();
    mHandler.removeMessages(1);
  }

  @NonNull
  @Override
  public Context getContext() {
    return this;
  }

  private void nextScreen() {
    AuthToken authToken = new AuthToken();
    Intent intent;
    if (authToken.isValid()) {
      intent = UiActivityNavigation.homeActivity(SplashActivity.this);
    } else {
      intent = UiActivityNavigation.startActivity(SplashActivity.this);
    }
    startActivity(intent);
    finish();
  }

}
