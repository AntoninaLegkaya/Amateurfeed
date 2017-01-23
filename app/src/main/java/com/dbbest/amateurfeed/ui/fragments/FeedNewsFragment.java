package com.dbbest.amateurfeed.ui.fragments;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.presenter.FeedListPresenter;
import com.dbbest.amateurfeed.view.FeedView;
import com.dbbest.amateurfeed.view.HomeView;

/**
 * Created by antonina on 20.01.17.
 */

public class FeedNewsFragment extends Fragment implements View.OnClickListener, FeedView, SwipeRefreshLayout.OnRefreshListener {

    private static final String PARAM_KEY = "param_key";

    FeedListPresenter mPresenter;

    public static FeedNewsFragment newInstance(String key) {
        FeedNewsFragment fragment = new FeedNewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_KEY, key);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new FeedListPresenter();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.attachView(this);
        if (getArguments() != null) {
            mPresenter.search(getArguments().getString(PARAM_KEY, ""));
        } else {
            mPresenter.search("");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.detachView();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed_list, container, false);
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onClick(View view) {

    }


}