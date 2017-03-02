package com.yuviii.restdata;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mantraideas.simplehttp.datamanager.DataRequestManager;
import com.mantraideas.simplehttp.datamanager.OnDataRecievedListener;
import com.mantraideas.simplehttp.datamanager.OnDataRecievedProgressListener;
import com.mantraideas.simplehttp.datamanager.dmmodel.DataRequest;
import com.mantraideas.simplehttp.datamanager.dmmodel.DataRequestPair;
import com.mantraideas.simplehttp.datamanager.dmmodel.Method;
import com.mantraideas.simplehttp.datamanager.dmmodel.Response;

import junit.framework.Assert;

/**
 * Created by yubaraj on 3/2/17.
 */

public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testPostMethod();
    }

    private void testPostMethod() {
        final Response expected = Response.OK;
        String samplebody = "{\n" +
                "\t\"address_1\": \"Test street 88\",\n" +
                "\t\"address_2\": \"test\",\n" +
                "\t\"city\": \"Berlin\",\n" +
                "\t\"company_id\": \"company\",\n" +
                "\t\"company\": \"company\",\n" +
                "\t\"country_id\": \"97\",\n" +
                "\t\"email\": \"testing@test.com\",\n" +
                "\t\"fax\": \"1\",\n" +
                "\t\"firstname\": \"Test\",\n" +
                "\t\"lastname\": \"User\",\n" +
                "\t\"postcode\": \"1111\",\n" +
                "\t\"tax_id\": \"1\",\n" +
                "\t\"telephone\": \"+36306668884\",\n" +
                "\t\"zone_id\": \"1433\",\n" +
                "\t\"password\": \"12345\",\n" +
                "\t\"confirm\": \"12345\",\n" +
                "\t\"agree\": \"1\"\n" +
                "}";
        DataRequestPair requestPair = DataRequestPair.create(samplebody);
        DataRequest request = DataRequest.getInstance()
                .addHeaders(new String[]{"Authorization"}, new String[]{"Bearer a304fd213c730859fa96b25635bea1b2866895c4"});

        // replace this with your domain to test
        request.addUrl("https://www.gorakhadepartmentstores.com/api/rest/register");
        request.addDataRequestPair(requestPair);
        request.addMethod(Method.POST);
        DataRequestManager<String> requestManager = DataRequestManager.getInstance(getApplicationContext(), String.class);
        requestManager.addRequestBody(request).addOnDataRecieveListner(new OnDataRecievedListener() {
            @Override
            public void onDataRecieved(Response response, Object object) {
                Log.d("test", " data from server = " + object.toString());
                Assert.assertEquals(response.getMessage(), expected, response);
            }
        }, new OnDataRecievedProgressListener() {
            @Override
            public void onDataRecievedProgress(int completedPercentage) {
                Log.d("MainActivity", "Progress = " + completedPercentage);
            }
        });
        requestManager.getData();
    }
}
