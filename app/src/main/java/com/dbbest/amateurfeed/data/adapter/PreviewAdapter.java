package com.dbbest.amateurfeed.data.adapter;

import android.common.widget.CursorRecyclerAdapter;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.data.FeedContract;
import com.dbbest.amateurfeed.data.adapter.PreviewAdapter.PreviewAdapterViewHolder;
import com.dbbest.amateurfeed.ui.fragments.FeedNewsFragment;
import com.dbbest.amateurfeed.utils.Constants;
import com.dbbest.amateurfeed.utils.Utils;

public class PreviewAdapter extends CursorRecyclerAdapter<PreviewAdapterViewHolder> {

  private static final String TAG = PreviewAdapter.class.getName();
  private final View mEmptyView;
  private final FeedAdapterOnClickHandler mClickHandler;
  private final FeedCommentAdapterOnClickHandler mCommentClickHandler;
  private final FeedLikeAdapterOnClickHandler mLikeClickHandler;
  private final FeedEditAdapterOnClickHandler mEditClickHandler;
  private final FeedRemoveAdapterOnClickHandler mRemoveClickHandler;
  private final FeedAdapterLoadNews mLoadNewsHandler;
  private Cursor mCursor;
  private RecyclerView mHorizontalList;
  private TagAdapter mTagAdapter;


  public PreviewAdapter(Cursor cursor, int flags, View emptyView, int choiceMode,
      FeedAdapterOnClickHandler clickHandler,
      FeedCommentAdapterOnClickHandler commentClickHandler,
      FeedLikeAdapterOnClickHandler likeClickHandler,
      FeedEditAdapterOnClickHandler editClickHandler,
      FeedRemoveAdapterOnClickHandler removeClickHandler,
      FeedAdapterLoadNews loadNewsHandler) {

    super(cursor, flags);
    mClickHandler = clickHandler;
    mCommentClickHandler = commentClickHandler;
    mLikeClickHandler = likeClickHandler;
    mEditClickHandler = editClickHandler;
    mRemoveClickHandler = removeClickHandler;
    mLoadNewsHandler = loadNewsHandler;
    mEmptyView = emptyView;
  }

  @Override
  public int getItemViewType(int position, @Nullable Cursor cursor) {
    if (cursor != null) {
      cursor.moveToPosition(position);
      return (cursor.getInt(FeedNewsFragment.COL_IS_MY) == 1) ? Constants.VIEW_TYPE_MY
          : Constants.VIEW_TYPE_USER;
    }
    return Constants.VIEW_TYPE_ITEM_EMPTY;
  }


  @Override
  public PreviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    if (viewGroup instanceof RecyclerView) {

      int layoutId = -1;
      switch (viewType) {
        case Constants.VIEW_TYPE_MY: {
          layoutId = R.layout.item_my_news_layout;
          break;
        }
        case Constants.VIEW_TYPE_USER: {
          layoutId = R.layout.item_news_layout;
          break;
        }
        case Constants.VIEW_TYPE_ITEM_EMPTY: {
          layoutId = R.layout.item_empty_news;
          break;
        }
      }
      View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
      view.setFocusable(true);
      return new PreviewAdapterViewHolder(view);
    } else {
      throw new RuntimeException("Not bound to RecyclerView");
    }
  }


  @Override
  public void onBindViewHolder(PreviewAdapterViewHolder holder, @Nullable Cursor cursor,
      int position) {
    if ((position >= getItemCount() - 1)) {
      Log.i(TAG, "Load new items : count = 5  offset: " + getItemCount());
      mLoadNewsHandler.load(holder, getItemCount(), 5);
    }

    cursor.moveToPosition(position);

    long mIdPreview = cursor.getLong(FeedNewsFragment.COL_FEED_ID);
    Glide.with(App.instance().getApplicationContext())
        .load(cursor.getString(FeedNewsFragment.COL_AUTHOR_IMAGE))
        .error(R.drawable.art_snow)
        .crossFade()
        .into(holder.mIconView);

    String fullName =
        cursor.getString(FeedNewsFragment.COL_AUTHOR);
    holder.mFullNameView.setText(fullName + String.valueOf(mIdPreview));

    String description = cursor.getString(FeedNewsFragment.COL_TEXT);
    if (description != null) {
      holder.mDescriptionView.setText(description);
    }

    String title =
        cursor.getString(FeedNewsFragment.COL_TITLE);
    holder.mTitleView.setText(title);

    String date =
        cursor.getString(FeedNewsFragment.COL_CREATE_DATE);
    String day = null;

    day = Utils
        .getFriendlyDayString(App.instance().getApplicationContext(), Utils.getLongFromString(date),
            true);

    if (day == null) {
      holder.mDateView.setText(date);
    } else {
      holder.mDateView.setText(day);
    }

    int countLikes =
        cursor.getInt(FeedNewsFragment.COL_LIKES);
    holder.mLikesCountView.setText(String.valueOf(countLikes));

    Glide.with(App.instance().getApplicationContext())
        .load(cursor.getString(FeedNewsFragment.COL_IMAGE))
        .error(R.drawable.art_snow)
        .crossFade()
        .into(holder.mImageView);
    int mIsLike = cursor.getInt(FeedNewsFragment.COL_IS_LIKE);
    if (mIsLike == 1) {
      holder.mLikeButton.setImageResource(R.drawable.ic_favorite_black_24dp);
      holder.mLikeButton.setTag("1");

    } else if (mIsLike == 0) {

      holder.mLikeButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
      holder.mLikeButton.setTag("0");
    }

    Uri uriCommentList = FeedContract.CommentEntry.getCommentsListById(mIdPreview);

    Cursor mCursorComments = App.instance().getContentResolver().query(
        uriCommentList,
        null,
        null,
        null,
        null
    );
    int count = mCursorComments.getCount();
    holder.mCommentCountView.setText(String.valueOf(count));

    Uri uriTagsList = FeedContract.TagEntry.getTagsListById(mIdPreview);
    Cursor mCursorTags = App.instance().getContentResolver().query(
        uriTagsList,
        null,
        null,
        null,
        null
    );

    if (mCursorTags != null) {
      mTagAdapter.swapCursor(mCursorTags);
    }

  }


  public void selectView(RecyclerView.ViewHolder viewHolder) {
    if (viewHolder instanceof PreviewAdapterViewHolder) {
      PreviewAdapterViewHolder vfh = (PreviewAdapterViewHolder) viewHolder;
      vfh.onClick(vfh.itemView);
    }
  }


  public interface FeedAdapterLoadNews {

    void load(PreviewAdapter.PreviewAdapterViewHolder vh, int count, int offset);

  }

  public interface FeedAdapterOnClickHandler {

    void onClick(PreviewAdapterViewHolder vh, long id);
  }

  public interface FeedCommentAdapterOnClickHandler {

    void onClick(PreviewAdapterViewHolder vh, long id);
  }

  public interface FeedLikeAdapterOnClickHandler {

    void onClick(PreviewAdapterViewHolder vh, long id);
  }

  public interface FeedEditAdapterOnClickHandler {

    void onClick(PreviewAdapterViewHolder vh, long id);
  }

  public interface FeedRemoveAdapterOnClickHandler {

    void onClick(PreviewAdapterViewHolder vh, long id);
  }

  public class PreviewAdapterViewHolder extends RecyclerView.ViewHolder implements
      View.OnClickListener {

    public final ImageView mIconView;
    public final TextView mFullNameView;
    public final TextView mTitleView;
    public final TextView mDateView;
    public final ImageView mImageView;
    public final TextView mLikesCountView;
    public final TextView mCommentCountView;
    public final TextView mDescriptionView;
    public final ImageButton mLikeButton;
    public final ImageButton mCommentButton;
    public final ImageButton mEditButton;
    public final ImageButton mRemoveButton;

    public boolean mIsLike;
    public boolean mIsMy;

    public PreviewAdapterViewHolder(final View itemView) {
      super(itemView);
      mIconView = (ImageView) itemView.findViewById(R.id.list_item_icon);
      mImageView = (ImageView) itemView.findViewById(R.id.list_item_image);
      mDateView = (TextView) itemView.findViewById(R.id.list_item_date_textview);
      mFullNameView = (TextView) itemView.findViewById(R.id.list_item_name_textview);
      mTitleView = (TextView) itemView.findViewById(R.id.list_item_title);
      mLikesCountView = (TextView) itemView.findViewById(R.id.list_item_likes_count);
      mCommentCountView = (TextView) itemView.findViewById(R.id.list_item_comment_count);
      mDescriptionView = (TextView) itemView.findViewById(R.id.list_item_description);

      mLikeButton = (ImageButton) itemView.findViewById(R.id.like_button);
      mLikeButton.setOnClickListener(this);
      mCommentButton = (ImageButton) itemView.findViewById(R.id.comment_button);
      mCommentButton.setOnClickListener(this);
      mEditButton = (ImageButton) itemView.findViewById(R.id.edit_button);
      if (mEditButton != null) {
        mEditButton.setOnClickListener(this);
      }
      mRemoveButton = (ImageButton) itemView.findViewById(R.id.delete_button);
      mRemoveButton.setOnClickListener(this);
      itemView.setOnClickListener(this);
      mHorizontalList = (RecyclerView) itemView.findViewById(R.id.list_tags_view);
      mHorizontalList.setLayoutManager(
          new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
      mTagAdapter = new TagAdapter(null, 0);
      mHorizontalList.setAdapter(mTagAdapter);
    }


    @Override
    public void onClick(View view) {
      int adapterPosition = getAdapterPosition();
      long id;
      mCursor = getCursor();
      if (mCursor != null) {
        mCursor.moveToPosition(adapterPosition);
        int idx = mCursor.getColumnIndex(FeedContract.PreviewEntry._ID);
        id = mCursor.getLong(idx);
        if (view.getId() == R.id.like_button) {
          mLikeClickHandler.onClick(this, id);
        }
        if (view.getId() == R.id.item || view.getId() == R.id.item_my) {
          mClickHandler.onClick(this, id);
        }
        if (view.getId() == R.id.comment_button) {
          mCommentClickHandler.onClick(this, id);
        }
        if (view.getId() == R.id.edit_button) {
          mEditClickHandler.onClick(this, id);
        }
        if (view.getId() == R.id.delete_button) {
          mRemoveClickHandler.onClick(this, id);
        }

      }
    }

  }
}
