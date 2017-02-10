package com.yuviii.restdata;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mantraideas.simplehttp.datamanager.DataRequestManager;
import com.mantraideas.simplehttp.datamanager.OnDataRecievedListener;
import com.mantraideas.simplehttp.datamanager.dmmodel.DataRequest;
import com.mantraideas.simplehttp.datamanager.dmmodel.Method;
import com.mantraideas.simplehttp.datamanager.dmmodel.Response;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testApi();
    }

    private void testApi() {
        final boolean expected = true;
        DataRequest request = DataRequest.getInstance().addHeaders(new String[]{"Authorization"}, new String[]{"Bearer c965de882f36bcef4eb820b1328ace7f6ec56600"});
        request.addUrl("https://www.gorakhadepartmentstores.com/api/rest/products");
        request.addMethod(Method.GET);
        DataRequestManager<String> requestManager = DataRequestManager.getInstance(getApplicationContext(), String.class);
        requestManager.addRequestBody(request).addOnDataReciveListner(new OnDataRecievedListener() {
            @Override
            public void onDataRecieved(Response response, Object object) {
                Log.d("test", " data from server = " + object.toString());
                try {
                    JSONObject jsonObject = new JSONObject((String) object);
                    Assert.assertEquals("Rest succed but with failed getting products", expected, jsonObject.optBoolean("success"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("test", " data from server = " + object.toString());
                }
            }
        });
        requestManager.getData();
    }
}
