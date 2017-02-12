package com.mantraideas.simplehttp.datamanager.dmmodel;


import com.mantraideas.simplehttp.datamanager.error.DataManagerException;
import com.mantraideas.simplehttp.datamanager.util.DmUtilities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yubraj on 1/27/17.
 */

public class DataRequestPair {
    private enum TYPE {
        JSON,
        PAIR
    }

    private String jsonObject;
    private String key, value;
    TYPE type;
    private List<DataRequestPair> list;

    private DataRequestPair() {
        list = new ArrayList<>();
        this.type = TYPE.PAIR;
    }

    private DataRequestPair(String jsonObject) {
        this.jsonObject = jsonObject;
        this.type = TYPE.JSON;
    }

    private DataRequestPair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static DataRequestPair create(String json) {
        return new DataRequestPair(json);
    }

    public static DataRequestPair create() {
        return new DataRequestPair();
    }

    public void put(String key, String value) {
        list.add(new DataRequestPair(key, value));

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        try {
            for (DataRequestPair data : list) {
                builder.append("\"" + data.key + "\" = \"" + data.value + "\"");
            }
        } catch (DataManagerException e) {
            e.printStackTrace();
        }
        DmUtilities.trace("DataRequestPair, value = " + builder.toString());
        return builder.toString();
    }

    public byte[] toUrlEncodedData() {
        if (type == TYPE.JSON) {
            try {
                DmUtilities.trace("DataRequest pair, json = " + jsonObject);
                return jsonObject.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (type == TYPE.PAIR && list != null) {

            StringBuilder result = new StringBuilder();
            boolean first = true;
                try {
                    for (DataRequestPair data : list) {
                        if (first) {
                            first = false;
                        } else {
                            result.append("&");
                        }
                        result.append(URLEncoder.encode(data.key, "UTF-8"));
                        result.append("=");
                        result.append(URLEncoder.encode(data.value, "UTF-8"));
                    }
                    byte[] bytes = result.toString().getBytes();
                    return bytes;
                } catch (DataManagerException dmException) {
                    dmException.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        return new byte[0];
    }
}
