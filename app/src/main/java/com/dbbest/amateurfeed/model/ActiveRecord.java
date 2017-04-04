package com.dbbest.amateurfeed.model;

public interface ActiveRecord<V> {

  void purge();

  boolean isValid();

  void subscribeChanges(OnRecordChangeListener<V> listener);

  void unsubscribeChanges();

  interface OnRecordChangeListener<V> {

    void onRecordChanged(V value);
  }
}
