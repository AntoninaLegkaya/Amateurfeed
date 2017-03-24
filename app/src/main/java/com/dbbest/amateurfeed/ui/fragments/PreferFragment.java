package com.dbbest.amateurfeed.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceClickListener;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.dbbest.amateurfeed.App;
import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.presenter.PreferencePresenter;
import com.dbbest.amateurfeed.ui.fragments.ProfileFragment.ProfileShowDetails;
import com.dbbest.amateurfeed.ui.navigator.UiActivityNavigation;
import com.dbbest.amateurfeed.view.PreferenceView;


public class PreferFragment extends PreferenceFragmentCompat implements PreferenceView,
    OnSharedPreferenceChangeListener {

  private PreferencePresenter mPresenter;
  private CheckBoxPreference prefNotification;

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
  public void onResume() {
    super.onResume();
    getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

  }

  @Override
  public void onPause() {
    getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    super.onPause();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mPresenter = new PreferencePresenter();
    ((AppCompatActivity) getActivity()).setSupportActionBar(createToolbar());
    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    Preference prefEditProfile = findPreference(getString(R.string.startEditProfile));
    prefEditProfile
        .setOnPreferenceClickListener(new OnPreferenceClickListener() {
          @Override
          public boolean onPreferenceClick(Preference preference) {
            ((ProfileShowDetails) getActivity()).showEditProfileFragment();
            return true;
          }
        });
    Preference prefChangePassword = getPreferenceManager()
        .findPreference(getString(R.string.startChangePassword));
    prefChangePassword
        .setOnPreferenceClickListener(new OnPreferenceClickListener() {
          @Override
          public boolean onPreferenceClick(Preference preference) {
            ((ProfileShowDetails) getActivity()).showChangePasswordFragment();
            return true;
          }
        });
    Preference prefLogout = getPreferenceManager()
        .findPreference(getString(R.string.startLogout));
    prefLogout
        .setOnPreferenceClickListener(new OnPreferenceClickListener() {
          @Override
          public boolean onPreferenceClick(Preference preference) {
            mPresenter.logout();
            return true;
          }
        });

    final Preference checkBoxPreference = getPreferenceManager().findPreference(
        getString(R.string.pref_enable_notifications_key));
    boolean notifyFlag = checkNotificationPref();
    if (checkBoxPreference instanceof CheckBoxPreference) {
      prefNotification = (CheckBoxPreference) checkBoxPreference;
      prefNotification.setChecked(notifyFlag);
      prefNotification.setOnPreferenceClickListener(new OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
          return true;
        }
      });
    }
  }


  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    if (key.equals(getContext().getString(R.string.pref_enable_notifications_key))) {

      Preference exercisesPref = findPreference(key);
      if (exercisesPref instanceof CheckBoxPreference) {
        SharedPreferences mySharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(App.instance().getApplicationContext());
        mySharedPreferences.edit()
            .putBoolean(getString(R.string.checkbox_preference),
                prefNotification.isChecked()).apply();
      }
    }
  }

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    addPreferencesFromResource(R.xml.pref_general);
  }

  private Toolbar createToolbar() {
    Toolbar toolbar = new Toolbar(getActivity());
    Toolbar.LayoutParams toolBarParams = new Toolbar.LayoutParams(
        Toolbar.LayoutParams.MATCH_PARENT,
        R.attr.actionBarSize);
    toolbar.setLayoutParams(toolBarParams);
    toolbar.setBackgroundColor(Color.BLUE);
    toolbar.setPopupTheme(R.style.MyToolbarStyle);
    toolbar.setVisibility(View.VISIBLE);
    TextView textView = new TextView(getContext());
    textView.setText(getString(R.string.preference_toolbar_label));
    toolbar.addView(textView);
    return toolbar;
  }

  @NonNull
  @Override
  public Context getContext() {
    return App.instance().getApplicationContext();
  }

  @Override
  public void navigateToStartScreen() {
    startActivity(UiActivityNavigation.startActivity(getContext()));
  }


  private boolean checkNotificationPref() {
    SharedPreferences mySharedPreferences = PreferenceManager
        .getDefaultSharedPreferences(App.instance().getApplicationContext());
    return mySharedPreferences.getBoolean(getString(R.string.checkbox_preference), true);

  }
}