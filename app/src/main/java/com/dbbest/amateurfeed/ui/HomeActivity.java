package com.dbbest.amateurfeed.ui;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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
import com.dbbest.amateurfeed.ui.fragments.AddItemDetailFragment;
import com.dbbest.amateurfeed.ui.fragments.BaseChangeDetailFragment;
import com.dbbest.amateurfeed.ui.fragments.FeedNewsFragment;
import com.dbbest.amateurfeed.ui.fragments.EditItemDetailFragment;
import com.dbbest.amateurfeed.ui.fragments.ProfileFragment;
import com.dbbest.amateurfeed.ui.fragments.SearchFragment;
import com.dbbest.amateurfeed.ui.util.UIDialogNavigation;
import com.dbbest.amateurfeed.utils.BottomTab;
import com.dbbest.amateurfeed.utils.TabManager;
import com.dbbest.amateurfeed.view.HomeView;

import java.util.HashMap;
import java.util.Stack;

import static com.dbbest.amateurfeed.R.id.imageView;


public class HomeActivity extends AppCompatActivity implements TabHost.OnTabChangeListener, HomeView, WarningDialog.OnWarningDialogListener, FeedNewsFragment.Callback,  BaseChangeDetailFragment.Callback{

    private static String TAG_HOME = "HomeActivity";

    public static final String FEED_NEWS_FRAGMENT_TAG = "FNFTAG";
    public static final String DETAIL_NEWS_FRAGMENT_TAG = "DNFTAG";
    public static final String DETAIL_ADD_NEWS_FRAGMENT_TAG = "DNFTAG_ADD";
    public static final String SEARCH_FRAGMENT_TAG = "STAG";
    public static final String PROFILE_FRAGMENT_TAG = "PTAG";
    public static final String EDIT_PROFILE_FRAGMENT_TAG = "PREFTAG";
    public static final String MANAGE_FRAGMENTS = "ManageFragments";
    public static int RESULT_LOAD_IMAGE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChosenTask;
    private DialogFragment mProgressDialog;
    private TabManager mTabManager;

    private int mLikeImage = R.drawable.ic_favorite_black_24dp;
    private int mDisLikeImage = R.drawable.ic_favorite_border_black_24dp;
    private int isLikeFlag = 0;
    private int mCountIsLikes = 0;
    private Uri mUriId;

    private CoordinatorLayout coordinatorLayout;
    private HomePresenter mPresenter;
    private FragmentTabHost mTabHost;
    private String mCurrentTag;
    private Bundle mArgsDetail;


    public HashMap<BottomTab, Stack<String>> getBackStacks() {
        return backStacks;
    }

    private HashMap<BottomTab, Stack<String>> backStacks;


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


        backStacks = (HashMap<BottomTab, Stack<String>>) savedInstanceState.getSerializable("stacks");
        refreshContent();

//        int saved = savedInstanceState.getInt("tab", 0);
//        String tag = savedInstanceState.getString("tag");
//
//        if (tag != null) {
//            Log.i(MANAGE_FRAGMENTS, " Restore selected fragment [Tag]: " + tag);
//            Uri uri = savedInstanceState.getParcelable(ItemDetailFragment.DETAIL_URI);
//            int type = savedInstanceState.getInt(ItemDetailFragment.DETAIL_TYPE);
//
//            if (tag != DETAIL_NEWS_FRAGMENT_TAG) {
//                if (saved != mTabHost.getCurrentTab())
//                    Log.i(MANAGE_FRAGMENTS, " Show Fragment by  [Tag]: " + tag);
//                mTabHost.setCurrentTab(saved);
//            } else {
//
//                if (uri != null) {
//                    Log.i(MANAGE_FRAGMENTS, " Restore Detail info : \n" + " Uri: " + uri.toString() + "\n Type: " + type);
//                    Bundle args = new Bundle();
//                    args.putParcelable(ItemDetailFragment.DETAIL_URI, uri);
//                    args.putInt(ItemDetailFragment.DETAIL_TYPE, type);
//
//                    addFragment(args);
//                } else {
//                    refreshContent();
//                    mTabHost.setCurrentTab(saved);
//
//                    Log.i(MANAGE_FRAGMENTS, " Show Default Fragment by  [Tab]: " + saved);
//
//                }
//
//            }
//        }


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putInt("tab", mTabHost.getCurrentTab());
            outState.putString("tag", mCurrentTag);
            outState.putSerializable("stacks", backStacks);
            if (mArgsDetail != null) {
                outState.putParcelable(EditItemDetailFragment.DETAIL_URI, mArgsDetail.getParcelable(EditItemDetailFragment.DETAIL_URI));
                outState.putInt(EditItemDetailFragment.DETAIL_TYPE, mArgsDetail.getInt(EditItemDetailFragment.DETAIL_TYPE));
            }

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
            backStacks = (HashMap<BottomTab, Stack<String>>) savedInstanceState.getSerializable("stacks");
            refreshContent();


        } else {

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
        if (backStacks != null) {

            refreshContent();

            Stack<String> backStack = backStacks.get(BottomTab.getByTag(tabTag));
            Fragment fragment = null;
            if (backStack == null || backStack.isEmpty()) {

                Log.i(MANAGE_FRAGMENTS, " If BackStack is empty?... instantiate and add initial tab fragment : " + tabTag);

                if (tabTag.equals(BottomTab.HOME.tag)) {

                    fragment = Fragment.instantiate(this, FeedNewsFragment.class.getName());

                } else if (tabTag.equals(BottomTab.SEARCH.tag)) {
                    fragment = Fragment.instantiate(this, SearchFragment.class.getName());

                } else if (tabTag.equals(BottomTab.PROFILE.tag)) {

                    fragment = Fragment.instantiate(this, ProfileFragment.class.getName());

                }
                if (fragment != null) {

                    addFragment(fragment, backStack, ft, tabTag);
                }
            } else {

                showFragment(backStack, ft);
            }

        }
        mCurrentTag = tabTag;
    }

    private void addFragment(Bundle args) {

        Log.i(MANAGE_FRAGMENTS, "------------------------- Attach Detail -------------------------------------- ");

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


        BottomTab bottomTabDetail = BottomTab.getByTag(DETAIL_NEWS_FRAGMENT_TAG);
        Stack<String> detailStack = backStacks.get(bottomTabDetail);


        BottomTab bottomTabHome = BottomTab.getByTag(FEED_NEWS_FRAGMENT_TAG);
        Stack<String> feedStack = backStacks.get(bottomTabHome);


        if (feedStack.isEmpty()) {
            feedStack.push(FEED_NEWS_FRAGMENT_TAG);
            Fragment instantiate = Fragment.instantiate(this, FeedNewsFragment.class.getName());
            Log.i(MANAGE_FRAGMENTS, " If feedStack is empty?... instantiate and add initial Feed Fragment [Tag]: " + feedStack.peek());

        } else

        {
            Fragment top = getSupportFragmentManager().findFragmentByTag(feedStack.peek());
            if (top != null || !top.isDetached()) {
                transaction.detach(top);
                Log.i(MANAGE_FRAGMENTS, "Detach   fragment" + " by [Tag]: " + feedStack.peek());
            }

        }

        refreshContent();

        detailStack.push(DETAIL_NEWS_FRAGMENT_TAG);
        Log.i(MANAGE_FRAGMENTS, "Create Item Detail Fragment [Tag]: " + detailStack.peek());
        Fragment instantiateDetailFragment;
        if (!args.isEmpty()) {
            instantiateDetailFragment = Fragment.instantiate(this, EditItemDetailFragment.class.getName());
            instantiateDetailFragment.setArguments(args);
        } else {
            instantiateDetailFragment = Fragment.instantiate(this, AddItemDetailFragment.class.getName());


        }
        transaction.add(android.R.id.tabcontent, instantiateDetailFragment, detailStack.peek());
        transaction.commit();
        mCurrentTag = detailStack.peek();
        Log.i(MANAGE_FRAGMENTS, "Attach  " + BottomTab.DETAIL.fragmentClass.getName() + " by [Tag]: " + detailStack.peek());

    }

    private void addFragment(Fragment fragment, Stack<String> backStack, FragmentTransaction ft, String tag) {
        ft.add(android.R.id.tabcontent, fragment, tag);
        backStack.push(tag);
    }

    private void showFragment(Stack<String> backStack, FragmentTransaction ft) {
        String tag = backStack.peek();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            ft.attach(fragment);
            ft.commit();
            Log.i(MANAGE_FRAGMENTS, "Show Fragment  by [Tag]: " + backStack.peek());
        } else {
            Log.i(MANAGE_FRAGMENTS, "Could not Show Fragment  by [Tag]: " + tag);
        }
    }

    @Override
    public void moveToFeedFragment() {

        refreshContent();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        BottomTab bottomTabHome = BottomTab.getByTag(BottomTab.HOME.tag);
        Stack<String> feedStack = backStacks.get(bottomTabHome);
        showFragment(feedStack, ft);
        mCurrentTag = BottomTab.HOME.tag;

    }


    private void refreshContent() {

        Log.i(MANAGE_FRAGMENTS, "Refresh Content");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (backStacks != null) {
            Stack<String> detailStack = backStacks.get(BottomTab.DETAIL);
            if (!detailStack.isEmpty()) {

                Fragment top = getSupportFragmentManager().findFragmentByTag(detailStack.peek());
                if (top != null || !top.isDetached()) {
                    ft.detach(top);

                    Log.i(MANAGE_FRAGMENTS, "--------------Detach " + BottomTab.DETAIL.fragmentClass.getName() + " by [Tag]: " + detailStack.peek() + "-----------");
                }

            }

            Stack<String> feedStack = backStacks.get(BottomTab.HOME);
            if (!feedStack.isEmpty()) {

                Fragment top = getSupportFragmentManager().findFragmentByTag(feedStack.peek());
                if (top != null && !top.isDetached()) {
                    ft.detach(top);
                    Log.i(MANAGE_FRAGMENTS, "--------------Detach " + BottomTab.HOME.fragmentClass.getName() + " by [Tag]: " + feedStack.peek() + "-----------");
                }

            }
            Stack<String> searchStack = backStacks.get(BottomTab.SEARCH);
            if (!searchStack.isEmpty()) {

                Fragment top = getSupportFragmentManager().findFragmentByTag(searchStack.peek());
                if (top != null || !top.isDetached()) {
                    ft.detach(top);
                    Log.i(MANAGE_FRAGMENTS, "--------------Detach " + BottomTab.SEARCH.fragmentClass.getName() + " by [Tag]: " + searchStack.peek() + "-----------");
                }

            }
            Stack<String> profileStack = backStacks.get(BottomTab.PROFILE);
            if (!profileStack.isEmpty()) {

                Fragment top = getSupportFragmentManager().findFragmentByTag(profileStack.peek());
                if (top != null) {
                    if (!top.isDetached()) {
                        ft.detach(top);
                        Log.i(MANAGE_FRAGMENTS, "--------------Detach " + BottomTab.PROFILE.fragmentClass.getName() + " by [Tag]: " + profileStack.peek() + "-----------");
                    }
                }
            }
            ft.commit();
        }
        Log.i(MANAGE_FRAGMENTS, "-------------------------Refresh Content----------------------------------------------- ");
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


    @Override
    public void onItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh) {

    }

    @Override
    public void onLikeItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh) {


        String mCountLikes = (vh.mLikesCountView.getText()).toString();
        if (mCountLikes != null) {
            mCountIsLikes = Integer.parseInt(mCountLikes);
            Log.i(TAG_HOME, "Count Likes: " + mCountIsLikes);
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
            Log.i(TAG_HOME, "Error in like clear All!");
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

        mArgsDetail = new Bundle();
        mArgsDetail.putParcelable(EditItemDetailFragment.DETAIL_URI, uri);
        mArgsDetail.putInt(EditItemDetailFragment.DETAIL_TYPE, layoutId);

        Fragment fragment = EditItemDetailFragment.newInstance(DETAIL_NEWS_FRAGMENT_TAG);
        fragment.setArguments(mArgsDetail);


        addFragment(mArgsDetail);


    }

    @Override
    public void onEditItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh) {

        int layoutId = R.layout.fragment_item_edit_my_detail;
        mArgsDetail = new Bundle();
        mArgsDetail.putParcelable(EditItemDetailFragment.DETAIL_URI, uri);
        mArgsDetail.putInt(EditItemDetailFragment.DETAIL_TYPE, layoutId);

        Fragment fragment = EditItemDetailFragment.newInstance(DETAIL_NEWS_FRAGMENT_TAG);
        fragment.setArguments(mArgsDetail);
        addFragment(mArgsDetail);


    }

    @Override
    public void onDeleteItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh) {
        mUriId = uri;
        UIDialogNavigation.warningDialog(R.string.abuse_dialog, R.string.ok, R.string.cancel, true, 100, this).show(getSupportFragmentManager(), "abuse_dialog");


    }

    @Override
    public void upLoadNewsItems(int offset, int count) {
        Log.i(TAG_HOME, "New Request to upload news: offset: " + offset);
        mPresenter.getNews(offset, count);
    }

    @Override
    public void addNewItemDetail() {
        Fragment fragment = AddItemDetailFragment.newInstance(DETAIL_ADD_NEWS_FRAGMENT_TAG);
        fragment.setArguments(Bundle.EMPTY);
        addFragment(Bundle.EMPTY);

    }


    @Override
    public void refreshFragmentFeedLoader() {

        FeedNewsFragment newsFragment = (FeedNewsFragment) getSupportFragmentManager().findFragmentByTag(FEED_NEWS_FRAGMENT_TAG);
        if (newsFragment != null) {
            newsFragment.refreshFragmentLoader();
        }

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
//        UIDialogNavigation.showWarningDialog(R.string.set_like_succes).show(getSupportFragmentManager(), "warn");
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
    public void onWarningDialogCancelClicked(int dialogCode) {
        UIDialogNavigation.showWarningDialog(R.string.set_good_by).show(getSupportFragmentManager(), "info");

    }

    @Override
    public void onWarningDialogOkClicked(int dialogCode) {
        if (mUriId != null) {

            long id = FeedContract.PreviewEntry.getIdFromUri(mUriId);
            Log.i(TAG_HOME, " Suggest Delete Item uri: " + id);
            mPresenter.putDelete(id, "Comment");
        }

    }

    @Override
    public void onLikeItemSelected(Uri uri, int isLike, int count) {
        mUriId = uri;
        isLikeFlag = isLike;
        mCountIsLikes = count;
        mPresenter.putLike(FeedContract.PreviewEntry.getIdFromUri(uri), isLikeFlag);
    }

    @Override
    public void onCommentItemSelected(Uri uri) {

    }

    @Override
    public void onEditItemSelected(Uri uri) {
        int layoutId = R.layout.fragment_item_edit_my_detail;
        mArgsDetail = new Bundle();
        mArgsDetail.putParcelable(EditItemDetailFragment.DETAIL_URI, uri);
        mArgsDetail.putInt(EditItemDetailFragment.DETAIL_TYPE, layoutId);

        Fragment fragment = EditItemDetailFragment.newInstance(DETAIL_NEWS_FRAGMENT_TAG);
        fragment.setArguments(mArgsDetail);
        addFragment(mArgsDetail);

    }

    @Override
    public void onDeleteItemSelected(Uri uri) {
        mUriId = uri;
        UIDialogNavigation.warningDialog(R.string.abuse_dialog, R.string.ok, R.string.cancel, true, 100, this).show(getSupportFragmentManager(), "abuse_dialog");

    }


    @NonNull
    @Override
    public Context getContext() {
        return this;
    }
}
