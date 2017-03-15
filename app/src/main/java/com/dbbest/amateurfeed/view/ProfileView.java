package com.dbbest.amateurfeed.view;

import android.common.framework.IView;
import android.os.Bundle;


public interface ProfileView extends IView {

  void initLoader(Bundle data);
  void showEmptySearchDialog();
}
