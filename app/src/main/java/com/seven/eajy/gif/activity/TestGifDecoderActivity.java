package com.seven.eajy.gif.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.seven.eajy.gif.GifJniTest;
import com.seven.eajy.gif.R;
import com.seven.eajy.gif.ThreadPool;

import java.io.File;

/**
 * 测试解码出gif的每张图片,解码的操作在{@link TestGifDecoderActivity#test()}里
 * 具体什么时候解码完成，看log
 *
 */
public class TestGifDecoderActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE_READ = 100;
    private static final String gifPath = Environment.getExternalStorageDirectory().getPath()+"/test_gif.gif";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_gif_decoder);
        findViewById(R.id.btn_gif).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });
        if (isNeedRead()) {
            requestRead();
        }
        //每次开始之前，先把上次解码的图片删除
        ThreadPool.addTask(new Runnable() {
            @Override
            public void run() {
                File giffile = new File(GifJniTest.rootPath);
                if (!giffile.exists()) {
                    giffile.mkdirs();
                } else {
                    clearFile(giffile);
                }
                Log.i("print","文件处理结束！");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean isNeedRead() {
        return  PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private void requestRead() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, permissions, REQUEST_WRITE_READ);
    }

    private void test() {
        ThreadPool.addTask(new Runnable() {
            @Override
            public void run() {
                File file = new File(gifPath);
                if (!file.exists()) {
                    Log.i("print","gifPath not exists : "+gifPath);
                }
                GifJniTest.initGifLib(gifPath);
                Log.i("print","initGifLib end");
            }
        });
    }
    private void clearFile(File file) {
        if (file.exists() && file.isDirectory()) {
            File[] fs = file.listFiles();
            for (File f : fs) {
                f.delete();
            }

        }
    }
}
