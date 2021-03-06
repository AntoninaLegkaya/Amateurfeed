package com.dbbest.amateurfeed.presenter;

import android.common.framework.Presenter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.dbbest.amateurfeed.app.net.NetworkUtil;
import com.dbbest.amateurfeed.app.net.command.Command;
import com.dbbest.amateurfeed.app.net.command.CommandResultReceiver;
import com.dbbest.amateurfeed.app.net.command.DeleteNewsCommand;
import com.dbbest.amateurfeed.app.net.command.GetNewsCommand;
import com.dbbest.amateurfeed.app.net.command.SetLikeCommand;
import com.dbbest.amateurfeed.view.HomeView;


public class HomePresenter extends Presenter<HomeView> implements
    CommandResultReceiver.CommandListener {

  private static final int CODE_GET_NEWS = 0;
  private static final int CODE_LIKE_NEWS = 1;
  private static final int CODE_DELETE_NEWS = 2;
  private CommandResultReceiver resultReceiver;

  @Override
  protected void onAttachView(@NonNull HomeView view) {
    if (resultReceiver == null) {
      resultReceiver = new CommandResultReceiver();
    }
    resultReceiver.setListener(this);
  }

  @Override
  protected void onDetachView(@NonNull HomeView view) {
    if (resultReceiver != null) {
      resultReceiver.setListener(null);
    }
  }

  @Override
  public void onSuccess(int code, Bundle data) {
    if (getView() != null) {
      if (code == CODE_GET_NEWS) {
        getView().refreshFragmentFeedLoader();
      } else if (code == CODE_LIKE_NEWS) {
        getView().showSuccessLikeDialog();
        getView().updateColumnLikeInBd();
      } else if (code == CODE_DELETE_NEWS) {
        getView().showSuccessDeleteDialog();
      }
    }
  }

  @Override
  public void onFail(int code, Bundle data) {
    if (getView() != null) {
      getView().dismissProgressDialog();
      int errCode = Command.grabErrorCode(data);
      if (errCode == NetworkUtil.CODE_SOCKET_TIMEOUT
          || errCode == NetworkUtil.CODE_UNABLE_TO_RESOLVE_HOST) {
        getView().showErrorConnectionDialog();
      } else {
        getView().showErrorDialog();
        if (code == CODE_LIKE_NEWS) {
          getView().showErrorLikeDialog();
        }
        if (code == CODE_DELETE_NEWS) {
          getView().showErrorDeleteDialog();
        }
      }
    }
  }

  @Override
  public void onProgress(int code, Bundle data, int progress) {
  }

  public void putLike(long id, int isLike) {
    boolean like = true;
    if (isLike == 0) {
      like = false;
    }
    Command command = new SetLikeCommand(id, like);
    command.send(CODE_LIKE_NEWS, resultReceiver);
  }

  public void putDelete(long id, String comment) {

    Command command = new DeleteNewsCommand((int) id, comment);
    command.send(CODE_DELETE_NEWS, resultReceiver);
  }

  public void getNews(int offset, int count) {
    Command command = new GetNewsCommand(offset, count);
    command.send(CODE_GET_NEWS, resultReceiver);
  }
}