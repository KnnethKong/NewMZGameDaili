package com.work.nzqpdaili.activity.club;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.work.nzqpdaili.Bean.ChengyuanBean;
import com.work.nzqpdaili.R;
import com.work.nzqpdaili.utils.Event;
import com.work.nzqpdaili.utils.ImageLoaderUtils;
import com.work.nzqpdaili.utils.LogCatUtils;
import com.work.nzqpdaili.utils.PreferenceUtils;
import com.work.nzqpdaili.utils.ToastUtils;
import com.work.nzqpdaili.view.CircleImageView;
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

/**
 * Created by range on 2017/12/13.
 */

public class ChengyuanYQActivity extends BaseActivity implements View.OnClickListener {
    private ImageView Search,jiaru;
    private LinearLayout is_show;
    private CircleImageView head;
    private TextView name,id;
    private EditText num;
    private ChengyuanBean array;
    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_chengyuan_yq);
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
        ((TextView) findViewById(R.id.tv_title)).setText("邀请玩家");
        TitleBarUtils.initBtnBack(this, R.id.tv_back);
        is_show= (LinearLayout) findViewById(R.id.is_show);
        Search= (ImageView) findViewById(R.id.search);
        num= (EditText) findViewById(R.id.num);
        head= (CircleImageView) findViewById(R.id.head);
        name= (TextView) findViewById(R.id.name);
        id= (TextView) findViewById(R.id.id);
        jiaru= (ImageView) findViewById(R.id.item_t);

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        Search.setOnClickListener(this);
        jiaru.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search:
                if(num.getText().toString().trim().equals("")){
                    ToastUtils.showToast(getActivity(),"请输入玩家ID", 1000);
                    return;
                }
                httpsearch();
                break;
            case R.id.item_t:
                 httpjoin();
                break;
        }
    }
    private void httpsearch(){
        SimpleHUD.showLoadingMessage(getActivity(), "加载中...", true);
        List<Parameter> list = new ArrayList<Parameter>();
        list.add(new Parameter("g", "Api"));
        list.add(new Parameter("m", "Club"));
        list.add(new Parameter("a", "clubsearch"));
        list.add(new Parameter("agent_id", PreferenceUtils.getPrefString(getActivity(),"agentid","")));
        list.add(new Parameter("token",PreferenceUtils.getPrefString(getActivity(),"token","")));
        list.add(new Parameter("clubid",getActivity().getIntent().getStringExtra("id")));
        list.add(new Parameter("userid",num.getText().toString().trim()));
        HttpManager.getInstance().get(list, PreferenceUtils.getPrefString(getActivity(),"quyudaili",""), 10001, new HttpManager.OnHttpResponseListener() {
            @Override
            public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                LogCatUtils.i("", "clublist=========" + resultData.toString());
                try {
                    SimpleHUD.dismiss();
                    JSONObject jsonObject = new JSONObject(resultData);
                    if (jsonObject.getString("status").equals("10001")) {
                        JSONObject result = jsonObject.getJSONObject("result");
                        JSONObject data = result.getJSONObject("data");
                        array = JSON.parseObject(data.toString(), ChengyuanBean.class);
//
                        if(array.getUserid()!=null){
                            dataListTo(array);
                        }else {
                            ToastUtils.showToast(getActivity(), "未搜索到该玩家", 1000);
                        }

                    } else {
                        ToastUtils.showToast(getActivity(), jsonObject.getString("msg"), 1000);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtils.showToast(getActivity(), "未搜索到该玩家", 1000);
                }
            }


            @Override
            public void onHttpRequestError(int requestCode, Exception exception) {
                LogCatUtils.i("", "exception=========" + exception.toString());
                SimpleHUD.dismiss();
            }
        });
    }
    private void dataListTo(ChengyuanBean chengyuanBean){
        is_show.setVisibility(View.VISIBLE);
        ImageLoaderUtils.displayImage(chengyuanBean.getHeadhttp(),head,R.mipmap.llogoi,10);
        name.setText(chengyuanBean.getNickname());
        id.setText("ID: "+chengyuanBean.getUserid());
    }
    private void httpjoin(){
        SimpleHUD.showLoadingMessage(getActivity(), "加载中...", true);
        List<Parameter> list = new ArrayList<Parameter>();
        list.add(new Parameter("g", "Api"));
        list.add(new Parameter("m", "Club"));
        list.add(new Parameter("a", "joinclubsearch"));
        list.add(new Parameter("agent_id", PreferenceUtils.getPrefString(getActivity(),"agentid","")));
        list.add(new Parameter("token",PreferenceUtils.getPrefString(getActivity(),"token","")));
        list.add(new Parameter("clubid",getActivity().getIntent().getStringExtra("id")));
        list.add(new Parameter("userid",array.getUserid()));
        HttpManager.getInstance().get(list, PreferenceUtils.getPrefString(getActivity(),"quyudaili",""), 10001, new HttpManager.OnHttpResponseListener() {
            @Override
            public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                LogCatUtils.i("", "clublist=========" + resultData.toString());
                try {
                    SimpleHUD.dismiss();
                    JSONObject jsonObject = new JSONObject(resultData);
                    if (jsonObject.getString("status").equals("10001")) {
                        ToastUtils.showToast(getActivity(), jsonObject.getString("msg"), 1000);
                        EventBus.getDefault().post(
                                new Event("wan_get"));
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
}
