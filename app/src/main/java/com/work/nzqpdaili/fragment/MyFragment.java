package com.work.nzqpdaili.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.work.nzqpdaili.R;
import com.work.nzqpdaili.activity.DlManageActivity;
import com.work.nzqpdaili.activity.GoukaActivity;
import com.work.nzqpdaili.activity.JieSanActivity;
import com.work.nzqpdaili.activity.LoginActivity;
import com.work.nzqpdaili.activity.MessageActivity;
import com.work.nzqpdaili.activity.PersonalInformationActivity;
import com.work.nzqpdaili.activity.TongjiActivity;
import com.work.nzqpdaili.activity.XiugaiPasswordActivity;
import com.work.nzqpdaili.utils.ImageLoaderUtils;
import com.work.nzqpdaili.utils.LogCatUtils;
import com.work.nzqpdaili.utils.LoginAndOutUtils;
import com.work.nzqpdaili.utils.PreferenceUtils;
import com.work.nzqpdaili.utils.ToastUtils;
import com.work.nzqpdaili.view.hud.SimpleHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.model.Parameter;

/**
 * Created by wj on 2017/5/23.
 */

public class MyFragment extends BaseFragment implements View.OnClickListener{
    private com.work.nzqpdaili.view.CircleImageView mMine_head;
    private RelativeLayout mXiaoxi;
    private RelativeLayout mDailiguanli;
    private RelativeLayout mPeycard;
    private RelativeLayout mJiesan;
    private RelativeLayout mXiugai;
    private RelativeLayout mTongji;
    private LinearLayout mGeren;
    private TextView mName;
    private TextView mDailiid;
    private TextView tuichu;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            setContentView(R.layout.fragment_my);

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
        ((TextView) findViewById(R.id.tv_title)).setText("我的");
        mMine_head = (com.work.nzqpdaili.view.CircleImageView) findViewById(R.id.mine_head);
        mGeren = (LinearLayout) findViewById(R.id.geren);
        mName = (TextView) findViewById(R.id.name);
        mDailiid = (TextView) findViewById(R.id.dailiid);
        tuichu = (TextView) findViewById(R.id.tuichu);
        mXiaoxi=findViewById(R.id.wodexiaoxi);
        mDailiguanli = (RelativeLayout) findViewById(R.id.dailiguanli);
        mPeycard = (RelativeLayout) findViewById(R.id.peycard);
        mJiesan = (RelativeLayout) findViewById(R.id.jiesan);
        mXiugai = (RelativeLayout) findViewById(R.id.xiugai);
        mTongji = (RelativeLayout) findViewById(R.id.tongji);
    }

    @Override
    public void initData() {
        SimpleHUD.showLoadingMessage(getActivity(), "加载中...", true);
        List<Parameter> list = new ArrayList<Parameter>();
        list.add(new Parameter("g", "Api"));
        list.add(new Parameter("m", "Index"));
        list.add(new Parameter("a", "center"));
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
                        mDailiid.setText("代理ID:"+data.getString("agentid"));
                        if(!"1".equals(data.getString("is_disband")))mJiesan.setVisibility(View.GONE);
                     //   ImageLoaderUtils.displayImage(data.getString("heartimg"),mMine_head,R.mipmap.llogoi,10);
                        ImageLoaderUtils.displayImage("",mMine_head,R.mipmap.llogoi,10);
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
        mGeren.setOnClickListener(this);
        mXiaoxi.setOnClickListener(this);
        mDailiguanli.setOnClickListener(this);
        mPeycard.setOnClickListener(this);
        mJiesan.setOnClickListener(this);
        mXiugai.setOnClickListener(this);
        tuichu.setOnClickListener(this);
        mTongji.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.geren:
                startActivity(new Intent(getActivity(), PersonalInformationActivity.class));
                break;
            case R.id.wodexiaoxi:
                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
            case R.id.dailiguanli:
                startActivity(new Intent(getActivity(), DlManageActivity.class));
                break;
            case R.id.peycard:
                startActivity(new Intent(getActivity(), GoukaActivity.class));
                break;
            case R.id.jiesan:
                startActivity(new Intent(getActivity(), JieSanActivity.class));
                break;
            case R.id.xiugai:
                startActivity(new Intent(getActivity(), XiugaiPasswordActivity.class));
                break;
            case R.id.tongji:
                startActivity(new Intent(getActivity(), TongjiActivity.class));
                break;
            case R.id.tuichu:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                (new LoginAndOutUtils()).logoutClearCache(getActivity());
                getActivity().finish();
                break;
        }
    }
}
