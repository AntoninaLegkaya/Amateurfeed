package com.dbbest.amateurfeed.ui;

import android.content.Context;
import android.content.Intent;
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

import com.dbbest.amateurfeed.ui.fragments.FeedNewsFragment;
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

public class HomeActivity extends AppCompatActivity implements HomeView, FeedNewsFragment.Callback {
    private static final String FEDD_NEWS_FRAGMENT_TAG = "FNFTAG";
    private static final String SEARCH_FRAGMENT_TAG = "STAG";
    public static final String PROFILE_FRAGMENT_TAG = "PTAG";
    public static final String EDITE_PROFILE_FRAGMENT_TAG = "PREFTAG";
    private DialogFragment mProgressDialog;

    private int mLikeImage = R.drawable.ic_favorite_black_24dp;
    private int mDisLikeImage = R.drawable.ic_favorite_border_black_24dp;

    private CoordinatorLayout coordinatorLayout;
    private HomePresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        mPresenter = new HomePresenter();
        mPresenter.getNews(0, 20);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.tabs_activity);
        Snackbar.make(coordinatorLayout, "Initial input", Snackbar.LENGTH_LONG).show();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                FeedNewsFragment.newInstance(""), FEDD_NEWS_FRAGMENT_TAG).commit();


        BottomBar bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItemsFromMenu(R.menu.bottom_tab, new OnMenuTabSelectedListener() {
            @Override
            public void onMenuItemSelected(int itemId) {
                switch (itemId) {
                    case R.id.home_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                FeedNewsFragment.newInstance(""), FEDD_NEWS_FRAGMENT_TAG).commit();
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
        Log.i(Utils.TAG_LOG, "Clik on Item......");
        UIDialogNavigation.showWarningDialog(R.string.item).show(getSupportFragmentManager(), "info");
    }

    @Override
    public void onLikeItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh) {
        int count = 0;
        String mCountLikes = (vh.mLikesCountView.getText()).toString();
        if (mCountLikes != null) {
            count = Integer.parseInt(mCountLikes);
            Log.i(Utils.TAG_LOG, "Count Likes: " + count);
        }
        if (vh.mLikeButton.getTag() == "1") {


            vh.mLikeButton.setTag("0");
            vh.mLikeButton.setImageResource(mDisLikeImage);
            Log.i(Utils.TAG_LOG, "Item ID: " + FeedContract.PreviewEntry.getIdFromUri(uri));
            mPresenter.putLike(FeedContract.PreviewEntry.getIdFromUri(uri), 0);
            vh.mLikesCountView.setText(String.valueOf(count - 1));


        } else if (vh.mLikeButton.getTag() == "0") {

            vh.mLikeButton.setTag("1");
            vh.mLikeButton.setImageResource(mLikeImage);
            vh.mLikesCountView.setText(String.valueOf(count + 1));
            Log.i(Utils.TAG_LOG, "Item ID: " + FeedContract.PreviewEntry.getIdFromUri(uri));
            mPresenter.putLike(FeedContract.PreviewEntry.getIdFromUri(uri), 1);
        }



    }

    @Override
    public void onCommentItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh) {


        UIDialogNavigation.showWarningDialog(R.string.comment).show(getSupportFragmentManager(), "info");
    }

    @Override
    public void onEditeItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh) {
        Log.i(Utils.TAG_LOG, "Add Edit......");
        UIDialogNavigation.showWarningDialog(R.string.edit).show(getSupportFragmentManager(), "info");
    }

    @Override
    public void onDeleteItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh) {
        Log.i(Utils.TAG_LOG, "Add Delete......");
        UIDialogNavigation.showWarningDialog(R.string.remove).show(getSupportFragmentManager(), "info");
    }

    @Override
    public void showSuccessDialog() {
        UIDialogNavigation.showWarningDialog(R.string.get_news_succes).show(getSupportFragmentManager(), "warn");
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
        UIDialogNavigation.showWarningDialog(R.string.set_like_succes).show(getSupportFragmentManager(), "warn");

    }

    @Override
    public void showErrorLikeDialog() {
        UIDialogNavigation.showWarningDialog(R.string.set_like_error).show(getSupportFragmentManager(), "warn");
    }
}
