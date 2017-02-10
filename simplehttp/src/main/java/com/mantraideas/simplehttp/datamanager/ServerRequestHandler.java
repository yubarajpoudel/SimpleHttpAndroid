package com.mantraideas.simplehttp.datamanager;

import android.text.TextUtils;
import android.util.Log;

import com.mantraideas.simplehttp.datamanager.dmmodel.DataRequest;
import com.mantraideas.simplehttp.datamanager.error.DataManagerException;
import com.mantraideas.simplehttp.datamanager.util.DmUtilities;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerRequestHandler {

    HttpURLConnection conn;
    DataRequest request;

    public ServerRequestHandler(DataRequest request) {
        this.request = request;
        try {
            if (!TextUtils.isEmpty(request.getUrl())) {
                URL mUrl = new URL(request.getUrl());
                this.conn = (HttpURLConnection) mUrl.openConnection();
                if (request.hasValidHeaders()) {
                    addHeaderifExists(request);
                } else {
                    Log.d("ServerRequestHandler", "Headers is either empty or not valid");
                }
            } else {
                throw new DataManagerException("Url cannot be null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String httpGetData() {
        String responseFromServer = "";
        try {
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            responseFromServer = DmUtilities.convertStreamToString(conn.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataManagerException("error occured in connection");
        }
        return responseFromServer;
    }

    public String httpPostData() {
        String responseFromServer = "";
        try {
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream writer = conn.getOutputStream();
            writer.write(request.getDataRequestPair().toUrlEncodedData());
            writer.flush();
            writer.close();
            responseFromServer = DmUtilities.convertStreamToString(conn.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseFromServer;
    }

    private void addHeaderifExists(DataRequest request) {
        if (request.hasValidHeaders()) {
            for (int i = 0; i < request.getHeaderKeys().length; i++) {
                conn.setRequestProperty(request.getHeaderKeys()[i], request.getHeaderValues()[i]);
            }
        }
    }
}

