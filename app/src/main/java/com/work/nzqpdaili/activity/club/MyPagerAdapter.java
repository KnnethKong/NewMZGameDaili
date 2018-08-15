package com.work.nzqpdaili.activity.club;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/3/23.
 */
public class MyPagerAdapter extends FragmentStatePagerAdapter {
    List<ChengyuanFragment> fragments;

    public MyPagerAdapter(FragmentManager fm, List<ChengyuanFragment> fragments) {

        super(fm);
        this.fragments = fragments;
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
//        if (position == 0) {
//            return new ShangpinFragment();
//        } else if (position == 1) {
//            return new PingjiaFragment();
//        } else if (position == 2) {
//            return new JieshaoFragment();
//        }
//        return new ShangpinFragment();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


}
