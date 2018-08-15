package com.work.nzqpdaili.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.work.nzqpdaili.Bean.DailiBean;
import com.work.nzqpdaili.MainActivity;
import com.work.nzqpdaili.R;
import com.work.nzqpdaili.adapter.baseadapter.BaseAdapterHelper;
import com.work.nzqpdaili.adapter.baseadapter.QuickAdapter;
import com.work.nzqpdaili.utils.ConfigConstants;
import com.work.nzqpdaili.utils.LogCatUtils;
import com.work.nzqpdaili.utils.PreferenceUtils;
import com.work.nzqpdaili.utils.ToastUtils;
import com.work.nzqpdaili.utils.VerifyUtils;
import com.work.nzqpdaili.view.hud.SimpleHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.model.Parameter;
import zuo.biao.library.ui.WebViewActivity;

public class LoginActivity extends BaseActivity {
    private EditText mName;
    private EditText mPassword;
    private TextView mLoginok, read_xy;
    private LinearLayout mDaililay;
    private TextView mDaili;
    private BaseAdapter adapter;
    private List<DailiBean> array;
    private CheckBox mXy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        mDaililay = (LinearLayout) findViewById(R.id.daililay);
        mDaili = (TextView) findViewById(R.id.daili);
        mName = (EditText) findViewById(R.id.name);
        mPassword = (EditText) findViewById(R.id.password);
        mLoginok = (TextView) findViewById(R.id.loginok);
        mXy = (CheckBox) findViewById(R.id.xy_text);
        read_xy = (TextView) findViewById(R.id.read_xy);
        mDaili.setText(PreferenceUtils.getPrefString(getActivity(), "quyum", ""));
    }

    @Override
    public void initData() {
        SimpleHUD.showLoadingMessage(getActivity(), "加载中...", true);
        List<Parameter> list = new ArrayList<Parameter>();
        list.add(new Parameter("g", "Api"));
        list.add(new Parameter("m", "Login"));
        list.add(new Parameter("a", "agentarea"));


        HttpManager.getInstance().get(list, ConfigConstants.APP_SERVER_API, 10001, new HttpManager.OnHttpResponseListener() {
            @Override
            public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                LogCatUtils.i("", "index=========" + resultData.toString());
                try {
                    SimpleHUD.dismiss();
                    JSONObject jsonObject = new JSONObject(resultData);
                    if (jsonObject.getString("status").equals("10001")) {
                        JSONObject result = jsonObject.getJSONObject("result");
//                        JSONObject data = result.getJSONObject("data");
                        JSONArray data = result.getJSONArray("data");

                        array = JSON.parseArray(data.toString(), DailiBean.class);
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
        mLoginok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (TextUtils.isEmpty(mDaili.getText().toString())) {
//                    ToastUtils.showToast(getActivity(), "请选择区域", 1000);
//                    return;
//                }
                if (!VerifyUtils.verifyInputIsNull(mName)) {
                    ToastUtils.showToast(getActivity(), "请输入用户名", 1000);
                    return;
                }
                if (!VerifyUtils.verifyInputIsNull(mPassword)) {
                    ToastUtils.showToast(getActivity(), "请输入密码", 1000);
                    return;
                }
                if (!mXy.isChecked()) {
                    ToastUtils.showToast(getActivity(), "请确认同意使用协议", 1000);
                    return;
                }
                SimpleHUD.showLoadingMessage(getActivity(), "加载中...", true);
                List<Parameter> list = new ArrayList<Parameter>();
                list.add(new Parameter("g", "Api"));
                list.add(new Parameter("m", "Login"));
                list.add(new Parameter("a", "login"));
                list.add(new Parameter("user", mName.getText().toString()));
                list.add(new Parameter("pass", mPassword.getText().toString()));
                list.add(new Parameter("type", "1"));
                HttpManager.getInstance().get(list, ConfigConstants.APP_SERVER_API_LOGIN, 10001, new HttpManager.OnHttpResponseListener() {
                    @Override
                    public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                        LogCatUtils.i("", "login=========" + resultData.toString());
                        try {
                            SimpleHUD.dismiss();
                            JSONObject jsonObject = new JSONObject(resultData);
                            if (jsonObject.getString("status").equals("10001")) {
                                JSONObject result = jsonObject.getJSONObject("result");
                                JSONObject data = result.getJSONObject("data");
                                PreferenceUtils.setPrefString(LoginActivity.this, "token", data.getString("token"));
                                PreferenceUtils.setPrefString(LoginActivity.this, "agentid", data.getString("agentid"));
                                PreferenceUtils.setPrefString(LoginActivity.this, "quyudaili", ConfigConstants.APP_SERVER_API);
                                String isclub = "";
                                if (data.has("is_club")) {
                                    isclub = data.getString("is_club");
                                }
                                PreferenceUtils.setPrefString(LoginActivity.this, "is_club", isclub);
                                startActivity(MainActivity.createIntent(LoginActivity.this));
                                finish();
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
        mDaililay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PopupWindows(getActivity(), mDaililay);
            }
        });
        read_xy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://daili.zai0312.com/index.php?g=user&m=Agree&a=detail";
                toActivity(WebViewActivity.createIntent(getActivity(), "使用协议", url));
            }
        });
    }

    public class PopupWindows extends PopupWindow {

        @SuppressWarnings("deprecation")
        public PopupWindows(Context mContext, View parent) {


            View view = View.inflate(mContext, R.layout.modifay_popupwindows,
                    null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ListView listView = (ListView) view.findViewById(R.id.list);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));
            setWidth(parent.getWidth());
            setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
//             showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            showAsDropDown(parent);
            adapter = new QuickAdapter<DailiBean>(getActivity(), R.layout.layout, array) {
                @Override
                protected void convert(BaseAdapterHelper helper, final DailiBean item) {
                    helper.setText(R.id.title, item.getContent_name());
                    helper.setOnClickListener(R.id.xiangqing, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDaili.setText(item.getContent_name());
//                            id = item.getId();
                            PreferenceUtils.setPrefString(LoginActivity.this, "quyum", item.getContent_name());
                            PreferenceUtils.setPrefString(LoginActivity.this, "quyudaili", item.getContent_url() + "index.php");
                            dismiss();
                        }
                    });
                }
            };
            listView.setAdapter(adapter);
        }
    }
}
