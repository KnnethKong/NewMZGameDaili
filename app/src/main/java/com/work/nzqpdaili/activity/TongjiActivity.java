package com.work.nzqpdaili.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.work.nzqpdaili.R;
import com.work.nzqpdaili.utils.LogCatUtils;
import com.work.nzqpdaili.utils.PreferenceUtils;
import com.work.nzqpdaili.utils.ToastUtils;
import com.work.nzqpdaili.view.TitleBarUtils;
import com.work.nzqpdaili.view.hud.SimpleHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.model.Parameter;

public class TongjiActivity extends BaseActivity {
    private LinearLayout mStar_timela;
    private TextView mStar_time;
    private LinearLayout mEnd_timela;
    private TextView mEnd_time;
    private TextView mChaxunok;
    private TextView mMay_shouka;
    private TextView mMy_gouka;
    private TextView mXiajishou;
    private TextView mXiajigou;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongji);
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
        ((TextView) findViewById(R.id.tv_title)).setText("统计");
        TitleBarUtils.initBtnBack(this, R.id.tv_back);
        mStar_timela = (LinearLayout) findViewById(R.id.star_timela);
        mStar_time = (TextView) findViewById(R.id.star_time);
        mEnd_timela = (LinearLayout) findViewById(R.id.end_timela);
        mEnd_time = (TextView) findViewById(R.id.end_time);
        mChaxunok = (TextView) findViewById(R.id.chaxunok);
        mMay_shouka = (TextView) findViewById(R.id.may_shouka);
        mMy_gouka = (TextView) findViewById(R.id.my_gouka);
        mXiajishou = (TextView) findViewById(R.id.xiajishou);
        mXiajigou = (TextView) findViewById(R.id.xiajigou);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        mChaxunok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mStar_time.getText().toString())|| TextUtils.isEmpty(mEnd_time.getText().toString())){
                    ToastUtils.showToast(getActivity(), "请选择开始时间或结束时间", 1000);
                    return;
                }
                List<Parameter> list = new ArrayList<Parameter>();
                list.add(new Parameter("g", "Api"));
                list.add(new Parameter("m", "Index"));
                list.add(new Parameter("a", "census"));
                list.add(new Parameter("start",mStar_time.getText().toString().trim()));
                list.add(new Parameter("end",mEnd_time.getText().toString().trim()));

                list.add(new Parameter("agent_id", PreferenceUtils.getPrefString(getActivity(),"agentid","")));
                list.add(new Parameter("token",PreferenceUtils.getPrefString(getActivity(),"token","")));

                HttpManager.getInstance().get(list,PreferenceUtils.getPrefString(getActivity(),"quyudaili",""), 10001, new HttpManager.OnHttpResponseListener() {
                    @Override
                    public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                        LogCatUtils.i("", "chagecard=========" + resultData.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(resultData);
                            if (jsonObject.getString("status").equals("10001")) {
                                JSONObject result = jsonObject.getJSONObject("result");
                                JSONObject data = result.getJSONObject("data");
                                mMay_shouka.setText(data.getString("mysal"));
                                mMy_gouka.setText(data.getString("myact"));
                                mXiajishou.setText(data.getString("nextsal"));
                                mXiajigou.setText(data.getString("nextact"));
                            }else {
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
        });
        mStar_timela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar ca = Calendar.getInstance();
                final int yeara = ca.get(Calendar.YEAR);//获取年份
                final int month1 = ca.get(Calendar.MONTH);//获取月份
                final int day1 = ca.get(Calendar.DATE);//获取年份
                SimpleDateFormat shijian = new SimpleDateFormat("yyyy-MM-dd");
                String date = shijian.format(new Date());
                DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(TongjiActivity.this, new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                        mStar_time.setText(dateDesc);
                    }
                }).textConfirm("CONFIRM") //text of confirm button
                        .textCancel("CANCEL") //text of cancel button
                        .btnTextSize(16) // button text size
                        .viewTextSize(25) // pick view text size
                        .colorCancel(Color.parseColor("#999999")) //color of cancel button
                        .colorConfirm(Color.parseColor("#009900"))//color of confirm button
                        .minYear(yeara) //min year in loop
                        .maxYear(2550) // max year in loop
                        .dateChose(date) // date chose when init popwindow
                        .build();
                pickerPopWin.showPopWin(TongjiActivity.this);
            }
        });
        mEnd_timela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar ca = Calendar.getInstance();
                final int yeara = ca.get(Calendar.YEAR);//获取年份
                final int month1 = ca.get(Calendar.MONTH);//获取月份
                final int day1 = ca.get(Calendar.DATE);//获取年份
                SimpleDateFormat shijian = new SimpleDateFormat("yyyy-MM-dd");
                String date = shijian.format(new Date());
                DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(TongjiActivity.this, new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                        mEnd_time.setText(dateDesc);
                    }
                }).textConfirm("CONFIRM") //text of confirm button
                        .textCancel("CANCEL") //text of cancel button
                        .btnTextSize(16) // button text size
                        .viewTextSize(25) // pick view text size
                        .colorCancel(Color.parseColor("#999999")) //color of cancel button
                        .colorConfirm(Color.parseColor("#009900"))//color of confirm button
                        .minYear(yeara) //min year in loop
                        .maxYear(2550) // max year in loop
                        .dateChose(date) // date chose when init popwindow
                        .build();
                pickerPopWin.showPopWin(TongjiActivity.this);
            }
        });
    }
}
