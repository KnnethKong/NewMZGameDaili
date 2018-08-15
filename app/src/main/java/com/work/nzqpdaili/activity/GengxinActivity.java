package com.work.nzqpdaili.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.work.nzqpdaili.R;

import zuo.biao.library.base.BaseActivity;

/**
 * Created by range on 2017/12/18.
 */

public class GengxinActivity extends BaseActivity {
    private Button quxiao,queren;
    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gengxin);
        initView();
        initData();
        initEvent();
    }
    @Override
    public void initView() {
      queren= (Button) findViewById(R.id.btnAlertDialogPositive);
        quxiao= (Button) findViewById(R.id.btnAlertDialogNegative);
    }

    @Override
    public void initData() {
    if(getIntent().getStringExtra("is_update").equals("1")){
        quxiao.setVisibility(View.GONE);
    }else {
        quxiao.setVisibility(View.VISIBLE);
    }
    }

    @Override
    public void initEvent() {
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(getIntent().getStringExtra("url"));
                intent.setData(content_url);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);

    }

}
