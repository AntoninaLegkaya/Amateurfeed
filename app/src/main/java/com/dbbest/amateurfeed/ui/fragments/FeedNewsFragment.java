package com.dbbest.amateurfeed.ui.fragments;

import android.common.framework.IView;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.data.PreviewEntry;
import com.dbbest.amateurfeed.data.adapter.PreviewAdapter;
import com.dbbest.amateurfeed.data.adapter.PreviewAdapter.FeedAdapterLoadNews;
import com.dbbest.amateurfeed.utils.Utils;


public class FeedNewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
    SharedPreferences.OnSharedPreferenceChangeListener,
    View.OnClickListener, IView {

  public static final String[] PREVIEW_COLUMNS = {
      PreviewEntry.TABLE_NAME + "." + PreviewEntry._ID,
      PreviewEntry.COLUMN_TITLE,
      PreviewEntry.COLUMN_TEXT,
      PreviewEntry.COLUMN_LIKES,
      PreviewEntry.COLUMN_IS_LIKE,
      PreviewEntry.COLUMN_AUTHOR,
      PreviewEntry.COLUMN_AUTHOR_IMAGE,
      PreviewEntry.COLUMN_CREATE_DATE,
      PreviewEntry.COLUMN_IMAGE,
      PreviewEntry.COLUMN_IS_MY
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
  public static final int COL_TAG_NAME = 1;
  public static final int COL_TAG_ID = 2;
  public static final int COL_COMMENT_CREATOR_KEY = 2;
  public static final int COL_COMMENT_BODY = 3;
  public static final int COL_COMMENT_CREATE_DATE = 5;
  public static final int COL_CREATOR_UNIC_ID = 0;
  public static final int COL_CREATOR_NAME = 1;
  private static final int NEWS_LOADER = 0;
  private static final String SELECTED_KEY = "selected_position";
  private static String FEED_FRAGMENT = "Feed Fragment ";
  private RecyclerView recyclerView;
  private int position = RecyclerView.NO_POSITION;
  private PreviewAdapter previewAdapter;
  //  private int choiceMode;
//  private boolean holdForTransition;
  private long initialSelectedDate = -1;
//  private boolean autoSelectView;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

  }

  @Override
  public void onStart() {
    super.onStart();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    getLoaderManager().initLoader(NEWS_LOADER, null, this);
    super.onActivityCreated(savedInstanceState);
  }


  @Override
  public void onStop() {
    super.onStop();
  }

//  @Override
//  public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
//    super.onInflate(activity, attrs, savedInstanceState);
//    TypedArray a = activity.obtainStyledAttributes(attrs, R.styleable.FeedNewsFragment,
//        0, 0);
//    choiceMode = a
//        .getInt(R.styleable.FeedNewsFragment_android_choiceMode, AbsListView.CHOICE_MODE_NONE);
//    autoSelectView = a.getBoolean(R.styleable.FeedNewsFragment_autoSelectView, false);
//    holdForTransition = a.getBoolean(R.styleable.FeedNewsFragment_sharedElementTransitions, false);
//    a.recycle();
//  }

  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    final View rootView = inflater.inflate(R.layout.fragment_feed_list, container, false);

    FloatingActionButton mFloatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab);

    mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ((Callback) getActivity()).addNewItemDetail();
      }
    });
    recyclerView = (RecyclerView) rootView.findViewById(R.id.view_feed_list);

    setInitialSelectedDate(Utils.getTodayLongDate());

    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    recyclerView.setHasFixedSize(true);

    previewAdapter = new PreviewAdapter(null, 0,

        new PreviewAdapter.FeedAdapterOnClickHandler() {
          @Override
          public void onClick(PreviewAdapter.PreviewAdapterViewHolder vh, long id) {
            ((Callback) getActivity())
                .onItemSelected(PreviewEntry.buildPreviewUriById(id), vh);
            position = vh.getAdapterPosition();
          }
        },
        new PreviewAdapter.FeedCommentAdapterOnClickHandler() {
          @Override
          public void onClick(PreviewAdapter.PreviewAdapterViewHolder vh, long id) {
            ((Callback) getActivity())
                .onCommentItemSelected(PreviewEntry.buildPreviewUriById(id), vh);
            position = vh.getAdapterPosition();
          }
        },
        new PreviewAdapter.FeedLikeAdapterOnClickHandler() {
          @Override
          public void onClick(PreviewAdapter.PreviewAdapterViewHolder vh, long id) {
            ((Callback) getActivity())
                .onLikeItemSelected(PreviewEntry.buildPreviewUriById(id), vh);
            position = vh.getAdapterPosition();

          }
        },
        new PreviewAdapter.FeedEditAdapterOnClickHandler() {
          @Override
          public void onClick(PreviewAdapter.PreviewAdapterViewHolder vh, long id) {
            ((Callback) getActivity())
                .onEditItemSelected(PreviewEntry.buildPreviewUriById(id), vh);
            position = vh.getAdapterPosition();
          }
        },
        new PreviewAdapter.FeedRemoveAdapterOnClickHandler() {
          @Override
          public void onClick(PreviewAdapter.PreviewAdapterViewHolder vh, long id) {
            ((Callback) getActivity())
                .onDeleteItemSelected(PreviewEntry.buildPreviewUriById(id), vh);

          }
        }, new FeedAdapterLoadNews() {

      @Override
      public void uploadNextNews(PreviewAdapter.PreviewAdapterViewHolder vh, int offset, int count) {
        ((Callback) getActivity()).upLoadNewsItems(offset, count);
        position = vh.getAdapterPosition() - 1;
      }
    }
    );

    recyclerView.setAdapter(previewAdapter);

    if (savedInstanceState != null) {
      if (savedInstanceState.containsKey(SELECTED_KEY)) {
        position = savedInstanceState.getInt(SELECTED_KEY);
      }
    }
    return rootView;
  }

  @Override
  public void onClick(View view) {
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    if (position != RecyclerView.NO_POSITION) {
      outState.putInt(SELECTED_KEY, position);
    }
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    Uri previewListUri = PreviewEntry.CONTENT_URI;
    String sortOrder = PreviewEntry.COLUMN_CREATE_DATE + " DESC";
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
    previewAdapter.swapCursor(data);
    if (data.getCount() == 0) {
      getActivity().supportStartPostponedEnterTransition();
    } else {
      recyclerView.getViewTreeObserver()
          .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
              // Since we know we're going to get items, we keep the listener around until
              // we see Children.
              if (recyclerView.getChildCount() > 0) {
                recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                int pos = FeedNewsFragment.this.position;

                if (pos == RecyclerView.NO_POSITION &&
                    -1 != initialSelectedDate) {
                  Cursor createData = previewAdapter.getCursor();
                  int count = createData.getCount();
                  int dateColumn = createData
                      .getColumnIndex(PreviewEntry.COLUMN_CREATE_DATE);
                  for (int i = 0; i < count; i++) {
                    createData.moveToPosition(i);
                    if (Utils.getLongFromString(createData.getString(dateColumn))
                        == initialSelectedDate) {
                      pos = i;
                      Log.i(FEED_FRAGMENT,
                          "Position Adapter: Get pos current day " + pos);
                      break;
                    }
                  }
                }
                if (pos == RecyclerView.NO_POSITION) {
                  pos = 0;
                }
                recyclerView.smoothScrollToPosition(pos);
                RecyclerView.ViewHolder vh = recyclerView
                    .findViewHolderForAdapterPosition(pos);
                if (null != vh) {
                  previewAdapter.selectView(vh);
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
    previewAdapter.swapCursor(null);
  }

  public void refreshFragmentLoader() {
    if (getLoaderManager().getLoader(NEWS_LOADER) != null) {
      getLoaderManager().restartLoader(NEWS_LOADER, null, this);
    }
  }

  public void setInitialSelectedDate(long initDate) {
    this.initialSelectedDate = initDate;
  }

  public interface Callback {

    void onItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh);

    void onLikeItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh);

    void onCommentItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh);

    void onEditItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh);

    void onDeleteItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh);

    void upLoadNewsItems(int count, int offset);

    void addNewItemDetail();
  }
}