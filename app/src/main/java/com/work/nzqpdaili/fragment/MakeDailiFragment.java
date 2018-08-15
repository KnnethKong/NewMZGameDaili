package com.work.nzqpdaili.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airsaid.pickerviewlibrary.CityPickerView;
import com.airsaid.pickerviewlibrary.listener.OnSimpleCitySelectListener;
import com.work.nzqpdaili.R;
import com.work.nzqpdaili.activity.DlManageActivity;
import com.work.nzqpdaili.activity.DlManageActivity2;
import com.work.nzqpdaili.utils.LogCatUtils;
import com.work.nzqpdaili.utils.PreferenceUtils;
import com.work.nzqpdaili.utils.ToastUtils;
import com.work.nzqpdaili.utils.VerifyUtils;
import com.work.nzqpdaili.view.hud.SimpleHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.model.Parameter;

/**
 * Created by wj on 2017/5/27.
 */

public class MakeDailiFragment extends BaseFragment implements View.OnClickListener{
    private EditText mZhanghao;
    private EditText mPassword;
    private EditText mCpassword;
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
    private EditText gemeid;
    private TextView mChongzhi;
    private CityPickerView mCityPickerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_make);

        argument = getArguments();
        if (argument != null) {
        }

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        return view;
    }
    @Override
    public void initView() {
        mZhanghao = (EditText) findViewById(R.id.zhanghao);
        gemeid = (EditText) findViewById(R.id.gemeid);
        mPassword = (EditText) findViewById(R.id.password);
        mCpassword = (EditText) findViewById(R.id.cpassword);
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
        mCityPickerView = new CityPickerView(getActivity());
    }

    @Override
    public void initData() {

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
                if (!VerifyUtils.verifyInputIsNull(gemeid)||!VerifyUtils.verifyInputIsNull(mZhanghao)||!VerifyUtils.verifyInputIsNull(mPassword)||!VerifyUtils.verifyInputIsNull(mCpassword)||
                        !VerifyUtils.verifyInputIsNull(mName)||!VerifyUtils.verifyInputIsNull(mPhone)||
                        !VerifyUtils.verifyInputIsNull(mWeixin)||!VerifyUtils.verifyInputIsNull(mZhanghao)||
                        !VerifyUtils.verifyInputIsNull(mAddress)|| TextUtils.isEmpty(mQuyu_tv.getText().toString())){
                    ToastUtils.showToast(getActivity(), "请将信息填写完整", 1000);
                    return;
                }
                List<Parameter> list = new ArrayList<Parameter>();
                list.add(new Parameter("g", "Api"));
                list.add(new Parameter("m", "Agent"));
                list.add(new Parameter("a", "agentadd"));
                list.add(new Parameter("a_name",mZhanghao.getText().toString().trim()));
                list.add(new Parameter("user_id",gemeid.getText().toString().trim()));
                list.add(new Parameter("a_password",mPassword.getText().toString().trim()));
                list.add(new Parameter("real_password",mCpassword.getText().toString().trim()));
                if (mRegister_check.isChecked()){
                    list.add(new Parameter("a_sex","男"));
                }else list.add(new Parameter("num","女"));
                list.add(new Parameter("a_realname",mName.getText().toString().trim()));
                list.add(new Parameter("a_mobile",mPhone.getText().toString().trim()));
                list.add(new Parameter("a_weixin",mWeixin.getText().toString().trim()));
                list.add(new Parameter("agent_addr",mQuyu_tv.getText().toString().trim()));
                list.add(new Parameter("a_content",mAddress.getText().toString().trim()));
                list.add(new Parameter("is_club", PreferenceUtils.getPrefString(getActivity(),"is_club","")));
                list.add(new Parameter("agent_id", PreferenceUtils.getPrefString(getActivity(),"agentid","")));
                list.add(new Parameter("token",PreferenceUtils.getPrefString(getActivity(),"token","")));

                HttpManager.getInstance().get(list, PreferenceUtils.getPrefString(getActivity(),"quyudaili",""), 10001, new HttpManager.OnHttpResponseListener() {
                    @Override
                    public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                        LogCatUtils.i("", "chagecard=========" + resultData.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(resultData);
                            if (jsonObject.getString("status").equals("10001")) {
                                ToastUtils.showToast(getActivity(), "创建成功", 1000);
                                if ("2".equals(getArguments().getString("type")))
                                startActivity(new Intent(getActivity(), DlManageActivity.class));
                                else   startActivity(new Intent(getActivity(), DlManageActivity2.class));
                                getActivity().finish();
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
