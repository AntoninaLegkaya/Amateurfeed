package com.dbbest.amateurfeed.data.adapter;


import android.common.widget.CursorRecyclerAdapter;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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


  public TagAdapter() {
    super(null, 0);
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
    if (cursor != null) {
      if (cursor.moveToPosition(holder.getAdapterPosition())) {
        if (holder.tagName != null) {
          String tag = Utils
              .formatTagName(App.instance().getApplicationContext(),
                  cursor.getString(FeedNewsFragment.COL_TAG_NAME));
          holder.tagName.setText(tag);
        }
      }
    }
  }

  static class TagViewHolder extends RecyclerView.ViewHolder {

    private TextView tagName;

    TagViewHolder(final View view) {
      super(view);
      tagName = (TextView) view.findViewById(R.id.item_tag_name);
    }
  }
}
