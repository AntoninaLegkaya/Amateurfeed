package com.dbbest.amateurfeed.data.adapter;

import android.common.widget.CursorRecyclerAdapter;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.support.annotation.Nullable;
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
import com.dbbest.amateurfeed.data.CommentEntry;
import com.dbbest.amateurfeed.data.PreviewEntry;
import com.dbbest.amateurfeed.data.TagEntry;
import com.dbbest.amateurfeed.data.adapter.PreviewAdapter.PreviewAdapterViewHolder;
import com.dbbest.amateurfeed.ui.fragments.FeedNewsFragment;
import com.dbbest.amateurfeed.utils.Constants;
import com.dbbest.amateurfeed.utils.Utils;

public class PreviewAdapter extends CursorRecyclerAdapter<PreviewAdapterViewHolder> {

  private static final String TAG = PreviewAdapter.class.getName();
  private final FeedAdapterOnClickHandler clickHandler;
  private final FeedCommentAdapterOnClickHandler commentClickHandler;
  private final FeedLikeAdapterOnClickHandler likeClickHandler;
  private final FeedEditAdapterOnClickHandler editClickHandler;
  private final FeedRemoveAdapterOnClickHandler removeClickHandler;
  private final FeedAdapterLoadNews loadNewsHandler;


  public PreviewAdapter(Cursor cursor, int flags,
      FeedAdapterOnClickHandler onClickHandler,
      FeedCommentAdapterOnClickHandler commentHandler,
      FeedLikeAdapterOnClickHandler likeHandler,
      FeedEditAdapterOnClickHandler editHandler,
      FeedRemoveAdapterOnClickHandler removeHandler,
      FeedAdapterLoadNews newsHandler) {

    super(cursor, flags);
    this.clickHandler = onClickHandler;
    this.commentClickHandler = commentHandler;
    this.likeClickHandler = likeHandler;
    this.editClickHandler = editHandler;
    this.removeClickHandler = removeHandler;
    this.loadNewsHandler = newsHandler;
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
          layoutId = R.layout.item_user_news_layout;
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
      Log.i(TAG, "Load new items : count = " + getItemCount() + ";  offset: " + getItemCount());
      loadNewsHandler.uploadNextNews(holder, getItemCount(), 5);
    }
    if (cursor != null) {
      long mIdPreview = cursor.getLong(FeedNewsFragment.COL_FEED_ID);
      Uri uriTagsList = TagEntry.getTagsListById(mIdPreview);
      String sortOrder = TagEntry.COLUMN_PREVIEW_ID + " DESC";
      Cursor cursorTags = App.instance().getContentResolver().query(
          uriTagsList,
          null,
          null,
          null,
          sortOrder
      );
      if (cursorTags != null) {
        StringBuilder tag = new StringBuilder();
        Log.d(TAG, " PREVIEW_ADAPTER: " + DatabaseUtils.dumpCursorToString(cursorTags));
        if (holder.tagView != null) {
          if (cursorTags.moveToFirst()) {
            do {
              tag.append(" " + Utils.formatTagName(App.instance().getApplicationContext(),
                  cursorTags.getString(FeedNewsFragment.COL_TAG_NAME))+ " " );
            } while (cursorTags.moveToNext());
          }
          holder.tagView.setText(tag);
        }
      }

      Glide.with(App.instance().getApplicationContext())
          .load(cursor.getString(FeedNewsFragment.COL_AUTHOR_IMAGE))
          .error(R.drawable.art_snow)
          .crossFade()
          .into(holder.iconView);
      String fullName =
          cursor.getString(FeedNewsFragment.COL_AUTHOR);
      holder.fullNameView.setText(fullName + " " + mIdPreview);
      String description = cursor.getString(FeedNewsFragment.COL_TEXT);
      if (description != null) {
        holder.descriptionView.setText(description);
      }

      String title =
          cursor.getString(FeedNewsFragment.COL_TITLE);
      holder.titleView.setText(title);

      String date =
          cursor.getString(FeedNewsFragment.COL_CREATE_DATE);
      String day = Utils
          .getFriendlyDayString(App.instance().getApplicationContext(), Utils.getLongFromString(date),
              true);
      if (day == null) {
        holder.dateView.setText(date);
      } else {
        holder.dateView.setText(day);
      }
      int countLikes =
          cursor.getInt(FeedNewsFragment.COL_LIKES);
      holder.likesCountView.setText(String.valueOf(countLikes));
      Glide.with(App.instance().getApplicationContext())
          .load(cursor.getString(FeedNewsFragment.COL_IMAGE))
          .error(R.drawable.art_snow)
          .crossFade()
          .into(holder.imageView);
      int mIsLike = cursor.getInt(FeedNewsFragment.COL_IS_LIKE);
      if (mIsLike == 1) {
        holder.likeButton.setImageResource(R.drawable.ic_favorite_black_24dp);
        holder.likeButton.setTag("1");

      } else if (mIsLike == 0) {

        holder.likeButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        holder.likeButton.setTag("0");
      }
      Uri uriCommentList = CommentEntry.getCommentsListById(mIdPreview);
      Cursor cursorComments = App.instance().getContentResolver().query(
          uriCommentList,
          null,
          null,
          null,
          null
      );
      int count = cursorComments != null ? cursorComments.getCount() : 0;
      holder.commentCountView.setText(String.valueOf(count));
      if (cursorComments != null) {
        cursorComments.close();
      }
    }
  }

  public void selectView(RecyclerView.ViewHolder viewHolder) {
    if (viewHolder instanceof PreviewAdapterViewHolder) {
      PreviewAdapterViewHolder vfh = (PreviewAdapterViewHolder) viewHolder;
      vfh.onClick(vfh.itemView);
    }
  }

  public class PreviewAdapterViewHolder extends RecyclerView.ViewHolder implements
      View.OnClickListener {

    public TextView likesCountView;
    public ImageButton likeButton;
    ImageView iconView;
    TextView fullNameView;
    TextView titleView;
    TextView dateView;
    TextView tagView;
    ImageView imageView;
    TextView commentCountView;
    TextView descriptionView;
    ImageButton commentButton;
    ImageButton editButton;
    ImageButton removeButton;

    @Override
    public void onClick(View view) {
      int adapterPosition = getAdapterPosition();
      long id;
      Cursor cursor = getCursor();
      if (cursor != null) {
        cursor.moveToPosition(adapterPosition);
        int idx = cursor.getColumnIndex(PreviewEntry._ID);
        id = cursor.getLong(idx);
        if (view.getId() == R.id.button_like) {
          likeClickHandler.onClick(this, id);
        }
        if (view.getId() == R.id.layout_user_news || view.getId() == R.id.layout_item_user) {
          clickHandler.onClick(this, id);
        }
        if (view.getId() == R.id.button_comment) {
          commentClickHandler.onClick(this, id);
        }
        if (view.getId() == R.id.button_edit_item_news) {
          editClickHandler.onClick(this, id);
        }
        if (view.getId() == R.id.button_delete_item_news) {
          removeClickHandler.onClick(this, id);
        }
      }
    }

    PreviewAdapterViewHolder(final View view) {
      super(view);
      iconView = (ImageView) view.findViewById(R.id.image_author_icon);
      imageView = (ImageView) view.findViewById(R.id.image_news_photo);
      dateView = (TextView) view.findViewById(R.id.text_item_date);
      fullNameView = (TextView) view.findViewById(R.id.text_author_name);
      titleView = (TextView) view.findViewById(R.id.text_edit_title_news);
      likesCountView = (TextView) view.findViewById(R.id.text_count_likes);
      commentCountView = (TextView) view.findViewById(R.id.text_comment_count);
      descriptionView = (TextView) view.findViewById(R.id.text_description);

      likeButton = (ImageButton) view.findViewById(R.id.button_like);
      likeButton.setOnClickListener(this);
      commentButton = (ImageButton) view.findViewById(R.id.button_comment);
      commentButton.setOnClickListener(this);
      editButton = (ImageButton) view.findViewById(R.id.button_edit_item_news);
      if (editButton != null) {
        editButton.setOnClickListener(this);
      }
      removeButton = (ImageButton) view.findViewById(R.id.button_delete_item_news);
      removeButton.setOnClickListener(this);
      view.setOnClickListener(this);
       tagView = (TextView) view.findViewById(R.id.text_tags_list);

    }

  }

  public interface FeedAdapterLoadNews {

    void uploadNextNews(PreviewAdapter.PreviewAdapterViewHolder vh, int count, int offset);
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
}
