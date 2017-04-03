package com.dbbest.amateurfeed.ui.fragments;

import static com.dbbest.amateurfeed.ui.fragments.FeedNewsFragment.PREVIEW_COLUMNS;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.data.FeedProvider;
import com.dbbest.amateurfeed.data.PreviewEntry;
import com.dbbest.amateurfeed.data.adapter.UserNewsAdapter;
import com.dbbest.amateurfeed.data.adapter.UserNewsAdapter.UserNewsHolder;
import com.dbbest.amateurfeed.presenter.SearchPresenter;
import com.dbbest.amateurfeed.ui.navigator.UIDialogNavigation;
import com.dbbest.amateurfeed.view.SearchView;
import java.util.ArrayList;


public class SearchFragment extends Fragment implements SearchView,
    LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

  public static final String TAG = SearchFragment.class.getName();
  public static final String IDS = "ids";
  private static final int SEARCH_NEWS_LOADER = 3;
  private AppCompatEditText searchField;
  private UserNewsAdapter userNewsAdapter;
  private SearchPresenter presenter;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    presenter = new SearchPresenter();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onStart() {
    super.onStart();
    presenter.attachView(this);
  }

  @Override
  public void onStop() {
    super.onStop();
    presenter.detachView();
  }

  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_search, container, false);
    searchField = (AppCompatEditText) view.findViewById(R.id.text_search);
    ImageButton deleteSearchParam = (ImageButton) view.findViewById(R.id.button_clear_search_param);
    deleteSearchParam.setOnClickListener(this);
    ImageButton searchButton = (ImageButton) view.findViewById(R.id.button_search);
    searchButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        userNewsAdapter.changeCursor(null);
        presenter.searchNews(searchField.getText().toString());
      }
    });
    RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.view_search_feed_list);

    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.setHasFixedSize(true);
    userNewsAdapter = new UserNewsAdapter(null, new UserNewsAdapter.SearchAdapterShowItemDetails() {
      @Override
      public void showItemDetailsFragment(UserNewsHolder vh, Uri uri,
          int typeItem) {
        ((Callback) getActivity()).showItemDetailsFragment(vh, uri, typeItem);
      }
    });
    GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(userNewsAdapter);
    return view;
  }


  @Override
  public void onClick(View view) {
    if (view != null) {
      if (view.getId() == R.id.button_clear_search_param) {
        if (searchField != null) {
          searchField.setText("");
        }
      }
    }
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    Uri previewListUri = PreviewEntry.CONTENT_URI;
    String sortOrder = PreviewEntry.COLUMN_CREATE_DATE + " DESC";
    String selection;
    ArrayList<String> selectionArgList = args.getStringArrayList("ids");
    String[] selectionArg;
    if (selectionArgList != null && !selectionArgList.isEmpty()) {
      selectionArg = selectionArgList.toArray(new String[selectionArgList.size()]);
      selection = FeedProvider.sPreviewSelectionId + " IN (";
      for (String aSelectionArg : selectionArg) {
        selection += aSelectionArg + ", ";
      }
      selection = selection.substring(0, selection.length() - 2) + ")";
      Log.i(TAG, "Query to  DB:  get Preview info : selection: " + selection
          + " Count selection arguments: " + selectionArg.length);
      return new CursorLoader(getActivity(),
          previewListUri,
          PREVIEW_COLUMNS,
          selection,
          null,
          null
      );
    } else {
      return null;
    }
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    if (loader.getId() == SEARCH_NEWS_LOADER && data != null) {
      userNewsAdapter.swapCursor(data);
    } else {
      showEmptySearchDialog();
    }
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    userNewsAdapter.swapCursor(null);
  }

  @Override
  public void initLoader(Bundle data) {
    ArrayList<String> list = data.getStringArrayList(IDS);
    if (list != null && !list.isEmpty()) {
      if (getLoaderManager().getLoader(SEARCH_NEWS_LOADER) != null) {
        getLoaderManager().restartLoader(SEARCH_NEWS_LOADER, data, this);
      } else if (getLoaderManager().getLoader(SEARCH_NEWS_LOADER) == null) {
        getLoaderManager().initLoader(SEARCH_NEWS_LOADER, data, this);
      }
    } else {
      showEmptySearchDialog();
    }
  }

  @Override
  public void showEmptySearchDialog() {
    UIDialogNavigation.showWarningDialog(R.string.search_error)
        .show(getActivity().getSupportFragmentManager(), "warn");
  }

  public interface Callback {
    void showItemDetailsFragment(UserNewsHolder vh, Uri uri, int typeItem);
  }
}