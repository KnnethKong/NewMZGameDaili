package com.work.nzqpdaili.activity.club;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.work.nzqpdaili.Bean.ZhanjiBean;
import com.work.nzqpdaili.R;
import com.work.nzqpdaili.adapter.baseadapter.BaseAdapterHelper;
import com.work.nzqpdaili.adapter.baseadapter.QuickAdapter;
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
 * Created by range on 2017/12/18.
 */

public class LishizhanjiFragment extends BaseFragment {
    private XListView mList;
    private List<ZhanjiBean.RoomlistBean> listall = new ArrayList<ZhanjiBean.RoomlistBean>();
    private QuickAdapter<ZhanjiBean.RoomlistBean> adapter;
    private int page = 1;

    private List<ZhanjiBean.RoomlistBean> array;
    String type = "", s_type = "";
    String start = "", end = "";
    private TextView num;
    String is_first = "";

    @Override
    public void onAttach(Activity activity) {
// TODO Auto-generated method stub
        super.onAttach(activity);
        if (activity instanceof LishizhanjiActivity) {
            LishizhanjiActivity mainActivity = (LishizhanjiActivity) activity;
            num = (TextView) mainActivity.findViewById(R.id.num);

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_chengyuan);

        argument = getArguments();
        if (argument != null) {
            type = argument.getString("type");
            s_type = argument.getString("s_type");
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
    }

    public void change(String sta, String ed) {
        start = sta;
        end = ed;
        page = 1;
        httpdata();
    }

    @Override
    public void initData() {
        httpdata();
        adapter = new QuickAdapter<ZhanjiBean.RoomlistBean>(getActivity(), R.layout.item_zhanji, listall) {

            @Override
            protected void convert(final BaseAdapterHelper helper, final ZhanjiBean.RoomlistBean item) {
                TextView shoukuan = (TextView) helper.getView().findViewById(R.id.shoukuan);
                if (s_type.equals("1")) {
                    if (type.equals("0")) {
                        helper.setVisible(R.id.ka_show, false);
                    } else {
                        helper.setVisible(R.id.ka_show, true);
                    }
                    shoukuan.setVisibility(View.GONE);
                } else {
                    if (type.equals("0")) {
                        helper.setVisible(R.id.ka_show, false);
                    } else if (type.equals("1")) {
                        helper.setVisible(R.id.ka_show, true);
                        shoukuan.setVisibility(View.VISIBLE);
                        shoukuan.setText("");
                        shoukuan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                httpshoukuan(item.getId());
                            }
                        });
                    } else {
                        helper.setVisible(R.id.ka_show, true);
                        shoukuan.setVisibility(View.VISIBLE);
                        shoukuan.setCompoundDrawables(null, null, null, null);
                        shoukuan.setText("已收");
                        shoukuan.setOnClickListener(null);
                    }
                }
                helper.setText(R.id.time1, item.getEnddatel());
                helper.setText(R.id.time2, item.getEnddater());
                helper.setText(R.id.w1, item.getPlayername1());
                helper.setText(R.id.w2, item.getPlayername2());
                helper.setText(R.id.w3, item.getPlayername3());
                helper.setText(R.id.w4, item.getPlayername4());
                if (type.equals("0")) {
                    helper.setText(R.id.f_1, "");
                    helper.setText(R.id.f_2, "");
                    helper.setText(R.id.f_3, "");
                    helper.setText(R.id.f_4, "");
                } else {
                    helper.setText(R.id.f_1, ": " + item.getScore1());
                    helper.setText(R.id.f_2, ": " + item.getScore2());
                    helper.setText(R.id.f_3, ": " + item.getScore3());
                    helper.setText(R.id.f_4, ": " + item.getScore4());
                }

                helper.setText(R.id.num, item.getInsurescore());
                if (type.equals("0")) {
                    helper.setVisible(R.id.win_1, false);
                    helper.setVisible(R.id.win_2, false);
                    helper.setVisible(R.id.win_3, false);
                    helper.setVisible(R.id.win_4, false);
                } else {
                    if (item.getUserwin().equals("1")) {
                        helper.setVisible(R.id.win_1, true);
                        helper.setVisible(R.id.win_2, false);
                        helper.setVisible(R.id.win_3, false);
                        helper.setVisible(R.id.win_4, false);
                    } else if (item.getUserwin().equals("2")) {
                        helper.setVisible(R.id.win_1, false);
                        helper.setVisible(R.id.win_2, true);
                        helper.setVisible(R.id.win_3, false);
                        helper.setVisible(R.id.win_4, false);
                    } else if (item.getUserwin().equals("3")) {
                        helper.setVisible(R.id.win_1, false);
                        helper.setVisible(R.id.win_2, false);
                        helper.setVisible(R.id.win_3, true);
                        helper.setVisible(R.id.win_4, false);
                    } else if (item.getUserwin().equals("4")) {
                        helper.setVisible(R.id.win_1, false);
                        helper.setVisible(R.id.win_2, false);
                        helper.setVisible(R.id.win_3, false);
                        helper.setVisible(R.id.win_4, true);
                    }
                }
            }
        };
        mList.setAdapter(adapter);
        mList.setPullLoadEnable(false);
    }

    @Override
    public void initEvent() {
        mList.setPullLoadEnable(true);
        mList.setFooterGone();
        mList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                page = 1;
                httpdata();
            }

            @Override
            public void onLoadMore() {
                page++;
                httpdata();
            }
        });
    }

    private void httpshoukuan(String id) {
        SimpleHUD.showLoadingMessage(getActivity(), "加载中...", true);
        List<Parameter> list = new ArrayList<Parameter>();
        list.add(new Parameter("g", "Api"));
        list.add(new Parameter("m", "Club"));
        list.add(new Parameter("a", "clubrecv"));
        list.add(new Parameter("agent_id", PreferenceUtils.getPrefString(getActivity(), "agentid", "")));
        list.add(new Parameter("token", PreferenceUtils.getPrefString(getActivity(), "token", "")));
        list.add(new Parameter("recvid", id));

        HttpManager.getInstance().get(list, PreferenceUtils.getPrefString(getActivity(), "quyudaili", ""), 10001, new HttpManager.OnHttpResponseListener() {
            @Override
            public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                LogCatUtils.i("", "clublist=========" + resultData.toString());
                try {
                    SimpleHUD.dismiss();
                    JSONObject jsonObject = new JSONObject(resultData);
                    if (jsonObject.getString("status").equals("10001")) {
                        page = 1;
                        httpdata();
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

    private void httpdata() {
        SimpleHUD.showLoadingMessage(getActivity(), "加载中...", true);
        List<Parameter> list = new ArrayList<Parameter>();
        list.add(new Parameter("g", "Api"));
        list.add(new Parameter("m", "Club"));
        if (s_type.equals("1")) {
            if (type.equals("0")) {//已开始
                list.add(new Parameter("a", "historyrecordps"));
            } else if (type.equals("1")) {//已结束
                list.add(new Parameter("a", "historyrecord"));
            }
        } else {
            if (type.equals("0")) {//已开始
                list.add(new Parameter("a", "historyrecordgs"));
            } else if (type.equals("1")) {//未收
                list.add(new Parameter("a", "historyrecordn"));
            } else {//已收
                list.add(new Parameter("a", "historyrecordy"));
            }
        }
        list.add(new Parameter("p", page + ""));
        list.add(new Parameter("num", "20"));
        list.add(new Parameter("agent_id", PreferenceUtils.getPrefString(getActivity(), "agentid", "")));
        list.add(new Parameter("token", PreferenceUtils.getPrefString(getActivity(), "token", "")));
        list.add(new Parameter("clubid", getActivity().getIntent().getStringExtra("id")));

        list.add(new Parameter("start", start));
        list.add(new Parameter("end", end));
        HttpManager.getInstance().get(list, PreferenceUtils.getPrefString(getActivity(), "quyudaili", ""), 10001, new HttpManager.OnHttpResponseListener() {
            @Override
            public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                LogCatUtils.i("", "clublist=========" + resultData.toString());
                try {
                    SimpleHUD.dismiss();
                    JSONObject jsonObject = new JSONObject(resultData);
                    if (jsonObject.getString("status").equals("10001")) {
                        JSONObject result = jsonObject.getJSONObject("result");
                        JSONObject data = result.getJSONObject("data");
                        JSONArray data1 = data.getJSONArray("roomlist");

                        if (is_first.equals("1")) {
                            num.setText(data.getString("consume"));
                        }
                        array = JSON.parseArray(data1.toString(), ZhanjiBean.RoomlistBean.class);
                        // num.setText(data.getString("consume"));
                        dataListToArrayList(array);
                        is_first = "1";
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
    private void dataListToArrayList(List<ZhanjiBean.RoomlistBean> dataList) {
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
        for (ZhanjiBean.RoomlistBean o : dataList) {
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


    /**
     * 加载完成
     */
    private void onLoadEnd() {
        mList.stopRefresh();
        mList.stopLoadMore();
        mList.setRefreshTime("刚刚");
    }
}
