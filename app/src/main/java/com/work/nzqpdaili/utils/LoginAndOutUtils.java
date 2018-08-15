package com.work.nzqpdaili.utils;

import android.content.Context;

/**
 * Created by Administrator on 2016/11/22.
 */

public class LoginAndOutUtils {


    /**
     * 清除用户缓存
     */
    public void logoutClearCache(Context context) {
        PreferenceUtils.setPrefString(context, "token", "");
        PreferenceUtils.setPrefString(context, "agentid", "");

    }

}
