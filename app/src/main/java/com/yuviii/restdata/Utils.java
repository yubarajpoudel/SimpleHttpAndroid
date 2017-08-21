package com.yuviii.restdata;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by yubaraj on 8/20/17.
 */

public class Utils {

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

            Log.i("DmUtilities", "Original image size is " + o.outWidth + "X" + o.outHeight);
            if (o.outHeight > MAX_PHOTO_RESOLUTION || o.outWidth > MAX_PHOTO_RESOLUTION) {
                scale = (int) Math.pow(2, (int) Math.round(Math.log(MAX_PHOTO_RESOLUTION /
                        (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("DmUtilities", "Image scale factor = " + scale);
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
