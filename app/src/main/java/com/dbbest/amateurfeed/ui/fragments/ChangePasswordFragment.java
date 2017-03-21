package com.dbbest.amateurfeed.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.presenter.ChangePasswordPresenter;
import com.dbbest.amateurfeed.ui.fragments.ProfileFragment.ProfileShowDetails;
import com.dbbest.amateurfeed.view.ChangePasswordView;

public class ChangePasswordFragment extends Fragment implements ChangePasswordView {

  private ChangePasswordPresenter mPresenter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mPresenter = new ChangePasswordPresenter();
    setHasOptionsMenu(true);
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

  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_change_password, container, false);
    Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    return view;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.action_back_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    String upTitle = null;
    String upDescription = null;
    switch (item.getItemId()) {
      case android.R.id.home:
        ((ProfileShowDetails) getActivity()).showPreferencesFragment();
        return true;
      case R.id.action: {
        return true;
      }
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}

