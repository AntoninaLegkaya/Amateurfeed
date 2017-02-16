package com.dbbest.amateurfeed.ui.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.data.FeedContract;
import com.dbbest.amateurfeed.data.adapter.PreviewAdapter;
import com.dbbest.amateurfeed.data.adapter.PreviewAdapter.FeedAdapterLoadNews;
import com.dbbest.amateurfeed.presenter.FeedListPresenter;
import com.dbbest.amateurfeed.utils.Utils;
import com.dbbest.amateurfeed.view.FeedView;
import com.melnykov.fab.FloatingActionButton;

/**
 * Created by antonina on 20.01.17.
 */

public class FeedNewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener, View.OnClickListener, FeedView {

    private static final String PARAM_KEY = "param_key";
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;

    private FeedListPresenter mPresenter;
    private PreviewAdapter mPreviewAdapter;
    private static final int NEWS_LOADER = 0;

    private int mChoiceMode;

    private static final String SELECTED_KEY = "selected_position";
    private boolean mHoldForTransition;
    private long mInitialSelectedDate = -1;
    private boolean mAutoSelectView;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    private static final String[] PREVIEW_COLUMNS = {


            FeedContract.PreviewEntry.TABLE_NAME + "." + FeedContract.PreviewEntry._ID,
            FeedContract.PreviewEntry.COLUMN_TITLE,
            FeedContract.PreviewEntry.COLUMN_TEXT,
            FeedContract.PreviewEntry.COLUMN_LIKES,
            FeedContract.PreviewEntry.COLUMN_IS_LIKE,
            FeedContract.PreviewEntry.COLUMN_AUTHOR,
            FeedContract.PreviewEntry.COLUMN_AUTHOR_IMAGE,
            FeedContract.PreviewEntry.COLUMN_CREATE_DATE,
            FeedContract.PreviewEntry.COLUMN_IMAGE,
            FeedContract.PreviewEntry.COLUMN_IS_MY


    };

    public static final int COL_FEED_ID = 0;
    //    public static final int COL_POST_ID_KEY = 1;
    public static final int COL_TITLTE = 1;
    public static final int COL_TEXT = 2;
    public static final int COL_LIKES = 3;
    public static final int COL_IS_LIKE = 4;
    public static final int COL_AUTHOR = 5;
    public static final int COL_AUTHOR_IMAGE = 6;
    public static final int COL_CREATE_DATE = 7;
    public static final int COL_IMAGE = 8;
    public static final int COL_IS_MY = 9;

    private static final String[] TAG_COLUMNS = {


            FeedContract.TagEntry.TABLE_NAME + "." + FeedContract.TagEntry._ID,
            FeedContract.TagEntry.COLUMN_NAME,
            FeedContract.TagEntry.COLUMN_TAG_ID,
            FeedContract.TagEntry.COLUMN_PREVIEW_ID,


    };

    public static final int COL_TAG_UNIC_ID = 0;
    public static final int COL_TAG_NAME = 1;
    public static final int COL_TAG_ID = 2;
    public static final int COL_TAG_PREVIEW_ID = 3;


    public static FeedNewsFragment newInstance(String key) {
        FeedNewsFragment fragment = new FeedNewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_KEY, key);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public interface Callback {

        public void onItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh);

        public void onLikeItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh);

        public void onCommentItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh);

        public void onEditItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh);

        public void onDeleteItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh);

        public void upLoadNewsItems(int count, int offset);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new FeedListPresenter();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.attachView(this);
        if (getArguments() != null) {
            mPresenter.search(getArguments().getString(PARAM_KEY, ""));
        } else {
            mPresenter.search("");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        // We hold for transition here just in-case the activity
        // needs to be re-created. In a standard return transition,
        // this doesn't actually make a difference.
        if (mHoldForTransition) {
            getActivity().supportPostponeEnterTransition();
        }
        getLoaderManager().initLoader(NEWS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.detachView();
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        TypedArray a = activity.obtainStyledAttributes(attrs, R.styleable.FeedNewsFragment,
                0, 0);
        mChoiceMode = a.getInt(R.styleable.FeedNewsFragment_android_choiceMode, AbsListView.CHOICE_MODE_NONE);
        mAutoSelectView = a.getBoolean(R.styleable.FeedNewsFragment_autoSelectView, false);
        mHoldForTransition = a.getBoolean(R.styleable.FeedNewsFragment_sharedElementTransitions, false);
        a.recycle();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        final View rootView = inflater.inflate(R.layout.fragment_feed_list, container, false);
        View emptyView = rootView.findViewById(R.id.recycle_feed_empty);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.feed_list_view);

        setInitialSelectedDate(Utils.getTodayLongDate());


        // Set the layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        final View parallaxView = rootView.findViewById(R.id.parallax_bar);
//        if (null != parallaxView) {
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//
//
//                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//                    @Override
//                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                        super.onScrolled(recyclerView, dx, dy);
//                        int max = parallaxView.getHeight();
//                        if (dy > 0) {
//
//                            parallaxView.setTranslationY(Math.max(-max, parallaxView.getTranslationY() - dy / 2));
//
//
//                        } else {
//                            parallaxView.setTranslationY(Math.min(0, parallaxView.getTranslationY() - dy / 2));
//                        }
//
//
//                    }
//                });
//
//            }
//
//
//        }


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);


        mPreviewAdapter = new PreviewAdapter(getContext(), emptyView, mChoiceMode,

                new PreviewAdapter.FeedAdapterOnClickHandler() {
                    @Override
                    public void onClick(PreviewAdapter.PreviewAdapterViewHolder vh, long id) {
                        ((Callback) getActivity()).onItemSelected(FeedContract.PreviewEntry.buildPreviewUriById(id), vh);
                        mPosition = vh.getAdapterPosition();
                    }
                },
                new PreviewAdapter.FeedCommentAdapterOnClickHandler() {
                    @Override
                    public void onClick(PreviewAdapter.PreviewAdapterViewHolder vh, long id) {
                        Uri uri = null;
                        ((Callback) getActivity()).onCommentItemSelected(uri, vh);
                    }
                },
                new PreviewAdapter.FeedLikeAdapterOnClickHandler() {
                    @Override
                    public void onClick(PreviewAdapter.PreviewAdapterViewHolder vh, long id) {
                        ((Callback) getActivity()).onLikeItemSelected(FeedContract.PreviewEntry.buildPreviewUriById(id), vh);
                        mPosition = vh.getAdapterPosition();

                    }
                },
                new PreviewAdapter.FeedEditAdapterOnClickHandler() {
                    @Override
                    public void onClick(PreviewAdapter.PreviewAdapterViewHolder vh, long id) {
                        Uri uri = null;
                        ((Callback) getActivity()).onEditItemSelected(uri, vh);
                    }
                },
                new PreviewAdapter.FeedRemoveAdapterOnClickHandler() {
                    @Override
                    public void onClick(PreviewAdapter.PreviewAdapterViewHolder vh, long id) {
                        Uri uri = null;
                        ((Callback) getActivity()).onDeleteItemSelected(FeedContract.PreviewEntry.buildPreviewUriById(id), vh);

                    }
                }, new FeedAdapterLoadNews() {

            @Override
            public void load(PreviewAdapter.PreviewAdapterViewHolder vh, int offset, int count) {
                ((Callback) getActivity()).upLoadNewsItems(offset, count);
                mPosition = vh.getAdapterPosition() - 1;
            }
        }
        );

        mPreviewAdapter.swapCursor(null);
        mRecyclerView.setAdapter(mPreviewAdapter);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.attachToRecyclerView(mRecyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "AddItem", Toast.LENGTH_SHORT).show();
            }
        });


        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SELECTED_KEY)) {
                // The RecyclerView probably hasn't even been populated yet.  Actually perform the
                // swapout in onLoadFinished.
                mPosition = savedInstanceState.getInt(SELECTED_KEY);
            }
            mPreviewAdapter.onRestoreInstanceState(savedInstanceState);
        }
        return rootView;
    }


    public void refreshFragmentLoader() {
        if (getLoaderManager().getLoader(NEWS_LOADER) != null) {
            getLoaderManager().restartLoader(NEWS_LOADER, null, this);
        }

    }


    @Override
    public void onClick(View view) {

    }

    private void updateEmptyView() {


        if (mPreviewAdapter.getItemCount() == 0) {

            TextView tv = (TextView) getView().findViewById(R.id.recycle_feed_empty);
            if (null != tv) {

                //if cursor is empty, why? do we have an invalid location

                int message = R.string.empty_feed_list;

                tv.setText(message);

            }

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to RecyclerView.NO_POSITION,
        // so check for that before storing.
        if (mPosition != RecyclerView.NO_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        mPreviewAdapter.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        updateEmptyView();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri previewListUri = FeedContract.PreviewEntry.CONTENT_URI;

        // Sort order:  Ascending, by date.
        String sortOrder = FeedContract.PreviewEntry.COLUMN_CREATE_DATE + " DESC";
        Log.i(Utils.TAG_LOG, "Query to  DB get Preview Table All items");
        return new CursorLoader(getActivity(),
                previewListUri,
                PREVIEW_COLUMNS,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mPreviewAdapter.swapCursor(data);
        mSwipeRefreshLayout.setRefreshing(false);


        updateEmptyView();
        if (data.getCount() == 0) {

            getActivity().supportStartPostponedEnterTransition();


        } else {

            mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    // Since we know we're going to get items, we keep the listener around until
                    // we see Children.
                    if (mRecyclerView.getChildCount() > 0) {
                        mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                        int position = mPosition;
//                                mPreviewAdapter.getSelectedItemPosition();

//                        Log.i(Utils.TAG_LOG, "Position Adapter: " + position);
                        if (position == RecyclerView.NO_POSITION &&
                                -1 != mInitialSelectedDate) {
                            Cursor data = mPreviewAdapter.getCursor();
                            int count = data.getCount();
                            int dateColumn = data.getColumnIndex(FeedContract.PreviewEntry.COLUMN_CREATE_DATE);
                            for (int i = 0; i < count; i++) {
                                data.moveToPosition(i);
                                if (Utils.getLongFromString(data.getString(dateColumn)) == mInitialSelectedDate) {
                                    position = i;
                                    Log.i(Utils.TAG_LOG, "Position Adapter: Get position current day " + position);
                                    break;
                                }
                            }
                        }
                        if (position == RecyclerView.NO_POSITION) position = 0;
//                        Log.i(Utils.TAG_LOG, "Position Adapter: Move to " + position);
                        mRecyclerView.smoothScrollToPosition(position);
                        RecyclerView.ViewHolder vh = mRecyclerView.findViewHolderForAdapterPosition(position);
                        if (null != vh && mAutoSelectView) {
                            mPreviewAdapter.selectView(vh);
                        }
                        if (mHoldForTransition) {
                            getActivity().supportStartPostponedEnterTransition();
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPreviewAdapter.swapCursor(null);
    }

    public void setInitialSelectedDate(long initialSelectedDate) {
        mInitialSelectedDate = initialSelectedDate;
    }
}