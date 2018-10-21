package com.seven.eajy.gif.util;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by zhangxiao on 2018/9/10
 */
public class DeviceInfoUtils {

    /**
     * 可以用于应用实例ID 或GUID
     *
     * @return UUID
     */
    public static String getUUId() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取SN序列号 不推荐
     *
     * @param context
     * @return
     */
    public static String getSerial(Context context) {
        try {
            return Build.SERIAL;
        } catch (Throwable e) {
            return "";
        }
    }

    /**
     * 获取 Android ID
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        try {
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Throwable e) {
            return "";
        }
    }

    /**
     * 获取设备wifi mac地址, 链接WI-FI时才能获取到， 不推荐
     *
     * @param context
     * @return
     */
    public static String getWifiMacAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager == null || !wifiManager.isWifiEnabled()) {
                return "";
            }
            if (Build.VERSION.SDK_INT < 23) {
                return wifiManager.getConnectionInfo().getMacAddress();
            } else {
                return getMacAddressAfterAndroidM();
            }
        } catch (Throwable e) {
            return "";
        }
    }

    private static String getMacAddressAfterAndroidM() {
        String macAddress = "";
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iF = interfaces.nextElement();

                byte[] addr = iF.getHardwareAddress();
                if (addr == null || addr.length == 0) {
                    continue;
                }

                StringBuilder buf = new StringBuilder();
                for (byte b : addr) {
                    buf.append(String.format("%02X:", b));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                String mac = buf.toString();
                Log.d("mac", "interfaceName=" + iF.getName() + ", mac=" + mac);

                if (TextUtils.equals(iF.getName(), "wlan0")) {
                    return mac;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return macAddress;
    }

    /**
     * 获取国家代码
     *
     * @param context
     * @return
     */
    public static String getCountryCode(Context context) {
        try {
            Locale locale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = context.getResources().getConfiguration().getLocales().get(0);
            } else {
                locale = context.getResources().getConfiguration().locale;
            }
            return locale.getCountry();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取设备厂商
     *
     * @return
     */
    public static String getManufacturer() {
        try {
            return Build.MANUFACTURER;
        } catch (Throwable e) {
            return "";
        }
    }

    /**
     * 获取设备型号
     *
     * @return
     */
    public static String getModel() {
        try {
            return Build.MODEL;
        } catch (Throwable e) {
            return "";
        }
    }

    /**
     * 获取设备厂商和型号
     *
     * @return
     */
    public static String getManufactureAndModel() {
        try {
            return Build.MANUFACTURER + "_" + Build.MODEL;
        } catch (Throwable e) {
            return "";
        }
    }


    public static boolean isSamsung() {
        return getManufacturer().toLowerCase().contains("samsung");
    }

    public static boolean isLG() {
        return getManufacturer().toLowerCase().contains("lge");
    }

    public static boolean isHuaWei() {
        return getManufacturer().toLowerCase().contains("huawei");
    }

    public static boolean isOppo() {
        return getManufacturer().toLowerCase().contains("oppo");
    }

    public static boolean isHTC() {
        return getModel().toLowerCase().contains("htc") || getModel().toLowerCase().contains("desire");
    }

    public static boolean isXiaoMi() {
        return getManufactureAndModel().toLowerCase().contains("xiaomi");
    }

    public static boolean isHongMi() {
        return getManufactureAndModel().toLowerCase().contains("xiaomi") && Build.DEVICE.startsWith("HM");
    }

    public static boolean isOnePlus() {
        return getManufacturer().equalsIgnoreCase("oneplus");
    }

    public static boolean isLM_G710() {
        return isLG() && getModel().equalsIgnoreCase("LM-G710");
    }

    public static boolean isS6() {
        return getModel().contains("SM-G920");
    }

    public static boolean isMi2() {
        return getModel().contains("MI 2");
    }

}
