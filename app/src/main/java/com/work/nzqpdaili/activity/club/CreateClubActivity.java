package com.work.nzqpdaili.activity.club;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.selectavatar.utils.SDFileUtil;
import com.dev.selectavatar.utils.SelectAvatarUtil;
import com.dev.selectavatar.utils.compress.CompressHelper;
import com.work.nzqpdaili.R;
import com.work.nzqpdaili.utils.Event;
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
 * Created by range on 2017/12/13.
 */

public class CreateClubActivity extends BaseActivity implements View.OnClickListener {
    private RadioGroup radio_group;
    private LinearLayout is_pt;
    private TextView is_text;
    private ImageView create_que;
    private String type="2";
    String path="";
    private EditText name,num,miaoshu;
    private RelativeLayout head_relate;
    private CircleImageView head;
    @Override
    public Activity getActivity() {
        return this;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_create);
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
        ((TextView) findViewById(R.id.tv_title)).setText("创建俱乐部");
        TitleBarUtils.initBtnBack(this, R.id.tv_back);
        radio_group= (RadioGroup) findViewById(R.id.radio_group);
        is_pt= (LinearLayout) findViewById(R.id.is_pt);
        is_text= (TextView) findViewById(R.id.is_text);
        create_que= (ImageView) findViewById(R.id.create_que);
        name= (EditText) findViewById(R.id.create_name);
        num= (EditText) findViewById(R.id.create_num);
        head= (CircleImageView) findViewById(R.id.head);
        head_relate= (RelativeLayout) findViewById(R.id.head_relate);
        miaoshu= (EditText) findViewById(R.id.create_miaoshu);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        head_relate.setOnClickListener(this);
        create_que.setOnClickListener(this);
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.radio_left){
                    is_pt.setVisibility(View.VISIBLE);
                    is_text.setText("高级俱乐部: 玩家创建房间，消耗俱乐部房卡，不消耗创建者房卡");
                    type="2";
                }else if(i==R.id.radio_right){
                    is_pt.setVisibility(View.GONE);
                    is_text.setText("普通俱乐部: 玩家创建房间消耗该玩家房卡");
                    type="1";
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.head_relate:
                SelectAvatarUtil.showSheet(getActivity(), getSupportFragmentManager());
                break;
            case R.id.create_que:
                if(path.equals("")){
                    ToastUtils.showToast(getActivity(), "请选择俱乐部头像", 1000);
                    return;
                }
                if(name.getText().toString().trim().equals("")){
                    ToastUtils.showToast(getActivity(), "请输入俱乐部名称", 1000);
                    return;
                }
                if(miaoshu.getText().toString().trim().equals("")){
                    ToastUtils.showToast(getActivity(), "请输入俱乐部简介", 1000);
                    return;
                }
                if(type.equals("2")){
                    if(num.getText().toString().equals("")){
                        ToastUtils.showToast(getActivity(), "请输入房卡数量", 1000);
                        return;
                    }
                }
                httplist();
                break;
        }
    }
    private void httplist() {
        SimpleHUD.showLoadingMessage(getActivity(), "加载中...", true);
        String  url=PreferenceUtils.getPrefString(getActivity(),"quyudaili","")+"/?g=Api&m=Club&a=addclub";
        List<Parameter> list = new ArrayList<Parameter>();
        list.add(new Parameter("agent_id", PreferenceUtils.getPrefString(getActivity(),"agentid","")));
        list.add(new Parameter("token",PreferenceUtils.getPrefString(getActivity(),"token","")));
        list.add(new Parameter("type",type));
        list.add(new Parameter("name", name.getText().toString().trim()));
        list.add(new Parameter("tel", num.getText().toString()));
        list.add(new Parameter("desc", miaoshu.getText().toString()));
        File file = CompressHelper.getDefault(context).compressToFile(new File(path));
        list.add(new Parameter("face",file));
        HttpManager.getInstance().upload(getActivity(),list,url, 10001, new HttpManager.OnHttpResponseListener() {
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
                path  = SelectAvatarUtil.setPicToView(data);
                File file = new File(path);
                 //   Glide.with(this).load(file).into(ivAvatar);
//                GLoadingDialog.showDialogForLoading(getActivity(), "上传中...", true);
//                uploadAvatar(path);
                GlideUtil.loadImageView(getActivity(), R.mipmap.llogoi, path, head);
            }
        }
    }

}
