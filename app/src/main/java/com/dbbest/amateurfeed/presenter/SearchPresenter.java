package com.dbbest.amateurfeed.presenter;

import android.common.framework.Presenter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.app.net.command.Command;
import com.dbbest.amateurfeed.app.net.command.CommandResultReceiver;
import com.dbbest.amateurfeed.app.net.command.SearchCommand;
import com.dbbest.amateurfeed.data.PreviewEntry;
import com.dbbest.amateurfeed.model.Dictionary;
import com.dbbest.amateurfeed.model.NewsModel;
import com.dbbest.amateurfeed.view.SearchView;
import java.util.ArrayList;


public class SearchPresenter extends Presenter<SearchView>
    implements CommandResultReceiver.CommandListener {

  private static final int CODE_SEARCH_NEWS = 0;
  private CommandResultReceiver resultReceiver;
  private String TAG = SearchPresenter.class.getName();

  @Override
  protected void onAttachView(@NonNull SearchView view) {
    if (resultReceiver == null) {
      resultReceiver = new CommandResultReceiver();
    }
    resultReceiver.setListener(this);
  }

  @Override
  protected void onDetachView(@NonNull SearchView view) {
    if (resultReceiver != null) {
      resultReceiver.setListener(null);
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
          for (NewsModel news : dictionary.getNews()) {

            Cursor cursor = getPreviewByIdCursor(news.getId());
            if (cursor != null && cursor.moveToFirst()) {
              ids.add(String.valueOf(news.getId()));
              Log.i(TAG, "news : "
                  + String.valueOf(news.getId())
                  + " Found in BD: "
                  + cursor.moveToFirst());
            }
          }
          bundle.putStringArrayList("ids", ids);
          getView().initLoader(bundle);
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

  public void searchNews(String searchParam) {
    Command command = new SearchCommand(searchParam);
    command.send(CODE_SEARCH_NEWS, resultReceiver);
  }

  public Cursor getPreviewByIdCursor(long mIdPreview) {
    Uri uriPreview = PreviewEntry.buildGetPreviewById(mIdPreview);
    return App.instance().getContentResolver().query(uriPreview, null, null, null, null);
  }
}
