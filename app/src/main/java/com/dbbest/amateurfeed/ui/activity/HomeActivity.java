package com.dbbest.amateurfeed.ui.activity;

import static com.dbbest.amateurfeed.R.id.image_tab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.crashlytics.android.Crashlytics;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.data.PreviewEntry;
import com.dbbest.amateurfeed.data.UserNewsEntry;
import com.dbbest.amateurfeed.data.adapter.ItemNewsAdapter;
import com.dbbest.amateurfeed.data.adapter.ItemNewsAdapter.UserNewsHolder;
import com.dbbest.amateurfeed.data.adapter.PreviewAdapter;
import com.dbbest.amateurfeed.data.sync.AmateurfeedSyncAdapter;
import com.dbbest.amateurfeed.model.UserNewsModel;
import com.dbbest.amateurfeed.presenter.HomePresenter;
import com.dbbest.amateurfeed.ui.dialog.WarningDialog;
import com.dbbest.amateurfeed.ui.fragments.AddItemDetailFragment;
import com.dbbest.amateurfeed.ui.fragments.BaseEditDetailFragment;
import com.dbbest.amateurfeed.ui.fragments.ChangePasswordFragment;
import com.dbbest.amateurfeed.ui.fragments.EditItemDetailFragment;
import com.dbbest.amateurfeed.ui.fragments.EditProfileFragment;
import com.dbbest.amateurfeed.ui.fragments.FeedNewsFragment;
import com.dbbest.amateurfeed.ui.fragments.PreferFragment;
import com.dbbest.amateurfeed.ui.fragments.ProfileFragment;
import com.dbbest.amateurfeed.ui.fragments.SearchFragment;
import com.dbbest.amateurfeed.ui.fragments.UserNewsPreviewFragment;
import com.dbbest.amateurfeed.ui.navigator.UIDialogNavigation;
import com.dbbest.amateurfeed.utils.BottomTab;
import com.dbbest.amateurfeed.utils.Constants;
import com.dbbest.amateurfeed.utils.Utils;
import com.dbbest.amateurfeed.view.HomeView;
import io.fabric.sdk.android.Fabric;
import java.util.HashMap;
import java.util.Stack;


public class HomeActivity extends AppCompatActivity implements TabHost.OnTabChangeListener,
    HomeView,
    WarningDialog.OnWarningDialogListener, FeedNewsFragment.Callback,
    BaseEditDetailFragment.Callback, SearchFragment.Callback,
    ItemNewsAdapter.ShowItemDetailsCallback, EditProfileFragment.Callback,
    ProfileFragment.ProfileShowDetails, AmateurfeedSyncAdapter.Callback {

  public static final String FEED_NEWS_FRAGMENT_TAG = "FNFTAG";
  public static final String DETAIL_NEWS_FRAGMENT_TAG = "DNFTAG";
  public static final String DETAIL_ADD_NEWS_FRAGMENT_TAG = "DNFTAG_ADD_TAG";
  public static final String SEARCH_FRAGMENT_TAG = "STAG";
  public static final String PROFILE_FRAGMENT_TAG = "PTAG";
  public static final String EDIT_PROFILE_FRAGMENT_TAG = "PROFTAG";
  public static final String USER_NEWS_FRAGMENT_TAG = "UNFTAG";
  public static final String USER_PREFERENCES_TAG = "UPTAG";
  public static final String CHANGE_PASSWORD_TAG = "CHPTAG";
  public static final String MANAGE_FRAGMENTS = "ManageFragments";
  public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
  public static final String MODEL = "model";
  public static final String STACKS = "stacks";
  private static final String TAG = HomeActivity.class.getName();
  private DialogFragment progressDialog;
  private int isLikeFlag = 0;
  private int countIsLikes = 0;
  private Uri uriId;
  private HomePresenter presenter;
  private FragmentTabHost tabHost;
  private String currentTag;
  private Bundle argsDetail;
  private HashMap<BottomTab, Stack<String>> backStacks;

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    backStacks = (HashMap<BottomTab, Stack<String>>) savedInstanceState.getSerializable(STACKS);
    refreshContent();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (outState != null) {
      outState.putInt("tab", tabHost.getCurrentTab());
      outState.putString("tag", currentTag);
      outState.putSerializable("stacks", backStacks);
      if (argsDetail != null) {
        outState.putParcelable(EditItemDetailFragment.DETAIL_URI,
            argsDetail.getParcelable(EditItemDetailFragment.DETAIL_URI));
        outState.putInt(EditItemDetailFragment.DETAIL_TYPE,
            argsDetail.getInt(EditItemDetailFragment.DETAIL_TYPE));
      }
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    Fabric.with(this, new Crashlytics());
    presenter = new HomePresenter();
    presenter.getNews(0, 5);
    if (getIntent().getExtras() != null) {
      for (String key : getIntent().getExtras().keySet()) {
        Object value = getIntent().getExtras().get(key);
        Log.d(TAG, "Key: " + key + " Value: " + value);
      }
    }

    tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
    tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
    addTab(BottomTab.HOME);
    addTab(BottomTab.SEARCH);
    addTab(BottomTab.PROFILE);
    tabHost.setOnTabChangedListener(this);
    if (savedInstanceState != null) {
      backStacks = (HashMap<BottomTab, Stack<String>>) savedInstanceState.getSerializable("stacks");
      refreshContent();
    } else {

      backStacks = new HashMap<>();
      backStacks.put(BottomTab.HOME, new Stack<String>());
      backStacks.put(BottomTab.SEARCH, new Stack<String>());
      backStacks.put(BottomTab.PROFILE, new Stack<String>());
      backStacks.put(BottomTab.DETAIL, new Stack<String>());
      backStacks.put(BottomTab.USER_NEWS, new Stack<String>());
      backStacks.put(BottomTab.EDIT_PROFILE, new Stack<String>());
      backStacks.put(BottomTab.PREFERENCES, new Stack<String>());
      backStacks.put(BottomTab.CHANGE_PASSWORD, new Stack<String>());
    }

    AmateurfeedSyncAdapter.initializeSyncAdapter(this);
//    TimerReceiverSyncInterval.scheduleAlarms(this);

  }

  @Override
  protected void onStart() {
    super.onStart();
    presenter.attachView(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
    presenter.detachView();
  }

  @Override
  public void onTabChanged(String tabTag) {
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    if (backStacks != null) {
      refreshContent();
      Stack<String> backStack = backStacks.get(BottomTab.getByTag(tabTag));
      Fragment fragment = null;
      if (backStack == null || backStack.isEmpty()) {
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
    currentTag = tabTag;
  }

  @Override
  public void moveToFeedFragment() {
    refreshContent();
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    BottomTab bottomTabHome = BottomTab.getByTag(BottomTab.HOME.tag);
    Stack<String> feedStack = backStacks.get(bottomTabHome);
    showFragment(feedStack, ft);
    currentTag = BottomTab.HOME.tag;
    tabHost.setCurrentTab(0);
  }

  @Override
  public void onItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh) {
  }

  @Override
  public void onLikeItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh) {
    String mCountLikes = (vh.likesCountView.getText()).toString();
    countIsLikes = Integer.parseInt(mCountLikes);
    uriId = uri;
    if (vh.likeButton.getTag() == "1") {
      isLikeFlag = 0;
      vh.likeButton.setTag("0");
      int disLikeImage = R.drawable.ic_favorite_border_black_24dp;
      vh.likeButton.setImageResource(disLikeImage);
      countIsLikes = countIsLikes - 1;
    } else if (vh.likeButton.getTag() == "0") {
      isLikeFlag = 1;
      vh.likeButton.setTag("1");
      int likeImage = R.drawable.ic_favorite_black_24dp;
      vh.likeButton.setImageResource(likeImage);
      countIsLikes = countIsLikes + 1;
    }
    vh.likesCountView.setText(String.valueOf(countIsLikes));
    presenter.putLike(PreviewEntry.getIdFromUri(uri), isLikeFlag);
  }

  @Override
  public void onCommentItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh) {
    int layoutId = getLayoutId(vh);
    argsDetail = new Bundle();
    argsDetail.putParcelable(EditItemDetailFragment.DETAIL_URI, uri);
    argsDetail.putInt(EditItemDetailFragment.DETAIL_TYPE, layoutId);
    Fragment fragment = EditItemDetailFragment.newInstance(DETAIL_NEWS_FRAGMENT_TAG);
    fragment.setArguments(argsDetail);
    addFragment(argsDetail);
  }

  @Override
  public void onEditItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh) {
    int layoutId = R.layout.fragment_item_edit_user_detail;
    argsDetail = new Bundle();
    argsDetail.putParcelable(EditItemDetailFragment.DETAIL_URI, uri);
    argsDetail.putInt(EditItemDetailFragment.DETAIL_TYPE, layoutId);
    Fragment fragment = EditItemDetailFragment.newInstance(DETAIL_NEWS_FRAGMENT_TAG);
    fragment.setArguments(argsDetail);
    addFragment(argsDetail);
  }

  @Override
  public void onDeleteItemSelected(Uri uri, PreviewAdapter.PreviewAdapterViewHolder vh) {
    uriId = uri;
    UIDialogNavigation
        .abuseDialog(R.string.abuse_dialog, R.string.ok, R.string.cancel, "Abuse taxt", true, 100, this)
        .show(getSupportFragmentManager(), "abuse_dialog");
  }

  @Override
  public void upLoadNewsItems(int offset, int count) {
    presenter.getNews(offset, count);
  }

  @Override
  public void addNewItemDetail() {
    Fragment fragment = AddItemDetailFragment.newInstance(DETAIL_ADD_NEWS_FRAGMENT_TAG);
    fragment.setArguments(Bundle.EMPTY);
    addFragment(Bundle.EMPTY);
  }

  @Override
  public void refreshFeed() {
    refreshFragmentFeedLoader();
  }

  @Override
  public void refreshFragmentFeedLoader() {
    FeedNewsFragment newsFragment = (FeedNewsFragment) getSupportFragmentManager()
        .findFragmentByTag(FEED_NEWS_FRAGMENT_TAG);
    if (newsFragment != null) {
      newsFragment.refreshFragmentLoader();
    }
  }

  @Override
  public void updateColumnLikeInBd() {
    ContentValues values = new ContentValues();
    values.put(PreviewEntry.COLUMN_IS_LIKE, isLikeFlag);
    values.put(PreviewEntry.COLUMN_LIKES, countIsLikes);
    if (uriId != null) {
      long id = PreviewEntry.getIdFromUri(uriId);
      Uri uriPreviewId = PreviewEntry.buildSetLikeInPreviewUriById(id);
      App.instance().getContentResolver().update(uriPreviewId, values, null, null);
    }
  }

  @Override
  public void showSuccessDialog() {
  }

  @Override
  public void showErrorConnectionDialog() {
    UIDialogNavigation.showWarningDialog(R.string.no_internet_connection)
        .show(getSupportFragmentManager(), "warn");
  }

  @Override
  public void showErrorDialog() {
    UIDialogNavigation.showWarningDialog(R.string.get_news_error)
        .show(getSupportFragmentManager(), "warn");
  }

  @Override
  public void showProgressDialog() {
    progressDialog = UIDialogNavigation.showProgressDialog();
    progressDialog.show(getSupportFragmentManager(), "progress");
  }

  @Override
  public void dismissProgressDialog() {
    if (progressDialog != null) {
      progressDialog.dismiss();
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
    UIDialogNavigation.showWarningDialog(R.string.set_like_error)
        .show(getSupportFragmentManager(), "warn");
  }

  @Override
  public void showSuccessDeleteDialog() {
    UIDialogNavigation.showWarningDialog(R.string.set_delete_success)
        .show(getSupportFragmentManager(), "warn");
  }

  @Override
  public void showErrorDeleteDialog() {
    UIDialogNavigation.showWarningDialog(R.string.set_delete_error)
        .show(getSupportFragmentManager(), "warn");
  }

  @Override
  public void onWarningDialogCancelClicked(int dialogCode) {
    UIDialogNavigation.showWarningDialog(R.string.set_good_by)
        .show(getSupportFragmentManager(), "info");
  }

  @Override
  public void onWarningDialogOkClicked(int dialogCode, String textMessage) {
    if (uriId != null) {
      long id = PreviewEntry.getIdFromUri(uriId);
      presenter.putDelete(id, textMessage);
    }
  }

  @Override
  public void onLikeItemSelected(Uri uri, int isLike, int count) {
    uriId = uri;
    isLikeFlag = isLike;
    countIsLikes = count;
    presenter.putLike(PreviewEntry.getIdFromUri(uri), isLikeFlag);
  }


  @Override
  public void onEditItemSelected(Uri uri) {
    int layoutId = R.layout.fragment_item_edit_user_detail;
    argsDetail = new Bundle();
    argsDetail.putParcelable(EditItemDetailFragment.DETAIL_URI, uri);
    argsDetail.putInt(EditItemDetailFragment.DETAIL_TYPE, layoutId);
    Fragment fragment = EditItemDetailFragment.newInstance(DETAIL_NEWS_FRAGMENT_TAG);
    fragment.setArguments(argsDetail);
    addFragment(argsDetail);
  }

  @Override
  public void onDeleteItemSelected(Uri uri) {
    uriId = uri;
    UIDialogNavigation
        .abuseDialog(R.string.abuse_dialog, R.string.ok, R.string.cancel, "Abuse text",
            true, 100, this)
        .show(getSupportFragmentManager(), "abuse_dialog");
  }

  @NonNull
  @Override
  public Context getContext() {
    return this;
  }

  @Override
  public void showItemDetailsFragment(
      com.dbbest.amateurfeed.data.adapter.UserNewsAdapter.UserNewsHolder vh, Uri uri,
      int layoutId) {
    argsDetail = new Bundle();
    argsDetail.putParcelable(EditItemDetailFragment.DETAIL_URI, uri);
    argsDetail.putInt(EditItemDetailFragment.DETAIL_TYPE, layoutId);
    Fragment fragment = EditItemDetailFragment.newInstance(DETAIL_NEWS_FRAGMENT_TAG);
    fragment.setArguments(argsDetail);
    addFragment(argsDetail);
  }

  @Override
  public void showUserNewsDetailFragment(UserNewsHolder vh, int id) {

    Cursor cursor = getUserNewsCursor(id);
    if (cursor.moveToFirst()) {
      String day = null;
      String date = cursor.getString(ProfileFragment.COL_MY_NEWS_UPDATE_DATE);
      if (date != null) {
        Log.i(TAG, "Update date : " + date);
        day = Utils
            .getFriendlyDayString(App.instance().getApplicationContext(),
                Utils.getLongFromString(date),
                true);
      } else {
        date = "";
      }
      int idNews = cursor.getInt(ProfileFragment.COL_MY_NEWS_ID);
      Log.i(TAG, "Id : " + "[" + ProfileFragment.COL_MY_NEWS_ID + "]" + idNews);

      String title = cursor.getString(ProfileFragment.COL_MY_NEWS_TITLE);
      Log.i(TAG, "Title : " + "[" + ProfileFragment.COL_MY_NEWS_TITLE + "]" + title);

      Log.i(TAG, "Date : " + "[" + ProfileFragment.COL_MY_NEWS_UPDATE_DATE + "]" + date);

      String status = cursor.getString(ProfileFragment.COL_MY_NEWS_STATUS);
      Log.i(TAG, "Status : " + "[" + ProfileFragment.COL_MY_NEWS_STATUS + "]" + status);

      String image = cursor.getString(ProfileFragment.COL_MY_NEWS_IMAGE);
      Log.i(TAG, "Image : " + "[" + ProfileFragment.COL_MY_NEWS_IMAGE + "]" + image);

      int likes = cursor.getInt(ProfileFragment.COL_MY_NEWS_LIKES);
      Log.i(TAG, "Likes : " + "[" + ProfileFragment.COL_MY_NEWS_LIKES + "]" + likes);

      UserNewsModel model = new UserNewsModel(idNews, title, day, status, image, likes);

      Bundle args = new Bundle();
      args.putParcelable(MODEL, model);
      FragmentTransaction transaction = getSupportFragmentManager()
          .beginTransaction();
      Fragment instantiateUserNewsFragment;
      if (!args.isEmpty()) {

        BottomTab bottomTabUserNews = BottomTab.getByTag(USER_NEWS_FRAGMENT_TAG);
        Stack<String> userNewsStack = backStacks.get(bottomTabUserNews);
        userNewsStack.push(USER_NEWS_FRAGMENT_TAG);
        refreshContent();
        instantiateUserNewsFragment = Fragment
            .instantiate(getContext(), UserNewsPreviewFragment.class.getName());
        instantiateUserNewsFragment.setArguments(args);
        transaction.add(android.R.id.tabcontent, instantiateUserNewsFragment,
            HomeActivity.USER_NEWS_FRAGMENT_TAG);
        transaction.commit();
      }
    }
  }

  @Override
  public void showEditProfileFragment() {

    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    Fragment instantiateEditProfileFragment;

    BottomTab bottomTabUserNews = BottomTab.getByTag(EDIT_PROFILE_FRAGMENT_TAG);
    Stack<String> userNewsStack = backStacks.get(bottomTabUserNews);
    userNewsStack.push(EDIT_PROFILE_FRAGMENT_TAG);
    refreshContent();
    instantiateEditProfileFragment = Fragment
        .instantiate(getContext(), EditProfileFragment.class.getName());
    transaction.add(android.R.id.tabcontent, instantiateEditProfileFragment,
        HomeActivity.EDIT_PROFILE_FRAGMENT_TAG);
    transaction.commit();
  }

  @Override
  public void moveToProfileFragment() {
    refreshContent();
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    BottomTab bottomTabHome = BottomTab.getByTag(BottomTab.PROFILE.tag);
    Stack<String> profileStack = backStacks.get(bottomTabHome);
    showFragment(profileStack, ft);
    currentTag = BottomTab.PROFILE.tag;
  }

  @Override
  public void showPreferencesFragment() {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    BottomTab bottomTabUserNews = BottomTab.getByTag(USER_PREFERENCES_TAG);
    Stack<String> prefStack = backStacks.get(bottomTabUserNews);
    prefStack.push(USER_PREFERENCES_TAG);
    refreshContent();
    Fragment instantiatePrefFragment = Fragment
        .instantiate(getContext(), PreferFragment.class.getName());
    transaction.add(android.R.id.tabcontent, instantiatePrefFragment,
        HomeActivity.USER_PREFERENCES_TAG);
    transaction.commit();
  }

  @Override
  public void showChangePasswordFragment() {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    BottomTab bottomTabUserNews = BottomTab.getByTag(CHANGE_PASSWORD_TAG);
    Stack<String> passStack = backStacks.get(bottomTabUserNews);
    passStack.push(CHANGE_PASSWORD_TAG);
    refreshContent();
    Fragment instantiatePrefFragment = Fragment
        .instantiate(getContext(), ChangePasswordFragment.class.getName());
    transaction.add(android.R.id.tabcontent, instantiatePrefFragment,
        HomeActivity.CHANGE_PASSWORD_TAG);
    transaction.commit();
  }

  @Override
  public void onBackPressed() {

    BottomTab bottomTabHome = BottomTab.getByTag(FEED_NEWS_FRAGMENT_TAG);
    Stack<String> feedStack = backStacks.get(bottomTabHome);
    if (feedStack.isEmpty()) {
      FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      initiateTabFeedFragment(transaction, feedStack);
    }
    moveToFeedFragment();
  }


  public void refreshContent() {
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    if (backStacks != null) {
      Stack<String> detailStack = backStacks.get(BottomTab.DETAIL);
      if (!detailStack.isEmpty()) {
        Fragment top = getSupportFragmentManager().findFragmentByTag(detailStack.peek());
        if (top != null) {
          if (!top.isDetached()) {
            ft.detach(top);
            Log.i(MANAGE_FRAGMENTS, "Detach : " + detailStack.peek());
          }
        }
      }

      Stack<String> feedStack = backStacks.get(BottomTab.HOME);
      if (!feedStack.isEmpty()) {
        Fragment top = getSupportFragmentManager().findFragmentByTag(feedStack.peek());
        if (top != null && !top.isDetached()) {
          ft.detach(top);
          Log.i(MANAGE_FRAGMENTS, "Detach : " + feedStack.peek());
        }
      }
      Stack<String> searchStack = backStacks.get(BottomTab.SEARCH);
      if (!searchStack.isEmpty()) {
        Fragment top = getSupportFragmentManager().findFragmentByTag(searchStack.peek());
        if (top != null) {
          if (!top.isDetached()) {
            ft.detach(top);
            Log.i(MANAGE_FRAGMENTS, "Detach : " + searchStack.peek());
          }
        }
      }
      Stack<String> profileStack = backStacks.get(BottomTab.PROFILE);
      if (!profileStack.isEmpty()) {
        Fragment top = getSupportFragmentManager().findFragmentByTag(profileStack.peek());
        if (top != null) {
          if (!top.isDetached()) {
            ft.detach(top);
            Log.i(MANAGE_FRAGMENTS, "Detach : " + profileStack.peek());
          }
        }
      }
      Stack<String> userNewsStack = backStacks.get(BottomTab.USER_NEWS);
      if (!userNewsStack.isEmpty()) {
        Fragment top = getSupportFragmentManager().findFragmentByTag(userNewsStack.peek());
        if (top != null) {
          if (!top.isDetached()) {
            ft.detach(top);
            Log.i(MANAGE_FRAGMENTS, "Detach : " + userNewsStack.peek());
          }
        }
      }
      Stack<String> editProfileStack = backStacks.get(BottomTab.EDIT_PROFILE);
      if (!editProfileStack.isEmpty()) {
        Fragment top = getSupportFragmentManager().findFragmentByTag(editProfileStack.peek());
        if (top != null) {
          if (!top.isDetached()) {
            ft.detach(top);
            Log.i(MANAGE_FRAGMENTS, "Detach : " + editProfileStack.peek());
          }
        }
      }
      Stack<String> prefStack = backStacks.get(BottomTab.PREFERENCES);
      if (!prefStack.isEmpty()) {
        Fragment top = getSupportFragmentManager().findFragmentByTag(prefStack.peek());
        if (top != null) {
          if (!top.isDetached()) {
            ft.detach(top);
            Log.i(MANAGE_FRAGMENTS, "Detach : " + prefStack.peek());
          }
        }
      }
      Stack<String> passStack = backStacks.get(BottomTab.CHANGE_PASSWORD);
      if (!passStack.isEmpty()) {
        Fragment top = getSupportFragmentManager().findFragmentByTag(passStack.peek());
        if (top != null) {
          if (!top.isDetached()) {
            ft.detach(top);
            Log.i(MANAGE_FRAGMENTS, "Detach : " + passStack.peek());
          }
        }
      }
      ft.commit();
    }
  }

  private void addFragment(Bundle args) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    BottomTab bottomTabDetail = BottomTab.getByTag(DETAIL_NEWS_FRAGMENT_TAG);
    Stack<String> detailStack = backStacks.get(bottomTabDetail);
    BottomTab bottomTabHome = BottomTab.getByTag(FEED_NEWS_FRAGMENT_TAG);
    Stack<String> feedStack = backStacks.get(bottomTabHome);
    initiateTabFeedFragment(transaction, feedStack);
    refreshContent();
    detailStack.push(DETAIL_NEWS_FRAGMENT_TAG);
    Fragment instantiateDetailFragment;
    if (!args.isEmpty()) {
      instantiateDetailFragment = Fragment
          .instantiate(this, EditItemDetailFragment.class.getName());
      instantiateDetailFragment.setArguments(args);
    } else {
      instantiateDetailFragment = Fragment.instantiate(this, AddItemDetailFragment.class.getName());
    }
    transaction.add(android.R.id.tabcontent, instantiateDetailFragment, detailStack.peek());
    transaction.commit();
    currentTag = detailStack.peek();
  }

  private void initiateTabFeedFragment(FragmentTransaction transaction, Stack<String> feedStack) {
    if (feedStack.isEmpty()) {
      feedStack.push(FEED_NEWS_FRAGMENT_TAG);
      Fragment.instantiate(this, FeedNewsFragment.class.getName());
    } else {
      Fragment top = getSupportFragmentManager().findFragmentByTag(feedStack.peek());
      if (top != null) {
        if (!top.isDetached()) {
          transaction.detach(top);
        }
      }
    }
  }

  private void addFragment(Fragment fragment, Stack<String> backStack, FragmentTransaction ft,
      String tag) {
    ft.add(android.R.id.tabcontent, fragment, tag);
    backStack.push(tag);
  }

  private void showFragment(Stack<String> backStack, FragmentTransaction ft) {
    String tag = backStack.peek();
    Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
    if (fragment != null) {
      ft.attach(fragment);
      ft.commit();
    }
  }

  private void addTab(BottomTab homeTab) {
    TabHost.TabSpec spec = tabHost.newTabSpec(homeTab.tag);
    spec.setIndicator(getTabIndicator(this, homeTab.iconRes));
    tabHost.addTab(spec, homeTab.fragmentClass, null);
  }

  private View getTabIndicator(Context context, int icon) {
    View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
    ImageView iv = (ImageView) view.findViewById(image_tab);
    iv.setImageResource(icon);
    return view;
  }

  private int getLayoutId(PreviewAdapter.PreviewAdapterViewHolder vh) {
    int layoutId = -1;
    switch (vh.getItemViewType()) {
      case Constants.VIEW_TYPE_MY: {
        layoutId = R.layout.fragment_item_user_detail;
        break;
      }
      case Constants.VIEW_TYPE_USER: {
        layoutId = R.layout.fragment_item_detail;
        break;
      }
      case Constants.VIEW_TYPE_ITEM_EMPTY: {
        layoutId = R.layout.item_empty_detail;
        break;
      }
    }
    return layoutId;
  }

  private Cursor getUserNewsCursor(int mIdUserNews) {
    Uri uri = UserNewsEntry.buildGetNewsById(mIdUserNews);
    return App.instance().getContentResolver().query(
        uri,
        UserNewsEntry.NEWS_COLUMNS,
        null,
        null,
        null
    );
  }
}