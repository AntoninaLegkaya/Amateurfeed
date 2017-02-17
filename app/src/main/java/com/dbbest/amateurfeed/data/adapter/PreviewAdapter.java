package com.dbbest.amateurfeed.data.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
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
import com.dbbest.amateurfeed.ui.fragments.FeedNewsFragment;
import com.dbbest.amateurfeed.utils.ItemChoiceManager;
import com.dbbest.amateurfeed.utils.Utils;

import java.text.ParseException;

/**
 * Created by antonina on 06.02.17.
 */

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.PreviewAdapterViewHolder> {

    private Cursor mCursor;
    final private Context mContext;
    public static final int VIEW_TYPE_MY = 0;
    public static final int VIEW_TYPE_USER = 1;
    public static final int VIEW_TYPE_ITEM_EMPTY = 2;

    private final View mEmptyView;
    private final ItemChoiceManager mICM;
    private RecyclerView mHorizontalList;
    private HorizontalListAdapter mHorizontalListAdapter;


    private final FeedAdapterOnClickHandler mClickHandler;
    private final FeedCommentAdapterOnClickHandler mCommentClickHandler;
    private final FeedLikeAdapterOnClickHandler mLikeClickHandler;
    private final FeedEditAdapterOnClickHandler mEditClickHandler;
    private final FeedRemoveAdapterOnClickHandler mRemoveClickHandler;
    private final FeedAdapterLoadNews mLoadNewsHandler;


    public PreviewAdapter(Context context, View emptyView, int choiceMode, FeedAdapterOnClickHandler clickHandler, FeedCommentAdapterOnClickHandler commentClickHandler, FeedLikeAdapterOnClickHandler likeClickHandler, FeedEditAdapterOnClickHandler editeClickHandler, FeedRemoveAdapterOnClickHandler removeClickHandler, FeedAdapterLoadNews loadNewsHandler) {
        mContext = context;
        mClickHandler = clickHandler;
        mCommentClickHandler = commentClickHandler;
        mLikeClickHandler = likeClickHandler;
        mEditClickHandler = editeClickHandler;
        mRemoveClickHandler = removeClickHandler;
        mLoadNewsHandler = loadNewsHandler;
        mEmptyView = emptyView;
        mICM = new ItemChoiceManager(this);
        mICM.setChoiceMode(choiceMode);
    }


    public class PreviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
            mHorizontalList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            mHorizontalListAdapter = new HorizontalListAdapter(mContext);
            mHorizontalList.setAdapter(mHorizontalListAdapter);


        }


        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            long id;
            if (mCursor != null) {
                mCursor.moveToPosition(adapterPosition);


                int idx = mCursor.getColumnIndex(FeedContract.PreviewEntry._ID);
                id = mCursor.getLong(idx);
                Log.i(Utils.TAG_LOG, "PreviewAdapter You Get Item By ID: " + id);


                if (view.getId() == R.id.like_button) {
                    mLikeClickHandler.onClick(this, id);
                    mICM.onClick(this);
                }
                if (view.getId() == R.id.item || view.getId() == R.id.item_my) {
                    mClickHandler.onClick(this, id);
                    mICM.onClick(this);

                }
                if (view.getId() == R.id.comment_button) {
                    mCommentClickHandler.onClick(this, id);
                    mICM.onClick(this);
                }
                if (view.getId() == R.id.edit_button) {
                    mEditClickHandler.onClick(this, id);
                    mICM.onClick(this);
                }
                if (view.getId() == R.id.delete_button) {
                    mRemoveClickHandler.onClick(this, id);
                    mICM.onClick(this);
                }

            }
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


    @Override
    public int getItemViewType(int position) {
        if (mCursor != null) {
            mCursor.moveToPosition(position);
            return (mCursor.getInt(FeedNewsFragment.COL_IS_MY) == 1) ? VIEW_TYPE_MY : VIEW_TYPE_USER;
        }
        return VIEW_TYPE_ITEM_EMPTY;
    }

    @Override
    public PreviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewGroup instanceof RecyclerView) {

            int layoutId = -1;
            switch (viewType) {

                case VIEW_TYPE_MY: {
                    layoutId = R.layout.item_my_news_layout;
                    break;

                }
                case VIEW_TYPE_USER: {
                    layoutId = R.layout.item_news_layout;
                    break;
                }
                case VIEW_TYPE_ITEM_EMPTY: {
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
    public void onBindViewHolder(PreviewAdapterViewHolder holder, int position) {

        //check for last item
        if ((position >= getItemCount() - 1)) {
            Log.i(Utils.TAG_LOG_LOAD_NEW_DATA, "Load new items : count = 5  offset: " + getItemCount());
            mLoadNewsHandler.load(holder, getItemCount(), 5);
        }

        mCursor.moveToPosition(position);

        long mIdPreview = mCursor.getLong(FeedNewsFragment.COL_FEED_ID);
        Glide.with(mContext)
                .load(mCursor.getString(FeedNewsFragment.COL_AUTHOR_IMAGE))
                .error(R.drawable.art_snow)
                .crossFade()
                .into(holder.mIconView);

        String fullName =
                mCursor.getString(FeedNewsFragment.COL_AUTHOR);
        holder.mFullNameView.setText(fullName + String.valueOf(mIdPreview));

        String description = mCursor.getString(FeedNewsFragment.COL_TEXT);
        if (description != null) {
            holder.mDescriptionView.setText(description);
        }


        String title =
                mCursor.getString(FeedNewsFragment.COL_TITLTE);
        holder.mTitleView.setText(title);

        String date =
                mCursor.getString(FeedNewsFragment.COL_CREATE_DATE);
        String day = null;

        day = Utils.getFriendlyDayString(mContext, Utils.getLongFromString(date), true);

        if (day == null) {
            holder.mDateView.setText(date);
        } else {
            holder.mDateView.setText(day);
        }

        int countLikes =
                mCursor.getInt(FeedNewsFragment.COL_LIKES);
        holder.mLikesCountView.setText(String.valueOf(countLikes));


        Glide.with(mContext)
                .load(mCursor.getString(FeedNewsFragment.COL_IMAGE))
                .error(R.drawable.art_snow)
                .crossFade()
                .into(holder.mImageView);
        int mIsLike = mCursor.getInt(FeedNewsFragment.COL_IS_LIKE);
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

        if (mCursorTags.moveToFirst()) {
            mHorizontalListAdapter.swapCursor(mCursorTags);
        }


        // this enables better animations. even if we lose state due to a device rotation,
        // the animator can use this to re-find the original view
        ViewCompat.setTransitionName(holder.mIconView, "iconView" + position);
        mICM.onBindViewHolder(holder, position);


    }


    @Override
    public int getItemCount() {

        if (null == mCursor) {
            return 0;
        } else {
            return mCursor.getCount();
        }

    }

    public int getSelectedItemPosition() {
        return mICM.getSelectedItemPosition();
    }

    public Cursor getCursor() {
        return mCursor;
    }


    public void selectView(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof PreviewAdapterViewHolder) {
            PreviewAdapterViewHolder vfh = (PreviewAdapterViewHolder) viewHolder;
            vfh.onClick(vfh.itemView);
        }
    }


    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mICM.onRestoreInstanceState(savedInstanceState);
    }

    public void onSaveInstanceState(Bundle outState) {
        mICM.onSaveInstanceState(outState);
    }


    private void deleteItem(RecyclerView.ViewHolder holder, int position) {
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mCursor.getCount());
        holder.itemView.setVisibility(View.GONE);
    }


}
