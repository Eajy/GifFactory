package com.seven.eajy.gif.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.seven.eajy.gif.BuildConfig;
import com.seven.eajy.gif.R;

import java.io.File;

/**
 * Created by zhangxiao on 2018/9/10
 */
public class ShareUtils {

    public static final int RQ_SHARE = 1010;

    /**
     * // TODO: 2018/9/11 需要在在manifest的application节点添加provider
     * <p>
     * for example:
     * <provider
     * android:name="android.support.v4.content.FileProvider"
     * android:authorities="${applicationId}.provider"
     * android:exported="false"
     * android:grantUriPermissions="true">
     * <meta-data
     * android:name="android.support.FILE_PROVIDER_PATHS"
     * android:resource="@xml/file_paths" />
     * </provider>
     * <p>
     * // TODO: 2018/9/11 以及在file_paths中声明path
     * <p>
     * for example:
     * <paths>
     * <external-cache-path
     * name="share_file_path_1"
     * path="video" />
     * <external-files-path
     * name="share_file_path_2"
     * path="image" />
     * </paths>
     */
    public static final String FILE_PROVIDER_AUTHORITIES = BuildConfig.APPLICATION_ID + ".provider";


    /**
     * 分享文本
     *
     * @param context
     * @param content
     */
    public static void shareText(Context context, @NonNull String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);

        try {
            getActivity(context).startActivityForResult(
                    Intent.createChooser(shareIntent, context.getString(R.string.share_title)), RQ_SHARE);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.share_failed), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 分享文本到指定app
     *
     * @param context
     * @param packageName
     * @param content
     */
    public static void shareTextWithPackage(Context context, @NonNull String packageName, @NonNull String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (TextUtils.isEmpty(packageName) || !AppUtils.checkAppInstalled(context, packageName)) {
            Log.e("share", "app not installed:" + packageName);
            return;
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        shareIntent.setPackage(packageName);

        try {
            getActivity(context).startActivityForResult(
                    Intent.createChooser(shareIntent, context.getString(R.string.share_title)), RQ_SHARE);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.share_failed), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 分享文本到指定app的指定class
     *
     * @param context
     * @param packageName
     * @param className
     * @param content
     */
    public static void shareTextWithPackageAndClassName(Context context, @NonNull String packageName, @NonNull String className, @Nullable String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (TextUtils.isEmpty(packageName) || !AppUtils.checkAppInstalled(context, packageName)) {
            Log.e("share", "app not installed:" + packageName);
            return;
        }
        if (TextUtils.isEmpty(className)) {
            Log.e("share", "class name not found:" + className);
            return;
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, content != null ? content : "");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setPackage(packageName);
        shareIntent.setClassName(packageName, className);
        try {
            getActivity(context).startActivityForResult(
                    Intent.createChooser(shareIntent, context.getString(R.string.share_title)), RQ_SHARE);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.share_failed), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @param context
     * @param imagePath 本地图片文件路径
     * @param content   文本内容，有些App不一定显示
     */
    public static void shareImage(Context context, @NonNull String imagePath, @Nullable String content) {
        if (TextUtils.isEmpty(imagePath)) {
            return;
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        File file = new File(imagePath);
        if (file.exists() && file.isFile()) {
            Uri uri = getUri(context, file);
            shareIntent.setType("image/*;text/plain");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, content != null ? content : "");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            return;
        }

        try {
            getActivity(context).startActivityForResult(
                    Intent.createChooser(shareIntent, context.getString(R.string.share_title)), RQ_SHARE);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.share_failed), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 分享图片和文本到指定App
     *
     * @param context
     * @param packageName
     * @param imagePath
     * @param content
     */
    public static void shareImageWithPackage(Context context, @NonNull String packageName, @NonNull String imagePath, @Nullable String content) {
        if (TextUtils.isEmpty(imagePath)) {
            return;
        }
        if (TextUtils.isEmpty(packageName) || !AppUtils.checkAppInstalled(context, packageName)) {
            Log.e("share", "app not installed:" + packageName);
            return;
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setPackage(packageName);
        File file = new File(imagePath);
        if (file.exists() && file.isFile()) {
            Uri uri = getUri(context, file);
            shareIntent.setType("image/*;text/plain");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, content != null ? content : "");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            return;
        }

        try {
            getActivity(context).startActivityForResult(
                    Intent.createChooser(shareIntent, context.getString(R.string.share_title)), RQ_SHARE);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.share_failed), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 分享文字和 Drawable
     *
     * @param context
     * @param drawable
     * @param content
     */
    public static void shareTextAndDrawableIcon(Context context, @NonNull Drawable drawable, @Nullable String content) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain;image/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, content != null ? content : "");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        String iconPath = FileUtils.getExternalStorageFile(context, "image") + "/share_icon.png";
        File file = new File(iconPath);
        Uri uri = null;
        if (file.exists() && file.isFile()) {
            uri = getUri(context, file);
        } else {
            if (FileUtils.saveDrawableImageToFile(context, iconPath, (BitmapDrawable) drawable)) {
                file = new File(iconPath);
                uri = getUri(context, file);
            }
        }
        if (uri != null) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        }

        try {
            getActivity(context).startActivityForResult(
                    Intent.createChooser(shareIntent, context.getString(R.string.share_title)), RQ_SHARE);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.share_failed), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 分享文本和drawable到指定App
     *
     * @param context
     * @param packageName
     * @param drawable
     * @param content
     */
    public static void shareTextAndIconWithPackage(Context context, @NonNull String packageName, @NonNull Drawable drawable, @Nullable String content) {
        if (TextUtils.isEmpty(packageName) || !AppUtils.checkAppInstalled(context, packageName)) {
            Log.e("share", "app not installed:" + packageName);
            return;
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, content != null ? content : "");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setPackage(packageName);

        String iconPath = FileUtils.getExternalStorageFile(context, "image") + "/share_icon.png";
        File file = new File(iconPath);
        Uri uri = null;
        if (file.exists() && file.isFile()) {
            uri = getUri(context, file);
        } else {
            if (FileUtils.saveDrawableImageToFile(context, iconPath, (BitmapDrawable) drawable)) {
                file = new File(iconPath);
                uri = getUri(context, file);
            }
        }
        if (uri != null) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        }

        try {
            getActivity(context).startActivityForResult(
                    Intent.createChooser(shareIntent, context.getString(R.string.share_title)), RQ_SHARE);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.share_failed), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * @param context
     * @param videoPath 本地视频文件路径。 （getFileDir()路径暂不支持，7.0+需要使用file provider提供；
     *                  外部存储和getExternalFileDir()路径均可，接收分享的App需有READ_EXTERNAL_STORAGE权限）
     * @param content   文本内容，有些App不一定显示
     */
    public static void shareVideo(Context context, @NonNull String videoPath, @Nullable String content) {
        if (TextUtils.isEmpty(videoPath)) {
            Log.e("share", "videoPath:" + videoPath);
            return;
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        File file = new File(videoPath);
        if (file.exists() && file.isFile()) {
            Uri uri = getUri(context, file);
            shareIntent.setType("video/*;text/plain");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, content != null ? content : "");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            return;
        }

        try {
            getActivity(context).startActivityForResult(
                    Intent.createChooser(shareIntent, context.getString(R.string.share_title)), RQ_SHARE);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.share_failed), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 分享video到指定app
     *
     * @param context
     * @param packageName
     * @param videoPath
     * @param content
     */
    public static void shareVideoWithPackageName(Context context, @NonNull String packageName, @NonNull String videoPath, @Nullable String content) {
        if (TextUtils.isEmpty(videoPath)) {
            Log.e("share", "videoPath:" + videoPath);
            return;
        }
        if (TextUtils.isEmpty(packageName) || !AppUtils.checkAppInstalled(context, packageName)) {
            Log.e("share", "app not installed:" + packageName);
            return;
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        File file = new File(videoPath);
        if (file.exists() && file.isFile()) {
            Uri uri = getUri(context, file);
            shareIntent.setType("video/*;text/plain");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, content != null ? content : "");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.setPackage(packageName);
        } else {
            return;
        }

        try {
            getActivity(context).startActivityForResult(
                    Intent.createChooser(shareIntent, context.getString(R.string.share_title)), RQ_SHARE);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.share_failed), Toast.LENGTH_SHORT).show();
        }

    }

    private static Activity getActivity(Context context) {
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        if (context instanceof Activity) {
            return (Activity) context;
        }
        return null;
    }

    private static Uri getUri(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITIES, file);
        } else {
            return Uri.fromFile(file);
        }
    }

}
