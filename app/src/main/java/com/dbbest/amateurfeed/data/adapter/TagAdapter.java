package com.dbbest.amateurfeed.data.adapter;


import android.common.widget.CursorRecyclerAdapter;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.data.adapter.TagAdapter.TagViewHolder;
import com.dbbest.amateurfeed.ui.fragments.FeedNewsFragment;
import com.dbbest.amateurfeed.utils.Utils;

public class TagAdapter extends CursorRecyclerAdapter<TagViewHolder> {

  private String TAG = TagAdapter.class.getName();
  public TagAdapter(Cursor c, int flags) {
    super(c, flags);

  }


  @Override
  public TagViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    if (viewGroup instanceof RecyclerView) {
      View view = LayoutInflater.from(viewGroup.getContext())
          .inflate(R.layout.item_tag, viewGroup, false);
      view.setFocusable(true);
      return new TagViewHolder(view);
    } else {
      throw new RuntimeException("Not bound to RecyclerView");
    }
  }

  @Override
  public void onBindViewHolder(TagViewHolder holder, @Nullable Cursor cursor, int position) {
    if (cursor.moveToPosition(holder.getAdapterPosition())) {
      if (holder.mTagName != null) {
        String tag = Utils
            .foramatTagName(App.instance().getApplicationContext(),
                cursor.getString(FeedNewsFragment.COL_TAG_NAME));
        holder.mTagName.setText(tag);
//        Log.i(TAG, "Set Tag to List:   " + cursor.getString(FeedNewsFragment.COL_TAG_NAME));
      }

    }
  }

  class TagViewHolder extends RecyclerView.ViewHolder {

    private TextView mTagName;

    public TagViewHolder(final View view) {
      super(view);
      mTagName = (TextView) view.findViewById(R.id.item_tag_name);
    }
  }
}
