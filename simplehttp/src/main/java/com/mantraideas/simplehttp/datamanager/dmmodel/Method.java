package com.mantraideas.simplehttp.datamanager.dmmodel;

/**
 * Created by yubraj on 1/27/17.
 */

public enum Method {
    GET ("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE");

    String mMethod = "GET";

    Method(String mMethod){
        this.mMethod = mMethod;
    }

    public String getMethod(){
        return this.mMethod;
    }
}
