package com.dbbest.amateurfeed.data.adapter;

/**
 * Created by antonina on 07.02.17.
 */

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.data.FeedContract;
import com.dbbest.amateurfeed.ui.fragments.FeedNewsFragment;
import com.dbbest.amateurfeed.utils.Utils;

public class HorizontalListAdapter extends RecyclerView.Adapter<HorizontalListAdapter.HorizontalViewHolder> {

    private static String TAG_ADAPTER="Tag Adapter ";
    private Context mContext;

    public Cursor getCursor() {
        return mCursor;
    }

    private Cursor mCursor;


    class HorizontalViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout linearLayout;
        private final TextView mTagName;

        public HorizontalViewHolder(final View view) {
            super(view);

            mTagName = (TextView) view.findViewById(R.id.item_tag_name);
            if (mTagName == null) {

                Log.i(TAG_ADAPTER, "Could not get Text field");

            }
            linearLayout = (LinearLayout) view.findViewById(R.id.layout_tag);
            if (linearLayout == null) {

                Log.i(TAG_ADAPTER, " Could not get linearLayout");


            }

        }
    }

    public HorizontalListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public HorizontalViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewGroup instanceof RecyclerView) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tag, viewGroup, false);
            view.setFocusable(true);


            return new HorizontalViewHolder(view);
        } else

        {
            throw new RuntimeException("Not bound to RecyclerView");
        }

    }

    @Override
    public void onBindViewHolder(final HorizontalViewHolder holder, final int position) {
        Cursor cursor = mCursor;

        if (cursor.moveToPosition(holder.getAdapterPosition())) {
            if (holder.mTagName != null) {
                String tag = Utils.foramatTagName(mContext, cursor.getString(FeedNewsFragment.COL_TAG_NAME));
                holder.mTagName.setText(tag);
                Log.i(TAG_ADAPTER, "Set Tag to List:   " + cursor.getString(FeedNewsFragment.COL_TAG_NAME));
            }

        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Position clicked: " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        });
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
