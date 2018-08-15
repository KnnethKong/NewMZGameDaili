package com.work.nzqpdaili.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferenceUtils {
	/**
	 * 设置String数据
	 * */
	public static void setPrefString(Context context, final String key,
									 final String value) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		settings.edit().putString(key, value).commit();
	}

	/**
	 * 获取String数据
	 * */
	public static String getPrefString(Context context, String key,
									   final String defaultValue) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getString(key, defaultValue);
	}

	/**
	 * 设置Boolean数据
	 * */
	public static void setPrefBoolean(Context context, final String key,
									  final boolean value) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		settings.edit().putBoolean(key, value).commit();
	}

	/**
	 * 获取Boolean数据
	 * */
	public static boolean getPrefBoolean(Context context, final String key,
										 final boolean defaultValue) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getBoolean(key, defaultValue);
	}

	/**
	 * 设置Int数据
	 * */
	public static void setPrefInt(Context context, final String key,
								  final int value) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		settings.edit().putInt(key, value).commit();
	}

	/**
	 * 获取Int数据
	 * */
	public static int getPrefInt(Context context, final String key,
								 final int defaultValue) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getInt(key, defaultValue);
	}

	/**
	 * 设置Float数据
	 * */
	public static void setPrefFloat(Context context, final String key,
									final float value) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		settings.edit().putFloat(key, value).commit();
	}

	/**
	 * 获取Float数据
	 * */
	public static float getPrefFloat(Context context, final String key,
									 final float defaultValue) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getFloat(key, defaultValue);
	}

	/**
	 * 设置Long数据
	 * */
	public static void setPrefLong(Context context, final String key,
								   final long value) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		settings.edit().putLong(key, value).commit();
	}

	/**
	 * 获取Long数据
	 * */
	public static long getPrefLong(Context context, final String key,
								   final long defaultValue) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getLong(key, defaultValue);
	}

	/**
	 * 是否存在Key
	 * */
	public static boolean hasKey(Context context, final String key) {
		return PreferenceManager.getDefaultSharedPreferences(context).contains(
				key);
	}

	/**
	 * 清理SharedPreferences缓存
	 * */
	public static void clearPreference(Context context,
			final SharedPreferences p) {
		final Editor editor = p.edit();
		editor.clear();
		editor.commit();
	}
}
