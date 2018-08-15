package com.work.nzqpdaili.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.work.nzqpdaili.Bean.MessageBean;
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
import zuo.biao.library.util.CommonUtil;

/**
 * Created by range on 2017/12/25.
 */

public class MessageActivity extends BaseActivity {
    private XListView mList;
    private QuickAdapter<MessageBean.ResultBean.DataBean> adapter;
    private List<MessageBean.ResultBean.DataBean> listall = new ArrayList<MessageBean.ResultBean.DataBean>();
    private int page = 1;
    private List<MessageBean.ResultBean.DataBean> array;
    @Override
    public Activity getActivity() {
        return this;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
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
        ((TextView) findViewById(R.id.tv_title)).setText("消息");
        TitleBarUtils.initBtnBack(this, R.id.tv_back);
        mList = (XListView) findViewById(R.id.list);
    }

    @Override
    public void initData() {
        httplist();
        adapter = new QuickAdapter<MessageBean.ResultBean.DataBean>(getActivity(),R.layout.item_message_item,listall){

            @Override
            protected void convert(BaseAdapterHelper helper, final MessageBean.ResultBean.DataBean item) {
              helper.setText(R.id.time,item.getCreate_time());
                helper.setText(R.id.content,item.getTitle());
            }
        };
        mList.setAdapter(adapter);
    }

    private void httplist() {
        SimpleHUD.showLoadingMessage(getActivity(), "加载中...", true);
        List<Parameter> list = new ArrayList<Parameter>();
        list.add(new Parameter("g", "Api"));
        list.add(new Parameter("m", "msg"));
        list.add(new Parameter("a", "agentmsg"));
        list.add(new Parameter("p", ""+page));
        list.add(new Parameter("num", "10"));
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
                        JSONArray agent = result.getJSONArray("data");
                        array = JSON.parseArray(agent.toString(), MessageBean.ResultBean.DataBean.class);

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
    private void dataListToArrayList(List<MessageBean.ResultBean.DataBean> dataList) {
        if (page == 1) {
            listall.clear();
            adapter.clear();
        }
//        if (dataList.size() > 0) {
//            if (dataList.size() >= ConfigConstants.PAGE_SIZE) {
//                mList.setPullLoadEnable(true);
//            } else {
//                mList.setPullLoadEnable(false);
//            }
        for (MessageBean.ResultBean.DataBean o : dataList) {
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
       mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               String url =listall.get(i-1).getContent();
               String title=listall.get(i-1).getTitle();
           CommonUtil.toActivity(getActivity(),new Intent(getActivity(),MessageMoreActivity.class)
                   .putExtra("content",url)
                   .putExtra("title",title),true);
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
