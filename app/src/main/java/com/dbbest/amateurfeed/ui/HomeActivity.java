package com.dbbest.amateurfeed.ui;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;

import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.data.FeedContract;
import com.dbbest.amateurfeed.data.adapter.PreviewAdapter;
import com.dbbest.amateurfeed.presenter.HomePresenter;
import com.dbbest.amateurfeed.ui.dialog.WarningDialog;
import com.dbbest.amateurfeed.ui.fragments.FeedNewsFragment;
import com.dbbest.amateurfeed.ui.fragments.ItemDetailFragment;
import com.dbbest.amateurfeed.ui.fragments.ProfileFragment;
import com.dbbest.amateurfeed.ui.fragments.SearchFragment;
import com.dbbest.amateurfeed.ui.util.UIDialogNavigation;
import com.dbbest.amateurfeed.utils.BottomTab;
import com.dbbest.amateurfeed.utils.TabManager;
import com.dbbest.amateurfeed.utils.Utils;
import com.dbbest.amateurfeed.view.HomeView;

import java.util.HashMap;
import java.util.Stack;
import java.util.UUID;

import static android.R.attr.tag;
import static com.dbbest.amateurfeed.R.id.add;
import static com.dbbest.amateurfeed.R.id.imageView;


public class HomeActivity extends FragmentActivity implements TabHost.OnTabChangeListener, HomeView, WarningDialog.OnWarningDialogListener, FeedNewsFragment.Callback, ItemDetailFragment.Callback {

    public static final String FEED_NEWS_FRAGMENT_TAG = "FNFTAG";
    public static final String DETAIL_NEWS_FRAGMENT_TAG = "DNFTAG";
    public static final String SEARCH_FRAGMENT_TAG = "STAG";
    public static final String PROFILE_FRAGMENT_TAG = "PTAG";
    public static final String EDIT_PROFILE_FRAGMENT_TAG = "PREFTAG";
    public static final String MANAGE_FRAGMENTS = "ManageFragments";
    private DialogFragment mProgressDialog;
    private TabManager mTabManager;

    private int mLikeImage = R.drawable.ic_favorite_black_24dp;
    private int mDisLikeImage = R.drawable.ic_favorite_border_black_24dp;
    private int isLikeFlag = 0;
    private int mCountIsLikes = 0;
    private Uri mUriId;
//    private ItemDetailFragment mDetailFragment;

    private CoordinatorLayout coordinatorLayout;
    private HomePresenter mPresenter;
    private FragmentTabHost mTabHost;


    private HashMap<BottomTab, Stack<String>> backStacks;


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            int saved = savedInstanceState.getInt("tab", 0);
            Log.i(MANAGE_FRAGMENTS, " Restore selected Tab index: " + saved);
            if (saved != mTabHost.getCurrentTab())
                mTabHost.setCurrentTab(saved);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putInt("tab", mTabHost.getCurrentTab());
            outState.putSerializable("stacks", backStacks);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.home_activity);


        mPresenter = new HomePresenter();
        mPresenter.getNews(0, 5);


        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        addTab(BottomTab.HOME);
        addTab(BottomTab.SEARCH);
        addTab(BottomTab.PROFILE);
        mTabHost.setOnTabChangedListener(this);

        if (savedInstanceState != null) {
            int saved = savedInstanceState.getInt("tab", 0);
            Log.i(MANAGE_FRAGMENTS, " Restore Selected Tab index : " + saved);
            if (saved != mTabHost.getCurrentTab()) mTabHost.setCurrentTab(saved);
            Log.i(MANAGE_FRAGMENTS, "Restore Read back stacks after");
            backStacks = (HashMap<BottomTab, Stack<String>>) savedInstanceState.getSerializable("stacks");
        } else {
            // Initialize back stacks on first run

            Log.i(MANAGE_FRAGMENTS, "Initialize back stacks on first run");
            backStacks = new HashMap<BottomTab, Stack<String>>();
            backStacks.put(BottomTab.HOME, new Stack<String>());
            backStacks.put(BottomTab.SEARCH, new Stack<String>());
            backStacks.put(BottomTab.PROFILE, new Stack<String>());
            backStacks.put(BottomTab.DETAIL, new Stack<String>());
        }


    }


    @Override
    public void onTabChanged(String tabTag) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Select proper stack
        if (backStacks != null) {
            refreshContent();
            Stack<String> backStack = backStacks.get(BottomTab.getByTag(tabTag));
            Fragment fragment = null;
            if (backStack == null || backStack.isEmpty()) {


                Log.i(MANAGE_FRAGMENTS, " If BackStack is empty?... instantiate and add initial tab fragment");

                if (tabTag.equals(BottomTab.HOME.tag)) {

                    fragment = Fragment.instantiate(this, FeedNewsFragment.class.getName());

                } else if (tabTag.equals(BottomTab.SEARCH.tag)) {
                    fragment = Fragment.instantiate(this, SearchFragment.class.getName());

                } else if (tabTag.equals(BottomTab.PROFILE.tag)) {

                    fragment = Fragment.instantiate(this, ProfileFragment.class.getName());
                }
                if (fragment != null) {

                    addFragment(fragment, backStack, ft);
                }
            } else {
                Log.i(MANAGE_FRAGMENTS, "Show Fragment " + tabTag + "by tag: " + backStack.peek());
                showFragment(backStack, ft);
            }

        }
    }


    private void addFragment(Bundle args) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


        BottomTab bottomTabHome = BottomTab.getByTag(mTabHost.getCurrentTabTag());
        Stack<String> feedStack = backStacks.get(bottomTabHome);

        BottomTab bottomTabDetail = BottomTab.getByTag(DETAIL_NEWS_FRAGMENT_TAG);
        Stack<String> detailStack = backStacks.get(bottomTabDetail);

        Fragment feed = getSupportFragmentManager().findFragmentByTag(FEED_NEWS_FRAGMENT_TAG);
        if (feed != null) {
            transaction.detach(feed);
            Log.i(MANAGE_FRAGMENTS, "Detach  fragment " + bottomTabHome.fragmentClass.getName() + " by tag: " + FEED_NEWS_FRAGMENT_TAG);

        }

        if (feedStack.isEmpty()) {

            feedStack.push(UUID.randomUUID().toString());
            Fragment instantiate = Fragment.instantiate(this, FeedNewsFragment.class.getName());

//            transaction.add(android.R.id.tabcontent, instantiate, feedStack.peek());
            Log.i(MANAGE_FRAGMENTS, " If BackStack is empty?... instantiate and add initial Feed Fragment [Tag]: " + feedStack.peek());
        }
        feedStack = backStacks.get(bottomTabHome);


        if (!detailStack.isEmpty()) {

            Fragment top = getSupportFragmentManager().findFragmentByTag(detailStack.peek());
            transaction.detach(top);
            Log.i(MANAGE_FRAGMENTS, "Detach  fragment" + bottomTabDetail.fragmentClass.getName() + " by tag: " + detailStack.peek());
        }

        refreshContent();

        String tag = UUID.randomUUID().toString();
        detailStack.push(tag);
        Log.i(MANAGE_FRAGMENTS, " If BackStack is empty?... instantiate and add initial Item Detail Fragment [Tag]: " + detailStack.peek());
        Fragment instantiateDetailFragment = Fragment.instantiate(this, ItemDetailFragment.class.getName());
        instantiateDetailFragment.setArguments(args);
        transaction.add(android.R.id.tabcontent, instantiateDetailFragment, detailStack.peek());
        transaction.commit();
        Log.i(MANAGE_FRAGMENTS, "Attach  fragment" + BottomTab.DETAIL.fragmentClass.getName() + " by tag: " + detailStack.peek());


    }

    private void addFragment(Fragment fragment, Stack<String> backStack, FragmentTransaction ft) {
        String tag = UUID.randomUUID().toString();
        ft.add(android.R.id.tabcontent, fragment, tag);
        backStack.push(tag);
//        Log.i(MANAGE_FRAGMENTS, "Check  Push UUID tag to backStack: " + backStack.peek());
    }

    private void showFragment(Stack<String> backStack, FragmentTransaction ft) {
        String tag = backStack.peek();

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        ft.attach(fragment);
    }

    private void refreshContent() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Stack<String> detailStack = backStacks.get(BottomTab.DETAIL);
        if (!detailStack.isEmpty()) {

            Fragment top = getSupportFragmentManager().findFragmentByTag(detailStack.peek());
            if (top != null && !top.isDetached()) {
                ft.detach(top);

                Log.i(MANAGE_FRAGMENTS, "Detach" + BottomTab.DETAIL.fragmentClass.getName() + " by tag: " + detailStack.peek());
            }

        }

        Stack<String> feedStack = backStacks.get(BottomTab.HOME);
        if (!feedStack.isEmpty()) {

            Fragment top = getSupportFragmentManager().findFragmentByTag(feedStack.peek());
            if (top != null && !top.isDetached()) {
                ft.detach(top);
                Log.i(MANAGE_FRAGMENTS, "Detach" + BottomTab.HOME.fragmentClass.getName() + " by tag: " + feedStack.peek());
            }

        }
        Stack<String> searchStack = backStacks.get(BottomTab.SEARCH);
        if (!searchStack.isEmpty()) {

            Fragment top = getSupportFragmentManager().findFragmentByTag(searchStack.peek());
            if (top != null && !top.isDetached()) {
                ft.detach(top);
                Log.i(MANAGE_FRAGMENTS, "Detach" + BottomTab.SEARCH.fragmentClass.getName() + " by tag: " + searchStack.peek());
            }

        }
        Stack<String> profileStack = backStacks.get(BottomTab.PROFILE);
        if (!profileStack.isEmpty()) {

            Fragment top = getSupportFragmentManager().findFragmentByTag(profileStack.peek());
            if (top != null && !top.isDetached()) {
                ft.detach(top);
                Log.i(MANAGE_FRAGMENTS, "Detach" + BottomTab.PROFILE.fragmentClass.getName() + " by tag: " + profileStack.peek());
            }

        }
        ft.commit();

    }


    private void addTab(BottomTab homeTab) {
        TabHost.TabSpec spec = mTabHost.newTabSpec(homeTab.tag);
        spec.setIndicator(getTabIndicator(this, homeTab.iconRes));
        mTabHost.addTab(spec, homeTab.fragmentClass, null);
    }

    private View getTabIndicator(Context context, int icon) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        ImageView iv = (ImageView) view.findViewById(imageView);
        iv.setImageResource(icon);
        return view;
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

    }

    @Override
    public void onLikeItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh) {


        String mCountLikes = (vh.mLikesCountView.getText()).toString();
        if (mCountLikes != null) {
            mCountIsLikes = Integer.parseInt(mCountLikes);
            Log.i(Utils.TAG_LOG, "Count Likes: " + mCountIsLikes);
        }
        mUriId = uri;
        if (mCountIsLikes >= 0) {
            if (vh.mLikeButton.getTag() == "1") {

                isLikeFlag = 0;
                vh.mLikeButton.setTag("0");
                vh.mLikeButton.setImageResource(mDisLikeImage);
                mCountIsLikes = mCountIsLikes - 1;


            } else if (vh.mLikeButton.getTag() == "0") {
                isLikeFlag = 1;
                vh.mLikeButton.setTag("1");
                vh.mLikeButton.setImageResource(mLikeImage);
                mCountIsLikes = mCountIsLikes + 1;

            }


            vh.mLikesCountView.setText(String.valueOf(mCountIsLikes));
            mPresenter.putLike(FeedContract.PreviewEntry.getIdFromUri(uri), isLikeFlag);
        } else {
            Log.i(Utils.TAG_LOG, "Error in like clear All!");
            vh.mLikesCountView.setText(String.valueOf(0));
            mPresenter.putLike(FeedContract.PreviewEntry.getIdFromUri(uri), 0);
        }


    }

    @Override
    public void onCommentItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh) {

        int layoutId = -1;
        switch (vh.getItemViewType()) {

            case PreviewAdapter.VIEW_TYPE_MY: {
                layoutId = R.layout.fragment_item_my_detail;
                break;

            }
            case PreviewAdapter.VIEW_TYPE_USER: {
                layoutId = R.layout.fragment_item_detail;
                break;
            }
            case PreviewAdapter.VIEW_TYPE_ITEM_EMPTY: {
                layoutId = R.layout.item_empty_detail;
                break;
            }

        }


        Bundle args = new Bundle();
        args.putParcelable(ItemDetailFragment.DETAIL_URI, uri);
        args.putInt(ItemDetailFragment.DETAIL_TYPE, layoutId);
//        Fragment fragment = Fragment.instantiate(this, ItemDetailFragment.class.getName());
//        fragment.setArguments(args);
        Fragment fragment = ItemDetailFragment.newInstance(DETAIL_NEWS_FRAGMENT_TAG);
        fragment.setArguments(args);


        addFragment(args);

//        Stack<String> backStack = backStacks.get(BottomTab.getByTag(DETAIL_NEWS_FRAGMENT_TAG));
//        if (backStack != null) {
//            showFragment(backStack, getSupportFragmentManager().beginTransaction());
//        }
//        getSupportFragmentManager().beginTransaction()
//                .replace(android.R.id.tabcontent, mDetailFragment, DETAIL_NEWS_FRAGMENT_TAG)
//                .commit();

    }

    @Override
    public void onEditItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh) {
//        Bundle args = new Bundle();
//        args.putParcelable(ItemDetailFragment.DETAIL_URI, uri);
//        mDetailFragment = ItemDetailFragment.newInstance(DETAIL_NEWS_FRAGMENT_TAG);
//        mDetailFragment.setArguments(args);
//
//
//        getSupportFragmentManager().beginTransaction()
//                .replace(android.R.id.tabcontent, mDetailFragment, DETAIL_NEWS_FRAGMENT_TAG)
//                .commit();
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

        FeedNewsFragment newsFragment = (FeedNewsFragment) getSupportFragmentManager().findFragmentByTag(FEED_NEWS_FRAGMENT_TAG);
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

    @Override
    public void onLikeItemSelected(Uri uri, int isLikeFlag) {
        mPresenter.putLike(FeedContract.PreviewEntry.getIdFromUri(uri), isLikeFlag);
    }

    @Override
    public void onCommentItemSelected(Uri uri) {

    }

    @Override
    public void onEditItemSelected(Uri uri) {

    }

    @Override
    public void onDeleteItemSelected(Uri uri) {
        mUriId = uri;
        UIDialogNavigation.warningDialog(R.string.abuse_dialog, R.string.ok, R.string.cancel, true, 100, this).show(getSupportFragmentManager(), "abuse_dialog");

    }


}
