package com.dbbest.amateurfeed.view;

import android.common.framework.IView;
import android.os.Bundle;

/**
 * Created by antonina on 23.01.17.
 */

public interface SearchView extends IView {

    public void initLoader(Bundle bundle);
    public void showEmptySearchDialog();
}
