package com.work.nzqpdaili.wxapi;


//import com.umeng.weixin.callback.WXCallbackActivity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.work.nzqpdaili.R;
import com.work.nzqpdaili.utils.ToastUtils;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    /**分享到微信接口**/
    private IWXAPI mWxApi;
    /**分享结果信息**/
    private TextView txtShareResult;
    /**分享结果图片**/
    private ImageView imgShareResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.share);

        mWxApi = WXAPIFactory.createWXAPI(this,"wx287651ff7b2915ae");
        mWxApi.registerApp("wx287651ff7b2915ae");
        mWxApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWxApi.handleIntent(intent, this);
    }

    /***
     * 请求微信的相应码
     * @author YOLANDA
     */
    @Override
    public void onResp(BaseResp baseResp) {

       // setTitle("分享失败");
        int result = 0;
       // Log.i("错误号：" + baseResp.errCode + "；信息：" + baseResp.errStr);
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
             //   setTitle("分享成功");
                ToastUtils.showToast(this, "分享成功", 1000);
//                result = R.string.sharewechat_success;//成功
//                imgShareResult.setImageResource(R.drawable.operation_succeed);
//                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
               // ToastUtils.showToast(this, "分享取消", 1000);
             // result = R.string.sharewechat_cancel;//取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
               // ToastUtils.showToast(this, "分享失败", 1000);
              //  result = R.string.sharewechat_deny;//被拒绝
                break;
            default:
               // ToastUtils.showToast(this, "分享取消", 1000);
           //     result = R.string.sharewechat_back;//返回
                break;
        }
        finish();
     //   txtShareResult.setText(result);
    }

    /**微信主动请求我们**/
    @Override
    public void onReq(BaseReq baseResp) {
        try {
//            Intent intent = new Intent(Application.getInstance(), MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Application.getInstance().startActivity(intent);
        } catch (Exception e) {
        }
    }
}
