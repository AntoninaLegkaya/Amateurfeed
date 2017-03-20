package com.dbbest.amateurfeed.ui.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import com.dbbest.amateurfeed.presenter.DetailPresenter;
import com.dbbest.amateurfeed.view.DetailView;

public class BaseEditDetailFragment extends EditFragment implements DetailView {

  public final static String TAG_BASE = BaseEditDetailFragment.class.getName();
  public final static String DETAIL_FRAGMENT_COMMENT = "DetailFragmentI_comment";
  protected String TAG = BaseEditDetailFragment.class.getName();
  protected DetailPresenter mPresenter;
  protected Uri mUriPreview;
  protected TextView mDescriptionView;
  protected TextView mTitleView;


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
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mPresenter = new DetailPresenter();

  }

  @Override
  public void onStart() {
    super.onStart();
    mPresenter.attachView(this);


  }

  @Override
  public void onStop() {
    super.onStop();
    mPresenter.detachView();

  }


  public interface Callback {

    public void onLikeItemSelected(Uri uri, int isLike, int count);

    public void onCommentItemSelected(Uri uri);

    public void onEditItemSelected(Uri uri);

    public void onDeleteItemSelected(Uri uri);

    public void moveToFeedFragment();

    public void refreshFeed();


  }


}
