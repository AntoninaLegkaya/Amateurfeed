package com.dbbest.amateurfeed.data.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.data.FeedContract;
import com.dbbest.amateurfeed.ui.fragments.FeedNewsFragment;
import com.dbbest.amateurfeed.utils.Utils;

/**
 * Created by antonina on 07.02.17.
 */

public class VerticalListAdapter extends RecyclerView.Adapter<VerticalListAdapter.ViewHolder> {

    private Context mContext;
    private Cursor mCursor;


    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mCommentText;
        private TextView mCommentAuthor;
        private TextView mCommentDate;

        public ViewHolder(final View view) {
            super(view);
            mCommentText = (TextView) view.findViewById(R.id.item_list_text_comment);
            mCommentAuthor = (TextView) view.findViewById(R.id.item_list_author_comment);
            mCommentDate = (TextView) view.findViewById(R.id.item_list_date_comment);
        }
    }

    public VerticalListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewGroup instanceof RecyclerView) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_comment, viewGroup, false);

            return new ViewHolder(view);
        }
        {
            throw new RuntimeException("Not bound to RecyclerView");
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Cursor cursor = mCursor;

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

                String day = Utils.getFriendlyDayString(mContext, Utils.getLongFromString(date), true);

                if (day == null) {
                    holder.mCommentDate.setText(date);
                } else {
                    holder.mCommentDate.setText(day);
                }
            }

        }


    }

    @Override
    public int getItemCount() {

        if (null == mCursor) return 0;
        return mCursor.getCount();

    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    private String getAuthorComment(int idCreator) {
        Uri uriAuthorCommentName = FeedContract.CreatorEntry.buildCreatorUriById(idCreator);

        Cursor cursor = App.instance().getContentResolver().query(
                uriAuthorCommentName,
                null,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            return cursor.getString(FeedNewsFragment.COL_CREATOR_NAME);
        }

        return null;


    }

}