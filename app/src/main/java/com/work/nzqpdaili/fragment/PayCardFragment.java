package com.work.nzqpdaili.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.work.nzqpdaili.Bean.GoukaBean;
import com.work.nzqpdaili.R;
import com.work.nzqpdaili.activity.PayActivity;
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
 * Created by wj on 2017/5/23.
 */

public class PayCardFragment extends BaseFragment{
    private XListView mList;
    private List<GoukaBean> listall = new ArrayList<GoukaBean>();
    private QuickAdapter<GoukaBean> adapter;
    private int page = 1;
    private TextView chongz;
    private List<GoukaBean> array;
    private boolean flag = true;
    private int nums = 0;
    @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            setContentView(R.layout.fragment_pay_card );

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
        ((TextView) findViewById(R.id.tv_title)).setText("购卡");
        mList = (XListView) findViewById(R.id.list);
        View foot = LayoutInflater.from(getActivity()).inflate(R.layout.gouka_foot, null);
        chongz = (TextView)foot. findViewById(R.id.chongzhi);
        mList.addFooterView(foot);
    }

    @Override
    public void initData() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 3; i++) {
            list.add((1000+i+1)+"");
        }
        httplist();
        adapter = new QuickAdapter<GoukaBean>(getActivity(),R.layout.item_gouka,listall){

            @Override
            protected void convert(final BaseAdapterHelper helper, GoukaBean item) {
                RelativeLayout all_layout = (RelativeLayout) helper.getView().findViewById(R.id.all_layout);
                ImageView image = (ImageView) helper.getView().findViewById(R.id.image);
                if (0 == helper.getPosition()%3 )all_layout.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tcbg1));
                else if (1 == helper.getPosition()%3 )all_layout.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tcbg2));
                else if (2 == helper.getPosition()%3 )all_layout.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tcbg3));
                image.setVisibility(View.GONE);
                if (helper.getPosition()==nums){
                    image.setVisibility(View.VISIBLE);
//                    flag = false;
                }else  image.setVisibility(View.GONE);
                helper.setText(R.id.num,item.getPackcount());
                helper.setText(R.id.nums,"张房卡 赠"+item.getGivecount()+"张");
                helper.setText(R.id.taocan,item.getPackname());
                helper.setText(R.id.money,item.getPackprice());
                helper.setOnClickListener(R.id.all_layout, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nums = helper.getPosition();
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        mList.setAdapter(adapter);
        mList.setPullLoadEnable(false);
    }
    private void httplist() {
        SimpleHUD.showLoadingMessage(getActivity(), "加载中...", true);
        List<Parameter> list = new ArrayList<Parameter>();
        list.add(new Parameter("g", "Api"));
        list.add(new Parameter("m", "Profile"));
        list.add(new Parameter("a", "buycard"));
        list.add(new Parameter("p", page+""));
        HttpManager.getInstance().get(list, PreferenceUtils.getPrefString(getActivity(),"quyudaili",""), 10001, new HttpManager.OnHttpResponseListener() {
            @Override
            public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                LogCatUtils.i("", "expert2=========" + resultData.toString());
                try {
                    SimpleHUD.dismiss();
                    JSONObject jsonObject = new JSONObject(resultData);
                    if (jsonObject.getString("status").equals("10001")) {
                        JSONObject result = jsonObject.getJSONObject("result");
                        JSONArray data = result.getJSONArray("data");
                        array = JSON.parseArray(data.toString(), GoukaBean.class);
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
    private void dataListToArrayList(List<GoukaBean> dataList) {
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
            for (GoukaBean o : dataList) {
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
        chongz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoukaBean bean = new GoukaBean();
                bean = listall.get(nums);
                startActivity(new Intent(getActivity(), PayActivity.class).putExtra("id",bean.getPackid()));
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
