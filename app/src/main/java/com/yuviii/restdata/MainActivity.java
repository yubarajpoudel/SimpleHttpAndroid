package com.yuviii.restdata;

import android.os.Bundle;
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

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Currently supports GET, POST, PUT, DELETE method

//         for the getRequest
        doGetMethodOfRestAPIForJSONArray();
        doGetMethodOfRestAPIForJSONObject();
//
//        //for the post request by passing the form data parameters
        doPostMethodOfRestAPIWithFormData();

        // for the post request by passing the jsonobject in post body
        doPostMethodOfRestApiWithJSONBody();

        // for delete method
        testDELETEMethod();
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

    public void doPostMethodOfRestAPIWithFormData() {
        final Response expected = Response.OK;

        DataRequestPair requestPair = DataRequestPair.create();
        requestPair.put("password", "12345");
        requestPair.put("email", "test@tests.com");


        // mulitple header can be passed by keeping it in array
        DataRequest request = DataRequest.getInstance();
//        request.addHeaders(new String[]{"your_header_key"}, new String[]{"your_header_value"});
        request.addHeaders(new String[]{"X-APP-PKG"}, new String[]{"com.np.lokbhaka"});
//        request.addUrl("https://www.yourdomain.com/api/rest/login");
        request.addUrl("http://yourdomain.com");
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

    public void testDELETEMethod(){
        DataRequest request = DataRequest.getInstance();
        request.addUrl("https://www.yourdomain.com");
        // headers are optional
        request.addHeaders(new String[]{"headerKey"}, new String[]{"heade value"});
        request.addMethod(Method.DELETE);
        DataRequestManager<String> requestManager = DataRequestManager.getInstance(getApplicationContext(), String.class);
        requestManager.addRequestBody(request).addOnDataRecieveListner(new OnDataRecievedListener() {
            @Override
            public void onDataRecieved(Response response, Object object) {
                Log.d("MainActivity", "Response = " + response.getMessage() + " , data From Server = " + object.toString());
            }
        });
        requestManager.getData();
    }

    public void doGetMethodOfRestAPIForJSONArray() {
        Log.d("test", "started checking get test");
        DataRequest request = DataRequest.getInstance();
        // this is optional, mulitple header can be passed by keeping it in array
        // request.addHeaders(new String[]{"your_header_key"}, new String[]{"your_header_value"});
        request.addUrl("http://github.yubrajpoudel.com.np/others/sample1.json");
        request.addMethod(Method.GET);
        DataRequestManager<Profile> requestManager = DataRequestManager.getInstance(getApplicationContext(),
                String.class);
        requestManager.addRequestBody(request).addOnDataRecieveListner(new OnDataRecievedListener() {
            @Override
            public void onDataRecieved(Response response, Object object) {
                // since the json Array is recieved from the get Request this library gives the list of the class passed as parameter
                // here Profile is passed hence the object will be the list of profile.
                // if it is jsonobject it will give the pofile object
                if (response == Response.OK) {
                    Log.d("MainActivity", "data = " + object.toString());
                    List<Profile> profileList = (List<Profile>) object;
                    Log.d("MainActivity", "list size = " + profileList.size());
                } else {
                    Log.d("MainActivity", "response = " + response.getMessage());
                }
            }
        }, new OnDataRecievedProgressListener() {
            @Override
            public void onDataRecievedProgress(int completedPercentage) {
                Log.d("MainActivity", " progress = " + completedPercentage);
            }
        });
        requestManager.getData();
    }

    public void doGetMethodOfRestAPIForJSONObject() {
        Log.d("test", "started checking get test");
        // this is optional, mulitple header can be passed by keeping it in array
        DataRequest request = DataRequest.getInstance();
        // request.addHeaders(new String[]{"your_header_key"}, new String[]{"your_header_value"});
        request.addUrl("http://github.yubrajpoudel.com.np/others/sample2.json");
        request.addMethod(Method.GET);
        DataRequestManager<Profile> requestManager = DataRequestManager.getInstance(getApplicationContext(),
                Profile.class);
        requestManager.addRequestBody(request).addOnDataRecieveListner(new OnDataRecievedListener() {
            @Override
            public void onDataRecieved(Response response, Object object) {
                // since the json Array is recieved from the get Request this library gives the list of the class passed as parameter
                // here Profile is passed hence the object will be the list of profile.
                // if it is jsonobject it will give the pofile object
                if (response == Response.OK) {
                    Profile profile = (Profile) object;
                    Log.d("MainActivity", "Profile name = " + profile.name + " address = " + profile.address + " profession = " + profile.profession);
                } else {
                    Log.d("MainActivity", "response = " + response.getMessage());
                }
            }
        });
        requestManager.getData();
    }


}
