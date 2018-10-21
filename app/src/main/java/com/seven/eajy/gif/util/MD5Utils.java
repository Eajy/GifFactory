package com.seven.eajy.gif.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

/**
 * Created by zhangxiao on 2018/9/10
 */
public class MD5Utils {

    private static MessageDigest md = null;

    public MD5Utils() {
    }

    public static synchronized String digest(String value) {
        return value == null ? null : digest(value.getBytes());
    }

    public static synchronized String digest(byte[] value) {
        if (md == null) {
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }

        if (md != null) {
            md.reset();
            md.update(value);
            byte[] b = md.digest();
            StringBuffer buf = new StringBuffer("");

            for (int offset = 0; offset < b.length; ++offset) {
                int i = b[offset];
                if (i < 0) {
                    i += 256;
                }

                if (i < 16) {
                    buf.append("0");
                }

                buf.append(Integer.toHexString(i));
            }

            return buf.toString();
        } else {
            return "";
        }
    }

    public static synchronized String digest(File file) {
        if (file != null && file.exists()) {
            FileInputStream fis = null;

            try {
                if (md == null) {
                    try {
                        md = MessageDigest.getInstance("MD5");
                    } catch (Exception var13) {
                        ;
                    }
                }

                if (md != null) {
                    md.reset();
                    fis = new FileInputStream(file);
                    byte[] buff = new byte[1024];
                    boolean var3 = false;

                    int len;
                    while ((len = fis.read(buff)) != -1) {
                        md.update(buff, 0, len);
                    }

                    fis.close();
                    byte[] b = md.digest();
                    StringBuffer buf = new StringBuffer("");

                    for (int offset = 0; offset < b.length; ++offset) {
                        int i = b[offset];
                        if (i < 0) {
                            i += 256;
                        }

                        if (i < 16) {
                            buf.append("0");
                        }

                        buf.append(Integer.toHexString(i));
                    }

                    String var9 = buf.toString();
                    return var9;
                }
            } catch (Exception var14) {
                return "";
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException ioe) {
                    // ignore
                }
            }

            return null;
        } else {
            return "";
        }
    }

}
