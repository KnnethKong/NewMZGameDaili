package com.work.nzqpdaili.activity.club;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.work.nzqpdaili.R;
import com.work.nzqpdaili.utils.TabLayoutUtils;
import com.work.nzqpdaili.view.TitleBarUtils;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.util.CommonUtil;

/**
 * Created by range on 2017/12/13.
 */

public class ChengyuanActivity extends BaseActivity implements View.OnClickListener {
    private TabLayout tl_display;
    private ViewPager vp_display;
    public List<ChengyuanFragment> fragments;
    private FragmentManager manager;
    MyPagerAdapter adapter = null;
    private ChengyuanFragment chengyuan,jiaru,tuichu;
    private ImageView join;
    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_chengyuan);
        LinearLayout linearLayout= (LinearLayout) findViewById(R.id.ll_space);
        int color = 0xffffffff;
        linearLayout.setBackgroundColor(color);
        setImmersiveStatusBar(true, color,linearLayout);
        initView();
        initData();
        initEvent();
    }
    @Override
    public void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("成员管理");
        TitleBarUtils.initBtnBack(this, R.id.tv_back);
        tl_display= (TabLayout) findViewById(R.id.tl_display);
        vp_display= (ViewPager) findViewById(R.id.vp_display);
        join= (ImageView) findViewById(R.id.jion);
    }

    @Override
    public void initData() {
        fragments = new ArrayList<>();
        if (fragments != null && fragments.size() > 0) {
            fragments.clear();
        }
        chengyuan  = new ChengyuanFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", "1");
        chengyuan.setArguments(bundle);
        fragments.add(chengyuan);
        jiaru = new ChengyuanFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("type", "2");
        jiaru.setArguments(bundle1);
        fragments.add(jiaru);
        tuichu = new ChengyuanFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("type", "3");
        tuichu.setArguments(bundle2);
        fragments.add(tuichu);


        vp_display.setOffscreenPageLimit(3);
        manager=getSupportFragmentManager();
        adapter = new MyPagerAdapter(manager, fragments);
        vp_display.setAdapter(adapter);
        tl_display.setupWithViewPager(vp_display);

        tl_display.getTabAt(0).setText("成员");
        tl_display.getTabAt(1).setText("加入申请");
        tl_display.getTabAt(2).setText("退出申请");
        new TabLayoutUtils().reflex(tl_display);
        tl_display.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void initEvent() {
        join.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.jion:
                CommonUtil.toActivity(getActivity(),new Intent(getActivity(),ChengyuanYQActivity.class)
                        .putExtra("id",getIntent().getStringExtra("id")),true);
                break;
        }
    }
}
