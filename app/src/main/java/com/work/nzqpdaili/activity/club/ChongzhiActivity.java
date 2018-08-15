package com.work.nzqpdaili.activity.club;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.work.nzqpdaili.R;
import com.work.nzqpdaili.utils.Event;
import com.work.nzqpdaili.utils.LogCatUtils;
import com.work.nzqpdaili.utils.PreferenceUtils;
import com.work.nzqpdaili.utils.ToastUtils;
import com.work.nzqpdaili.view.TitleBarUtils;
import com.work.nzqpdaili.view.hud.SimpleHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.model.Parameter;
import zuo.biao.library.util.CommonUtil;

/**
 * Created by range on 2017/12/13.
 */

public class ChongzhiActivity extends BaseActivity implements View.OnClickListener {
    private EditText num;
    private ImageView que;
    private TextView tv_right;

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chongzhi);
        LinearLayout linearLayout= (LinearLayout) findViewById(R.id.ll_space);
        int color = 0xffffffff;
        linearLayout.setBackgroundColor(color);
        setImmersiveStatusBar(true, color,linearLayout);
        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {

        ((TextView) findViewById(R.id.tv_title)).setText("充卡");
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setVisibility(View.VISIBLE);
        TitleBarUtils.initBtnBack(this, R.id.tv_back);
        num = (EditText) findViewById(R.id.num);
        que = (ImageView) findViewById(R.id.que);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        tv_right.setOnClickListener(this);
        que.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                CommonUtil.toActivity(getActivity(),new Intent(getActivity(),ChongzhijiluActivity.class)
                        .putExtra("id",getIntent().getStringExtra("id")),true);
                break;
            case R.id.que:
                if (num.getText().toString().trim().equals("")) {
                    ToastUtils.showToast(getActivity(), "请输入房卡数量", 1000);
                    return;
                }
                httplist();
                break;
        }
    }

    private void httplist() {
        SimpleHUD.showLoadingMessage(getActivity(), "加载中...", true);
        List<Parameter> list = new ArrayList<Parameter>();
        list.add(new Parameter("g", "Api"));
        list.add(new Parameter("m", "Club"));

        list.add(new Parameter("a", "clubcharge"));

        list.add(new Parameter("agent_id", PreferenceUtils.getPrefString(getActivity(), "agentid", "")));
        list.add(new Parameter("token", PreferenceUtils.getPrefString(getActivity(), "token", "")));
        list.add(new Parameter("clubid",getIntent().getStringExtra("id")));
        list.add(new Parameter("tel",num.getText().toString().trim()));
        HttpManager.getInstance().get(list, PreferenceUtils.getPrefString(getActivity(), "quyudaili", ""), 10001, new HttpManager.OnHttpResponseListener() {
            @Override
            public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                LogCatUtils.i("", "clublist=========" + resultData.toString());
                try {
                    SimpleHUD.dismiss();
                    JSONObject jsonObject = new JSONObject(resultData);
                    if (jsonObject.getString("status").equals("10001")) {
                        ToastUtils.showToast(getActivity(), jsonObject.getString("msg"), 1000);
                        num.setText("");
                        EventBus.getDefault().post(
                                new Event("wan"));
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
