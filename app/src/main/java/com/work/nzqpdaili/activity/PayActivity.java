package com.work.nzqpdaili.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
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

public class PayActivity extends BaseActivity {
    private TextView mName;
    private TextView mMiaos;
    private TextView mMoney_all;
    private TextView mPay_money;
    private CheckBox mRegister_check1;
    private TextView mButton_ok;
    private IWXAPI api;
    public static PayActivity payActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        LinearLayout linearLayout= (LinearLayout) findViewById(R.id.ll_space);
        int color = 0xffffffff;
        linearLayout.setBackgroundColor(color);
        setImmersiveStatusBar(true, color,linearLayout);
        payActivity = this;
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
        api = WXAPIFactory.createWXAPI(this, "wx9050fc6198a0c9d6");
        api.registerApp("wx9050fc6198a0c9d6");
        ((TextView) findViewById(R.id.tv_title)).setText("购卡详情");
        TitleBarUtils.initBtnBack(this, R.id.tv_back);
        mName = (TextView) findViewById(R.id.name);
        mMiaos = (TextView) findViewById(R.id.miaos);
        mMoney_all = (TextView) findViewById(R.id.money_all);
        mPay_money = (TextView) findViewById(R.id.pay_money);
        mRegister_check1 = (CheckBox) findViewById(R.id.register_check1);
        mButton_ok = (TextView) findViewById(R.id.button_ok);
        mRegister_check1.setEnabled(false);

    }

    @Override
    public void initData() {
        SimpleHUD.showLoadingMessage(getActivity(), "加载中...", true);
        List<Parameter> list = new ArrayList<Parameter>();
        list.add(new Parameter("g", "Api"));
        list.add(new Parameter("m", "Profile"));
        list.add(new Parameter("a", "buycardnext"));
        list.add(new Parameter("buyid",getIntent().getStringExtra("id")));

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
                        mName.setText(data.getString("packname"));
                        mMiaos.setText(data.getString("packinfo"));
                        mMoney_all.setText(data.getString("packprice"));
                        mPay_money.setText(data.getString("packprice"));

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
        mButton_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleHUD.showLoadingMessage(getActivity(), "加载中...", true);
                List<Parameter> list = new ArrayList<Parameter>();
                list.add(new Parameter("g", "Api"));
                list.add(new Parameter("m", "Pay"));
                list.add(new Parameter("a", "wxpay"));
                list.add(new Parameter("pid",getIntent().getStringExtra("id")));
                list.add(new Parameter("uid", PreferenceUtils.getPrefString(getActivity(),"agentid","")));
                list.add(new Parameter("ptype","2"));

                HttpManager.getInstance().get(list, PreferenceUtils.getPrefString(getActivity(),"quyudaili",""), 10001, new HttpManager.OnHttpResponseListener() {
                    @Override
                    public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                        LogCatUtils.i("", "index=========" + resultData.toString());
                        try {
                            SimpleHUD.dismiss();
                            JSONObject jsonObject = new JSONObject(resultData);
                            if (jsonObject.getString("status").equals("200")) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                String appid = data.getString("appid");
                                String partnerId = data.getString("partnerid");
                                String prepayId = data.getString("prepayid");
                                String nonceStr = data.getString("noncestr");
                                String timeStamp = data.getString("timestamp");
                                String packageValue = data.getString("package");
                                String sign = data.getString("sign");
                                PayReq req = new PayReq();
//                                        req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                                req.appId = appid;
                                req.partnerId = partnerId;
                                req.prepayId = prepayId;
                                req.nonceStr = nonceStr;
                                req.timeStamp = timeStamp;
                                req.packageValue = packageValue;
                                req.sign = sign;
                                //req.extData = "Android"; // optional
                                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                api.sendReq(req);
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
        });
    }
}
