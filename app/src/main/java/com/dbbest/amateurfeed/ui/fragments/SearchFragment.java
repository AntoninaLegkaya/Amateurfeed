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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.data.FeedContract;
import com.dbbest.amateurfeed.data.FeedProvider;
import com.dbbest.amateurfeed.data.adapter.GridViewAdapter;
import com.dbbest.amateurfeed.presenter.SearchPresenter;
import com.dbbest.amateurfeed.view.SearchView;

import static com.dbbest.amateurfeed.ui.fragments.FeedNewsFragment.PREVIEW_COLUMNS;


public class SearchFragment extends Fragment implements SearchView, LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {
    private static final String PARAM_KEY = "param_key";
    private static final int SEARCH_NEWS_LOADER = 1;
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
                Log.i(SEARCH_FRAGMENT, "Click in search button");
                mPresenter.searchNews(mSearchField.getText().toString());
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.search_feed_list_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGridViewAdapter = new GridViewAdapter(getActivity());
        mGridViewAdapter.swapCursor(null);

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
            if (view.getId() == R.id.search_button) {


            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri previewListUri = FeedContract.PreviewEntry.CONTENT_URI;
        String sortOrder = FeedContract.PreviewEntry.COLUMN_CREATE_DATE + " DESC";
        String selection = FeedProvider.sPreviewSelection;
        String[] selectionArg = args.getStringArray("ids");
        if (selectionArg != null) {
            int count = args.getStringArray("ids").length;
            Log.i(SEARCH_FRAGMENT, "Items: count= " + count);
            for (int i = 0; i < count - 1; i++) {

                selection = selection + " OR " + selection;
            }
            Log.i(SEARCH_FRAGMENT, "Query to  DB:  get Preview info items: Selection: " + selection);
            return new CursorLoader(getActivity(),
                    previewListUri,
                    PREVIEW_COLUMNS,
                    selection,
                    selectionArg,
                    sortOrder
            );
        } else return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        mGridViewAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void initLoader(Bundle data) {
        getLoaderManager().initLoader(SEARCH_NEWS_LOADER, data, this);
    }
}