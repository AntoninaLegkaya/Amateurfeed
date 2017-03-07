package com.dbbest.amateurfeed.presenter;

import android.common.framework.Presenter;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.dbbest.amateurfeed.app.net.command.AddCommentCommand;
import com.dbbest.amateurfeed.app.net.command.AddNewItemNewsCommand;
import com.dbbest.amateurfeed.app.net.command.CheckTagCommand;
import com.dbbest.amateurfeed.app.net.command.Command;
import com.dbbest.amateurfeed.app.net.command.CommandResultReceiver;
import com.dbbest.amateurfeed.app.net.command.EditNewsCommand;
import com.dbbest.amateurfeed.model.CommentModel;
import com.dbbest.amateurfeed.model.TagModel;
import com.dbbest.amateurfeed.view.DetailView;

import java.util.List;

import static android.R.attr.id;
import static android.R.attr.tag;

public class DetailPresenter extends Presenter<DetailView> implements CommandResultReceiver.CommandListener {

    private static final int CODE_CHECK_TAG = 0;
    private static final int CODE_EDIT_NEWS = 1;
    private static final int CODE_ADD_COMMENT = 2;
    private static final int CODE_ADD_NEW_ITEM = 3;
    private CommandResultReceiver mResultReceiver;


    public void checkTag(String tag) {
        if (getView() != null) {
            DetailView view = getView();

        }
        Command command = new CheckTagCommand(tag);
        command.send(CODE_CHECK_TAG, mResultReceiver);


    }

    public void updateNews(List<TagModel> tagModels, String title, String text, String image, int id) {
        if (getView() != null) {
            DetailView view = getView();

        }
        Command command = new EditNewsCommand(tagModels, title, text, image, id);
        command.send(CODE_EDIT_NEWS, mResultReceiver);


    }

    public void postComment(int postId, String body, int parentCommentId) {
        if (getView() != null) {
            DetailView view = getView();

        }
        AddCommentCommand command = new AddCommentCommand(postId, body, parentCommentId);
        command.send(CODE_ADD_COMMENT, mResultReceiver);


    }

    public void addNewNews(String title, String text, String image,List<TagModel> tagModels) {
        if (getView() != null) {
            DetailView view = getView();

        }
        Command command = new AddNewItemNewsCommand(title, text, image, tagModels);
        command.send(CODE_ADD_NEW_ITEM, mResultReceiver);


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
            if (code == CODE_CHECK_TAG) {

                getView().addTagToItemDetail(data);
            }
            if (code == CODE_EDIT_NEWS) {

                getView().updateDetailsFields(data);

            }
            if (code == CODE_ADD_COMMENT) {

                getView().showSuccessAddCommentDialog();

            }
            if (code == CODE_ADD_NEW_ITEM) {

                getView().refreshFeedNews(data);

            }

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
