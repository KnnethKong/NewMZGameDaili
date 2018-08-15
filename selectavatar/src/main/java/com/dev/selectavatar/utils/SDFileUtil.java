package com.dev.selectavatar.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.IOException;


@SuppressLint({"DefaultLocale", "SimpleDateFormat"})
public class SDFileUtil {
    private final static String ROOT_DIR = "SelctAvatar";

    /**
     * 获取SD图片缓存地址
     */
    public static String getPhotoCachePath() {
        String filePath = getAppSDRootPath() + "/PhotoCache/";
        File file = new File(filePath);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        file = null;
        return filePath;
    }

    /**
     * 获取到SD卡根目录
     */
    public static String getAppSDRootPath() {
        String filePath = "/" + ROOT_DIR;
        // 判断SD卡是否存在，并且是否具有读写权限
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // 获取SD卡路径
            filePath = Environment.getExternalStorageDirectory() + filePath;
        }

        File folder = new File(filePath);
        // 判断文件目录是否存在
        if (folder.exists() && folder.isDirectory()) {
            // do nothing
        } else {
            folder.mkdirs();
        }
        folder = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            File nomedia = new File(filePath + "/.nomedia");
            if (!nomedia.exists())
                try {
                    nomedia.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return filePath;
    }

    /**
     * 删除文件
     */
    public static boolean delFile(String filePath) {
        File file = new File(filePath);
        try {
            if (file.exists() && file.isFile()) {
                return file.delete();
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
