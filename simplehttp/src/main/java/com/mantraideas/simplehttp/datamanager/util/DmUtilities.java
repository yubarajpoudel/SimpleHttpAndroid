package com.mantraideas.simplehttp.datamanager.util;

import android.content.Context;
import android.net.ConnectivityManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;

/**
 * Created by yubraj on 1/27/17.
 */

public class DmUtilities {

    public static boolean DEBUG = false;

    public static void trace(String message){
        if(DEBUG) {
            System.out.println(message);
        }
    }

    public static String getSha1Hex(String string) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(string.getBytes());
            byte[] bytes = messageDigest.digest();
            StringBuilder buffer = new StringBuilder();
            for (byte b : bytes) {
                buffer.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            DmUtilities.trace("SHA-1, params = " + string + " signature = " + buffer.toString());
            return buffer.toString();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return "";
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static boolean isNetworkConnected(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
