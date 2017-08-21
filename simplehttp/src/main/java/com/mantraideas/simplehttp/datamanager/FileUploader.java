package com.mantraideas.simplehttp.datamanager;

import com.mantraideas.simplehttp.datamanager.util.DmUtilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URLConnection;

/**
 * Created by yubaraj on 8/15/17.
 */

public class FileUploader {
    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection httpConn;
    private String charset;
    private OutputStream outputStream;
    private PrintWriter writer;
    OnDataRecievedProgressListener listener;

    public FileUploader(HttpURLConnection httpConn, String charset, OnDataRecievedProgressListener listener)
            throws IOException {
        this.httpConn = httpConn;
        this.charset = charset;
        this.listener = listener;

        // creates a unique boundary based on time stamp
        boundary = "===" + System.currentTimeMillis() + "===";
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true); // indicates POST method
        httpConn.setDoInput(true);
        httpConn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundary);
        httpConn.setRequestProperty("cache-control", "no-cache");
        outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                true);
    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                .append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(
                LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a upload file section to the request
     *
     * @param uploadFile a File to be uploaded
     */

    public void addFilePart(String fieldName, File uploadFile)
            throws IOException {
        String fileName = uploadFile.getName();
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append(
                "Content-Disposition: form-data; name=\"" + fieldName
                        + "\"; filename=\"" + fileName + "\"")
                .append(LINE_FEED);
        writer.append(
                "Content-Type: "
                        + URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();

        writer.append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a header field to the request.
     *
     * @param name  - name of the header field
     * @param value - value of the header field
     */
    public void addHeaderField(String name, String value) {
        writer.append(name + ": " + value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Completes the request and receives response from the server.
     *
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public String finish() throws IOException {
        String response = "";
        writer.append(LINE_FEED).flush();
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.close();
        int total = 0;
        int connectionLength = httpConn.getContentLength();
        int lastProgress = 0;
        byte data[] = new byte[10];
        int count;

        // checks server's status code first
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            try {
//                while ((count = httpConn.getInputStream().read(data)) != -1) {
//                    total += count;
//                    DmUtilities.trace("FileUploader, total = " + total);
//                    int progress = (total * 100) / connectionLength;
//                    if (lastProgress < progress) {
//                        lastProgress = progress;
//                        if (listener != null) {
//                            DmUtilities.trace("FileUploader, Progress = " + progress);
//                            listener.onDataRecievedProgress(progress);
//                        }
//                    }
//                }
                response = DmUtilities.convertStreamToString(httpConn.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                httpConn.disconnect();
            }
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }
        DmUtilities.trace("FileUploader, response = " + response);
        return response;
    }

}
