package com.work.nzqpdaili.activity.club;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.work.nzqpdaili.Bean.ChongzhijiluBean;
import com.work.nzqpdaili.R;
import com.work.nzqpdaili.adapter.baseadapter.BaseAdapterHelper;
import com.work.nzqpdaili.adapter.baseadapter.QuickAdapter;
import com.work.nzqpdaili.utils.LogCatUtils;
import com.work.nzqpdaili.utils.PreferenceUtils;
import com.work.nzqpdaili.utils.ToastUtils;
import com.work.nzqpdaili.view.TitleBarUtils;
import com.work.nzqpdaili.view.hud.SimpleHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.model.Parameter;
import zuo.biao.library.ui.xlistview.XListView;

/**
 * Created by range on 2017/12/13.
 */

public class ChongzhijiluActivity extends BaseActivity {
    private XListView mList;
    private List<ChongzhijiluBean> listall = new ArrayList<ChongzhijiluBean>();
    private QuickAdapter<ChongzhijiluBean> adapter;
    private int page = 1;

    private List<ChongzhijiluBean> array;

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chongzhi_jilu);
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
        ((TextView) findViewById(R.id.tv_title)).setText("充卡记录");
        TitleBarUtils.initBtnBack(this, R.id.tv_back);
        mList = (XListView) findViewById(R.id.list);
    }

    @Override
    public void initData() {
        httplist();
        adapter = new QuickAdapter<ChongzhijiluBean>(getActivity(), R.layout.item_chongzhi_jl, listall) {

            @Override
            protected void convert(final BaseAdapterHelper helper, final ChongzhijiluBean item) {
             helper.setText(R.id.time1,item.getG_ltime());
                helper.setText(R.id.time2,item.getG_rtime());
                helper.setText(R.id.num,item.getG_glodnumber());
            }
        };
        mList.setAdapter(adapter);
        mList.setPullLoadEnable(true);
    }

    private void httplist() {
        SimpleHUD.showLoadingMessage(getActivity(), "加载中...", true);
        List<Parameter> list = new ArrayList<Parameter>();
        list.add(new Parameter("g", "Api"));
        list.add(new Parameter("m", "Club"));

        list.add(new Parameter("a", "clubchargelist"));
        list.add(new Parameter("p",page+""));
        list.add(new Parameter("num","10"));
        list.add(new Parameter("agent_id", PreferenceUtils.getPrefString(getActivity(), "agentid", "")));
        list.add(new Parameter("token", PreferenceUtils.getPrefString(getActivity(), "token", "")));
        list.add(new Parameter("clubid", getActivity().getIntent().getStringExtra("id")));
        HttpManager.getInstance().get(list, PreferenceUtils.getPrefString(getActivity(), "quyudaili", ""), 10001, new HttpManager.OnHttpResponseListener() {
            @Override
            public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                LogCatUtils.i("", "clublist=========" + resultData.toString());
                try {
                    SimpleHUD.dismiss();
                    JSONObject jsonObject = new JSONObject(resultData);
                    if (jsonObject.getString("status").equals("10001")) {
                        JSONObject result = jsonObject.getJSONObject("result");
                        JSONArray data = result.getJSONArray("data");
                        array = JSON.parseArray(data.toString(), ChongzhijiluBean.class);
//
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
    private void dataListToArrayList(List<ChongzhijiluBean> dataList) {
        if (page == 1 ) {
            listall.clear();
            adapter.clear();
        }
//        if (dataList.size() > 0) {
//            if (dataList.size() >= ConfigConstants.PAGE_SIZE) {
//                mList.setPullLoadEnable(true);
//            } else {
//                mList.setPullLoadEnable(false);
//            }
        for (ChongzhijiluBean o : dataList) {
            listall.add(o);
        }
//        } else {
//            mList.setPullLoadEnable(false);
//            if (listall.size() > 0) {
////                ToastUtils.showShort(ReceiveOrderActivity.this, ConfigConstants.NO_MORE_DATA);
//            } else {
////                ToastUtils.showShort(ReceiveOrderActivity.this, ConfigConstants.NO_SEARCH_DATA);
//            }
//        }
        adapter.addAll(dataList);
        onLoadEnd();
    }

    @Override
    public void initEvent() {

        mList.setPullLoadEnable(true);
        mList.setFooterGone();
        mList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {

                page = 1;
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
