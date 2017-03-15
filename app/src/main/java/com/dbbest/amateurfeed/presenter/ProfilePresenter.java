package com.dbbest.amateurfeed.presenter;

import android.common.framework.Presenter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.dbbest.amateurfeed.app.net.command.Command;
import com.dbbest.amateurfeed.app.net.command.CommandResultReceiver;
import com.dbbest.amateurfeed.app.net.command.GetMyNewsCommand;
import com.dbbest.amateurfeed.view.ProfileView;
import java.util.ArrayList;


public class ProfilePresenter extends Presenter<ProfileView> implements
    CommandResultReceiver.CommandListener {

  private static final int CODE_GET_MY_NEWS = 0;
  private CommandResultReceiver mResultReceiver;

  public void getNews() {
    Command command = new GetMyNewsCommand();
    command.send(CODE_GET_MY_NEWS, mResultReceiver);
  }

  @Override
  protected void onAttachView(@NonNull ProfileView view) {
    if (mResultReceiver == null) {
      mResultReceiver = new CommandResultReceiver();
    }
    mResultReceiver.setListener(this);
  }

  @Override
  protected void onDetachView(@NonNull ProfileView view) {
    if (mResultReceiver != null) {
      mResultReceiver.setListener(null);
    }
  }

  @Override
  public void onSuccess(int code, Bundle data) {
    ArrayList<String> ids = new ArrayList<>();
    if (getView() != null) {
      if (code == CODE_GET_MY_NEWS) {
        if (data != null) {
          getView().initLoader(data);
        }
      }
    }
  }


  @Override
  public void onFail(int code, Bundle data) {
  }

  @Override
  public void onProgress(int code, Bundle data, int progress) {
  }
}

