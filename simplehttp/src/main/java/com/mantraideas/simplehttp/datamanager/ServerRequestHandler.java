package com.mantraideas.simplehttp.datamanager;

import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.mantraideas.simplehttp.datamanager.dmmodel.DataRequest;
import com.mantraideas.simplehttp.datamanager.dmmodel.FileMultiPart;
import com.mantraideas.simplehttp.datamanager.dmmodel.Method;
import com.mantraideas.simplehttp.datamanager.error.DataManagerException;
import com.mantraideas.simplehttp.datamanager.util.DmUtilities;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

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
                this.conn.setConnectTimeout(request.getConnectionTime());
                if (request.hasValidHeaders()) {
                    addHeaderifExists(request);
                } else {
                    DmUtilities.trace("ServerRequestHandler, Headers is either empty or not valid");
                }
            } else {
                throw new DataManagerException("Url cannot be null or empty");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String upLoad() {
        FileMultiPart fileMultiPart = request.getFileMultiPart();
        if (fileMultiPart == null) {
            throw new DataManagerException("SimpleHttp :: fileMultipart cannot be null");
        }
        try {
            FileUploader fileUploader = new FileUploader(conn, fileMultiPart.getCharset(), listener);
            Pair<String, ArrayList<String>> filePart = fileMultiPart.getFilePart();
            if (TextUtils.isEmpty(filePart.first) || filePart.second.size() == 0) {
                Log.w("SimpleHttp", "FIleUploader :: file path to upload is not found");
                return "{}";
            }
            for (int i = 0; i < filePart.second.size(); i++) {
                fileUploader.addFilePart(filePart.first, new File(filePart.second.get(i)));
            }
            for (String key : fileMultiPart.getMapFormField().keySet()) {
                fileUploader.addFormField(key, fileMultiPart.getMapFormField().get(key));
            }
            return fileUploader.finish();

        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }


    public String get() {
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
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            throw new DataManagerException("ServerTimeout exception");
        } catch (IOException e) {
            e.printStackTrace();
            throw new DataManagerException("Invalid url request");
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataManagerException("error occured in connection");
        } finally {
            conn.disconnect();
        }
        return responseFromServer;
    }


    public String post() {
        String responseFromServer = "";
        DataOutputStream outputStreamWriter = null;
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Encoding", "identity");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            outputStreamWriter = new DataOutputStream(conn.getOutputStream());
            outputStreamWriter.write(request.getDataRequestPair() != null ? request.getDataRequestPair().toUrlEncodedData() : new byte[0]);
            responseFromServer = DmUtilities.convertStreamToString(conn.getInputStream());
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            throw new DataManagerException("ServerTimeout exception");
        } catch (IOException e) {
            e.printStackTrace();
            throw new DataManagerException("Invalid url request");
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataManagerException("error occured in connection");
        } finally {
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.flush();
                    outputStreamWriter.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return responseFromServer;
    }

    public String delete() {
        DataOutputStream outputStreamWriter = null;
        String responseFromServer = "";
        try {
            conn.setRequestMethod("DELETE");
            conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            if (request.getDataRequestPair() != null) {
                throw new DataManagerException("Delete method doesnot support writting in the body");
            }
            int httpResult = conn.getResponseCode();
            if (httpResult == HttpURLConnection.HTTP_OK) {
                responseFromServer = DmUtilities.convertStreamToString(conn.getInputStream());
                DmUtilities.trace("ServerRequestHandler, Delete :: successed");
            } else {
                responseFromServer = "{}";
                DmUtilities.trace("ServerRequestHandler, Delete :: failed");
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            throw new DataManagerException("ServerTimeout exception");
        } catch (IOException e) {
            e.printStackTrace();
            throw new DataManagerException("Invalid url request");
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataManagerException("error occured in connection");
        } finally {
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.flush();
                    outputStreamWriter.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return responseFromServer;
    }


    public String put() {
        DataOutputStream outputStreamWriter = null;
        String responseFromServer = "";
        try {
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            outputStreamWriter = new DataOutputStream(conn.getOutputStream());
            outputStreamWriter.write(request.getDataRequestPair() != null ? request.getDataRequestPair().toUrlEncodedData() : new byte[0]);
            responseFromServer = DmUtilities.convertStreamToString(conn.getInputStream());
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            throw new DataManagerException("ServerTimeout exception");
        } catch (IOException e) {
            e.printStackTrace();
            throw new DataManagerException("Invalid url request");
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataManagerException("error occured in connection");
        } finally {
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.flush();
                    outputStreamWriter.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return responseFromServer;
    }

    private void addHeaderifExists(DataRequest request) {
        if (request.hasValidHeaders()) {
            for (int i = 0; i < request.getHeaderKeys().length; i++) {
                if (!TextUtils.isEmpty(request.getHeaderKeys()[i]) && !TextUtils.isEmpty(request.getHeaderValues()[i])) {
                    conn.setRequestProperty(request.getHeaderKeys()[i], request.getHeaderValues()[i]);
                } else {
                    Log.w("SimpleHttp", "Header key or value is empty");
                }
            }
        }
    }
}

