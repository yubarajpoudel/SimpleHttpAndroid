package com.mantraideas.simplehttp.datamanager;


import com.mantraideas.simplehttp.datamanager.dmmodel.Response;

/**
 * Created by yubraj on 1/27/17.
 */

public interface OnDataRecievedListener {
    void onDataRecieved(Response response, Object object);


}
