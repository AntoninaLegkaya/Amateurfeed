package com.dbbest.amateurfeed.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;

import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.presenter.HomePresenter;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.dbbest.amateurfeed.ui.fragments.FeedNewsFragment;
import com.dbbest.amateurfeed.ui.fragments.ProfileFragment;
import com.dbbest.amateurfeed.ui.fragments.SearchFragment;
import com.dbbest.amateurfeed.ui.util.UiActivityNavigation;
import com.dbbest.amateurfeed.view.HomeView;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

/**
 * Created by antonina on 19.01.17.
 */

public class HomeActivity extends AppCompatActivity implements HomeView {
    private static final String FEDD_NEWS_FRAGMENT_TAG = "FNFTAG";
    private static final String SEARCH_FRAGMENT_TAG = "STAG";
    public static final String PROFILE_FRAGMENT_TAG = "PTAG";
    public static final String EDITE_PROFILE_FRAGMENT_TAG = "PREFTAG";

    private CoordinatorLayout coordinatorLayout;
    private HomePresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        mPresenter = new HomePresenter();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.tabs_activity);
        if(savedInstanceState==null){

            Snackbar.make(coordinatorLayout, "Initial input", Snackbar.LENGTH_LONG).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    FeedNewsFragment.newInstance(""), FEDD_NEWS_FRAGMENT_TAG).commit();


        }


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




    @Override
    public void requestPermission(int code, @NonNull String... permissions) {

    }

    @Override
    public void onLoginSuccess() {

    }

    @Override
    public void onLoginError() {

    }

    @NonNull
    @Override
    public Context getContext() {
        return null;
    }


}
