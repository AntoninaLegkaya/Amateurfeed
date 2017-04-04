package com.dbbest.amateurfeed.utils;

import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.ui.activity.HomeActivity;
import com.dbbest.amateurfeed.ui.fragments.ChangePasswordFragment;
import com.dbbest.amateurfeed.ui.fragments.EditItemDetailFragment;
import com.dbbest.amateurfeed.ui.fragments.EditProfileFragment;
import com.dbbest.amateurfeed.ui.fragments.FeedNewsFragment;
import com.dbbest.amateurfeed.ui.fragments.PreferFragment;
import com.dbbest.amateurfeed.ui.fragments.ProfileFragment;
import com.dbbest.amateurfeed.ui.fragments.SearchFragment;
import com.dbbest.amateurfeed.ui.fragments.UserNewsPreviewFragment;

public enum BottomTab {

  HOME(HomeActivity.FEED_NEWS_FRAGMENT_TAG, R.drawable.ic_home_black_18dp, FeedNewsFragment.class),
  SEARCH(HomeActivity.SEARCH_FRAGMENT_TAG, R.drawable.ic_search_black_18dp, SearchFragment.class),
  PROFILE(HomeActivity.PROFILE_FRAGMENT_TAG, R.drawable.ic_perm_identity_black_18dp,
      ProfileFragment.class),
  DETAIL(HomeActivity.DETAIL_NEWS_FRAGMENT_TAG, R.drawable.ic_home_black_18dp,
      EditItemDetailFragment.class),
  USER_NEWS(HomeActivity.USER_NEWS_FRAGMENT_TAG, R.drawable.ic_perm_identity_black_18dp,
      UserNewsPreviewFragment.class),
  EDIT_PROFILE(HomeActivity.EDIT_PROFILE_FRAGMENT_TAG, R.drawable.ic_perm_identity_black_18dp,
      EditProfileFragment.class),
  PREFERENCES(HomeActivity.USER_PREFERENCES_TAG, R.drawable.ic_perm_identity_black_18dp,
      PreferFragment.class),
  CHANGE_PASSWORD(HomeActivity.CHANGE_PASSWORD_TAG, R.drawable.ic_perm_identity_black_18dp,
      ChangePasswordFragment.class);

  public String tag;
  public
  @DrawableRes
  int iconRes;
  public Class fragmentClass;

  @Nullable
  public static BottomTab getByTag(String tag) {
    if (tag != null) {
      for (BottomTab hostTab : values()) {
        if (hostTab.tag.equals(tag)) {
          return hostTab;
        }
      }
    }
    return null;
  }

  BottomTab(String t, @DrawableRes int icon, Class fragment) {
    this.tag = t;
    this.iconRes = icon;
    this.fragmentClass = fragment;
  }

  public String getTag() {
    return tag;
  }
}
