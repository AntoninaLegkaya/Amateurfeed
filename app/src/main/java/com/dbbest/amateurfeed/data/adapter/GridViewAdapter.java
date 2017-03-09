package com.dbbest.amateurfeed.data.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.ui.fragments.FeedNewsFragment;
import com.dbbest.amateurfeed.ui.fragments.SearchFragment;
import com.dbbest.amateurfeed.utils.Utils;

/**
 * Created by antonina on 06.02.17.
 */

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.ViewHolder> {

    private Activity activity;
    private static String TAG_ADAPTER = "Grid Adapter ";
    private Context mContext;

    public Cursor getCursor() {
        return mCursor;
    }

    private Cursor mCursor;

    public GridViewAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_grid, viewGroup, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final GridViewAdapter.ViewHolder holder, final int position) {
        Cursor cursor = mCursor;

        if (cursor.moveToPosition(holder.getAdapterPosition())) {
            if (holder.mTextView != null) {
                (holder.mTextView).setText(cursor.getString(FeedNewsFragment.COL_TITLTE));
                Log.i(SearchFragment.SEARCH_FRAGMENT, "Set title to news:   " + cursor.getString(FeedNewsFragment.COL_TAG_NAME));
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


    protected class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;
        private ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.image);
            mTextView = (TextView) view.findViewById(R.id.text);
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
}