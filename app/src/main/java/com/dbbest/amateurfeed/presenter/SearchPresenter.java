package com.dbbest.amateurfeed.presenter;

import android.common.framework.Presenter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.command.CheckTagCommand;
import com.dbbest.amateurfeed.app.net.command.Command;
import com.dbbest.amateurfeed.app.net.command.CommandResultReceiver;
import com.dbbest.amateurfeed.app.net.command.SearchCommand;
import com.dbbest.amateurfeed.data.FeedContract;
import com.dbbest.amateurfeed.model.Dictionary;
import com.dbbest.amateurfeed.model.News;
import com.dbbest.amateurfeed.ui.fragments.SearchFragment;
import com.dbbest.amateurfeed.view.DetailView;
import com.dbbest.amateurfeed.view.SearchView;

import java.util.ArrayList;

import static android.R.attr.tag;

/**
 * Created by antonina on 23.01.17.
 */

public class SearchPresenter extends Presenter<SearchView> implements CommandResultReceiver.CommandListener {

    private static final int CODE_SEARCH_NEWS = 0;
    private CommandResultReceiver mResultReceiver;

    public void searchNews(String searchParam) {
        if (getView() != null) {
            SearchView view = getView();

        }
        Command command = new SearchCommand(searchParam);
        command.send(CODE_SEARCH_NEWS, mResultReceiver);
    }

    @Override
    protected void onAttachView(@NonNull SearchView view) {
        if (mResultReceiver == null) {
            mResultReceiver = new CommandResultReceiver();
        }
        mResultReceiver.setListener(this);
    }

    @Override
    protected void onDetachView(@NonNull SearchView view) {
        if (mResultReceiver != null) {
            mResultReceiver.setListener(null);
        }
    }

    @Override
    public void onSuccess(int code, Bundle data) {
        ArrayList<String> ids = new ArrayList<>();
        if (getView() != null) {
            if (code == CODE_SEARCH_NEWS) {
                if (data != null) {
                    Dictionary dictionary = (Dictionary) data.get("dictionary");
                    Bundle bundle = new Bundle();
                    for (News news : dictionary.getNews()) {

                        Cursor cursor = getPreviewByIdCursor(news.getId());
                        if (cursor.moveToFirst()) {
                            ids.add(String.valueOf(news.getId()));
                            Log.i(SearchFragment.SEARCH_FRAGMENT, "news : " + String.valueOf(news.getId()) + " Found in BD: " + cursor.moveToFirst());
                        }
                    }
                    Log.i(SearchFragment.SEARCH_FRAGMENT, "Compose Bundle with ids");
                    bundle.putStringArrayList("ids", ids);
                    getView().initLoader(bundle);
                }
            }


        }

    }

    protected Cursor getPreviewByIdCursor(long mIdPreview) {
        Uri uriPreview = FeedContract.PreviewEntry.buildGetPreviewById(mIdPreview);

        return App.instance().getContentResolver().query(
                uriPreview,
                null,
                null,
                null,
                null
        );
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
