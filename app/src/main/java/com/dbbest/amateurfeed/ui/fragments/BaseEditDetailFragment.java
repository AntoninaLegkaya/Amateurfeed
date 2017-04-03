package com.dbbest.amateurfeed.ui.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import com.dbbest.amateurfeed.presenter.DetailPresenter;
import com.dbbest.amateurfeed.view.DetailView;

public class BaseEditDetailFragment extends EditFragment implements DetailView {

  protected String TAG = BaseEditDetailFragment.class.getName();
  protected DetailPresenter presenter;
  protected Uri uriPreview;
  protected TextView descriptionView;
  protected TextView titleView;


  @Override
  public void addTagToItemDetail(Bundle data) {

  }

  @Override
  public void updateDetailsFields(Bundle data) {

  }

  @Override
  public void showSuccessEditNewsDialog() {

  }

  @Override
  public void showErrorEditNewsDialog() {

  }

  @Override
  public void showSuccessAddCommentDialog() {

  }

  @Override
  public void showErrorAddCommentDialog() {

  }

  @Override
  public void refreshFeedNews(Bundle data) {

  }

  @Override
  public void checkUpdateImage() {

  }

  @Override
  public void addCommentToBd(Bundle data) {

  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    presenter = new DetailPresenter();

  }

  @Override
  public void onStart() {
    super.onStart();
    presenter.attachView(this);


  }

  @Override
  public void onStop() {
    super.onStop();
    presenter.detachView();

  }

  public interface Callback {

    void onLikeItemSelected(Uri uri, int isLike, int count);

    void onEditItemSelected(Uri uri);

    void onDeleteItemSelected(Uri uri);

    void moveToFeedFragment();

    void refreshFeed();

  }

}
