package com.work.nzqpdaili.activity;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.work.nzqpdaili.R;
import com.work.nzqpdaili.fragment.Dali2jifragment;
import com.work.nzqpdaili.fragment.MakeDailiFragment;
import com.work.nzqpdaili.view.TitleBarUtils;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseActivity;

public class DlManageActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout mLl_doing;
    private TextView mTv_xiaof;
    private ImageView mIv_xiaof;
    private LinearLayout jinghua;
    private TextView jinghua_tv;
    private ImageView jinghua_im;
    private ViewPager mVp_order;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private Dali2jifragment zixunFragment1;
    private MakeDailiFragment zixunFragment2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dl_manage);
        LinearLayout linearLayout= (LinearLayout) findViewById(R.id.ll_space);
        int color = 0xffffffff;
        linearLayout.setBackgroundColor(color);
        setImmersiveStatusBar(true, color,linearLayout);
        initView();
        initData();
        initEvent();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("代理管理");
        TitleBarUtils.initBtnBack(this, R.id.tv_back);

        mLl_doing = (LinearLayout) findViewById(R.id.ll_doing);
        mTv_xiaof = (TextView) findViewById(R.id.tv_xiaof);
        mIv_xiaof = (ImageView) findViewById(R.id.iv_xiaof);
        jinghua = (LinearLayout) findViewById(R.id.jinghua);
        jinghua_tv = (TextView) findViewById(R.id.jinghua_tv);
        jinghua_im = (ImageView) findViewById(R.id.jinghua_im);
        mVp_order = (ViewPager) findViewById(R.id.vp_order);
        Bundle bundle = new Bundle();
        bundle.putString("type","1");
        zixunFragment1 = new Dali2jifragment();
        fragments.add(zixunFragment1);
        zixunFragment2 = new MakeDailiFragment();
        zixunFragment2.setArguments(bundle);
        fragments.add(zixunFragment2);
        mVp_order.setAdapter(new DlManageActivity.mFragmentAdapter(getSupportFragmentManager()));
//        mVp_order.setCurrentItem(0);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        mLl_doing.setOnClickListener(this);
        jinghua.setOnClickListener(this);
        mVp_order.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    hideView();
                    jinghua_tv.setTextColor(Color.parseColor("#E73A3D"));
                    jinghua_im.setVisibility(View.VISIBLE);
                }else if (position == 1) {
                    hideView();
                    mTv_xiaof.setTextColor(Color.parseColor("#E73A3D"));
                    mIv_xiaof.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void hideView() {
        mTv_xiaof.setTextColor(Color.parseColor("#000000"));
        jinghua_tv.setTextColor(Color.parseColor("#000000"));

        mIv_xiaof.setVisibility(View.INVISIBLE);
        jinghua_im.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.jinghua:
                mVp_order.setCurrentItem(0);
                break;
            case R.id.ll_doing:
                mVp_order.setCurrentItem(1);
                break;

        }
    }

    class mFragmentAdapter extends FragmentPagerAdapter {

        public mFragmentAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub
            return fragments.get(arg0);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return fragments.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
        }
    }
}
