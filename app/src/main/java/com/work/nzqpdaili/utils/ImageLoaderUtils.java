package com.work.nzqpdaili.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 2016/11/17.
 */

public class ImageLoaderUtils {
    public static void displayImage(String imageUrl, ImageView view, int emptyImgId, int RoundNum) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(emptyImgId)
                .showImageForEmptyUri(emptyImgId).showImageOnFail(emptyImgId)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().displayImage(imageUrl, view, options);
    }
}
