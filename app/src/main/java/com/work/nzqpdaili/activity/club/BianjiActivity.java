package com.work.nzqpdaili.activity.club;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dev.selectavatar.utils.SDFileUtil;
import com.dev.selectavatar.utils.SelectAvatarUtil;
import com.dev.selectavatar.utils.compress.CompressHelper;
import com.work.nzqpdaili.Bean.BianjiBean;
import com.work.nzqpdaili.R;
import com.work.nzqpdaili.utils.Event;
import com.work.nzqpdaili.utils.ImageLoaderUtils;
import com.work.nzqpdaili.utils.LogCatUtils;
import com.work.nzqpdaili.utils.PreferenceUtils;
import com.work.nzqpdaili.utils.ToastUtils;
import com.work.nzqpdaili.view.CircleImageView;
import com.work.nzqpdaili.view.TitleBarUtils;
import com.work.nzqpdaili.view.hud.SimpleHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.model.Parameter;
import zuo.biao.library.util.glide.GlideUtil;

/**
 * Created by range on 2017/12/16.
 */

public class BianjiActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout head_relate;
    private CircleImageView head;
    private BianjiBean bianjiBean;
    private EditText name,miaoshu;
    private ImageView que;
    String path="";
    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_bianji);
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
        ((TextView) findViewById(R.id.tv_title)).setText("编辑");
        TitleBarUtils.initBtnBack(this, R.id.tv_back);
        head_relate= (RelativeLayout) findViewById(R.id.head_relate);
        head= (CircleImageView) findViewById(R.id.head);
        name= (EditText) findViewById(R.id.name);
        miaoshu= (EditText) findViewById(R.id.miaoshu);
        que= (ImageView) findViewById(R.id.que);
    }

    @Override
    public void initData() {
        httplist();
    }

    @Override
    public void initEvent() {
        head_relate.setOnClickListener(this);
        que.setOnClickListener(this);
    }
    private void httplist() {
        SimpleHUD.showLoadingMessage(getActivity(), "加载中...", true);
        List<Parameter> list = new ArrayList<Parameter>();
        list.add(new Parameter("g", "Api"));
        list.add(new Parameter("m", "Club"));
        list.add(new Parameter("a", "editclub"));
        list.add(new Parameter("agent_id", PreferenceUtils.getPrefString(getActivity(),"agentid","")));
        list.add(new Parameter("token",PreferenceUtils.getPrefString(getActivity(),"token","")));
        list.add(new Parameter("clubid",getIntent().getStringExtra("id")));

        HttpManager.getInstance().get(list, PreferenceUtils.getPrefString(getActivity(),"quyudaili",""), 10001, new HttpManager.OnHttpResponseListener() {
            @Override
            public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                LogCatUtils.i("", "clubadd=========" + resultData.toString());
                try {
                    SimpleHUD.dismiss();
                    JSONObject jsonObject = new JSONObject(resultData);
                    if (jsonObject.getString("status").equals("10001")) {
                        JSONObject jsonObject1=jsonObject.getJSONObject("result");
                        bianjiBean= JSON.parseObject(jsonObject1.getString("data"),BianjiBean.class);
                        setBean();
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
    private void setBean(){
        ImageLoaderUtils.displayImage(bianjiBean.getFace(),head,R.mipmap.mo_toux,10);
        name.setText(bianjiBean.getName());
        miaoshu.setText(bianjiBean.getDesc());
        path=bianjiBean.getFace();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.head_relate:
                SelectAvatarUtil.showSheet(getActivity(), getSupportFragmentManager());
                break;
            case R.id.que:
                if(path.equals("")){
                    ToastUtils.showToast(getActivity(), "请选择头像", 1000);
                    return;
                }
                httpcommit();
                break;
        }
    }
    private void httpcommit(){
        SimpleHUD.showLoadingMessage(getActivity(), "加载中...", true);
        String  url=PreferenceUtils.getPrefString(getActivity(),"quyudaili","")+"/?g=Api&m=Club&a=editclub_post";

        List<Parameter> list = new ArrayList<Parameter>();
        list.add(new Parameter("agent_id", PreferenceUtils.getPrefString(getActivity(),"agentid","")));
        list.add(new Parameter("token",PreferenceUtils.getPrefString(getActivity(),"token","")));
        list.add(new Parameter("clubid",getIntent().getStringExtra("id")));
        list.add(new Parameter("name",name.getText().toString()));
        list.add(new Parameter("desc",miaoshu.getText().toString()));
        if(path.contains("http")){
            list.add(new Parameter("face",path));
        }else {
            File file = CompressHelper.getDefault(context).compressToFile(new File(path));
            list.add(new Parameter("face",file));
        }
        HttpManager.getInstance().upload(getActivity(),list, url, 10001, new HttpManager.OnHttpResponseListener() {
            @Override
            public void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson) {
                LogCatUtils.i("", "clubadd=========" + resultData.toString());
                try {
                    SimpleHUD.dismiss();
                    JSONObject jsonObject = new JSONObject(resultData);
                    if (jsonObject.getString("status").equals("10001")) {
                        ToastUtils.showToast(getActivity(), jsonObject.getString("msg"), 1000);
                        EventBus.getDefault().post(
                                new Event("wan"));
                        finish();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SelectAvatarUtil.OpenCamera) {
            SelectAvatarUtil.startPhotoZoom(getActivity(), SDFileUtil.getPhotoCachePath() + "temp.jpg");
        } else if (requestCode == SelectAvatarUtil.OpenAlbum && data != null) {
            SelectAvatarUtil.startPhotoZoom(getActivity(), SelectAvatarUtil.getRealFilePath(getActivity(), data.getData()));
        } else if (requestCode == SelectAvatarUtil.OpenPreview) {
            if (data != null) {
               path = SelectAvatarUtil.setPicToView(data);
                //   Glide.with(this).load(file).into(ivAvatar);
//                GLoadingDialog.showDialogForLoading(getActivity(), "上传中...", true);
//                uploadAvatar(path);
                GlideUtil.loadImageView(getActivity(), R.mipmap.llogoi, path, head);
            }
        }
    }
}
