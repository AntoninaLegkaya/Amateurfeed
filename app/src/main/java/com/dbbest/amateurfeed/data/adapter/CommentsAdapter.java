package com.dbbest.amateurfeed.data.adapter;

import android.common.widget.CursorRecyclerAdapter;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.data.CreatorEntry;
import com.dbbest.amateurfeed.data.adapter.CommentsAdapter.CommentViewHolder;
import com.dbbest.amateurfeed.ui.fragments.FeedNewsFragment;
import com.dbbest.amateurfeed.utils.Utils;


public class CommentsAdapter extends CursorRecyclerAdapter<CommentViewHolder> {


  public CommentsAdapter(Cursor cursor, int flags) {
    super(cursor, flags);
  }

  @Override
  public CommentViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    View view = LayoutInflater
        .from(viewGroup.getContext()).inflate(R.layout.item_list_comment, viewGroup, false);
    return new CommentViewHolder(view);
  }


  @Override
  public void onBindViewHolder(CommentViewHolder holder, @Nullable Cursor cursor, int position) {
    if (cursor != null) {
      if (cursor.moveToPosition(holder.getAdapterPosition())) {
        if (holder.mCommentText != null) {
          holder.mCommentText.setText(cursor.getString(FeedNewsFragment.COL_COMMENT_BODY));
        }
        if (holder.mCommentAuthor != null) {
          String author = getAuthorComment(cursor.getInt(FeedNewsFragment.COL_COMMENT_CREATOR_KEY));
          if (author != null) {
            holder.mCommentAuthor.setText(author);
          }
        }
        if (holder.mCommentDate != null) {
          String date = cursor.getString(FeedNewsFragment.COL_COMMENT_CREATE_DATE);
          String day = Utils.getFriendlyDayString(App.instance().getApplicationContext(),
              Utils.getLongFromString(date), true);
          if (day == null) {
            holder.mCommentDate.setText(date);
          } else {
            holder.mCommentDate.setText(day);
          }
        }
      }
    }
  }


  private String getAuthorComment(int idCreator) {
    Uri uriAuthorCommentName = CreatorEntry.buildCreatorUriById(idCreator);
    Cursor cursor = App.instance().getContentResolver().query(
        uriAuthorCommentName,
        null,
        null,
        null,
        null
    );
    if (cursor != null) {
      if (cursor.moveToFirst()) {
        String author = cursor.getString(FeedNewsFragment.COL_CREATOR_NAME);
        cursor.close();
        return author;
      }
    }
    return null;
  }

  class CommentViewHolder extends RecyclerView.ViewHolder {

    private TextView mCommentText;
    private TextView mCommentAuthor;
    private TextView mCommentDate;

    public CommentViewHolder(final View view) {
      super(view);
      mCommentText = (TextView) view.findViewById(R.id.text_comment);
      mCommentAuthor = (TextView) view.findViewById(R.id.text_author_name);
      mCommentDate = (TextView) view.findViewById(R.id.text_date_create);
    }
  }
}