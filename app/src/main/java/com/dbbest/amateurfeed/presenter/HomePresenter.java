package com.dbbest.amateurfeed.presenter;

import android.common.framework.Presenter;
import android.common.util.TextUtils;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.dbbest.amateurfeed.app.net.NetworkUtil;
import com.dbbest.amateurfeed.app.net.command.Command;
import com.dbbest.amateurfeed.app.net.command.CommandResultReceiver;
import com.dbbest.amateurfeed.app.net.command.GetNewsCommand;
import com.dbbest.amateurfeed.app.net.command.RegistrationCommand;
import com.dbbest.amateurfeed.utils.Utils;
import com.dbbest.amateurfeed.view.HomeView;
import com.dbbest.amateurfeed.view.SignUpView;

/**
 * Created by antonina on 20.01.17.
 */

public class HomePresenter extends Presenter<HomeView> implements CommandResultReceiver.CommandListener {

    private static final int CODE_GET_NEWS = 0;

    private CommandResultReceiver mResultReceiver;

    public void getNews( int offset, int count) {
        if (getView() != null) {
            HomeView view = getView();

            view.showProgressDialog();
        }
        Command command = new GetNewsCommand( offset,  count);
        command.send(CODE_GET_NEWS, mResultReceiver);


    }


    @Override
    protected void onAttachView(@NonNull HomeView view) {
        if (mResultReceiver == null) {
            mResultReceiver = new CommandResultReceiver();
        }
        mResultReceiver.setListener(this);
    }

    @Override
    protected void onDetachView(@NonNull HomeView view) {
        if (mResultReceiver != null) {
            mResultReceiver.setListener(null);
        }
    }


    @Override
    public void onSuccess(int code, Bundle data) {
        if (getView() != null) {
            getView().dismissProgressDialog();
            getView().showSuccessDialog();
        }
    }

    @Override
    public void onFail(int code, Bundle data) {
        if (getView() != null) {
            getView().dismissProgressDialog();
            int errCode = Command.grabErrorCode(data);
            if (errCode == NetworkUtil.CODE_SOCKET_TIMEOUT || errCode == NetworkUtil.CODE_UNABLE_TO_RESOLVE_HOST) {
                getView().showErrorConnectionDialog();
            } else {
                getView().showErrorDialog();
            }
        }
    }

    @Override
    public void onProgress(int code, Bundle data, int progress) {
        if (getView() != null) {
            getView().dismissProgressDialog();
        }
    }
}