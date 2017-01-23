package com.dbbest.amateurfeed.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.presenter.ProfilePresenter;
import com.dbbest.amateurfeed.presenter.SearchPresenter;
import com.dbbest.amateurfeed.view.ProfileView;
import com.dbbest.amateurfeed.view.SearchView;

/**
 * Created by antonina on 23.01.17.
 */

public class SearchFragment extends Fragment implements SearchView {
    private static final String PARAM_KEY = "param_key";

    SearchPresenter mPresenter;

    public static SearchFragment newInstance(String key) {
        SearchFragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_KEY, key);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SearchPresenter();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.attachView(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.detachView();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }


}