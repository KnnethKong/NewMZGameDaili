package com.work.nzqpdaili.Application;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


/**
 * Created by wj on 2017/3/15.
 */

public class application extends Application{
    public static Context applicationContext;
    private static application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(getApplicationContext());
//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(this);
//        PreferenceUtils.setPrefString(this, "registration_id",  JPushInterface.getRegistrationID(this));
//        UMShareAPI.get(this);
//        PlatformConfig.setWeixin("wxcdc9c837afd7f94f", "45a9ada8be3088f774909072865d6d0c");
//        PlatformConfig.setQQZone("1106106978", "xYcltTcGWSiZvnDB");
    }
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
}
