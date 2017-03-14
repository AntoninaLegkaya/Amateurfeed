package com.dbbest.amateurfeed.ui.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import com.dbbest.amateurfeed.R;


public class PrefFragment extends PreferenceFragmentCompat {


  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    // Indicate here the XML resource you created above that holds the preferences
    addPreferencesFromResource(R.xml.pref_general);
  }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        return inflater.inflate(R.layout.fragment_preference, container, false);
//    }


}