package com.work.nzqpdaili;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.work.nzqpdaili.fragment.ClubFragment;
import com.work.nzqpdaili.fragment.HomeFragment;
import com.work.nzqpdaili.fragment.MyFragment;
import com.work.nzqpdaili.fragment.PayCardFragment;
import com.work.nzqpdaili.utils.PreferenceUtils;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.manager.SystemBarTintManager;

public class MainActivity extends BaseActivity {
    // 系统自带监听方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private static final String TAG = "MainZFTActivity";

    /**
     * 启动这个Activity的Intent
     *
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @SuppressWarnings("unused")
    private View rlBottomTabTopbar;
    private TextView tvBottomTabTitle;
    public static boolean isForeground = false;
    private View[] llBottomTabTabs;
    private ImageView[] ivBottomTabTabs;
    //    private TextView[] tvBottomTabTabs;
    public static MainActivity mainActivity;
    private static final String[] TABS = {"首页", "购卡","俱乐部", "我的"};
    private static final int[][] TAB_IMAGE_RES_IDS = {
            {R.mipmap.fot1r, R.mipmap.fot1},
            {R.mipmap.fot2, R.mipmap.fot2r},
            {R.mipmap.fot_cf, R.mipmap.fot_ct},
            {R.mipmap.fot3r, R.mipmap.fot3},
    };

    protected int currentPosition = 0;

    private Fragment[] fragments;
    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    private long firstTime = 0;//第一次返回按钮计时

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        exitAnim = R.anim.bottom_push_out;

        rlBottomTabTopbar = findViewById(R.id.rlBottomTabTopbar);
        tvBottomTabTitle = (TextView) findViewById(R.id.tvBottomTabTitle);

        llBottomTabTabs = new View[4];
        llBottomTabTabs[0] = findViewById(R.id.llBottomTabTab0);
        llBottomTabTabs[1] = findViewById(R.id.llBottomTabTab1);
        llBottomTabTabs[2] = findViewById(R.id.llBottomTabTab2);
        llBottomTabTabs[3] = findViewById(R.id.llBottomTabTab3);

        if (PreferenceUtils.getPrefString(MainActivity.this, "quyum", "").equals("顺平麻将")||PreferenceUtils.getPrefString(MainActivity.this, "quyum", "").equals("梁山棋牌")) {
            llBottomTabTabs[1].setVisibility(View.VISIBLE);
        }else {
            llBottomTabTabs[1].setVisibility(View.GONE);
        }
        if(!PreferenceUtils.getPrefString(MainActivity.this, "is_club", "").equals("1")){
            llBottomTabTabs[2].setVisibility(View.GONE);
        }
        ivBottomTabTabs = new ImageView[4];
        ivBottomTabTabs[0] = (ImageView) findViewById(R.id.ivBottomTabTab0);
        ivBottomTabTabs[1] = (ImageView) findViewById(R.id.ivBottomTabTab1);
        ivBottomTabTabs[2] = (ImageView) findViewById(R.id.ivBottomTabTab2);
        ivBottomTabTabs[3] = (ImageView) findViewById(R.id.ivBottomTabTab3);


    }

    /**
     * 获取新的Fragment
     *
     * @param position
     * @return
     */
    protected Fragment getFragment(int position) {
//        bundle = new Bundle();
        switch (position) {
            case 0:

                return new HomeFragment();
            case 1:

                return new PayCardFragment();
            case 2:
                return new ClubFragment();
            default:
                MyFragment fragment = new MyFragment();

                //bundle.putInt(UserListFragment.ARGUMENT_RANGE, UserListFragment.RANGE_ALL);
                return fragment;
        }
    }

    /**
     * 选择并显示fragment
     *
     * @param position
     */
    public void selectFragment(int position) {
        if (currentPosition == position) {
            if (fragments[position] != null && fragments[position].isVisible()) {
                Log.e(TAG, "selectFragment currentPosition == position" +
                        " >> fragments[position] != null && fragments[position].isVisible()" +
                        " >> return;	");
                return;
            }
        }

        //导致切换时闪屏，建议去掉BottomTabActivity中的topbar，在fragment中显示topbar
        //		rlBottomTabTopbar.setVisibility(position == 2 ? View.GONE : View.VISIBLE);

        tvBottomTabTitle.setText(TABS[position]);

        for (int i = 0; i < llBottomTabTabs.length; i++) {
            ivBottomTabTabs[i].setImageResource(TAB_IMAGE_RES_IDS[i][i == position ? 1 : 0]);
//            tvBottomTabTabs[i].setTextColor(getResources().getColor(i == position ? R.color.head : R.color.black_slight));
        }

        if (fragments[position] == null) {
            fragments[position] = getFragment(position);
        }
//        try {
//            if (position == 0) {
//                new Handler().postDelayed(new Runnable() {
//                    public void run() {
//                        //显示dialog
//                        ((HomeFragment) fragments[0]).updateUI();
//                    }
//                }, 200);   //2秒
//            } else if (position == 1) {
//                new Handler().postDelayed(new Runnable() {
//                    public void run() {
//                        //显示dialog
//                        ((SudyFragment) fragments[1]).updateUI();
//                    }
//                }, 200);   //2秒
//            } else if (position == 2) {
//                new Handler().postDelayed(new Runnable() {
//                    public void run() {
//                        //显示dialog
//                        ((BaShopFragment) fragments[2]).updateUI();
//                    }
//                }, 200);   //2秒
//            }else if (position == 3) {
//                new Handler().postDelayed(new Runnable() {
//                    public void run() {
//                        //显示dialog
//                        ((MyFragment) fragments[3]).updateUI();
//                    }
//                }, 200);   //2秒
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "点击切换初始化标题BUG:" + e);
//        }
        // 用全局的fragmentTransaction因为already committed 崩溃
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(fragments[currentPosition]);
        if (fragments[position].isAdded() == false) {
            fragmentTransaction.add(R.id.flBottomTabFragmentContainer, fragments[position], "Fragment" + position);
        }
        fragmentTransaction.show(fragments[position]).commit();

        this.currentPosition = position;

//            setStatusBarColor(R.color.topbar_bg);

    }

    public void setStatusBarColor(int color) {
        if (getActivity() == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            getActivity().getWindow().setStatusBarColor(getResources().getColor(color));
        } else {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //创建状态栏的管理实例
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            //激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            //设置状态栏颜色
            tintManager.setTintResource(color);
            //激活导航栏设置
            tintManager.setNavigationBarTintEnabled(true);
//				//设置导航栏颜色
//				tintManager.setNavigationBarTintResource(R.color.transparent);
        }
    }

    @Override
    public void initData() {
// fragmentActivity子界面初始化<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        fragments = new Fragment[TABS.length];
        selectFragment(currentPosition);
        // fragmentActivity子界面初始化>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    }

    @Override
    public void initEvent() {
        for (int i = 0; i < llBottomTabTabs.length; i++) {
            final int which = i;
            llBottomTabTabs[which].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    selectFragment(which);
                }
            });
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {
                    showShortToast("再按一次退出");
                    firstTime = secondTime;
                } else {//完全退出
                    moveTaskToBack(false);//应用退到后台
                    System.exit(0);
                }
                return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
