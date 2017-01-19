package com.dbbest.amateurfeed.ui.util.exception;

/**
 * Created by antonina on 19.01.17.
 */

public class NotImplementedException extends RuntimeException {

    public NotImplementedException(String featureName) {
        super(String.format("%s feature not implemented yet!", featureName));
    }
}
