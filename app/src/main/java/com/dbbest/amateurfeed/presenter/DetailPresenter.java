package com.dbbest.amateurfeed.presenter;

import android.common.framework.Presenter;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.dbbest.amateurfeed.app.net.NetworkUtil;
import com.dbbest.amateurfeed.app.net.command.CheckTagCommand;
import com.dbbest.amateurfeed.app.net.command.Command;
import com.dbbest.amateurfeed.app.net.command.CommandResultReceiver;
import com.dbbest.amateurfeed.app.net.command.SetLikeCommand;
import com.dbbest.amateurfeed.view.DetailView;
import com.dbbest.amateurfeed.view.HomeView;
import com.dbbest.amateurfeed.view.SignUpView;

import static android.R.attr.id;
import static com.dbbest.amateurfeed.R.string.like;

public class DetailPresenter extends Presenter<DetailView> implements CommandResultReceiver.CommandListener {

    private static final int CODE_CHECK_TAG = 0;
    private CommandResultReceiver mResultReceiver;


    public void checkTag(String tag) {
        if (getView() != null) {
            DetailView view = getView();

        }
        Command command = new CheckTagCommand(tag);
        command.send(CODE_CHECK_TAG, mResultReceiver);


    }

    @Override
    protected void onAttachView(@NonNull DetailView view) {
        if (mResultReceiver == null) {
            mResultReceiver = new CommandResultReceiver();
        }
        mResultReceiver.setListener(this);
    }

    @Override
    protected void onDetachView(@NonNull DetailView view) {
        if (mResultReceiver != null) {
            mResultReceiver.setListener(null);
        }
    }


    @Override
    public void onSuccess(int code, Bundle data) {
        if (getView() != null) {
        }
    }

    @Override
    public void onFail(int code, Bundle data) {
        if (getView() != null) {
        }
    }

    @Override
    public void onProgress(int code, Bundle data, int progress) {

    }
}
