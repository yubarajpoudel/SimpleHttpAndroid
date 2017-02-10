package com.mantraideas.simplehttp.datamanager.error;

/**
 * Created by yubraj on 1/27/17.
 */

public class DataManagerException extends RuntimeException {

    public DataManagerException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public DataManagerException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
