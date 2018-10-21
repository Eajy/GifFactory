package com.seven.eajy.gif.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiao on 2018/9/10
 */
public class PrefUtils {

    public static final String PREF_NAME = "app_pref";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String getStringPref(Context context, String key, String defaultValue) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getString(key, defaultValue);
    }

    public static void setStringPref(Context context, String key, String value) {
        SharedPreferences.Editor editPrefs = getSharedPreferences(context).edit();
        editPrefs.putString(key, value);
        editPrefs.apply();
    }

    public static int getIntPref(Context context, String key, int defaultValue) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getInt(key, defaultValue);
    }

    public static void setIntPref(Context context, String key, int value) {
        SharedPreferences.Editor editPrefs = getSharedPreferences(context).edit();
        editPrefs.putInt(key, value);
        editPrefs.apply();
    }

    public static boolean getBooleanPref(Context context, String key, boolean defaultValue) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getBoolean(key, defaultValue);
    }

    public static void setBooleanPref(Context context, String key, boolean value) {
        SharedPreferences.Editor editPrefs = getSharedPreferences(context).edit();
        editPrefs.putBoolean(key, value);
        editPrefs.apply();
    }

    public static long getLongPref(Context context, String key, long defaultValue) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getLong(key, defaultValue);
    }

    public static void setLongPref(Context context, String key, long value) {
        SharedPreferences.Editor editPrefs = getSharedPreferences(context).edit();
        editPrefs.putLong(key, value);
        editPrefs.apply();
    }

    public static float getFloatPref(Context context, String key, float defaultValue) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getFloat(key, defaultValue);
    }

    public static void setFloatPref(Context context, String key, float value) {
        SharedPreferences.Editor editPrefs = getSharedPreferences(context).edit();
        editPrefs.putFloat(key, value);
        editPrefs.apply();
    }


    public static boolean containsPref(Context context, String key) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.contains(key);
    }

    public static void removePref(Context context, String key) {
        SharedPreferences.Editor editPrefs = getSharedPreferences(context).edit();
        editPrefs.remove(key);
        editPrefs.apply();
    }

    public static List<String> getAllPrefKeys(Context context) {
        List<String> ret = new ArrayList<String>();
        SharedPreferences prefs = getSharedPreferences(context);
        Map<String, ?> map = prefs.getAll();
        if (map != null) {
            ret.addAll(map.keySet());
        }
        return ret;
    }

}
