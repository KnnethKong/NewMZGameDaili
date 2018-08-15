package com.work.nzqpdaili.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.work.nzqpdaili.Bean.GengxinBean;
import com.work.nzqpdaili.Bean.MoneyBean;
import com.work.nzqpdaili.R;
import com.work.nzqpdaili.activity.GengxinActivity;
import com.work.nzqpdaili.activity.MessageActivity;
import com.work.nzqpdaili.utils.GetVersion;
import com.work.nzqpdaili.utils.ImageLoaderUtils;
import com.work.nzqpdaili.utils.LogCatUtils;
import com.work.nzqpdaili.utils.PreferenceUtils;
import com.work.nzqpdaili.utils.ToastUtils;
import com.work.nzqpdaili.utils.VerifyUtils;
import com.work.nzqpdaili.view.hud.SimpleHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.model.Parameter;
import zuo.biao.library.util.CommonUtil;

/**
 * Created by wj on 2017/5/23.
 */

public class HomeFragment extends BaseFragment {
    private TextView mName;
    private TextView mShengyu;
    private TextView mJinshou;
    private TextView mXiaji;
    private EditText mChongzhiid;
    private TextView mChongzhiok;
    private TextView mMessage;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String money1 = "", money2 = "", money3 = "";
    private String num1 = "", num2 = "", num3 = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_home);

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
        ((TextView) findViewById(R.id.tv_title)).setText("首页");
        mMessage = findViewById(R.id.tv_message);
        mMessage.setVisibility(View.VISIBLE);
        mName = (TextView) findViewById(R.id.name);
        mShengyu = (TextView) findViewById(R.id.shengyu);
        mJinshou = (TextView) findViewById(R.id.jinshou);
        mXiaji = (TextView) findViewById(R.id.xiaji);
        mChongzhiid = (EditText) findViewById(R.id.chongzhiid);
        mChongzhiok = (TextView) findViewById(R.id.chongzhiok);
        swipeRefreshLayout = findViewById(R.id.psv_display);
    }

    @Override
    public void initData() {
        httpversion();
        httpdata();
        httpMoney();

    }

    private void httpversion() {
        String quyudaili = PreferenceUtils.getPrefString(getActivity(), "quyudaili", "");
        Log.e("kxflog", "quyudaili   : " + quyudaili);
        SimpleHUD.showLoadingMessage(getActivity(), "加载中...", true);
        List<Parameter> list = new ArrayList<Parameter>();
        list.add(new Parameter("g", "Api"));
        list.add(new Parameter("m", "Version"));
        list.add(new Parameter("a", "agent_version"));
        list.add(new Parameter("agent_id", PreferenceUtils.getPrefString(getActivity(), "agentid", "")));
        list.add(new Parameter("token", PreferenceUtils.getPrefString(getActivity(), "token", "")));
        list.add(new Parameter("type", "1"));
        HttpManager.getInstance().get(list, PreferenceUtils.getPrefString(getActivity(), "quyudaili", ""), 10001, new HttpManager.OnHttpResponseListener() {
            @Override
            public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                LogCatUtils.i("", "index=========" + resultData.toString());
                GengxinBean bean = new Gson().fromJson(resultData.toString(), GengxinBean.class);
                if (!bean.getResult().getData().getGameversion().equals(GetVersion.getAppVersionCode(getActivity()))) {
                    CommonUtil.toActivity(getActivity(), new Intent(getActivity(), GengxinActivity.class)
                            .putExtra("url", bean.getResult().getData().getDownurl())
                            .putExtra("name", bean.getResult().getData().getGamename())
                            .putExtra("is_update", bean.getResult().getData().getIs_upgrade()), true);
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
        list.add(new Parameter("m", "Index"));
        list.add(new Parameter("a", "index"));
        list.add(new Parameter("agent_id", PreferenceUtils.getPrefString(getActivity(), "agentid", "")));
        list.add(new Parameter("token", PreferenceUtils.getPrefString(getActivity(), "token", "")));

        HttpManager.getInstance().get(list, PreferenceUtils.getPrefString(getActivity(), "quyudaili", ""), 10001, new HttpManager.OnHttpResponseListener() {
            @Override
            public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                LogCatUtils.i("", "index=========" + resultData.toString());
                try {
                    SimpleHUD.dismiss();
                    JSONObject jsonObject = new JSONObject(resultData);
                    if (jsonObject.getString("status").equals("10001")) {
                        JSONObject result = jsonObject.getJSONObject("result");
                        JSONObject data = result.getJSONObject("data");
                        mName.setText(data.getString("a_name"));
                        mShengyu.setText(data.getString("a_tel"));
                        mJinshou.setText(data.getString("today_num"));
                        mXiaji.setText(data.getString("todaynext_num"));
                    } else {
                        ToastUtils.showToast(getActivity(), jsonObject.getString("msg"), 1000);
                    }
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }


            @Override
            public void onHttpRequestError(int requestCode, Exception exception) {
                LogCatUtils.i("", "exception=========" + exception.toString());
                SimpleHUD.dismiss();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void httpMoney() {
        List<Parameter> list = new ArrayList<Parameter>();
        list.add(new Parameter("g", "Api"));
        list.add(new Parameter("m", "Agent"));
        list.add(new Parameter("a", "agentDic"));
        ;
        list.add(new Parameter("token", PreferenceUtils.getPrefString(getActivity(), "token", "")));

        HttpManager.getInstance().get(list, PreferenceUtils.getPrefString(getActivity(), "quyudaili", ""), 10001, new HttpManager.OnHttpResponseListener() {
            @Override
            public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                LogCatUtils.i("", "index=========" + resultData.toString());
                try {
                    SimpleHUD.dismiss();
                    JSONObject jsonObject = new JSONObject(resultData);
                    if (jsonObject.getString("status").equals("10001")) {
                        JSONObject result = jsonObject.getJSONObject("result");
                        List<MoneyBean> li = JSON.parseArray(result.getString("data"), MoneyBean.class);
                        money1 = li.get(0).getLabel();
                        num1 = li.get(0).getValue();
                        money2 = li.get(1).getLabel();
                        num2 = li.get(1).getValue();
                        if (li.size() > 2) {
                            money3 = li.get(2).getLabel();
                            num3 = li.get(2).getValue();
                        }

                    } else {
                        ToastUtils.showToast(getActivity(), jsonObject.getString("msg"), 1000);
                    }
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }


            @Override
            public void onHttpRequestError(int requestCode, Exception exception) {
                LogCatUtils.i("", "exception=========" + exception.toString());
                SimpleHUD.dismiss();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void initEvent() {
        mMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtil.toActivity(getActivity(), new Intent(getActivity(), MessageActivity.class), true);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 开始刷新，设置当前为刷新状态
                //swipeRefreshLayout.setRefreshing(true);
                // TODO
                httpdata();
                httpMoney();
                // 这个不能写在外边，不然会直接收起来
                //psvDisplay.setRefreshing(false);
            }
        });
        mChongzhiok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!VerifyUtils.verifyInputIsNull(mChongzhiid)) {
                    ToastUtils.showToast(getActivity(), "请输入要充值的玩家ID", 1000);
                    return;
                }
                List<Parameter> list = new ArrayList<Parameter>();
                list.add(new Parameter("g", "Api"));
                list.add(new Parameter("m", "Index"));
                list.add(new Parameter("a", "user"));
                list.add(new Parameter("userid", mChongzhiid.getText().toString().trim()));
                list.add(new Parameter("token", PreferenceUtils.getPrefString(getActivity(), "token", "")));

                HttpManager.getInstance().get(list, PreferenceUtils.getPrefString(getActivity(), "quyudaili", ""), 10001, new HttpManager.OnHttpResponseListener() {
                    @Override
                    public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                        LogCatUtils.i("", "index=========" + resultData.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(resultData);
                            if (jsonObject.getString("status").equals("10001")) {
                                JSONObject result = jsonObject.getJSONObject("result");
                                JSONObject data = result.getJSONObject("data");
                                if (data != null) {
                                    showSheetDialog(resultData);
                                }

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
//
            }
        });
    }

    private void showSheetDialog(String resultData) {
        View view = getActivity().getLayoutInflater().inflate(
                R.layout.tishi_buju, null);

        final Dialog dialog = new Dialog(getActivity(),
                R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.onWindowAttributesChanged(wl);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        TextView chongzhi = (TextView) view.findViewById(R.id.chongzhi);
        final TextView name = (TextView) view.findViewById(R.id.name);
        final TextView name_id = (TextView) view.findViewById(R.id.name_id);
        final TextView fangka = (TextView) view.findViewById(R.id.fangka);
        ImageView close = (ImageView) view.findViewById(R.id.close);
        ImageView mMine_head = (ImageView) view.findViewById(R.id.mine_head);
        LinearLayout shiyuan = (LinearLayout) view.findViewById(R.id.shiyuan);
        LinearLayout wushiyuan = (LinearLayout) view.findViewById(R.id.wushiyuan);
        TextView m1 = (TextView) view.findViewById(R.id.money1);
        TextView m2 = (TextView) view.findViewById(R.id.money2);
        TextView m3 = (TextView) view.findViewById(R.id.money3);
        if (money3.equals("")) {
            wushiyuan.setVisibility(View.GONE);
        } else {
            wushiyuan.setVisibility(View.VISIBLE);
        }
        m1.setText(money1);
        m2.setText(money2);
        m3.setText(money3);
        final CheckBox register_check = (CheckBox) view.findViewById(R.id.register_check);
        final CheckBox register_check1 = (CheckBox) view.findViewById(R.id.register_check1);
        final CheckBox register_check2 = (CheckBox) view.findViewById(R.id.register_check2);
        LinearLayout ershi = (LinearLayout) view.findViewById(R.id.ershi);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        try {
            JSONObject jsonObject = new JSONObject(resultData);
            if (jsonObject.getString("status").equals("10001")) {
                JSONObject result = jsonObject.getJSONObject("result");
                JSONObject data = result.getJSONObject("data");
                ImageLoaderUtils.displayImage(data.getString("img"), mMine_head, R.mipmap.fimg5, 10);
                name.setText(data.getString("nickname"));
                name_id.setText(data.getString("userid"));
                fangka.setText(data.getString("a_tel"));

            } else {
                ToastUtils.showToast(getActivity(), jsonObject.getString("desc"), 1000);
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        shiyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_check.setChecked(true);
                register_check1.setChecked(false);
                register_check2.setChecked(false);
            }
        });
        ershi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_check.setChecked(false);
                register_check1.setChecked(true);
                register_check2.setChecked(false);
            }
        });
        wushiyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_check.setChecked(false);
                register_check1.setChecked(false);
                register_check2.setChecked(true);
            }
        });
        register_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_check.setChecked(true);
                register_check1.setChecked(false);
                register_check2.setChecked(false);
            }
        });
        register_check1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_check.setChecked(false);
                register_check1.setChecked(true);
                register_check2.setChecked(false);
            }
        });
        register_check2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_check.setChecked(false);
                register_check1.setChecked(false);
                register_check2.setChecked(true);
            }
        });
        chongzhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Parameter> list = new ArrayList<Parameter>();
                list.add(new Parameter("g", "Api"));
                list.add(new Parameter("m", "Index"));
                list.add(new Parameter("a", "chagecard"));
                list.add(new Parameter("nick", name_id.getText().toString().trim()));
                if (register_check.isChecked()) {
                    list.add(new Parameter("num", num1));
                } else if (register_check1.isChecked()) {
                    list.add(new Parameter("num", num2));
                } else if (register_check2.isChecked()) {
                    list.add(new Parameter("num", num3));
                }
                list.add(new Parameter("agent_id", PreferenceUtils.getPrefString(getActivity(), "agentid", "")));
                list.add(new Parameter("token", PreferenceUtils.getPrefString(getActivity(), "token", "")));

                HttpManager.getInstance().get(list, PreferenceUtils.getPrefString(getActivity(), "quyudaili", ""), 10001, new HttpManager.OnHttpResponseListener() {
                    @Override
                    public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                        LogCatUtils.i("", "chagecard=========" + resultData.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(resultData);
                            if (jsonObject.getString("status").equals("10001")) {
                                ToastUtils.showToast(getActivity(), "充值成功", 1000);
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
                dialog.dismiss();
            }
        });
    }
}
