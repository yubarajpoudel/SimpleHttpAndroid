package com.yuviii.restdata;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mantraideas.simplehttp.datamanager.DataRequestManager;
import com.mantraideas.simplehttp.datamanager.OnDataRecievedListener;
import com.mantraideas.simplehttp.datamanager.dmmodel.DataRequest;
import com.mantraideas.simplehttp.datamanager.dmmodel.DataRequestPair;
import com.mantraideas.simplehttp.datamanager.dmmodel.Method;
import com.mantraideas.simplehttp.datamanager.dmmodel.Response;

import junit.framework.Assert;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // for the getRequest
        doGetMethodOfRestAPI();

        //for the post request by passing the form data parameters
        doPostMethodOfRestAPIWithFormData();

        // for the post request by passing the jsonobject in post body
        doPostMethodOfRestApiWithJSONBody();

    }

    public void doPostMethodOfRestApiWithJSONBody() {
        final Response expected = Response.OK;
        String samplebody = "{\n" +
                "\t\"address_1\": \"Test street 88\",\n" +
                "\t\"address_2\": \"test\",\n" +
                "\t\"city\": \"Berlin\",\n" +
                "\t\"company_id\": \"company\",\n" +
                "\t\"company\": \"company\",\n" +
                "\t\"country_id\": \"97\",\n" +
                "\t\"email\": \"test@test.com\",\n" +
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
                .addHeaders(new String[]{"your_header_key"}, new String[]{"your_header_value"});

        // replace this with your domain to test
        request.addUrl("https://www.yourdomain.com");
        request.addDataRequestPair(requestPair);
        request.addMethod(Method.POST);
        DataRequestManager<String> requestManager = DataRequestManager.getInstance(getApplicationContext(), String.class);
        requestManager.addRequestBody(request).addOnDataReciveListner(new OnDataRecievedListener() {
            @Override
            public void onDataRecieved(Response response, Object object) {
                Log.d("test", " data from server = " + object.toString());
                Assert.assertEquals(response.getMessage(), expected, response);
            }
        });
        requestManager.getData();
    }

    public void doPostMethodOfRestAPIWithFormData() {
        final Response expected = Response.OK;
        DataRequestPair requestPair = DataRequestPair.create();
        requestPair.put("password", "12345");
        requestPair.put("email", "test@tests.com");

        // mulitple header can be passed by keeping it in array
        DataRequest request = DataRequest.getInstance().addHeaders(new String[]{"your_header_key"}, new String[]{"your_header_value"});
        request.addUrl("https://www.yourdomain.com/api/rest/login");
        request.addDataRequestPair(requestPair);
        request.addMethod(Method.POST);
        DataRequestManager<String> requestManager = DataRequestManager.getInstance(getApplicationContext(), String.class);
        requestManager.addRequestBody(request).addOnDataReciveListner(new OnDataRecievedListener() {
            @Override
            public void onDataRecieved(Response response, Object object) {
                Log.d("test", " data from server = " + object.toString());
                Assert.assertEquals(response.getMessage(), expected, response);
            }
        });
        requestManager.getData();
    }

    public void doGetMethodOfRestAPI() {
        final Response expected = Response.OK;
        Log.d("test", "started checking get test");
        // this is optional, mulitple header can be passed by keeping it in array
        DataRequest request = DataRequest.getInstance().addHeaders(new String[]{"your_header_key"}, new String[]{"your_header_value"});
        request.addUrl("https://www.yourdomain.com");
        request.addMethod(Method.GET);
        DataRequestManager<String> requestManager = DataRequestManager.getInstance(getApplicationContext(), String.class);
        requestManager.addRequestBody(request).addOnDataReciveListner(new OnDataRecievedListener() {
            @Override
            public void onDataRecieved(Response response, Object object) {
                Log.d("test", " data from server = " + object.toString());
                Assert.assertEquals(response.getMessage(), expected, response.OK);

            }
        });
        requestManager.getData();
    }
}
