package com.work.nzqpdaili.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;


import com.work.nzqpdaili.MainActivity;
import com.work.nzqpdaili.R;
import com.work.nzqpdaili.utils.AppInfoUtils;
import com.work.nzqpdaili.utils.PreferenceUtils;

import zuo.biao.library.base.BaseNoStatusBarActivity;


/**
 * Created by zhang on 2016/9/30.
 */

public class SplashActivity extends BaseNoStatusBarActivity {

    private static final int sleepTime = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();
        initData();
        initEvent();
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Thread(new Runnable() {
            public void run() {
                if (false) {
                    // auto login mode, make sure all group and conversation is loaed before enter the main screen
                    long start = System.currentTimeMillis();
                    long costTime = System.currentTimeMillis() - start;
                    //wait
                    if (sleepTime - costTime > 0) {
                        try {
                            Thread.sleep(sleepTime - costTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //enter main screen
                    IntentAct();
                    finish();
                } else {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                    IntentAct();
                    finish();
                }
            }
        }).start();

    }

    @NonNull
    @Override
    public BaseNoStatusBarActivity getActivity() {
        return this;
    }

    @Override
    public void initView() {
//        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.ll_splash);
//        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
//        animation.setDuration(1500);
//        rootLayout.startAnimation(animation);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    private void IntentAct() {

        // 判断是否APP第一次启动
        int versionCode = PreferenceUtils.getPrefInt(this, "versionCode", -1);
        if (versionCode == -1) {

            // 跳转教程页(用户第一次安装)
            PreferenceUtils.setPrefInt(this, "versionCode", AppInfoUtils.getVersionCode(this));
            startActivity(new Intent(getActivity(),LoginActivity.class));
//            startActivity(YindaoActivity.createIntent(SplashActivity.this));


        }
        // 跳转主页(用户已经安装)
        else if (AppInfoUtils.getVersionCode(this) == versionCode) {
            // 安装APP版本号相同，直接跳转主页面
            if (TextUtils.isEmpty(PreferenceUtils.getPrefString(this,"token",""))) startActivity(new Intent(getActivity(),LoginActivity.class));
            else {

                    startActivity(MainActivity.createIntent(SplashActivity.this));
            }

//            startActivity(LoginActivity.createIntent(SplashActivity.this));

        }
        // 跳转教程页(用户已经安装，但是版本已经升级，也跳转教程页)
        else if (AppInfoUtils.getVersionCode(this) > versionCode) {
            // 重新存入数据
            PreferenceUtils.setPrefInt(this, "versionCode", AppInfoUtils.getVersionCode(this));
//            startActivity(LogingActivity.createIntent(SplashActivity.this));
            startActivity(new Intent(getActivity(),MainActivity.class));
//            startActivity(YindaoActivity.createIntent(SplashActivity.this));
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade, R.anim.hold);
    }
}
