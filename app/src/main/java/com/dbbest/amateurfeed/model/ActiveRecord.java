package com.dbbest.amateurfeed.model;

/**
 * Created by antonina on 19.01.17.
 */

public interface ActiveRecord<V> {

    void purge();

    boolean isValid();

    void subscribeChanges(OnRecordChangeListener<V> listener);

    void unsubscribeChanges();

    interface OnRecordChangeListener<V> {
        void onRecordChanged(V value);
    }
}
