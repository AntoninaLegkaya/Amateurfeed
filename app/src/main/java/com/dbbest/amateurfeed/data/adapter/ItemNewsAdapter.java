package com.dbbest.amateurfeed.data.adapter;

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
import com.dbbest.amateurfeed.data.UserNewsEntry;
import com.dbbest.amateurfeed.data.adapter.ItemNewsAdapter.UserNewsHolder;
import com.dbbest.amateurfeed.ui.fragments.FeedNewsFragment;
import com.dbbest.amateurfeed.ui.fragments.ProfileFragment;

public class ItemNewsAdapter extends CursorRecyclerAdapter<UserNewsHolder> {

  private final int ITEM_TYPE = 0;
  private final ShowItemDetailsCallback showItemDetails;
  private final String TAG = ItemNewsAdapter.class.getName();
  private Cursor cursor;


  public ItemNewsAdapter(Cursor c, ShowItemDetailsCallback detailsHandler) {
    super(c, false);
    showItemDetails = detailsHandler;
  }

  @Override
  public UserNewsHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.item_grid, viewGroup, false);
    view.setFocusable(true);
    return new UserNewsHolder(view);
  }

  @Override
  public int getItemViewType(int position, @Nullable Cursor cursor) {
    return ITEM_TYPE;
  }

  @Override
  public void onBindViewHolder(UserNewsHolder holder, @Nullable Cursor cursor,
      int position) {

    if (cursor.moveToPosition(position)) {
      if (holder.textView != null) {
        (holder.textView).setText(cursor.getString(FeedNewsFragment.COL_TITLE));
      }
      if (holder.imageView != null) {
        Glide.with(App.instance().getApplicationContext())
            .load(cursor.getString(ProfileFragment.COL_MY_NEWS_IMAGE))
            .error(R.drawable.art_snow)
            .crossFade()
            .into(holder.imageView);
      }
    }
  }

  public class UserNewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView textView;
    private ImageView imageView;

    public UserNewsHolder(View view) {
      super(view);
      imageView = (ImageView) view.findViewById(R.id.image_preview);
      textView = (TextView) view.findViewById(R.id.text_title);
      imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      if (v != null) {
        int id;
        cursor = getCursor();
        if (cursor != null) {
          cursor.moveToPosition(getAdapterPosition());
          int idx = cursor.getColumnIndex(UserNewsEntry._ID);
          id = (int) cursor.getLong(idx);
          showItemDetails.showUserNewsDetailFragment(this, id);
        }
      }
    }
  }

  public interface ShowItemDetailsCallback {

    void showUserNewsDetailFragment(UserNewsHolder vh, int id);
  }
}