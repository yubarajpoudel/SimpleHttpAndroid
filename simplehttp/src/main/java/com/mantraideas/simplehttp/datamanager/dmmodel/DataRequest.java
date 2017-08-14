package com.mantraideas.simplehttp.datamanager.dmmodel;

/**
 * Created by yubraj on 1/27/17.
 */

public class DataRequest {
    private String url = "";
    private DataRequestPair dataRequestPair = null;
    private Method method = null;
    private long minimumServerCallTime = 0;
    private boolean force = false;
    private boolean saveForOffline = false;
    private String[] headerKeys = null, headerValues = null;
    private int connectionTime = 10000;

    public String getUrl() {
        return url;
    }

    public DataRequestPair getDataRequestPair() {
        return dataRequestPair;
    }

    public Method getMethod() {
        return method;
    }

    public long getMinimumServerCallTime() {
        return minimumServerCallTime;
    }

    public boolean isForce() {
        return force;
    }

    public boolean isSaveForOffline() {
        return saveForOffline;
    }

    private DataRequest() {
    }


    public static DataRequest getInstance() {
        return new DataRequest();
    }

    public DataRequest addUrl(String url) {
        this.url = url;
        return this;
    }

    public DataRequest addDataRequestPair(DataRequestPair requestPair) {
        this.dataRequestPair = requestPair;
        return this;
    }

    public DataRequest addMethod(Method method) {
        this.method = method;
        return this;
    }

    public DataRequest addMinimumServerCallTimeDifference(long minimumServerCallTimeDifference) {
        this.minimumServerCallTime = minimumServerCallTimeDifference;
        return this;
    }

    public DataRequest setIgnoreServerCallTimeDiference(boolean force) {
        this.force = force;
        return this;
    }

    /**
     *
     * @param connectionTime in milliseconds
     * @return servercallTime
     */
    public DataRequest setServerConnectionTime(int connectionTime){
        this.connectionTime = connectionTime;
        return this;
    }
    public DataRequest addHeaders(String[] headerKeys, String[] headerValues) {
        this.headerKeys = headerKeys;
        this.headerValues = headerValues;
        return this;
    }

    public boolean hasValidHeaders() {
        return (headerKeys != null && headerValues != null)
                && headerKeys.length > 0 && (headerKeys.length == headerValues.length);
    }

    public int getConnectionTime(){
        return this.connectionTime;
    }

    public String[] getHeaderKeys() {
        return this.headerKeys;
    }

    public String[] getHeaderValues() {
        return this.headerValues;
    }

    public void persistData(boolean persist) {
        this.saveForOffline = persist;
    }
}
