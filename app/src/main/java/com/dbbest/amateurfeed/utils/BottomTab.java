package com.dbbest.amateurfeed.utils;

import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;

import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.ui.HomeActivity;
import com.dbbest.amateurfeed.ui.fragments.FeedNewsFragment;
import com.dbbest.amateurfeed.ui.fragments.EditItemDetailFragment;
import com.dbbest.amateurfeed.ui.fragments.ProfileFragment;
import com.dbbest.amateurfeed.ui.fragments.SearchFragment;

public enum BottomTab {

    HOME(HomeActivity.FEED_NEWS_FRAGMENT_TAG, R.drawable.ic_home_black_18dp, FeedNewsFragment.class),
    SEARCH(HomeActivity.SEARCH_FRAGMENT_TAG,R.drawable.ic_search_black_18dp, SearchFragment.class),
    PROFILE(HomeActivity.PROFILE_FRAGMENT_TAG,R.drawable.ic_perm_identity_black_18dp, ProfileFragment.class),
    DETAIL(HomeActivity.DETAIL_NEWS_FRAGMENT_TAG, R.drawable.ic_home_black_18dp, EditItemDetailFragment.class);

    public String tag;
    public
    @DrawableRes
    int iconRes;
    public Class fragmentClass;

    BottomTab(String tag, @DrawableRes int iconRes, Class fragmentClass) {
        this.tag = tag;
        this.iconRes = iconRes;
        this.fragmentClass = fragmentClass;
    }

    @Nullable
    public static BottomTab getByTag(String tag) {
        if (tag != null) {
            for (BottomTab hostTab : values())
                if (hostTab.tag.equals(tag))
                    return hostTab;
        }

        return null;
    }

    public String getTag() {
        return tag;
    }
}
