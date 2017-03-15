package com.dbbest.amateurfeed.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.model.UserNewsModel;


public class MyNewsPreviewFragment extends Fragment {

  private ImageView mPreviewImage;
  private TextView mPreviewTitle;
  private TextView mPreviewUpdateDate;
  private TextView mPreviewStatus;
  private TextView mPreviewCountLikes;
  private UserNewsModel userNewsModel;


  public MyNewsPreviewFragment() {
    setHasOptionsMenu(true);
  }

  public static MyNewsPreviewFragment newInstance(Bundle bundle) {
    MyNewsPreviewFragment fragment = new MyNewsPreviewFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    String upTitle = null;
    String upDescription = null;

    switch (item.getItemId()) {
      case android.R.id.home:

//        ((Callback) getActivity()).moveToFeedFragment();
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    final View rootView = inflater.inflate(R.layout.fragment_my_news, container, false);

    Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
    ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

    Bundle arguments = getArguments();
    if (arguments != null) {
      userNewsModel = arguments.getParcelable("model");
    }

    mPreviewTitle = (TextView) rootView.findViewById(R.id.my_item_title);
    mPreviewUpdateDate = (TextView) rootView.findViewById(R.id.my_item_update_date);
    mPreviewStatus = (TextView) rootView.findViewById(R.id.my_item_status);
    mPreviewCountLikes = (TextView) rootView.findViewById(R.id.my_item_amount_likes);
    mPreviewImage = (ImageView) rootView.findViewById(R.id.my_item_image);
    if (userNewsModel != null) {

      if (mPreviewTitle != null && userNewsModel.getTitle() != null) {
        mPreviewTitle.setText(userNewsModel.getTitle());
      }
      if (mPreviewUpdateDate != null && userNewsModel.getUpdateDate() != null) {
        mPreviewUpdateDate.setText(userNewsModel.getUpdateDate());
      }
      if (mPreviewStatus != null && userNewsModel.getStatus() != null) {
        mPreviewStatus.setText(userNewsModel.getStatus());
      }
      if (mPreviewCountLikes != null) {
        mPreviewCountLikes.setText(String.valueOf(userNewsModel.getLikes()));
      }
      if (mPreviewImage != null && userNewsModel.getImage() != null) {
        Glide.with(App.instance().getApplicationContext())
            .load(userNewsModel.getImage())
            .error(R.drawable.art_snow)
            .crossFade()
            .into(mPreviewImage);
      }

    }
    return rootView;
  }
}
