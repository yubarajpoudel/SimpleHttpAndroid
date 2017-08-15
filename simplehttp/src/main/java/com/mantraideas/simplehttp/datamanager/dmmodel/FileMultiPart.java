package com.mantraideas.simplehttp.datamanager.dmmodel;

import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.mantraideas.simplehttp.datamanager.error.DataManagerException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yubaraj on 8/15/17.
 */

public class FileMultiPart {

    String charSet = "";
    HashMap<String, String> mapFormField;
    Pair<String, ArrayList<String>> filePart;
    ArrayList<String> filePaths;

    private FileMultiPart() {
        this.mapFormField = new HashMap<>();
        this.filePaths = new ArrayList<>();
    }

    public static FileMultiPart instance() {
        return new FileMultiPart();
    }

    public String getCharset() {
        return this.charSet;
    }

    public FileMultiPart setCharSet(String charSet) {
        this.charSet = charSet;
        return this;
    }

    public HashMap<String, String> getMapFormField() {
        return this.mapFormField;
    }

    public Pair<String, ArrayList<String>> getFilePart() {
        return this.filePart;
    }


    public FileMultiPart addFromField(String key, String value) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            Log.w("SimpleHttp", "FileMultipart :: addFromField - cannot added, either key or value is empty");
        } else {
            mapFormField.put(key, value);
        }
        return this;
    }

    public FileMultiPart addMultipleFiles(String key, ArrayList<String> filePathList) {
        if (filePathList == null) {
            throw new DataManagerException("SimpleHttp :: ImagesPath list is null");
        } else if (filePathList.size() == 0) {
            Log.e("SimpleHttp", "FileMultipart :: imageList is empty");
        } else {
            this.filePaths.addAll(filePathList);
            this.filePart = new Pair<>(key, filePaths);
        }
        return this;
    }

    public FileMultiPart addFile(String key, String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            throw new DataManagerException("SimpleHttp :: ImagesPath is null");
        } else {
            this.filePaths.add(filePath);
            this.filePart = new Pair<>(key, filePaths);
        }
        return this;
    }

    @Override
    public String toString() {
        String output = "{";
        if (!TextUtils.isEmpty(charSet)) {
            output += "\"charset\":\"" + charSet + "\",";
        }

        if (mapFormField.size() > 0) {
            output += "\"fileField\":{";
            boolean first = true;
            for (String key : mapFormField.keySet()) {
                if (first) {
                    output += ",";
                    first = false;
                }
                output += "\"" + key + "\":\"" + mapFormField.get(key) + "\"";
            }
            output += "},";
        }
        if(filePart != null) {
            output += "\"filePart\":{";
            if (filePart.second.size() > 0) {
                boolean first = true;
                ArrayList<String> filePaths = filePart.second;
                for (String data : filePaths) {
                    if (first) {
                        output += ",";
                        first = false;
                    }
                    output += "\""+ data + "\"";
                }
            }
            output += "},";
        }
        if (filePaths != null && filePaths.size() > 0) {
            output += "\"filePath\":{";
            boolean first = true;
            for (int i = 0; i < filePaths.size(); i++) {
                if (first) {
                    output += ",";
                    first = false;
                }
                output += "\"path" + i + "\":\"" + filePaths.get(i) + "\"";
            }
            output += "}";
        }
        output += "}";
        return output;
    }
}
