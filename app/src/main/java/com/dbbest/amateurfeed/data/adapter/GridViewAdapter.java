package com.dbbest.amateurfeed.data.adapter;

import android.app.Activity;
import android.common.widget.CursorRecyclerAdapter;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.data.FeedContract;
import com.dbbest.amateurfeed.ui.fragments.FeedNewsFragment;
import com.dbbest.amateurfeed.ui.fragments.SearchFragment;
import com.dbbest.amateurfeed.utils.Constants;

/**
 * Created by antonina on 06.02.17.
 */

public class GridViewAdapter extends CursorRecyclerAdapter<GridViewAdapter.GridViewHolder> {

    private static final int ITEM_TYPE = 0;
    private Activity activity;
    private static String TAG_ADAPTER = "Grid Adapter ";
    private Context mContext;
    private final SearchAdapterShowItemDetails mSearchAdapterShowItemDetails;
    private Cursor mCursor;


    public GridViewAdapter(Cursor c, int flags, Context context, SearchAdapterShowItemDetails detailsHandler) {
        super(c, flags);
        this.mContext = context;
        mSearchAdapterShowItemDetails = detailsHandler;
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_grid, viewGroup, false);
        view.setFocusable(true);
        return new GridViewHolder(view);
    }


    @Override
    public int getItemViewType(int position, @Nullable Cursor cursor) {
        return ITEM_TYPE;
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, @Nullable Cursor cursor, int position) {

        mCursor = cursor;
        if (mCursor.moveToPosition(position)) {
            if (holder.mTextView != null) {
                (holder.mTextView).setText(mCursor.getString(FeedNewsFragment.COL_TITLTE));
            }
            if (holder.mImageView != null) {

                Glide.with(mContext)
                        .load(mCursor.getString(FeedNewsFragment.COL_IMAGE))
                        .error(R.drawable.art_snow)
                        .crossFade()
                        .into(holder.mImageView);
            }


        }
    }


    public class GridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextView;
        private ImageView mImageView;

        public GridViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.image);
            mTextView = (TextView) view.findViewById(R.id.text);
            mImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v != null) {
                int id;
                if (mCursor != null) {
                    mCursor.moveToPosition(getAdapterPosition());
                    int idx = mCursor.getColumnIndex(FeedContract.PreviewEntry._ID);
                    id = (int) mCursor.getLong(idx);
                    int typeItem = (mCursor.getInt(FeedNewsFragment.COL_IS_MY) == 1) ? Constants.VIEW_TYPE_MY : Constants.VIEW_TYPE_USER;
                    int layoutId = -1;
                    switch (typeItem) {

                        case Constants.VIEW_TYPE_MY: {
                            layoutId = R.layout.fragment_item_my_detail;
                            break;

                        }
                        case Constants.VIEW_TYPE_USER: {
                            layoutId = R.layout.fragment_item_detail;
                            break;
                        }
                        case Constants.VIEW_TYPE_ITEM_EMPTY: {
                            layoutId = R.layout.item_empty_detail;
                            break;
                        }

                    }
                    if (v.getId() == R.id.image) {
                        Log.i(SearchFragment.SEARCH_FRAGMENT, "Click on Item with id: " + id + "Type: " + layoutId);
                        mSearchAdapterShowItemDetails.showItemDetailsFragment(this, FeedContract.PreviewEntry.buildPreviewUriById(id), layoutId);
                    }


                }

            }

        }
    }


    @Override
    public int getItemCount() {

        if (mCursor == null) return 0;
        return mCursor.getCount();

    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
        return super.swapCursor(newCursor);
    }

    public interface SearchAdapterShowItemDetails {
        void showItemDetailsFragment(GridViewHolder vh, Uri uri, int typeItem);
    }
}