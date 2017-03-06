package com.dbbest.amateurfeed.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.app.azur.task.BlobUploadTask;
import com.dbbest.amateurfeed.presenter.AddItemDetailPresenter;
import com.dbbest.amateurfeed.utils.Utils;
import com.dbbest.amateurfeed.view.DetailView;

/**
 * Created by antonina on 24.01.17.
 */

public class AddItemDetailFragment extends BaseChangeDetailFragment implements DetailView {
    private static final String PARAM_KEY = "param_key";
    public final static String ADD_DETAIL_FRAGMENT = "AddDetailFragment";
    AddItemDetailPresenter mPresenter;

    public static AddItemDetailFragment newInstance(String key) {
        AddItemDetailFragment fragment = new AddItemDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putString(PARAM_KEY, key);
        fragment.setArguments(bundle);
        return fragment;
    }

    public AddItemDetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new AddItemDetailPresenter();
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String upTitle = null;
        String upDescription = null;

        switch (item.getItemId()) {
            case android.R.id.home:

                ((Callback) getActivity()).moveToFeedFragment();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item_detail, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.add_image_item);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i(ADD_DETAIL_FRAGMENT, "Click  image button");

            }
        });
        return view;
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
