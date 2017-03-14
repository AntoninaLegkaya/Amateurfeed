package com.dbbest.amateurfeed.ui.navigator;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;

import com.dbbest.amateurfeed.ui.dialog.ProgressDialog;
import com.dbbest.amateurfeed.ui.dialog.WarningDialog;


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


    public static DialogFragment warningDialog(@StringRes int message, @StringRes int okText, @StringRes int cancelText, boolean cancelable, int code,
                                               WarningDialog.OnWarningDialogListener listener) {
        return new WarningDialog.Builder()
                .setMessageText(message)
                .setOkText(okText)
                .setCancelText(cancelText)
                .setListener(listener)
                .setCancelable(cancelable)
                .setCode(code)
                .build();
    }
}