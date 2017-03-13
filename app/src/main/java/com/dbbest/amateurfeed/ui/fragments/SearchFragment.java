package com.dbbest.amateurfeed.ui.fragments;

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

import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.data.FeedContract;
import com.dbbest.amateurfeed.data.FeedProvider;
import com.dbbest.amateurfeed.data.adapter.GridViewAdapter;
import com.dbbest.amateurfeed.presenter.SearchPresenter;
import com.dbbest.amateurfeed.ui.util.UIDialogNavigation;
import com.dbbest.amateurfeed.view.SearchView;

import java.util.ArrayList;

import static com.dbbest.amateurfeed.ui.fragments.FeedNewsFragment.PREVIEW_COLUMNS;


public class SearchFragment extends Fragment implements SearchView, LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {
    private static final String PARAM_KEY = "param_key";
    private static final int SEARCH_NEWS_LOADER = 3;
    public static String SEARCH_FRAGMENT = "SearchFragment";
    private AppCompatEditText mSearchField;
    private ImageButton mDeleteSearchParam;
    private ImageButton mSearchButton;
    private GridViewAdapter mGridViewAdapter;
    private RecyclerView mRecyclerView;
    private SearchPresenter mPresenter;

    public static SearchFragment newInstance(String key) {
        SearchFragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_KEY, key);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SearchPresenter();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.attachView(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.detachView();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mSearchField = (AppCompatEditText) view.findViewById(R.id.item_search_text);
        mDeleteSearchParam = (ImageButton) view.findViewById(R.id.delete_param_button);
        mDeleteSearchParam.setOnClickListener(this);
        mSearchButton = (ImageButton) view.findViewById(R.id.search_button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGridViewAdapter.changeCursor(null);
                mPresenter.searchNews(mSearchField.getText().toString());
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.search_feed_list_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGridViewAdapter = new GridViewAdapter(null, 0, getContext(),

                new GridViewAdapter.SearchAdapterShowItemDetails() {
                    @Override
                    public void showItemDetailsFragment(GridViewAdapter.GridViewHolder vh, Uri uri, int typeItem) {
                        ((Callback) getActivity()).showItemDetailsFragment(vh, uri, typeItem);
                    }
                });
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mGridViewAdapter);

        return view;
    }


    @Override
    public void onClick(View view) {
        if (view != null) {

            if (view.getId() == R.id.delete_param_button) {

                if (mSearchField != null) {
                    mSearchField.setText("");
                }
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri previewListUri = FeedContract.PreviewEntry.CONTENT_URI;
        String sortOrder = FeedContract.PreviewEntry.COLUMN_CREATE_DATE + " DESC";
        String selection;
        ArrayList<String> selectionArgList = args.getStringArrayList("ids");
        String[] selectionArg;

        if (selectionArgList != null && !selectionArgList.isEmpty()) {
            selectionArg = selectionArgList.toArray(new String[selectionArgList.size()]);


            selection = FeedProvider.sPreviewSelectionId + " IN (";
            for (int i = 0; i < selectionArg.length; i++) {
                selection += selectionArg[i] + ", ";
            }
            selection = selection.substring(0, selection.length() - 2) + ")";

            Log.i(SEARCH_FRAGMENT, "Query to  DB:  get Preview info : selection: " + selection + " Count selection arguments: " + selectionArg.length);
            return new CursorLoader(getActivity(),
                    previewListUri,
                    PREVIEW_COLUMNS,
                    selection,
                    null,
                    null
            );
        } else return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(SearchFragment.SEARCH_FRAGMENT, " Loading finished");
        if (loader.getId() == SEARCH_NEWS_LOADER && data != null) {
            Log.i(SearchFragment.SEARCH_FRAGMENT, " Loading finished, data not Null");
            mGridViewAdapter.swapCursor(data);
        } else {

            showEmptySearchDialog();

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.i(SearchFragment.SEARCH_FRAGMENT, "Reset Loader");
        mGridViewAdapter.swapCursor(null);
    }

    @Override
    public void initLoader(Bundle data) {
        ArrayList<String> list = data.getStringArrayList("ids");
        if (!list.isEmpty()) {
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
        UIDialogNavigation.showWarningDialog(R.string.search_error).show(getActivity().getSupportFragmentManager(), "warn");
    }

    public interface Callback {
        public void showItemDetailsFragment(GridViewAdapter.GridViewHolder vh, Uri uri, int typeItem);
    }
}