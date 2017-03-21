package com.dbbest.amateurfeed.ui.fragments;

import android.app.Activity;
import android.common.framework.IView;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.data.FeedContract;
import com.dbbest.amateurfeed.data.adapter.PreviewAdapter;
import com.dbbest.amateurfeed.data.adapter.PreviewAdapter.FeedAdapterLoadNews;
import com.dbbest.amateurfeed.utils.Utils;


public class FeedNewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
    SharedPreferences.OnSharedPreferenceChangeListener,
    View.OnClickListener, IView {

  public static final String[] PREVIEW_COLUMNS = {
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
  public static final int COL_TITLE = 1;
  public static final int COL_TEXT = 2;
  public static final int COL_LIKES = 3;
  public static final int COL_IS_LIKE = 4;
  public static final int COL_AUTHOR = 5;
  public static final int COL_AUTHOR_IMAGE = 6;
  public static final int COL_CREATE_DATE = 7;
  public static final int COL_IMAGE = 8;
  public static final int COL_IS_MY = 9;

  public static final String[] TAG_COLUMNS = {
      FeedContract.TagEntry.TABLE_NAME + "." + FeedContract.TagEntry._ID,
      FeedContract.TagEntry.COLUMN_NAME,
      FeedContract.TagEntry.COLUMN_TAG_ID,
      FeedContract.TagEntry.COLUMN_PREVIEW_ID,
  };

  public static final int COL_TAG_UNIC_ID = 0;
  public static final int COL_TAG_NAME = 1;
  public static final int COL_TAG_ID = 2;
  public static final int COL_TAG_PREVIEW_ID = 3;

  public static final String[] COMMENT_COLUMNS = {
      FeedContract.CommentEntry.TABLE_NAME + "." + FeedContract.CommentEntry._ID,
      FeedContract.CommentEntry.COLUMN_POST_ID,
      FeedContract.CommentEntry.COLUMN_CREATOR_KEY,
      FeedContract.CommentEntry.COLUMN_BODY,
      FeedContract.CommentEntry.COLUMN_PARENT_COMMENT_ID,
      FeedContract.CommentEntry.COLUMN_CREATE_DATE,
  };

  public static final int COL_COMMENT_UNIC_ID = 0;
  public static final int COL_COMMENT_POST_ID = 1;
  public static final int COL_COMMENT_CREATOR_KEY = 2;
  public static final int COL_COMMENT_BODY = 3;
  public static final int COL_COMMENT_PARENT_COMMENT_ID = 4;
  public static final int COL_COMMENT_CREATE_DATE = 5;

  public static final String[] CREATOR_COLUMNS = {
      FeedContract.CreatorEntry.TABLE_NAME + "." + FeedContract.CreatorEntry._ID,
      FeedContract.CreatorEntry.COLUMN_NAME,
      FeedContract.CreatorEntry.COLUMN_IS_ADMIN,
      FeedContract.CreatorEntry.COLUMN_IMAGE
  };

  public static final int COL_CREATOR_UNIC_ID = 0;
  public static final int COL_CREATOR_NAME = 1;
  public static final int COL_CREATOR_IS_ADMIN = 2;
  public static final int COL_CREATOR_IMAGE = 3;




  private static final String PARAM_KEY = "param_key";
  private static final int NEWS_LOADER = 0;
  private static final String SELECTED_KEY = "selected_position";
  private static String FEED_FRAGMENT = "Feed Fragment ";
  private RecyclerView mRecyclerView;
  private int mPosition = RecyclerView.NO_POSITION;
  private PreviewAdapter mPreviewAdapter;
  private int mChoiceMode;
  private boolean mHoldForTransition;
  private long mInitialSelectedDate = -1;
  private boolean mAutoSelectView;
  private Context mContext;
  private FloatingActionButton mFloatingActionButton;

  public static FeedNewsFragment newInstance(String key) {
    FeedNewsFragment fragment = new FeedNewsFragment();
    Bundle bundle = new Bundle();
    bundle.putString(PARAM_KEY, key);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;

  }

  @Override
  public void onStart() {
    super.onStart();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    if (mHoldForTransition) {
      getActivity().supportPostponeEnterTransition();
    }
    getLoaderManager().initLoader(NEWS_LOADER, null, this);
    super.onActivityCreated(savedInstanceState);
  }


  @Override
  public void onStop() {
    super.onStop();
  }

  @Override
  public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
    super.onInflate(activity, attrs, savedInstanceState);
    TypedArray a = activity.obtainStyledAttributes(attrs, R.styleable.FeedNewsFragment,
        0, 0);
    mChoiceMode = a
        .getInt(R.styleable.FeedNewsFragment_android_choiceMode, AbsListView.CHOICE_MODE_NONE);
    mAutoSelectView = a.getBoolean(R.styleable.FeedNewsFragment_autoSelectView, false);
    mHoldForTransition = a.getBoolean(R.styleable.FeedNewsFragment_sharedElementTransitions, false);
    a.recycle();
  }

  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    final View rootView = inflater.inflate(R.layout.fragment_feed_list, container, false);
    View emptyView = rootView.findViewById(R.id.recycle_feed_empty);

    mFloatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab);

    mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ((Callback) getActivity()).addNewItemDetail();
      }
    });
    mRecyclerView = (RecyclerView) rootView.findViewById(R.id.feed_list_view);

    setInitialSelectedDate(Utils.getTodayLongDate());

    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    mRecyclerView.setHasFixedSize(true);

    mPreviewAdapter = new PreviewAdapter(null, 0, emptyView, mChoiceMode,

        new PreviewAdapter.FeedAdapterOnClickHandler() {
          @Override
          public void onClick(PreviewAdapter.PreviewAdapterViewHolder vh, long id) {
            ((Callback) getActivity())
                .onItemSelected(FeedContract.PreviewEntry.buildPreviewUriById(id), vh);
            mPosition = vh.getAdapterPosition();
          }
        },
        new PreviewAdapter.FeedCommentAdapterOnClickHandler() {
          @Override
          public void onClick(PreviewAdapter.PreviewAdapterViewHolder vh, long id) {
            ((Callback) getActivity())
                .onCommentItemSelected(FeedContract.PreviewEntry.buildPreviewUriById(id), vh);
            mPosition = vh.getAdapterPosition();
          }
        },
        new PreviewAdapter.FeedLikeAdapterOnClickHandler() {
          @Override
          public void onClick(PreviewAdapter.PreviewAdapterViewHolder vh, long id) {
            ((Callback) getActivity())
                .onLikeItemSelected(FeedContract.PreviewEntry.buildPreviewUriById(id), vh);
            mPosition = vh.getAdapterPosition();

          }
        },
        new PreviewAdapter.FeedEditAdapterOnClickHandler() {
          @Override
          public void onClick(PreviewAdapter.PreviewAdapterViewHolder vh, long id) {
            ((Callback) getActivity())
                .onEditItemSelected(FeedContract.PreviewEntry.buildPreviewUriById(id), vh);
            mPosition = vh.getAdapterPosition();
          }
        },
        new PreviewAdapter.FeedRemoveAdapterOnClickHandler() {
          @Override
          public void onClick(PreviewAdapter.PreviewAdapterViewHolder vh, long id) {
            Uri uri = null;
            ((Callback) getActivity())
                .onDeleteItemSelected(FeedContract.PreviewEntry.buildPreviewUriById(id), vh);

          }
        }, new FeedAdapterLoadNews() {

      @Override
      public void load(PreviewAdapter.PreviewAdapterViewHolder vh, int offset, int count) {
        ((Callback) getActivity()).upLoadNewsItems(offset, count);
        mPosition = vh.getAdapterPosition() - 1;
      }
    }
    );

    mRecyclerView.setAdapter(mPreviewAdapter);

    if (savedInstanceState != null) {
      if (savedInstanceState.containsKey(SELECTED_KEY)) {
        mPosition = savedInstanceState.getInt(SELECTED_KEY);
      }
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
        int message = R.string.empty_feed_list;
        tv.setText(message);
      }

    }

  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    if (mPosition != RecyclerView.NO_POSITION) {
      outState.putInt(SELECTED_KEY, mPosition);
    }
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
    updateEmptyView();
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {

    Uri previewListUri = FeedContract.PreviewEntry.CONTENT_URI;

    // Sort order:  Ascending, by .
    String sortOrder = FeedContract.PreviewEntry.COLUMN_CREATE_DATE + " DESC";
    Log.i(FEED_FRAGMENT, "Query to  DB get Preview Table All items");
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

    updateEmptyView();
    if (data.getCount() == 0) {
      getActivity().supportStartPostponedEnterTransition();

    } else {

      mRecyclerView.getViewTreeObserver()
          .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
              // Since we know we're going to get items, we keep the listener around until
              // we see Children.
              if (mRecyclerView.getChildCount() > 0) {
                mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                int position = mPosition;

                if (position == RecyclerView.NO_POSITION &&
                    -1 != mInitialSelectedDate) {
                  Cursor data = mPreviewAdapter.getCursor();
                  int count = data.getCount();
                  int dateColumn = data
                      .getColumnIndex(FeedContract.PreviewEntry.COLUMN_CREATE_DATE);
                  for (int i = 0; i < count; i++) {
                    data.moveToPosition(i);
                    if (Utils.getLongFromString(data.getString(dateColumn))
                        == mInitialSelectedDate) {
                      position = i;
                      Log.i(FEED_FRAGMENT,
                          "Position Adapter: Get position current day " + position);
                      break;
                    }
                  }
                }
                if (position == RecyclerView.NO_POSITION) {
                  position = 0;
                }
                mRecyclerView.smoothScrollToPosition(position);
                RecyclerView.ViewHolder vh = mRecyclerView
                    .findViewHolderForAdapterPosition(position);
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

  public interface Callback {

    public void onItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh);

    public void onLikeItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh);

    public void onCommentItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh);

    public void onEditItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh);

    public void onDeleteItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh);

    public void upLoadNewsItems(int count, int offset);

    public void addNewItemDetail();
  }
}