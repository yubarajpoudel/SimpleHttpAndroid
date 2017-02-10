package com.mantraideas.simplehttp.datamanager.dmmodel;

/**
 * Created by yubraj on 1/27/17.
 */

public enum Response {
    NO_INTERNET("No internet connection available"), SERVER_CALL_TIME_NOT_VALID("Server call time is not valid"), NULL_DATA(" Data is null"), OK(" Connection ok"), NULL_REQUEST("DataRequest is null"), ERROR("Data error from server");

    public String message;

    Response(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
