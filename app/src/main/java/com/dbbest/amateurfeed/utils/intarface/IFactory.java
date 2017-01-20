package com.dbbest.amateurfeed.utils.intarface;

import com.dbbest.amateurfeed.app.storage.processor.DataProcessor;
import com.dbbest.amateurfeed.utils.PresenterFactory;

/**
 * Created by antonina on 20.01.17.
 */

public interface IFactory {

    DataProcessor data();
    PresenterFactory presenters();

}
