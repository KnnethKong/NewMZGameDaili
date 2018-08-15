package com.work.nzqpdaili.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class PersonalInformationActivity extends BaseActivity {
    private TextView mZhanghao;
    private TextView mName;
    private TextView mSex;
    private TextView mPhone;
    private TextView mWeix;
    private TextView mQuyu;
    private TextView mAddress;
    private TextView mBianji;
    private final int REQUESTCODE = 11;//返回的结果码
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
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
        mZhanghao = (TextView) findViewById(R.id.zhanghao);
        mName = (TextView) findViewById(R.id.name);
        mSex = (TextView) findViewById(R.id.sex);
        mPhone = (TextView) findViewById(R.id.phone);
        mWeix = (TextView) findViewById(R.id.weix);
        mQuyu = (TextView) findViewById(R.id.quyu);
        mAddress = (TextView) findViewById(R.id.address);
        mBianji = (TextView) findViewById(R.id.bianji);
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

        HttpManager.getInstance().get(list, PreferenceUtils.getPrefString(getActivity(),"quyudaili",""), 10001, new HttpManager.OnHttpResponseListener() {
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
                        mSex.setText(data.getString("a_sex"));
                        mPhone.setText(data.getString("a_mobile"));
                        mWeix.setText(data.getString("a_weixin"));
                        mQuyu.setText(data.getString("agent_addr"));
                        mAddress.setText(data.getString("a_content"));
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
        mBianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(),BianjiActivity.class),REQUESTCODE);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE && resultCode == RESULT_OK) {
            initData();
        }
    }
}
