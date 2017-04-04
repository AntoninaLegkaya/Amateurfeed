package com.dbbest.amateurfeed.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dbbest.amateurfeed.R;


public class ProgressDialog extends BaseDialogFragment {

  public static DialogFragment instance() {
    return new ProgressDialog();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    setCancelable(false);
    return inflater.inflate(R.layout.dialog_progress, container, false);
  }
}
