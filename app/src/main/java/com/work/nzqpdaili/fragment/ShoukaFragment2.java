package com.work.nzqpdaili.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.work.nzqpdaili.Bean.ShoukajlBean;
import com.work.nzqpdaili.R;
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
 * Created by wj on 2017/5/31.
 */

public class ShoukaFragment2 extends BaseFragment{
    private XListView mList;
    private QuickAdapter<ShoukajlBean> adapter;
    private List<ShoukajlBean> array;
    private List<ShoukajlBean> listall = new ArrayList<ShoukajlBean>();
    private int page = 1;
    private TextView title;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_shuola);

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
        title = (TextView) findViewById(R.id.title);
    }

    @Override
    public void initData() {
        final List<String> list = new ArrayList<String>();
        for (int i = 0; i < 4; i++) {
            list.add((1000+i+1)+"");
        }
        httplist();
        adapter = new QuickAdapter<ShoukajlBean>(getActivity(),R.layout.item_shouka,listall){

            @Override
            protected void convert(BaseAdapterHelper helper, ShoukajlBean item) {
                LinearLayout all_layout = (LinearLayout) helper.getView().findViewById(R.id.all_layout);
                if (0 == helper.getPosition()%2 )all_layout.setBackgroundColor(Color.parseColor("#F5F5F5"));
                else if (1 == helper.getPosition()%2 )all_layout.setBackgroundColor(Color.parseColor("#ffffff"));
                helper.setText(R.id.time1,item.getG_ltime());
                helper.setText(R.id.time2,item.getG_rtime());
                helper.setText(R.id.num,item.getG_glodnumber()+"");
                helper.setText(R.id.id,item.getG_agentid());
                helper.setText(R.id.money,item.getGlodnumber_price());
            }
        };
        mList.setAdapter(adapter);
    }
    private void httplist() {
        SimpleHUD.showLoadingMessage(getActivity(), "加载中...", true);
        List<Parameter> list = new ArrayList<Parameter>();
        list.add(new Parameter("g", "Api"));
        list.add(new Parameter("m", "Agent"));
        list.add(new Parameter("a", "cashrecord_act"));
        list.add(new Parameter("p", page+""));
        list.add(new Parameter("agent_id", getArguments().getString("id")));
        HttpManager.getInstance().get(list, PreferenceUtils.getPrefString(getActivity(),"quyudaili",""), 10001, new HttpManager.OnHttpResponseListener() {
            @Override
            public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                LogCatUtils.i("", "expert2=========" + resultData.toString());
                try {
                    SimpleHUD.dismiss();
                    JSONObject jsonObject = new JSONObject(resultData);
                    if (jsonObject.getString("status").equals("10001")) {
                        JSONObject result = jsonObject.getJSONObject("result");
                        JSONObject data = result.getJSONObject("data");
                        JSONArray agent = data.getJSONArray("action");
                        array = JSON.parseArray(agent.toString(), ShoukajlBean.class);
                        JSONObject zong = data.getJSONObject("zong");
                        title.setText("姓名："+zong.getString("name")+"   售卡总量："+zong.getString("price"));
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
    private void dataListToArrayList(List<ShoukajlBean> dataList) {
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
            for (ShoukajlBean o : dataList) {
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
