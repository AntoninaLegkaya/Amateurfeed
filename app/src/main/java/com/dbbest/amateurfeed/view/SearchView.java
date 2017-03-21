package com.dbbest.amateurfeed.view;

import android.common.framework.IView;
import android.os.Bundle;


public interface SearchView extends IView {

  public void initLoader(Bundle bundle);

  public void showEmptySearchDialog();
}
