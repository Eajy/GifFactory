package com.seven.eajy.gif.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zhangxiao on 2018/9/10
 */
public class BitmapUtils {

    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    public static void saveBitmap2File(Bitmap bitmap, String path) {
        try {
            FileOutputStream outputStream = FileUtils.openOutputStream(new File(path));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            IOUtils.closeQuietly(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap Bytes2Bitmap(byte[] bytes) {
        if (bytes.length != 0) {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }
    }

    /**
     * 缩放bitmap
     *
     * @param srcBitmap
     * @param newW
     * @param newH
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap srcBitmap, int newW, int newH) {
        int w = srcBitmap.getWidth();
        int h = srcBitmap.getHeight();
        float scaleWidth = ((float) newW) / w;
        float scaleHeight = ((float) newH) / h;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(srcBitmap, 0, 0, w, h, matrix, true);
    }

    public static Bitmap drawable2Bitmap(Context context, Drawable drawable) {
        return ((BitmapDrawable) drawable).getBitmap();
    }

}
