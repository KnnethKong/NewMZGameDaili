package com.work.nzqpdaili.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.work.nzqpdaili.Bean.ClubBean;
import com.work.nzqpdaili.R;
import com.work.nzqpdaili.activity.club.BianjiActivity;
import com.work.nzqpdaili.activity.club.ChengyuanActivity;
import com.work.nzqpdaili.activity.club.ChongzhiActivity;
import com.work.nzqpdaili.activity.club.CreateClubActivity;
import com.work.nzqpdaili.activity.club.LishizhanjiActivity;
import com.work.nzqpdaili.adapter.baseadapter.BaseAdapterHelper;
import com.work.nzqpdaili.adapter.baseadapter.QuickAdapter;
import com.work.nzqpdaili.utils.Event;
import com.work.nzqpdaili.utils.ImageLoaderUtils;
import com.work.nzqpdaili.utils.LogCatUtils;
import com.work.nzqpdaili.utils.PreferenceUtils;
import com.work.nzqpdaili.utils.ToastUtils;
import com.work.nzqpdaili.view.CircleImageView;
import com.work.nzqpdaili.view.hud.SimpleHUD;
import com.work.nzqpdaili.wxapi.WXShare;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.model.Parameter;
import zuo.biao.library.ui.WebViewActivity;
import zuo.biao.library.ui.xlistview.XListView;
import zuo.biao.library.util.CommonUtil;

/**
 * Created by range on 2017/12/13.
 */

public class ClubFragment extends BaseFragment implements View.OnClickListener {
    private XListView mList;
    private List<ClubBean> listall = new ArrayList<ClubBean>();
    private QuickAdapter<ClubBean> adapter;
    private int page = 1;
    private ImageView create_club;
    private RelativeLayout is_show;
    private List<ClubBean> array;
    private TextView shuoming;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_club);

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
        EventBus.getDefault().register(this);
        ((TextView) findViewById(R.id.tv_title)).setText("俱乐部");
        mList = (XListView) findViewById(R.id.list);
        is_show = findViewById(R.id.is_show);
        create_club = findViewById(R.id.create_club);
        shuoming = findViewById(R.id.tv_right);
        shuoming.setVisibility(View.VISIBLE);
        shuoming.setText("使用说明");
//        is_show.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        httplist();
        adapter = new QuickAdapter<ClubBean>(getActivity(), R.layout.item_club, listall) {

            @Override
            protected void convert(final BaseAdapterHelper helper, final ClubBean item) {
                if (item.getStatus().equals("1")) {
                    helper.setVisible(R.id.is_yijinyong, false);
                } else {
                    helper.setVisible(R.id.is_yijinyong, true);
                }
                CircleImageView img = (CircleImageView) helper.getView().findViewById(R.id.is_huiyuam);
                ImageLoaderUtils.displayImage(item.getFace(), img, R.mipmap.mo_toux, 10);
                helper.setText(R.id.club_miaoshu, item.getDesc());
                helper.setText(R.id.club_num, "人数:" + item.getUsernum());
                helper.setText(R.id.club_name, "俱乐部: " + item.getName());
                if (item.getType() == 1) {//俱乐部类型1 普通 2 高级
                    helper.setText(R.id.type, "类型: 普通俱乐部");
                    helper.setVisible(R.id.club_hy_show, false);
                    helper.setVisible(R.id.club_chongka, false);
                    helper.setImageResource(R.id.img_hui, R.mipmap.club_pt);
                } else {
                    helper.setText(R.id.type, "类型: 高级俱乐部");
                    helper.setVisible(R.id.club_hy_show, true);
                    helper.setVisible(R.id.club_chongka, true);
                    helper.setText(R.id.club_hy_card, item.getInsurescore());
                    helper.setImageResource(R.id.img_hui, R.mipmap.club_hui);
                }
                helper.setText(R.id.club_id, "俱乐部ID: " + item.getId());
                if (item.getApply().equals("1")) {
                    helper.setVisible(R.id.h_1, true);
                } else {
                    helper.setVisible(R.id.h_1, false);
                }
                helper.setOnClickListener(R.id.club_share, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url =PreferenceUtils.getPrefString(getActivity(),"quyudaili","")+"?g=User&m=Club&a=shareclub&clubid=" + item.getId();
                        String name = "";
                        if (item.getType() == 1) {
                            name = item.getName();
                        } else {
                            name = item.getName();
                        }
                        popuwindow(view, name, item.getId(), item.getType() + "", url);
                    }
                });
                helper.setOnClickListener(R.id.club_copy, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 从API11开始android推荐使用android.content.ClipboardManager
                        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        // 将文本内容放到系统剪贴板里。
                        if (item.getType() == 1) {
                            cm.setText("普通俱乐部: " + item.getName() + "  ID:" + item.getId());
                        } else {
                            cm.setText("高级俱乐部: " + item.getName() + "  ID:" + item.getId());
                        }

                        ToastUtils.showToast(getActivity(), "复制成功 ID:" + item.getId(), 1000);
                    }
                });

                helper.setOnClickListener(R.id.club_chengyuan, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //   if(item.getStatus().equals("1")) {
                        CommonUtil.toActivity(getActivity(), new Intent(getActivity(), ChengyuanActivity.class)
                                .putExtra("id", item.getId()), true);
//                        }else {
//                            ToastUtils.showToast(getActivity(), "该俱乐部已禁用", 1000);
//                        }

                    }
                });
                helper.setOnClickListener(R.id.club_chongka, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // if(item.getStatus().equals("1")) {
                        CommonUtil.toActivity(getActivity(), new Intent(getActivity(), ChongzhiActivity.class)
                                .putExtra("id", item.getId()), true);
//                        }else {
//                            ToastUtils.showToast(getActivity(), "该俱乐部已禁用", 1000);
//                        }
                    }
                });
                helper.setOnClickListener(R.id.club_lishi, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommonUtil.toActivity(getActivity(), new Intent(getActivity(), LishizhanjiActivity.class)
                                .putExtra("id", item.getId())
                                .putExtra("type", item.getType() + ""), true);
                    }
                });
                ImageView jinyong = (ImageView) helper.getView().findViewById(R.id.club_jinyong);

                jinyong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommonUtil.toActivity(getActivity(), new Intent(getActivity(), BianjiActivity.class)
                                .putExtra("id", item.getId()), true);
                    }
                });


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
        list.add(new Parameter("a", "clublist"));
        list.add(new Parameter("p",page+""));
        list.add(new Parameter("num","10"));
        list.add(new Parameter("agent_id", PreferenceUtils.getPrefString(getActivity(), "agentid", "")));
        list.add(new Parameter("token", PreferenceUtils.getPrefString(getActivity(), "token", "")));
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
                        array = JSON.parseArray(data.toString(), ClubBean.class);
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

    private void httpjiyong(String id, final int pos) {
        SimpleHUD.showLoadingMessage(getActivity(), "加载中...", true);
        List<Parameter> list = new ArrayList<Parameter>();
        list.add(new Parameter("g", "Api"));
        list.add(new Parameter("m", "Club"));
        list.add(new Parameter("a", "clubtoggle"));
        list.add(new Parameter("agent_id", PreferenceUtils.getPrefString(getActivity(), "agentid", "")));
        list.add(new Parameter("token", PreferenceUtils.getPrefString(getActivity(), "token", "")));
        list.add(new Parameter("clubid", id));
        HttpManager.getInstance().get(list, PreferenceUtils.getPrefString(getActivity(), "quyudaili", ""), 10001, new HttpManager.OnHttpResponseListener() {
            @Override
            public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                LogCatUtils.i("", "clublist=========" + resultData.toString());
                try {
                    SimpleHUD.dismiss();
                    JSONObject jsonObject = new JSONObject(resultData);
                    if (jsonObject.getString("status").equals("10001")) {
                        ToastUtils.showToast(getActivity(), jsonObject.getString("msg"), 1000);
                        page = 1;
                        httplist();
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
    private void dataListToArrayList(List<ClubBean> dataList) {
        if (page == 1 && dataList.size() > 0) {
            listall.clear();
            adapter.clear();
        }
        if(page==1){
            if(dataList.size()==0){
                is_show.setVisibility(View.VISIBLE);
            }else {
                is_show.setVisibility(View.GONE);
            }
        }
//        if (dataList.size() > 0) {
//            if (dataList.size() >= ConfigConstants.PAGE_SIZE) {
//                mList.setPullLoadEnable(true);
//            } else {
//                mList.setPullLoadEnable(false);
//            }
        for (ClubBean o : dataList) {
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
        create_club.setOnClickListener(this);
        shuoming.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_club:
                CommonUtil.toActivity(getActivity(), new Intent(getActivity(), CreateClubActivity.class), true);
                break;
            case R.id.tv_right:
                String url = PreferenceUtils.getPrefString(getActivity(), "quyudaili", "") + "?g=user&m=Club&a=expagentads";
                toActivity(WebViewActivity.createIntent(getActivity(), "使用说明", url));
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);


    }

    public void onEvent(Event event) {
        String msg = "onEventMainThread收到了消息：" + event.getMsg();
        android.util.Log.d("harvic", msg);

        if (event.getMsg().equals("wan")) {
            //   Dialog.toast("已下单成功",this);
            page = 1;
            httplist();
        }
        if (event.getMsg().equals("wan_get")) {
            page = 1;
            httplist();
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

    PopupWindow popu;
    WXShare wxShare = new WXShare();

    private void popuwindow(View v, final String name, final String id, final String type, final String url) {

        TextView weixin, pengyouquan, QQ, weibo, youjian, duanxin, erweima;
        View contentView = getActivity().getLayoutInflater().from(getActivity()).inflate(R.layout.mine_fenxiang_popu, null);

        weixin = (TextView) contentView.findViewById(R.id.popu_weixin);
        pengyouquan = (TextView) contentView.findViewById(R.id.popu_pengyouquan);


        popu = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        int height = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        popu.setHeight(height * 4 / 20);
        popu.setWidth(width);
        popu.setTouchable(true);
        popu.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("mengdd", "onTouch : ");
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });


        weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String finalHt = "";
                wxShare.share(getActivity(), 1, name, id, type, url);
            }
        });
        pengyouquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wxShare.share(getActivity(), 2, name, id, type, url);
            }
        });


        popu.setBackgroundDrawable(getResources().getDrawable(R.mipmap.touming));
        // 设置好参数之后再show
        popu.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.5f;
        getActivity().getWindow().setAttributes(lp);
        popu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }
}
