package com.dbbest.amateurfeed.view;

import android.common.framework.IView;
import android.support.annotation.NonNull;


public interface EditProfileView extends IView {

  void requestPermission(int code, @NonNull String... permissions);
}
