package com.dbbest.amateurfeed.utils;

import com.dbbest.amateurfeed.app.storage.processor.DataProcessor;
import com.dbbest.amateurfeed.utils.intarface.IFactory;

/**
 * Created by antonina on 20.01.17.
 */

public class FactoryImpl implements IFactory {

    private DataProcessor mDataProcessor;
    private PresenterFactory mPresenterFactory;

    @Override
    public DataProcessor data() {
        if (mDataProcessor == null) {
            mDataProcessor = new DataProcessor();
        }
        return mDataProcessor;
    }

    @Override
    public PresenterFactory presenters() {
        if (mPresenterFactory == null) {
            mPresenterFactory = new PresenterFactory();
        }
        return mPresenterFactory;
    }
}
