package com.work.nzqpdaili.activity.club;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airsaid.pickerviewlibrary.TimePickerView;
import com.work.nzqpdaili.R;
import com.work.nzqpdaili.utils.LogCatUtils;
import com.work.nzqpdaili.utils.PreferenceUtils;
import com.work.nzqpdaili.utils.TabLayoutUtils;
import com.work.nzqpdaili.utils.TimeUtil;
import com.work.nzqpdaili.utils.ToastUtils;
import com.work.nzqpdaili.view.TitleBarUtils;
import com.work.nzqpdaili.view.hud.SimpleHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.model.Parameter;

import static com.airsaid.pickerviewlibrary.TimePickerView.Type.MONTH_DAY_HOUR;

/**
 * Created by range on 2017/12/13.
 */

public class LishizhanjiActivity extends BaseActivity implements View.OnClickListener {

    private TabLayout tl_display;
    private ViewPager vp_display;


    public List<LishizhanjiFragment> fragments;
    private FragmentManager manager;
    MyPageradapter1 adapter1 = null;
    private LishizhanjiFragment weikaishi, weishou, yishou;
    private ImageView search;
    private TextView num, start_time, end_time;
    private TextView  ka,ka1,wanjia;
    String start = "", end = "";
    SimpleDateFormat format, format1;
    TimePickerView timePickerView;
    String type = "";

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lishizhanji);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_space);
        int color = 0xffffffff;
        linearLayout.setBackgroundColor(color);
        setImmersiveStatusBar(true, color, linearLayout);
        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
        timePickerView = new TimePickerView(this, MONTH_DAY_HOUR);
        format = new SimpleDateFormat("yyyy.MM.dd\nHH:ss");
        format1 = new SimpleDateFormat("yyyy");
        ((TextView) findViewById(R.id.tv_title)).setText("历史战绩");

        tl_display = (TabLayout) findViewById(R.id.tl_display);
        vp_display = (ViewPager) findViewById(R.id.vp_display);

        TitleBarUtils.initBtnBack(this, R.id.tv_back);
        num = (TextView) findViewById(R.id.num);
        search = (ImageView) findViewById(R.id.search);
        start_time = (TextView) findViewById(R.id.star_time);
        end_time = (TextView) findViewById(R.id.end_time);
        ka= (TextView) findViewById(R.id.ka_show);
        ka1= (TextView) findViewById(R.id.ka_show1);
        wanjia= (TextView) findViewById(R.id.wanjia);
    }

    @Override
    public void initData() {
        type = getIntent().getStringExtra("type");

        fragments = new ArrayList<>();
        if (fragments != null && fragments.size() > 0) {
            fragments.clear();
        }

        weikaishi = new LishizhanjiFragment();
        Bundle bundle0 = new Bundle();
        bundle0.putString("type", "0");
        bundle0.putString("s_type", type + "");
        weikaishi.setArguments(bundle0);
        fragments.add(weikaishi);

        weishou = new LishizhanjiFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", "1");
        bundle.putString("s_type", type + "");
        weishou.setArguments(bundle);
        fragments.add(weishou);
        if (!type.equals("1")) {
            yishou = new LishizhanjiFragment();
            Bundle bundle1 = new Bundle();
            bundle1.putString("type", "2");
            bundle1.putString("s_type", type + "");
            yishou.setArguments(bundle1);
            fragments.add(yishou);
        }
        vp_display.setOffscreenPageLimit(3);
        manager = getSupportFragmentManager();
        adapter1 = new MyPageradapter1(manager, fragments);
        vp_display.setAdapter(adapter1);

        tl_display.setupWithViewPager(vp_display);
        tl_display.getTabAt(0).setText("已开始");

        tl_display.getTabAt(1).setText("已结束");
        if (!type.equals("1")) {
            tl_display.getTabAt(1).setText("未收");
            tl_display.getTabAt(2).setText("已收");
        }
        new TabLayoutUtils().reflex(tl_display);
        tl_display.setTabMode(TabLayout.MODE_FIXED);

        httpdata(type);

    }

    @Override
    public void initEvent() {
        start_time.setOnClickListener(this);
        end_time.setOnClickListener(this);
        search.setOnClickListener(this);
        tl_display.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:
                        ka.setVisibility(View.GONE);
                        ka1.setVisibility(View.GONE);
                        // getTextView02设置权重是2
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                        wanjia.setLayoutParams(lp);

                        weikaishi.change(start, end);
                        break;
                    case 1:
                        ka.setVisibility(View.VISIBLE);
                        ka1.setVisibility(View.VISIBLE);
                        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2.3f);
                        wanjia.setLayoutParams(lp1);
                        weishou.change(start, end);
                        break;
                    default:
                        ka.setVisibility(View.VISIBLE);
                        ka1.setVisibility(View.VISIBLE);
                        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2.3f);
                        wanjia.setLayoutParams(lp2);
                        yishou.change(start, end);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search:
                if (start_time.getText().toString().equals("请选择") || end_time.getText().toString().equals("请选择")) {
                    ToastUtils.showToast(getActivity(), "请选择时间", 1000);
                    return;
                }
                if (Long.parseLong(end) < Long.parseLong(start)) {
                    ToastUtils.showToast(getActivity(), "结束时间应大于于开始时间", 2000);
                    return;
                }

                httpdata(type);
                break;
            case R.id.star_time:
                timePickerView = new TimePickerView(this, MONTH_DAY_HOUR);
                timePickerView.setRange(1900, Integer.parseInt(format1.format(new Date())));
                timePickerView.setTime(new Date());

                timePickerView.setTitle("");
                timePickerView.setCancelable(true);
                timePickerView.setCyclic(false);

                //设置默认选中的三级项目
                //监听确定选择按钮
                timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date) {
                        Log.e("result--->", TimeUtil.GetTime() + "," + date.getTime());
                        if (TimeUtil.GetTime() - date.getTime() / 1000 > 2592000) {
                            Log.e("result--->", TimeUtil.GetTime() + "," + date.getTime());
                            ToastUtils.showToast(getActivity(), "起始时间最多为当前时间的30天前", 2000);
                            return;
                        }
                        if (date.getTime() > new Date().getTime()) {
                            ToastUtils.showToast(getActivity(), "起始时间不得超过当前时间", 2000);
                            return;
                        }
                        start_time.setText(format.format(date));
                        start = date.getTime() / 1000 + "";
                    }

                });
                timePickerView.show();

                break;
            case R.id.end_time:

                timePickerView = new TimePickerView(this, MONTH_DAY_HOUR);
                timePickerView.setRange(1900, Integer.parseInt(format1.format(new Date())));
                timePickerView.setTime(new Date());
                timePickerView.setTitle("");

                timePickerView.setCancelable(true);
                timePickerView.setCyclic(false);
                //设置默认选中的三级项目
                //监听确定选择按钮
                timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date) {
                        if (TimeUtil.GetTime() - date.getTime() / 1000 > 2592000) {
                            ToastUtils.showToast(getActivity(), "结束时间最多为当前时间的30天前", 2000);
                            return;
                        }
                        if (date.getTime() > new Date().getTime()) {
                            ToastUtils.showToast(getActivity(), "结束时间不得超过当前时间", 2000);
                            return;
                        }
                        end_time.setText(format.format(date));
                        end = date.getTime() / 1000 + "";
                    }

                });
                timePickerView.show();

                break;
        }
    }

    private void httpdata(String i) {
        //SimpleHUD.showLoadingMessage(getActivity(), "加载中...", true);
        List<Parameter> list = new ArrayList<Parameter>();
        list.add(new Parameter("g", "Api"));
        list.add(new Parameter("m", "Club"));
        if (i.equals("1")) {
            list.add(new Parameter("a", "historyrecordps"));
        } else if (i.equals("2")) {
            list.add(new Parameter("a", "historyrecordgs"));
        }

        list.add(new Parameter("p", "1"));
        list.add(new Parameter("num", "1"));
        list.add(new Parameter("agent_id", PreferenceUtils.getPrefString(getActivity(), "agentid", "")));
        list.add(new Parameter("token", PreferenceUtils.getPrefString(getActivity(), "token", "")));
        list.add(new Parameter("clubid", getActivity().getIntent().getStringExtra("id")));

        list.add(new Parameter("start", start));
        list.add(new Parameter("end", end));
        HttpManager.getInstance().get(list, PreferenceUtils.getPrefString(getActivity(), "quyudaili", ""), 10001, new HttpManager.OnHttpResponseListener() {
            @Override
            public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                LogCatUtils.i("", "clublist=========" + resultData.toString());
                try {
                    //  SimpleHUD.dismiss();
                    JSONObject jsonObject = new JSONObject(resultData);
                    if (jsonObject.getString("status").equals("10001")) {
                        JSONObject result = jsonObject.getJSONObject("result");
                        JSONObject data = result.getJSONObject("data");
                        JSONArray data1 = data.getJSONArray("roomlist");
                        num.setText(data.getString("consume"));
                        weishou.change(start, end);
                        if (!type.equals("1")) {
                            yishou.change(start, end);
                        }
                        //   dataListToArrayList(array);
                    } else {
                        ToastUtils.showToast(getActivity(), jsonObject.getString("msg"), 1000);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }


            @Override
            public void onHttpRequestError(int requestCode, Exception exception) {
                LogCatUtils.i("", "exception=========" + exception.toString());
                SimpleHUD.dismiss();
            }
        });
    }


}
