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

public class JieSanActivity extends BaseActivity {
    private EditText mChongzhiid;
    private TextView mJiesanok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jie_san);
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
        ((TextView) findViewById(R.id.tv_title)).setText("解散房间");
        TitleBarUtils.initBtnBack(this, R.id.tv_back);
        mChongzhiid = (EditText) findViewById(R.id.chongzhiid);
        mJiesanok = (TextView) findViewById(R.id.jiesanok);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        mJiesanok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!VerifyUtils.verifyInputIsNull(mChongzhiid)){
                    ToastUtils.showToast(getActivity(), "请输入房间ID", 1000);
                    return;
                }
                List<Parameter> list = new ArrayList<Parameter>();
                list.add(new Parameter("g", "Api"));
                list.add(new Parameter("m", "Profile"));
                list.add(new Parameter("a", "disband"));
                list.add(new Parameter("roomid", mChongzhiid.getText().toString().trim()));

                list.add(new Parameter("agent_id", PreferenceUtils.getPrefString(getActivity(),"agentid","")));
                list.add(new Parameter("token",PreferenceUtils.getPrefString(getActivity(),"token","")));

                HttpManager.getInstance().get(list, PreferenceUtils.getPrefString(getActivity(),"quyudaili",""), 10001, new HttpManager.OnHttpResponseListener() {
                    @Override
                    public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                        LogCatUtils.i("", "chagecard=========" + resultData.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(resultData);
                            if (jsonObject.getString("status").equals("10001")) {
                                ToastUtils.showToast(getActivity(), "房间解散成功", 1000);

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
