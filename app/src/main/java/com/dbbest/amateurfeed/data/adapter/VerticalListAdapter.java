package com.dbbest.amateurfeed.data.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dbbest.amateurfeed.R;

/**
 * Created by antonina on 07.02.17.
 */

public class VerticalListAdapter extends RecyclerView.Adapter<VerticalListAdapter.ViewHolder> {

    private Context mContext;
    private Cursor mCursor;


    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mCommentText;

        public ViewHolder(final View view) {
            super(view);
            mCommentText = (TextView) view.findViewById(R.id.item_list_text_comment);
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
        final int adapterPosition = holder.getAdapterPosition();

        if (holder.mCommentText != null) {

            holder.mCommentText.setText("Comment user");
        }


    }

    @Override
    public int getItemCount() {

//         Unpack when configurate Cursor
        if (null == mCursor) return 1;
        return mCursor.getCount();

    }


}