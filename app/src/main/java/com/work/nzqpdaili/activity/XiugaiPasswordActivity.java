package com.work.nzqpdaili.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.work.nzqpdaili.R;
import com.work.nzqpdaili.utils.LogCatUtils;
import com.work.nzqpdaili.utils.PreferenceUtils;
import com.work.nzqpdaili.utils.ToastUtils;
import com.work.nzqpdaili.utils.VerifyUtils;
import com.work.nzqpdaili.view.TitleBarUtils;
import com.work.nzqpdaili.view.hud.SimpleHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.model.Parameter;

public class XiugaiPasswordActivity extends BaseActivity {
    private EditText mOld_password;
    private EditText mNew_password;
    private EditText mNew_passwords;
    private TextView mQuedok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiugai_password);
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
        ((TextView) findViewById(R.id.tv_title)).setText("修改密码");
        TitleBarUtils.initBtnBack(this, R.id.tv_back);
        mOld_password = (EditText) findViewById(R.id.old_password);
        mNew_password = (EditText) findViewById(R.id.new_password);
        mNew_passwords = (EditText) findViewById(R.id.new_passwords);
        mQuedok = (TextView) findViewById(R.id.quedok);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        mQuedok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!VerifyUtils.verifyInputIsNull(mOld_password)){
                    ToastUtils.showToast(getActivity(), "请输入原密码", 1000);
                    return;
                }
                if (!VerifyUtils.verifyInputIsNull(mNew_password)){
                    ToastUtils.showToast(getActivity(), "请输入新密码", 1000);
                    return;
                }
                if (!VerifyUtils.verifyInputIsNull(mNew_passwords)){
                    ToastUtils.showToast(getActivity(), "请输入确认密码", 1000);
                    return;
                }
                List<Parameter> list = new ArrayList<Parameter>();
                list.add(new Parameter("g", "Api"));
                list.add(new Parameter("m", "profile"));
                list.add(new Parameter("a", "password"));

                list.add(new Parameter("old_password",mOld_password.getText().toString().trim()));
                list.add(new Parameter("password",mNew_password.getText().toString().trim()));
                list.add(new Parameter("repassword",mNew_passwords.getText().toString().trim()));
                list.add(new Parameter("agent_id", PreferenceUtils.getPrefString(getActivity(),"agentid","")));
                list.add(new Parameter("token",PreferenceUtils.getPrefString(getActivity(),"token","")));

                HttpManager.getInstance().get(list, PreferenceUtils.getPrefString(getActivity(),"quyudaili",""), 10001, new HttpManager.OnHttpResponseListener() {
                    @Override
                    public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                        LogCatUtils.i("", "chagecard=========" + resultData.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(resultData);
                            if (jsonObject.getString("status").equals("10001")) {
                                ToastUtils.showToast(getActivity(), "修改成功", 1000);

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
            }
        });
    }
}
