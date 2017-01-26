package com.dbbest.amateurfeed.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.presenter.EditProfilePresenter;
import com.dbbest.amateurfeed.presenter.ProfilePresenter;
import com.dbbest.amateurfeed.view.EditeProfileView;
import com.melnykov.fab.FloatingActionButton;

/**
 * Created by antonina on 25.01.17.
 */

public class EditProfileFragment extends Fragment implements EditeProfileView {
    private EditProfilePresenter mPresenter;
    private static final String PARAM_KEY = "param_key";

    public static EditProfileFragment newInstance(String key) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_KEY, key);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new EditProfilePresenter();
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

        final View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        return rootView;
    }

    @Override
    public void showEditeProfileFragment() {

    }
}
