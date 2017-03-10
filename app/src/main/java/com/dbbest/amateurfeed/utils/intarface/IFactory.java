package com.dbbest.amateurfeed.utils.intarface;

import com.dbbest.amateurfeed.app.storage.processor.DataProcessor;
import com.dbbest.amateurfeed.utils.PresenterFactory;


public interface IFactory {

    DataProcessor data();
    PresenterFactory presenters();

}
