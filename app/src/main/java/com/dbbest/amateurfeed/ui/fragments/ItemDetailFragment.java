package com.dbbest.amateurfeed.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.presenter.DetailPresenter;
import com.dbbest.amateurfeed.presenter.ProfilePresenter;
import com.dbbest.amateurfeed.view.DetailView;
import com.dbbest.amateurfeed.view.ProfileView;

/**
 * Created by antonina on 24.01.17.
 */

public class ItemDetailFragment extends Fragment implements DetailView {
    private static final String PARAM_KEY = "param_key";
    public static final String DETAIL_URI = "URI";
    DetailPresenter mPresenter;

    public static ItemDetailFragment newInstance(String key) {
        ItemDetailFragment fragment = new ItemDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_KEY, key);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new DetailPresenter();
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
        return inflater.inflate(R.layout.fragment_item_detail, container, false);
    }


}