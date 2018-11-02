package com.seven.eajy.gif;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by songsaihua on 2018/10/18
 */
public class GifJniTest {
    public static String rootPath = Environment.getExternalStorageDirectory().getPath()+"/gif_f/";
    static {
        System.loadLibrary("gif-lib");

    }
    public static native int initGifLib(String path);

    public static void callback(int w,int h,int index,byte[] Bits) {
        Log.i("print","index ："+index+",w : "+w+",h : "+h);
        Log.i("print","Bits : "+ Arrays.toString(Bits));
        Bitmap bitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        ByteBuffer byteBuffer = ByteBuffer.wrap(Bits);
        byteBuffer.rewind();
        bitmap.copyPixelsFromBuffer(byteBuffer);
        String fileName = System.currentTimeMillis()+"t_gif"+index+".jpg";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(rootPath,fileName));
            boolean b = bitmap.compress(Bitmap.CompressFormat.JPEG,90,fos);
            if (b) {
                Log.i("print","compress is success");
                bitmap.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("print","callback is error : "+e.getMessage());
        }
    }

    public static Bitmap createBitmap(int w,int h) {
        Bitmap bitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        return bitmap;
    }

    public static void callbackBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            Log.i("print","callbackBitmap--is null");
        } else {
            FileOutputStream fos = null;
            try {
                String fileName = System.currentTimeMillis()+"t_gif.jpg";
                fos = new FileOutputStream(new File(rootPath,fileName));
                boolean b = bitmap.compress(Bitmap.CompressFormat.JPEG,90,fos);
                if (b) {
                    Log.i("print","compress is success");
                    bitmap.recycle();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("print","callback is error : "+e.getMessage());
            } finally {
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}
