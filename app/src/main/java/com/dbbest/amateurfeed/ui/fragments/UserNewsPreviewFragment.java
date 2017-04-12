package com.dbbest.amateurfeed.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.model.UserNewsModel;
import com.dbbest.amateurfeed.ui.activity.HomeActivity;


public class UserNewsPreviewFragment extends Fragment {

  public static final String MODEL = "model";
  private UserNewsModel userNewsModel;


  public UserNewsPreviewFragment() {
    setHasOptionsMenu(true);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        Toast.makeText(getActivity(), "Go to Profile Screen--->", Toast.LENGTH_SHORT).show();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager()
            .beginTransaction();
        ((HomeActivity) getActivity()).refreshContent();
        transaction.attach(getActivity().getSupportFragmentManager().findFragmentByTag(
            HomeActivity.PROFILE_FRAGMENT_TAG));
        transaction.commit();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    final View rootView = inflater.inflate(R.layout.fragment_user_news, container, false);
    Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
    ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowTitleEnabled(false);
      actionBar.setTitle(null);
    }
    Bundle arguments = getArguments();
    if (arguments != null) {
      userNewsModel = arguments.getParcelable(MODEL);
    }
    TextView previewTitle = (TextView) rootView.findViewById(R.id.text_user_news_title);
    TextView previewUpdateDate = (TextView) rootView.findViewById(R.id.text_uaser_news_update_date);
    TextView previewStatus = (TextView) rootView.findViewById(R.id.text_status_user_news);
    TextView previewCountLikes = (TextView) rootView.findViewById(R.id.text_user_amount_likes);
    ImageView previewImage = (ImageView) rootView.findViewById(R.id.image_user_photo);
    if (userNewsModel != null) {

      if (previewTitle != null && userNewsModel.getTitle() != null) {
        previewTitle.setText(userNewsModel.getTitle());
      }
      if (previewUpdateDate != null && userNewsModel.getUpdateDate() != null) {
        previewUpdateDate.setText(userNewsModel.getUpdateDate());
      }
      if (previewStatus != null && userNewsModel.getStatus() != null) {
        String s = (userNewsModel.getStatus().equals("1")) ? getContext().getString(R.string.status_news_active)
            : getContext().getString(R.string.status_news_pending);
        previewStatus.setText(s);
      }
      if (previewCountLikes != null) {
        previewCountLikes.setText(String.valueOf(userNewsModel.getLikes()));
      }
      if (previewImage != null && userNewsModel.getImage() != null) {
        Glide.with(App.instance().getApplicationContext())
            .load(userNewsModel.getImage())
            .error(R.drawable.art_snow)
            .crossFade()
            .into(previewImage);
      }
    }
    return rootView;
  }
}
