package com.dbbest.amateurfeed.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.data.FeedContract;
import com.dbbest.amateurfeed.data.adapter.PreviewAdapter;
import com.dbbest.amateurfeed.presenter.HomePresenter;

import android.support.design.widget.Snackbar;
import android.util.Log;

import com.dbbest.amateurfeed.ui.dialog.WarningDialog;
import com.dbbest.amateurfeed.ui.fragments.FeedNewsFragment;
import com.dbbest.amateurfeed.ui.fragments.ItemDetailFragment;
import com.dbbest.amateurfeed.ui.fragments.ProfileFragment;
import com.dbbest.amateurfeed.ui.fragments.SearchFragment;
import com.dbbest.amateurfeed.ui.util.UIDialogNavigation;
import com.dbbest.amateurfeed.utils.Utils;
import com.dbbest.amateurfeed.view.HomeView;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

/**
 * Created by antonina on 19.01.17.
 */

public class HomeActivity extends AppCompatActivity implements HomeView, WarningDialog.OnWarningDialogListener, FeedNewsFragment.Callback {
    private static final String FEDD_NEWS_FRAGMENT_TAG = "FNFTAG";
    private static final String DETAIL_NEWS_FRAGMENT_TAG = "DNFTAG";
    private static final String SEARCH_FRAGMENT_TAG = "STAG";
    public static final String PROFILE_FRAGMENT_TAG = "PTAG";
    public static final String EDITE_PROFILE_FRAGMENT_TAG = "PREFTAG";
    private DialogFragment mProgressDialog;

    private int mLikeImage = R.drawable.ic_favorite_black_24dp;
    private int mDisLikeImage = R.drawable.ic_favorite_border_black_24dp;
    private int isLikeFlag = 0;
    private int mCountIsLikes = 0;
    private Uri mUriId;
    private FeedNewsFragment mFeedNewsFragment;
    private ItemDetailFragment mDetailFragment;

    private CoordinatorLayout coordinatorLayout;
    private HomePresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);


        mPresenter = new HomePresenter();
        mPresenter.getNews(0, 5);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.tabs_activity);
        mFeedNewsFragment = FeedNewsFragment.newInstance(FEDD_NEWS_FRAGMENT_TAG);
        if (savedInstanceState == null) {
            Snackbar.make(coordinatorLayout, "Initial input", Snackbar.LENGTH_LONG).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    mFeedNewsFragment, FEDD_NEWS_FRAGMENT_TAG).commit();
        }


        BottomBar bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItemsFromMenu(R.menu.bottom_tab, new OnMenuTabSelectedListener() {
            @Override
            public void onMenuItemSelected(int itemId) {
                switch (itemId) {
                    case R.id.home_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                mFeedNewsFragment, FEDD_NEWS_FRAGMENT_TAG).commit();
                        Snackbar.make(coordinatorLayout, "Home Item Selected", Snackbar.LENGTH_LONG).show();
                        break;
                    case R.id.search_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                SearchFragment.newInstance(""), SEARCH_FRAGMENT_TAG).commit();
                        Snackbar.make(coordinatorLayout, "Search Item Selected", Snackbar.LENGTH_LONG).show();

                        break;
                    case R.id.profile_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                ProfileFragment.newInstance(""), PROFILE_FRAGMENT_TAG).commit();
                        Snackbar.make(coordinatorLayout, "Profile Item Selected", Snackbar.LENGTH_LONG).show();
                        break;
                }
            }
        });
        // Use custom text appearance in tab titles.
        bottomBar.setTextAppearance(R.style.TabHome);


    }


    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.attachView(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.detachView();
    }


    @NonNull
    @Override
    public Context getContext() {
        return null;
    }


    @Override
    public void onItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh) {
//        UIDialogNavigation.showWarningDialog(R.string.item).show(getSupportFragmentManager(), "info");
    }

    @Override
    public void onLikeItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh) {


        String mCountLikes = (vh.mLikesCountView.getText()).toString();
        if (mCountLikes != null) {
            mCountIsLikes = Integer.parseInt(mCountLikes);
            Log.i(Utils.TAG_LOG, "Count Likes: " + mCountIsLikes);
        }
        mUriId = uri;

        if (vh.mLikeButton.getTag() == "1") {

            isLikeFlag = 0;
            vh.mLikeButton.setTag("0");
            vh.mLikeButton.setImageResource(mDisLikeImage);
//            Log.i(Utils.TAG_LOG, "Item ID: " + FeedContract.PreviewEntry.getIdFromUri(uri));
            mPresenter.putLike(FeedContract.PreviewEntry.getIdFromUri(uri), isLikeFlag);
            mCountIsLikes = mCountIsLikes - 1;
            vh.mLikesCountView.setText(String.valueOf(mCountIsLikes));


        } else if (vh.mLikeButton.getTag() == "0") {
            isLikeFlag = 1;
            vh.mLikeButton.setTag("1");
            vh.mLikeButton.setImageResource(mLikeImage);
            mCountIsLikes = mCountIsLikes + 1;
            vh.mLikesCountView.setText(String.valueOf(mCountIsLikes));
//            Log.i(Utils.TAG_LOG, "Item ID: " + FeedContract.PreviewEntry.getIdFromUri(uri));
            mPresenter.putLike(FeedContract.PreviewEntry.getIdFromUri(uri), isLikeFlag);
        }

    }

    @Override
    public void onCommentItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh) {


        Bundle args = new Bundle();
        args.putParcelable(ItemDetailFragment.DETAIL_URI, uri);
        mDetailFragment = ItemDetailFragment.newInstance(DETAIL_NEWS_FRAGMENT_TAG);
        mDetailFragment.setArguments(args);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mDetailFragment, DETAIL_NEWS_FRAGMENT_TAG)
                .commit();
    }

    @Override
    public void onEditItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh) {
        Bundle args = new Bundle();
        args.putParcelable(ItemDetailFragment.DETAIL_URI, uri);
        mDetailFragment = ItemDetailFragment.newInstance(DETAIL_NEWS_FRAGMENT_TAG);
        mDetailFragment.setArguments(args);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mDetailFragment, DETAIL_NEWS_FRAGMENT_TAG)
                .commit();
    }

    @Override
    public void onDeleteItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh) {
        mUriId = uri;
        UIDialogNavigation.warningDialog(R.string.abuse_dialog, R.string.ok, R.string.cancel, true, 100, this).show(getSupportFragmentManager(), "abuse_dialog");


    }

    @Override
    public void upLoadNewsItems(int offset, int count) {
        Log.i(Utils.TAG_LOG_LOAD_NEW_DATA, "New Request to upload news: offset: " + offset);
        mPresenter.getNews(offset, count);
    }


    @Override
    public void refreshFragmentFeedLoader() {

        FeedNewsFragment newsFragment = (FeedNewsFragment) getSupportFragmentManager().findFragmentByTag(FEDD_NEWS_FRAGMENT_TAG);
        if (newsFragment != null) {

            newsFragment.refreshFragmentLoader();

        }


    }

    @Override
    public void showSuccessDialog() {

    }

    @Override
    public void showErrorConnectionDialog() {

        UIDialogNavigation.showWarningDialog(R.string.no_internet_connection).show(getSupportFragmentManager(), "warn");

    }

    @Override
    public void showErrorDialog() {
        UIDialogNavigation.showWarningDialog(R.string.get_news_error).show(getSupportFragmentManager(), "warn");
    }

    @Override
    public void showProgressDialog() {
        mProgressDialog = UIDialogNavigation.showProgressDialog();
        mProgressDialog.show(getSupportFragmentManager(), "progress");
    }

    @Override
    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void navigateToScreen() {

    }

    @Override
    public void showSuccessLikeDialog() {

    }

    @Override
    public void showErrorLikeDialog() {
        UIDialogNavigation.showWarningDialog(R.string.set_like_error).show(getSupportFragmentManager(), "warn");
    }

    @Override
    public void showSuccessDeleteDialog() {
        UIDialogNavigation.showWarningDialog(R.string.set_delete_succes).show(getSupportFragmentManager(), "warn");
    }

    @Override
    public void showErrorDeleteDialog() {
        UIDialogNavigation.showWarningDialog(R.string.set_delete_error).show(getSupportFragmentManager(), "warn");

    }

    @Override
    public void updateColumnLikeInBd() {

        ContentValues values = new ContentValues();
        values.put(FeedContract.PreviewEntry.COLUMN_IS_LIKE, isLikeFlag);
        values.put(FeedContract.PreviewEntry.COLUMN_LIKES, mCountIsLikes);
        if (mUriId != null) {

            long id = FeedContract.PreviewEntry.getIdFromUri(mUriId);
            Uri uriPreviewId = FeedContract.PreviewEntry.buildSetLikeInPreviewUriById(id);
            App.instance().getContentResolver().update(uriPreviewId, values, null, null);
        }


    }

    @Override
    public void onWarningDialogCancelClicked(int dialogCode) {
        UIDialogNavigation.showWarningDialog(R.string.set_good_by).show(getSupportFragmentManager(), "info");

    }

    @Override
    public void onWarningDialogOkClicked(int dialogCode) {
        if (mUriId != null) {

            long id = FeedContract.PreviewEntry.getIdFromUri(mUriId);
            Log.i(Utils.TAG_LOG, " Suggest Delete Item uri: " + id);
            mPresenter.putDelete(id, "Comment");
        }

    }
}
