package com.dbbest.amateurfeed.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.data.adapter.GridViewAdapter;
import com.dbbest.amateurfeed.presenter.ProfilePresenter;
import com.dbbest.amateurfeed.view.ProfileView;

import java.util.ArrayList;

/**
 * Created by antonina on 23.01.17.
 */

public class ProfileFragment extends Fragment implements ProfileView {
    private final String PREFERENCE_FRAGMENT_TAG = "PREFTAG";
    private static final String PARAM_KEY = "param_key";
    private RecyclerView mRecyclerView;
    private ImageButton mSettingsBtn;
    private ArrayList<String> stringArrayList;
    private RecyclerView.Adapter adapter;

    ProfilePresenter mPresenter;

    public static ProfileFragment newInstance(String key) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_KEY, key);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ProfilePresenter();
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

        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.grid_feed_list_view);
        mSettingsBtn = (ImageButton) rootView.findViewById(R.id.settings_button);
        mSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.settings_button) {
                    Toast.makeText(getActivity(), "Go to Settings Screen--->", Toast.LENGTH_SHORT).show();

                    getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent,
                            new PrefFragment(), PREFERENCE_FRAGMENT_TAG).commit();
//                    startActivity(UiActivityNavigation.settingsActivity(getActivity()));

                }

            }
        });
       /* FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.attachToRecyclerView(mRecyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "AddItem", Toast.LENGTH_SHORT).show();
            }
        });*/

/*
        FloatingActionButton fabEditeProfileBtn = (FloatingActionButton) rootView.findViewById(R.id.fab_edit);
        fabEditeProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        EditProfileFragment.newInstance(""), HomeActivity.EDIT_PROFILE_FRAGMENT_TAG).commit();
            }
        });
*/

//        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);

        mRecyclerView.setHasFixedSize(true);

        //set GridLayoutManager
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new GridViewAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);

        return rootView;
    }


}
