package com.dbbest.amateurfeed.view;

import android.common.framework.IView;
import android.os.Bundle;

/**
 * Created by antonina on 24.01.17.
 */

public interface DetailView extends IView {

    public void addTagToItemDetail(Bundle data);
    void showSuccessEditNewsDialog();
}
