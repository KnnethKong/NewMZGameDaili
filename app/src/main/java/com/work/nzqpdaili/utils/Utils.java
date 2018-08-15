package com.work.nzqpdaili.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;

import java.text.DecimalFormat;

/**
 * Created by root on 15-3-31.
 * 工具类
 */
//TODO 语义问题
public final class Utils {



    /**
     * 获取当前屏幕密度
     */
    public static float getScreenDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;// 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Activity context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(FragmentActivity context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(FragmentActivity context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 适配图片的宽高
     *
     * @param w         计算出来的图片的宽度
     * @param picWidth  图片的元始像素宽度
     * @param picHeight 图片的元始像素高度
     * @return 计算出来的图片的高度
     */
    public static float getHeight(int w, int picWidth, int picHeight) {
        return (int) ((float) w / picWidth * picHeight);
    }
    /**
     * 保留两位小数
     * */
    public static String get2Places(double double_value) {
        try {
            DecimalFormat df = new DecimalFormat("#####0.00");
            return df.format(double_value);
        } catch (Exception ex) {
            return "0.00";
        }
    }
}