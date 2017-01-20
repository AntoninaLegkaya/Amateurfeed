package com.dbbest.amateurfeed.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Window;
import android.view.WindowManager;

import com.dbbest.amateurfeed.R;

/**
 * Created by antonina on 20.01.17.
 */

public class BaseDialogFragment extends DialogFragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BaseDialogStyle);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            Window window = getDialog().getWindow();
            if (window != null) {
                WindowManager.LayoutParams windowParams = window.getAttributes();
                windowParams.dimAmount = 0.35f;
                windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                window.setAttributes(windowParams);
            }
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }
}
