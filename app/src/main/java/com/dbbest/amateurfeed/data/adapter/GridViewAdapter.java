package com.dbbest.amateurfeed.data.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dbbest.amateurfeed.R;

/**
 * Created by antonina on 06.02.17.
 */

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.ViewHolder> {

    private Activity activity;

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
    public void onBindViewHolder(final GridViewAdapter.ViewHolder viewHolder, final int position) {
        final int adapterPosition = viewHolder.getAdapterPosition();
        if (adapterPosition % 3 == 0) {
            viewHolder.imageView.setImageResource(R.mipmap.splash);
        } else if (adapterPosition % 3 == 1) {
            viewHolder.imageView.setImageResource(R.mipmap.splash);
        } else {
            viewHolder.imageView.setImageResource(R.mipmap.splash);
        }
        viewHolder.textView.setText("Position: " + (adapterPosition + 1));
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(activity, "You clicked at position: " + adapterPosition, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image);
            textView = (TextView) view.findViewById(R.id.text);
        }
    }
}