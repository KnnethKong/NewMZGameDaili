package com.work.nzqpdaili.activity.club;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.work.nzqpdaili.Bean.ChengyuanBean;
import com.work.nzqpdaili.R;
import com.work.nzqpdaili.adapter.baseadapter.BaseAdapterHelper;
import com.work.nzqpdaili.adapter.baseadapter.QuickAdapter;
import com.work.nzqpdaili.utils.Event;
import com.work.nzqpdaili.utils.ImageLoaderUtils;
import com.work.nzqpdaili.utils.LogCatUtils;
import com.work.nzqpdaili.utils.PreferenceUtils;
import com.work.nzqpdaili.utils.ToastUtils;
import com.work.nzqpdaili.view.CircleImageView;
import com.work.nzqpdaili.view.hud.SimpleHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.model.Parameter;
import zuo.biao.library.ui.AlertStyleDialog;
import zuo.biao.library.ui.xlistview.XListView;

/**
 * Created by range on 2017/12/13.
 */

public class ChengyuanFragment extends BaseFragment {
    private XListView mList;
    private List<ChengyuanBean> listall = new ArrayList<ChengyuanBean>();
    private QuickAdapter<ChengyuanBean> adapter;
    private int page = 1;

    private List<ChengyuanBean> array;
    String type="";

    private TextView h1,h2;
    @Override
    public void onAttach(Activity activity) {
// TODO Auto-generated method stub
        super.onAttach(activity);
        if (activity instanceof ChengyuanActivity) {
            ChengyuanActivity mainActivity = (ChengyuanActivity) activity;
            h1 = (TextView) mainActivity.findViewById(R.id.h_1);
            h2 = (TextView) mainActivity.findViewById(R.id.h_2);
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_chengyuan );

        argument = getArguments();
        if (argument != null) {
            type=argument.getString("type");
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
        EventBus.getDefault().register(this);
        mList = (XListView) findViewById(R.id.list);
    }

    @Override
    public void initData() {
        httplist();
        int i=0;
        if(type.equals("1")){
           i=R.layout.item_chengyuan;
        }else if(type.equals("2")){
            i=R.layout.item_chengyuan_jiaru;
        }else if(type.equals("3")){
            i=R.layout.item_chengyuan_jiaru;
        }
        adapter = new QuickAdapter<ChengyuanBean>(getActivity(),i,listall){

            @Override
            protected void convert(final BaseAdapterHelper helper, final ChengyuanBean item) {
                helper.setText(R.id.name,item.getNickname());
                helper.setText(R.id.id_1,"ID: "+item.getUserid());
                CircleImageView img= (CircleImageView) helper.getView().findViewById(R.id.head);
                ImageLoaderUtils.displayImage(item.getHeadhttp(),img,R.mipmap.llogoi,10);
//                if(type.equals("1")){
//
//                }else {
//                    helper.setText(R.id.id,"ID: aasd"+item.getUserid());
//                }
                if(type.equals("2")||type.equals("3")){
                    helper.setOnClickListener(R.id.item_t, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new AlertStyleDialog(getActivity(), "", "您确定要同意该玩家吗?", true, 0, new AlertStyleDialog.OnDialogButtonClickListener() {
                                @Override
                                public void onDialogButtonClick(int requestCode, boolean isPositive) {
                                    if (isPositive) {
                                        httptongyi(item.getUserid());
                                    }
                                }
                            }).show();
                        }
                    });
                    helper.setOnClickListener(R.id.item_j, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new AlertStyleDialog(getActivity(), "", "您确定要拒绝该玩家吗?", true, 0, new AlertStyleDialog.OnDialogButtonClickListener() {
                                @Override
                                public void onDialogButtonClick(int requestCode, boolean isPositive) {
                                    if (isPositive) {
                                        httpjujue(item.getUserid());
                                    }
                                }
                            }).show();
                        }
                    });
                }else if(type.equals("1")){
                    helper.setOnClickListener(R.id.item_t, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new AlertStyleDialog(getActivity(), "", "您确定要删除该玩家吗?", true, 0, new AlertStyleDialog.OnDialogButtonClickListener() {
                                @Override
                                public void onDialogButtonClick(int requestCode, boolean isPositive) {
                                    if (isPositive) {
                                        httptongyi(item.getUserid());
                                    }
                                }
                            }).show();
                        }
                    });
                }
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
        if(type.equals("1")){
            list.add(new Parameter("a", "clubmember"));
        }else if(type.equals("2")){
            list.add(new Parameter("a", "joinclublist"));
        }else if(type.equals("3")){
            list.add(new Parameter("a", "exitclublist"));
        }
        list.add(new Parameter("p",page+""));
        list.add(new Parameter("num","10"));
        list.add(new Parameter("agent_id", PreferenceUtils.getPrefString(getActivity(),"agentid","")));
        list.add(new Parameter("token",PreferenceUtils.getPrefString(getActivity(),"token","")));
        list.add(new Parameter("clubid",getActivity().getIntent().getStringExtra("id")));
        HttpManager.getInstance().get(list, PreferenceUtils.getPrefString(getActivity(),"quyudaili",""), 10001, new HttpManager.OnHttpResponseListener() {
            @Override
            public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                LogCatUtils.i("", "clublist=========" + resultData.toString());
                try {
                    SimpleHUD.dismiss();
                    JSONObject jsonObject = new JSONObject(resultData);
                    if (jsonObject.getString("status").equals("10001")) {
                        JSONObject result = jsonObject.getJSONObject("result");
                        JSONArray data = result.getJSONArray("data");
                        array = JSON.parseArray(String.valueOf(data), ChengyuanBean.class);
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
    private void httptongyi(String user_id){
        SimpleHUD.showLoadingMessage(getActivity(), "加载中...", true);
        List<Parameter> list = new ArrayList<Parameter>();
        list.add(new Parameter("g", "Api"));
        list.add(new Parameter("m", "Club"));
       if(type.equals("2")){
            list.add(new Parameter("a", "joinclubagree"));
        }else if(type.equals("3")){
            list.add(new Parameter("a", "exitclubagree"));
        }else if(type.equals("1")){
           list.add(new Parameter("a", "clubmemberdel"));
       }
        list.add(new Parameter("agent_id", PreferenceUtils.getPrefString(getActivity(),"agentid","")));
        list.add(new Parameter("token",PreferenceUtils.getPrefString(getActivity(),"token","")));
        list.add(new Parameter("clubid",getActivity().getIntent().getStringExtra("id")));
        list.add(new Parameter("userid",user_id));
        HttpManager.getInstance().get(list, PreferenceUtils.getPrefString(getActivity(),"quyudaili",""), 10001, new HttpManager.OnHttpResponseListener() {
            @Override
            public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                LogCatUtils.i("", "clublist=========" + resultData.toString());
                try {
                    SimpleHUD.dismiss();
                    JSONObject jsonObject = new JSONObject(resultData);
                    if (jsonObject.getString("status").equals("10001")) {
                        page=1;
                        httplist();
                        ToastUtils.showToast(getActivity(), jsonObject.getString("msg"), 1000);
                        EventBus.getDefault().post(
                                new Event("wan_get"));
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
    private void httpjujue(String user_id){
        SimpleHUD.showLoadingMessage(getActivity(), "加载中...", true);
        List<Parameter> list = new ArrayList<Parameter>();
        list.add(new Parameter("g", "Api"));
        list.add(new Parameter("m", "Club"));
        if(type.equals("2")){
            list.add(new Parameter("a", "joinclubrefuse"));
        }else if(type.equals("3")){
            list.add(new Parameter("a", "exitclubrefuse"));
        }
        list.add(new Parameter("agent_id", PreferenceUtils.getPrefString(getActivity(),"agentid","")));
        list.add(new Parameter("token",PreferenceUtils.getPrefString(getActivity(),"token","")));
        list.add(new Parameter("clubid",getActivity().getIntent().getStringExtra("id")));
        list.add(new Parameter("userid",user_id));
        HttpManager.getInstance().get(list, PreferenceUtils.getPrefString(getActivity(),"quyudaili",""), 10001, new HttpManager.OnHttpResponseListener() {
            @Override
            public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                LogCatUtils.i("", "clublist=========" + resultData.toString());
                try {
                    SimpleHUD.dismiss();
                    JSONObject jsonObject = new JSONObject(resultData);
                    if (jsonObject.getString("status").equals("10001")) {
                        page=1;
                        httplist();
                        ToastUtils.showToast(getActivity(), jsonObject.getString("msg"), 1000);
                        EventBus.getDefault().post(
                                new Event("wan_get"));
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
    private void dataListToArrayList(List<ChengyuanBean> dataList) {
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
        for (ChengyuanBean o : dataList) {
            listall.add(o);
        }
          if(type.equals("2")){
              if(dataList.size()==0){
                h1.setVisibility(View.GONE);
              }else {
                  h1.setVisibility(View.VISIBLE);
              }
          }else if(type.equals("3")){
              if(dataList.size()==0){
                  h2.setVisibility(View.GONE);
              }else {
                  h2.setVisibility(View.VISIBLE);
              }
          }

        adapter.addAll(dataList);
        onLoadEnd();
    }
    @Override
    public void initEvent() {
        mList.setFooterGone();
        mList.setPullLoadEnable(true);
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



    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);


    }
    public void onEvent(Event event) {
        String msg = "onEventMainThread收到了消息：" + event.getMsg();
        android.util.Log.d("harvic", msg);

        if (event.getMsg().equals("wan_get")) {
            //   Dialog.toast("已下单成功",this);
            if(type.equals("1")){
                page=1;
                httplist();
            }

        }

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
