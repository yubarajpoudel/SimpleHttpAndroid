package com.mantraideas.simplehttp;

import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.mantraideas.simplehttp.datamanager.DataRequestManager;
import com.mantraideas.simplehttp.datamanager.OnDataRecievedListener;
import com.mantraideas.simplehttp.datamanager.dmmodel.DataRequest;
import com.mantraideas.simplehttp.datamanager.dmmodel.DataRequestPair;
import com.mantraideas.simplehttp.datamanager.dmmodel.Method;
import com.mantraideas.simplehttp.datamanager.dmmodel.Response;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void checkPostRequestAPI(){
        DataRequestPair requestPair = DataRequestPair.create();
        requestPair.put("action", "get_video");
        requestPair.put("id", "29102");
        requestPair.put("sample", null);

        DataRequest request = DataRequest.getInstance();
        request.addUrl("http://videosanjal.com/apis/v4/");
        request.addDataRequestPair(requestPair);
        request.addMethod(Method.POST);

        DataRequestManager<String> requestManager = DataRequestManager.getInstance(InstrumentationRegistry.getTargetContext(), String.class);
        requestManager.addRequestBody(request).addOnDataRecieveListner(new OnDataRecievedListener() {
            @Override
            public void onDataRecieved(Response response, Object object) {
                Log.d("test", " data from server = " + object.toString() + "responseFromServer :: " + response.getMessage());
                // Assert.assertEquals(response.getMessage(), expected, response.OK);

            }
        });
        requestManager.sync();
    }

    @Test
    public void checkGetRequestAPI(){
        DataRequest request = DataRequest.getInstance();
        request.addUrl(  "http://github.yubrajpoudel.com.np/others/sample1.json");
        request.addMethod(Method.POST);

        DataRequestManager<String> requestManager = DataRequestManager.getInstance(InstrumentationRegistry.getTargetContext(), String.class);
        requestManager.addRequestBody(request).addOnDataRecieveListner(new OnDataRecievedListener() {
            @Override
            public void onDataRecieved(Response response, Object object) {
                Log.d("test", " data from server = " + object.toString() + " ResponseFromServer :: " + response.getMessage());
                // Assert.assertEquals(response.getMessage(), expected, response.OK);

            }
        });
        requestManager.sync();
    }
}