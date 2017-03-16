package com.dbbest.amateurfeed.data.adapter;

import android.app.Activity;
import android.common.widget.CursorRecyclerAdapter;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.data.FeedContract;
import com.dbbest.amateurfeed.data.adapter.ItemNewsAdapter.MyNewsHolder;
import com.dbbest.amateurfeed.ui.fragments.FeedNewsFragment;
import com.dbbest.amateurfeed.ui.fragments.ProfileFragment;

public class ItemNewsAdapter extends CursorRecyclerAdapter<MyNewsHolder> {

  private final int ITEM_TYPE = 0;
  private final ShowItemDetailsCallback mShowItemDetails;
  private final String TAG = ItemNewsAdapter.class.getName();
  protected Cursor mCursor;
  private Activity activity;


  public ItemNewsAdapter(Cursor c, int flags,
      ShowItemDetailsCallback detailsHandler) {
    super(c, false);
    mShowItemDetails = detailsHandler;
  }

  @Override
  public MyNewsHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.item_grid, viewGroup, false);
    view.setFocusable(true);
    return new MyNewsHolder(view);
  }

  @Override
  public int getItemViewType(int position, @Nullable Cursor cursor) {
    return ITEM_TYPE;
  }

  @Override
  public void onBindViewHolder(MyNewsHolder holder, @Nullable Cursor cursor,
      int position) {

    if (cursor.moveToPosition(position)) {
      if (holder.mTextView != null) {
        (holder.mTextView).setText(cursor.getString(FeedNewsFragment.COL_TITLTE));
      }
      if (holder.mImageView != null) {
        Glide.with(App.instance().getApplicationContext())
            .load(cursor.getString(ProfileFragment.COL_MY_NEWS_IMAGE))
            .error(R.drawable.art_snow)
            .crossFade()
            .into(holder.mImageView);
      }
    }
  }

  public interface ShowItemDetailsCallback {

    void showUserNewsDetailFragment(MyNewsHolder vh, int id);
  }

  public class MyNewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView mTextView;
    private ImageView mImageView;

    public MyNewsHolder(View view) {
      super(view);
      mImageView = (ImageView) view.findViewById(R.id.image);
      mTextView = (TextView) view.findViewById(R.id.text);
      mImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      if (v != null) {
        int id;
        mCursor = getCursor();
        if (mCursor != null) {
          mCursor.moveToPosition(getAdapterPosition());
          int idx = mCursor.getColumnIndex(FeedContract.UserNewsEntry._ID);
          id = (int) mCursor.getLong(idx);
          mShowItemDetails.showUserNewsDetailFragment(this, id);
        }
      }
    }
  }
}