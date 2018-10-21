package com.seven.eajy.gif.util;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * Created by zhangxiao on 2018/9/10
 */
public class ScreenUtils {

    public static int getScreenHeight(Context context) {
        try {
            return context.getResources().getDisplayMetrics().heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getScreenWidth(Context context) {
        try {
            return context.getResources().getDisplayMetrics().widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取屏幕原始尺寸高度，包括虚拟键
     *
     * @param context
     * @return
     */
    public static int getFullScreenHeight(Context context) {
        int height = 0;
        try {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            @SuppressWarnings("rawtypes")
            Class c;
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            height = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return height;
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusHeight = 24;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取虚拟按键的高度
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        int totalHeight = getFullScreenHeight(context);
        int contentHeight = getScreenHeight(context);
        return totalHeight - contentHeight;
    }

    // 计算视频宽高大小，视频比例xxx*xxx按屏幕比例放大或者缩小
    public static int[] reckonVideoWidthHeight(float width, float height, Context mContext) {
        try {
            int sWidth = getScreenWidth(mContext);
            float wRatio = 0.0f;
            wRatio = (sWidth - width) / width;
            // 等比缩放
            int nWidth = sWidth;
            int nHeight = (int) (height * (wRatio + 1));
            return new int[]{nWidth, nHeight};
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    private static boolean isPortrait(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        if (configuration != null && configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return false;
        }
        return true;
    }
}
