package com.dbbest.amateurfeed.model;

/**
 * Created by antonina on 19.01.17.
 */

public class CurrentUser implements ActiveRecord<CurrentUser> {
    @Override
    public void purge() {

    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public void subscribeChanges(OnRecordChangeListener<CurrentUser> listener) {

    }

    @Override
    public void unsubscribeChanges() {

    }
}
