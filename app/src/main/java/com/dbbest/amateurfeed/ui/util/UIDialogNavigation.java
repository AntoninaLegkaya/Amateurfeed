package com.dbbest.amateurfeed.ui.util;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;

import com.dbbest.amateurfeed.ui.dialog.ProgressDialog;
import com.dbbest.amateurfeed.ui.dialog.WarningDialog;

/**
 * Created by antonina on 20.01.17.
 */

public class UIDialogNavigation {

    @NonNull
    public static DialogFragment showWarningDialog(@StringRes int message) {
        return new WarningDialog.Builder()
                .setMessageText(message)
                .build();
    }

    @NonNull
    public static DialogFragment showProgressDialog() {
        return ProgressDialog.instance();
    }
}