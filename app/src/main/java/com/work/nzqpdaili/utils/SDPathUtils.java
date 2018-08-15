package com.work.nzqpdaili.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


@SuppressLint({ "DefaultLocale", "SimpleDateFormat" })
public class SDPathUtils {

	private static Context mAppContext;

	public static void setAppContext(Context context) {
		mAppContext = context;
	}

	public static Context getAppContext() {
		return mAppContext;
	}

	/**
	 * 保存图片
	 * */
	public static void saveBitmap(Bitmap bm, String imgName) {
		LogCatUtils.e("保存图片::", "保存图片::开始");
		try {
			File f = new File(getCachePath(), imgName);
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			LogCatUtils.e("保存图片::", "保存图片::已经保存");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存图片
	 * */
	public static void saveBitmap2(Bitmap bm, String imgName) {
		LogCatUtils.e("保存图片::", "保存图片::开始");
		try {
			File f = new File(Environment.getExternalStorageDirectory()+"/DCIM/Camera/", imgName);
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			LogCatUtils.e("保存图片::", "保存图片::已经保存");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取SD卡地址
	 */
	public static String getCrashPath() {
		String filePath = getAppRootPath() + "/crash/";
		File file = new File(filePath);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		file = null;
		return filePath;
	}

	/**
	 * 获取SD图片缓存地址
	 */
	public static String getCachePath() {
		String filePath = getAppRootPath() + "/cache/";
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
	public static String getAppRootPath() {
		String filePath = "/DDRCrash";
		// 判断SD卡是否存在，并且是否具有读写权限
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// 获取SD卡路径
			filePath = Environment.getExternalStorageDirectory() + filePath;
		} else {
			// 获取手机路径
			filePath = getAppContext().getCacheDir() + filePath;
		}

		File folder = new File(filePath);
		// 判断文件目录是否存在
		if (folder.exists() && folder.isDirectory()) {
			// do nothing
		} else {
			folder.mkdirs();
		}
		folder = null;

//		File nomedia = new File(filePath + "/.nomedia");
//		if (!nomedia.exists())
//			try {
//				nomedia.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		return filePath;
	}

	/**
	 * 删除文件
	 * */
	public static boolean delFile(String filePath) {
		File file = new File(filePath);
		try {
			if (file.exists() && file.isFile()) {
				LogCatUtils.i("DelFile文件删除", "文件删除成功");
				return file.delete();
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
}
