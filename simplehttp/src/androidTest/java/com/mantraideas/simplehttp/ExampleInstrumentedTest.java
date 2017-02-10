package com.mantraideas.simplehttp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.mantraideas.simplehttp.datamanager.DataRequestManager;
import com.mantraideas.simplehttp.datamanager.OnDataRecievedListener;
import com.mantraideas.simplehttp.datamanager.dmmodel.DataRequest;
import com.mantraideas.simplehttp.datamanager.dmmodel.DataRequestPair;
import com.mantraideas.simplehttp.datamanager.dmmodel.Method;
import com.mantraideas.simplehttp.datamanager.dmmodel.Response;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 * change the yourdomain.com url with your domain
 *
 * @author yubraj Poudel
 * @email yubarajpoudel708@gmail.com
 * @see <a href="http://www.yubrajpoudel.com.np">About the author</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.mantraideas.simplehttp.test", appContext.getPackageName());
    }

    @Test
    public void checkPostMethodOfRestApiWithJSONBody() {
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
        DataRequestManager<String> requestManager = DataRequestManager.getInstance(InstrumentationRegistry.getTargetContext(), String.class);
        requestManager.addRequestBody(request).addOnDataReciveListner(new OnDataRecievedListener() {
            @Override
            public void onDataRecieved(Response response, Object object) {
                Log.d("test", " data from server = " + object.toString());
                Assert.assertEquals(response.getMessage(), expected, response);
            }
        });
        requestManager.getData();
    }

    @Test
    public void checkPostMethodOfRestAPIWithFormData() {
        final Response expected = Response.OK;
        DataRequestPair requestPair = DataRequestPair.create();
        requestPair.put("password", "12345");
        requestPair.put("email", "test@tests.com");

        // mulitple header can be passed by keeping it in array
        DataRequest request = DataRequest.getInstance().addHeaders(new String[]{"your_header_key"}, new String[]{"your_header_value"});
        request.addUrl("https://www.yourdomain.com/api/rest/login");
        request.addDataRequestPair(requestPair);
        request.addMethod(Method.POST);
        DataRequestManager<String> requestManager = DataRequestManager.getInstance(InstrumentationRegistry.getTargetContext(), String.class);
        requestManager.addRequestBody(request).addOnDataReciveListner(new OnDataRecievedListener() {
            @Override
            public void onDataRecieved(Response response, Object object) {
                Log.d("test", " data from server = " + object.toString());
                Assert.assertEquals(response.getMessage(), expected, response);
            }
        });
        requestManager.getData();
    }

    @Test
    public void checkGetMethodOfRestAPI() {
        final Response expected = Response.OK;
        Log.d("test", "started checking get test");
        // mulitple header can be passed by keeping it in array
        DataRequest request = DataRequest.getInstance().addHeaders(new String[]{"your_header_key"}, new String[]{"your_header_value"});
        request.addUrl("https://www.yourdomain.com");
        request.addMethod(Method.GET);
        DataRequestManager<String> requestManager = DataRequestManager.getInstance(InstrumentationRegistry.getTargetContext(), String.class);
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
