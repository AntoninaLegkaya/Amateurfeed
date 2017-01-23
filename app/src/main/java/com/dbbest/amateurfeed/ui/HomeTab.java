package com.dbbest.amateurfeed.ui;

import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;

import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.ui.fragments.ProfileRootFragment;
import com.dbbest.amateurfeed.ui.fragments.FeedNewsFragment;
import com.dbbest.amateurfeed.ui.fragments.SearchRootFragment;


/**
 * Created by antonina on 20.01.17.
 */

public enum HomeTab {

    HOME("home_root_tag", R.drawable.tab_home_selector, FeedNewsFragment.class),
    SEARCH("search_home_root_tag", R.drawable.tab_search_selector, SearchRootFragment.class),
    PROFILE("profile_home_root_tag", R.drawable.tab_profile_selector, ProfileRootFragment.class);


    public String tag;
    public
    @DrawableRes
    int iconRes;

    public Class<? extends FeedNewsFragment> fragmentClass;

    HomeTab(String tab, @DrawableRes int iconRes, Class<? extends FeedNewsFragment> fragmentClass) {
        this.tag = tab;
        this.iconRes = iconRes;
        this.fragmentClass = fragmentClass;
    }

    @Nullable
    public static HomeTab getByTag(String tag) {
        if (tag != null) {
            for (HomeTab homeTab : values())
                if (homeTab.tag.equals(tag))
                    return homeTab;
        }

        return null;
    }
}
