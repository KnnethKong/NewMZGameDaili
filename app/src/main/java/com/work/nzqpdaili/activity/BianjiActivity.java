package com.work.nzqpdaili.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airsaid.pickerviewlibrary.CityPickerView;
import com.airsaid.pickerviewlibrary.listener.OnSimpleCitySelectListener;
import com.work.nzqpdaili.R;
import com.work.nzqpdaili.utils.LogCatUtils;
import com.work.nzqpdaili.utils.PreferenceUtils;
import com.work.nzqpdaili.utils.ToastUtils;
import com.work.nzqpdaili.view.TitleBarUtils;
import com.work.nzqpdaili.view.hud.SimpleHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.model.Parameter;

public class BianjiActivity extends BaseActivity implements View.OnClickListener{
    private EditText mZhanghao;
    private EditText mName;
    private LinearLayout mNan_la;
    private CheckBox mRegister_check;
    private LinearLayout mNv_la;
    private CheckBox mRegister_check1;
    private EditText mPhone;
    private EditText mWeixin;
    private LinearLayout mQuyu;
    private TextView mQuyu_tv;
    private EditText mAddress;
    private TextView mChongzhi;
    private CityPickerView mCityPickerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bianji);
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
        ((TextView) findViewById(R.id.tv_title)).setText("个人信息");
        TitleBarUtils.initBtnBack(this, R.id.tv_back);
        mZhanghao = (EditText) findViewById(R.id.zhanghao);
        mName = (EditText) findViewById(R.id.name);
        mNan_la = (LinearLayout) findViewById(R.id.nan_la);
        mRegister_check = (CheckBox) findViewById(R.id.register_check);
        mNv_la = (LinearLayout) findViewById(R.id.nv_la);
        mRegister_check1 = (CheckBox) findViewById(R.id.register_check1);
        mPhone = (EditText) findViewById(R.id.phone);
        mWeixin = (EditText) findViewById(R.id.weixin);
        mQuyu = (LinearLayout) findViewById(R.id.quyu);
        mQuyu_tv = (TextView) findViewById(R.id.quyu_tv);
        mAddress = (EditText) findViewById(R.id.address);
        mChongzhi = (TextView) findViewById(R.id.chongzhi);
        mCityPickerView = new CityPickerView(this);
    }

    @Override
    public void initData() {
        SimpleHUD.showLoadingMessage(getActivity(), "加载中...", true);
        List<Parameter> list = new ArrayList<Parameter>();
        list.add(new Parameter("g", "Api"));
        list.add(new Parameter("m", "Profile"));
        list.add(new Parameter("a", "look"));
        list.add(new Parameter("agent_id", PreferenceUtils.getPrefString(getActivity(),"agentid","")));
        list.add(new Parameter("token",PreferenceUtils.getPrefString(getActivity(),"token","")));

        HttpManager.getInstance().get(list,PreferenceUtils.getPrefString(getActivity(),"quyudaili",""), 10001, new HttpManager.OnHttpResponseListener() {
            @Override
            public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                LogCatUtils.i("", "index=========" + resultData.toString());
                try {
                    SimpleHUD.dismiss();
                    JSONObject jsonObject = new JSONObject(resultData);
                    if (jsonObject.getString("status").equals("10001")) {
                        JSONObject result = jsonObject.getJSONObject("result");
                        JSONObject data = result.getJSONObject("data");
                        mName.setText(data.getString("a_realname"));
                        mZhanghao.setText(data.getString("a_name"));
                        mPhone.setText(data.getString("a_mobile"));
                        mWeixin.setText(data.getString("a_weixin"));
                        mQuyu_tv.setText(data.getString("agent_addr"));
                        mAddress.setText(data.getString("a_content"));
                        if ("男        ".equals(data.getString("a_sex"))){
                            mRegister_check.setChecked(true);
                            mRegister_check1.setChecked(false);
                        }else {
                            mRegister_check.setChecked(false);
                            mRegister_check1.setChecked(true);
                        }
//                        ImageLoaderUtils.displayImage(data.getString("img"),mMine_head,R.mipmap.fimg5,10);
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

    @Override
    public void initEvent() {
        mQuyu.setOnClickListener(this);
        mNan_la.setOnClickListener(this);
        mNv_la.setOnClickListener(this);
        mRegister_check.setOnClickListener(this);
        mRegister_check1.setOnClickListener(this);
        mChongzhi.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.quyu:
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                selectCity(mQuyu);
                break;
            case R.id.nan_la:
                mRegister_check.setChecked(true);
                mRegister_check1.setChecked(false);
                break;
            case R.id.register_check:
                mRegister_check.setChecked(true);
                mRegister_check1.setChecked(false);
                break;
            case R.id.nv_la:
                mRegister_check.setChecked(false);
                mRegister_check1.setChecked(true);
                break;
            case R.id.register_check1:
                mRegister_check.setChecked(false);
                mRegister_check1.setChecked(true);
                break;
            case R.id.chongzhi:
                List<Parameter> list = new ArrayList<Parameter>();
                list.add(new Parameter("g", "Api"));
                list.add(new Parameter("m", "Profile"));
                list.add(new Parameter("a", "edit"));
                list.add(new Parameter("a_name",mZhanghao.getText().toString().trim()));
                if (mRegister_check.isChecked()){
                    list.add(new Parameter("a_sex","男"));
                }else list.add(new Parameter("num","女"));
                list.add(new Parameter("a_realname",mName.getText().toString().trim()));
                list.add(new Parameter("a_mobile",mPhone.getText().toString().trim()));
                list.add(new Parameter("a_weixin",mWeixin.getText().toString().trim()));
                list.add(new Parameter("agent_addr",mQuyu_tv.getText().toString().trim()));
                list.add(new Parameter("a_content",mAddress.getText().toString().trim()));
                list.add(new Parameter("agent_id",PreferenceUtils.getPrefString(getActivity(),"agentid","")));
                list.add(new Parameter("token",PreferenceUtils.getPrefString(getActivity(),"token","")));

                HttpManager.getInstance().get(list,PreferenceUtils.getPrefString(getActivity(),"quyudaili",""), 10001, new HttpManager.OnHttpResponseListener() {
                    @Override
                    public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                        LogCatUtils.i("", "chagecard=========" + resultData.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(resultData);
                            if (jsonObject.getString("status").equals("10001")) {
                                ToastUtils.showToast(getActivity(), "编辑成功", 1000);
                                Intent intent = new Intent();
                                setResult(RESULT_OK,intent);
                                finish();
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
                break;

        }
    }
    public void selectCity(View v){
        // 设置点击外部是否消失
//        mCityPickerView.setCancelable(true);
        // 设置滚轮字体大小
//        mCityPickerView.setTextSize(18f);
        // 设置标题
//        mCityPickerView.setTitle("我是标题");
        // 设置取消文字
//        mCityPickerView.setCancelText("我是取消文字");
        // 设置取消文字颜色
//        mCityPickerView.setCancelTextColor(Color.GRAY);
        // 设置取消文字大小
//        mCityPickerView.setCancelTextSize(14f);
        // 设置确定文字
//        mCityPickerView.setSubmitText("我是确定文字");
        // 设置确定文字颜色
//        mCityPickerView.setSubmitTextColor(Color.BLACK);
        // 设置确定文字大小
//        mCityPickerView.setSubmitTextSize(14f);
        // 设置头部背景
//        mCityPickerView.setHeadBackgroundColor(Color.RED);
        mCityPickerView.setOnCitySelectListener(new OnSimpleCitySelectListener(){
            @Override
            public void onCitySelect(String prov, String city, String area) {
                // 省、市、区 分开获取
//                Log.e(TAG, "省: " + prov + " 市: " + city + " 区: " + area);
//                mDizhi.setText(prov+"省、" + city + "市、" + area );
                mQuyu_tv.setText(prov+"" + city + "" + area );
            }

            @Override
            public void onCitySelect(String str) {
                // 一起获取
//                Toast.makeText(AddAddressActivity.this, "选择了：" + str, Toast.LENGTH_SHORT).show();
            }
        });
        mCityPickerView.show();
    }
}
