package com.mantraideas.simplehttp.datamanager;

import android.text.TextUtils;

import com.mantraideas.simplehttp.datamanager.dmmodel.DataRequest;
import com.mantraideas.simplehttp.datamanager.dmmodel.Method;
import com.mantraideas.simplehttp.datamanager.error.DataManagerException;
import com.mantraideas.simplehttp.datamanager.util.DmUtilities;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerRequestHandler {

    private static final String TAG = "ServerRequesthandler";
    private HttpURLConnection conn;
    private DataRequest request;
    private URL mUrl;
    private OnDataRecievedProgressListener listener = null;

    public ServerRequestHandler(DataRequest request, OnDataRecievedProgressListener listener) {
        this.request = request;
        this.listener = listener;
        try {
            if (!TextUtils.isEmpty(request.getUrl())) {
                this.mUrl = new URL(request.getUrl());
                this.conn = (HttpURLConnection) mUrl.openConnection();
                if (request.hasValidHeaders()) {
                    addHeaderifExists(request);
                } else {
                    DmUtilities.trace("ServerRequestHandler, Headers is either empty or not valid");
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
            InputStream is = new BufferedInputStream(mUrl.openStream(), 10);
            int total = 0;
            int connectionLength = conn.getContentLength();
            int lastProgress = 0;
            byte data[] = new byte[10];
            int count;
            while ((count = is.read(data)) != -1) {
                total += count;
                DmUtilities.trace("ServerRequestHandler, total = " + total);
                int progress = (total * 100) / connectionLength;
                if (lastProgress < progress) {
                    lastProgress = progress;
                    if (listener != null) {
                        DmUtilities.trace("ServerRequestHandler, Progress = " + progress);
                        listener.onDataRecievedProgress(progress);
                    }
                }
            }
            responseFromServer = DmUtilities.convertStreamToString(conn.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataManagerException("error occured in connection");
        }
        return responseFromServer;
    }


    public String httpPerformOperation(Method method) {
        String responseFromServer = "";
        try {
            conn.setRequestMethod(method.getMethod());
            conn.setRequestProperty("Accept-Encoding", "identity");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream writer = conn.getOutputStream();
            DmUtilities.trace("ServerRequestHandler, Contains DataRequestPair = " + (request.getDataRequestPair() != null));
            writer.write(request.getDataRequestPair() != null ? request.getDataRequestPair().toUrlEncodedData() : new byte[0]);
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

