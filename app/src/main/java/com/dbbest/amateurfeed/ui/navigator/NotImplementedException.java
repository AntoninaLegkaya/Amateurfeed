package com.dbbest.amateurfeed.ui.navigator;


public class NotImplementedException extends RuntimeException {

  public NotImplementedException(String featureName) {
    super(String.format("%s feature not implemented yet!", featureName));
  }
}
