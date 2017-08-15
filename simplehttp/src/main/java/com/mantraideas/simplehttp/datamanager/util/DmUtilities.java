package com.mantraideas.simplehttp.datamanager.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;

/**
 * Created by yubraj on 1/27/17.
 */

public class DmUtilities {

    public static boolean DEBUG = false;

    public static void trace(String message) {
        if (DEBUG) {
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

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public static final int MAX_PHOTO_RESOLUTION = 1000;

    public static Bitmap getDecodedBitmap(String path) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        int scale = 1;
        try {
            FileInputStream fis = new FileInputStream(new File(path));
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();

            Log.i("ImageUtils", "Original image size is " + o.outWidth + "X" + o.outHeight);
            if (o.outHeight > MAX_PHOTO_RESOLUTION || o.outWidth > MAX_PHOTO_RESOLUTION) {
                scale = (int) Math.pow(2, (int) Math.round(Math.log(MAX_PHOTO_RESOLUTION /
                        (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("ImgUtils", "Image scale factor = " + scale);
        Bitmap bitmap = null;
        try {
            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            o2.inJustDecodeBounds = false;
            FileInputStream fis2 = new FileInputStream(new File(path));
            bitmap = BitmapFactory.decodeStream(fis2, null, o2);
            fis2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
