package com.dbbest.amateurfeed.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dbbest.amateurfeed.R;
import com.dbbest.amateurfeed.presenter.PreferencePresenter;
import com.dbbest.amateurfeed.view.PreferenceView;

/**
 * Created by antonina on 25.01.17.
 */


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