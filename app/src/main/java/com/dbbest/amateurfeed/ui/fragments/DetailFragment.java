package com.dbbest.amateurfeed.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.presenter.EditeItemDetailPresenter;
import com.dbbest.amateurfeed.view.DetailView;

/**
 * Created by antonina on 24.01.17.
 */

public class DetailFragment extends Fragment implements DetailView {
    private static final String PARAM_KEY = "param_key";
    EditeItemDetailPresenter mPresenter;

    public static DetailFragment newInstance(String key) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_KEY, key);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new EditeItemDetailPresenter();
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


    @Override
    public void addTagToItemDetail(Bundle data) {

    }

    @Override
    public void updateDetailsFields(Bundle data) {

    }

    @Override
    public void showSuccessEditNewsDialog() {

    }

    @Override
    public void showErrorEditNewsDialog() {

    }

    @Override
    public void showSuccessAddCommentDialog() {

    }

    @Override
    public void showErrorAddCommentDialog() {

    }
}
