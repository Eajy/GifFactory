package com.seven.eajy.gif.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import java.util.Formatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangxiao on 2018/9/10
 */
public class StringUtils {

    public static boolean isEmailValid(String email) {
        Pattern p = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static int parseInt(String str) {
        int defaultValue = 0;
        if (str == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static String bytes2HexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 实现文本复制功能
     *
     * @param content
     */
    public static void copyText(String content, Context context) {
        try {
            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (cmb != null) {
                cmb.setPrimaryClip(ClipData.newPlainText(null, content));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 实现粘贴功能
     *
     * @param context
     * @return
     */
    public static CharSequence pasteText(Context context) {
        try {
            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (cmb != null && cmb.hasPrimaryClip()) {
                return cmb.getPrimaryClip().getItemAt(0).getText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 格式化视频时间
     *
     * @param duration
     * @return
     */
    public static String formatVideoDuration(long duration) {
        if (duration <= 0 || duration >= 24 * 60 * 60 * 1000) {
            return "00:00";
        }
        long totalSeconds = Math.round(duration / (double) 1000);
        int seconds = (int) (totalSeconds % 60);
        int minutes = (int) ((totalSeconds / 60) % 60);
        int hours = (int) (totalSeconds / 3600);
        StringBuilder stringBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

}
