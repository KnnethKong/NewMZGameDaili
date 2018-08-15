package com.work.nzqpdaili.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.work.nzqpdaili.Bean.Daili2j;
import com.work.nzqpdaili.R;
import com.work.nzqpdaili.activity.DlDetailsActivity;
import com.work.nzqpdaili.adapter.baseadapter.BaseAdapterHelper;
import com.work.nzqpdaili.adapter.baseadapter.QuickAdapter;
import com.work.nzqpdaili.utils.ConfigConstants;
import com.work.nzqpdaili.utils.LogCatUtils;
import com.work.nzqpdaili.utils.PreferenceUtils;
import com.work.nzqpdaili.utils.ToastUtils;
import com.work.nzqpdaili.view.hud.SimpleHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.model.Parameter;
import zuo.biao.library.ui.xlistview.XListView;

/**
 * Created by wj on 2017/5/27.
 */

public class Dali2jifragment extends BaseFragment{
    private XListView mList;
    private QuickAdapter<Daili2j> adapter;
    private List<Daili2j> listall = new ArrayList<Daili2j>();
    private int page = 1;
    private List<Daili2j> array;
    private TextView title;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_daili2j);

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
        mList = (XListView) findViewById(R.id.list);
        title =(TextView) findViewById(R.id.title);
    }

    @Override
    public void initData() {
        final List<String> list = new ArrayList<String>();
        for (int i = 0; i < 3; i++) {
            list.add((1000+i+1)+"");
        }
        httplist();
        adapter = new QuickAdapter<Daili2j>(getActivity(),R.layout.itrm_erjidaili,listall){

            @Override
            protected void convert(BaseAdapterHelper helper, final Daili2j item) {

                helper.setText(R.id.name,item.getA_name());
                helper.setText(R.id.fangka,item.getA_tel()+"");
                helper.setText(R.id.id,item.getAgentid());
                helper.setVisible(R.id.textline,true);
                if (helper.getPosition()==list.size()-1) helper.setVisible(R.id.textline,false);
                helper.setOnClickListener(R.id.all_layout, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), DlDetailsActivity.class).putExtra("id",item.getAgentid()));
                    }
                });

            }
        };
        mList.setAdapter(adapter);
    }

    private void httplist() {
        SimpleHUD.showLoadingMessage(getActivity(), "加载中...", true);
        List<Parameter> list = new ArrayList<Parameter>();
        list.add(new Parameter("g", "Api"));
        list.add(new Parameter("m", "Agent"));
        list.add(new Parameter("a", "agentlist"));
        list.add(new Parameter("p", page+""));
        list.add(new Parameter("agent_id", PreferenceUtils.getPrefString(getActivity(),"agentid","")));
        list.add(new Parameter("token",PreferenceUtils.getPrefString(getActivity(),"token","")));
        HttpManager.getInstance().get(list, PreferenceUtils.getPrefString(getActivity(),"quyudaili",""), 10001, new HttpManager.OnHttpResponseListener() {
            @Override
            public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                LogCatUtils.i("", "agentlist=========" + resultData.toString());
                try {
                    SimpleHUD.dismiss();
                    JSONObject jsonObject = new JSONObject(resultData);
                    if (jsonObject.getString("status").equals("10001")) {
                        JSONObject result = jsonObject.getJSONObject("result");
                        JSONObject data = result.getJSONObject("data");
                        JSONArray agent = data.getJSONArray("agent");
                        array = JSON.parseArray(agent.toString(), Daili2j.class);
                        JSONObject month = data.getJSONObject("month");
                        title.setText("本月购卡数量："+month.getString("salcard")+"   本月售卡数量："+month.getString("actcard"));
                        dataListToArrayList(array);
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
    /**
     * 读取的网络数据添加
     */
    private void dataListToArrayList(List<Daili2j> dataList) {
        if (page == 1 && dataList.size() > 0) {
            listall.clear();
            adapter.clear();
        }
        if (dataList.size() > 0) {
            if (dataList.size() >= ConfigConstants.PAGE_SIZE) {
                mList.setPullLoadEnable(true);
            } else {
                mList.setPullLoadEnable(false);
            }
            for (Daili2j o : dataList) {
                listall.add(o);
            }
        } else {
            mList.setPullLoadEnable(false);
            if (listall.size() > 0) {
//                ToastUtils.showShort(ReceiveOrderActivity.this, ConfigConstants.NO_MORE_DATA);
            } else {
//                ToastUtils.showShort(ReceiveOrderActivity.this, ConfigConstants.NO_SEARCH_DATA);
            }
        }
        adapter.addAll(dataList);
        onLoadEnd();
    }
    @Override
    public void initEvent() {
        mList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                page=1;
                httplist();
            }

            @Override
            public void onLoadMore() {
                page++;
                httplist();
            }
        });

    }
    /**
     * 加载完成
     */
    private void onLoadEnd() {
        mList.stopRefresh();
        mList.stopLoadMore();
        mList.setRefreshTime("刚刚");
    }
}
