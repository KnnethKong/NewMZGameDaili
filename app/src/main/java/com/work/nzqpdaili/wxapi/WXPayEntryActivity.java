package com.work.nzqpdaili.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.work.nzqpdaili.R;
import com.work.nzqpdaili.activity.PayActivity;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, WXConfig.APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if (resp.errCode == 0) {
				//支付成功

				Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
				if (PayActivity.payActivity!=null){
					PayActivity.payActivity.finish();
				}
				finish();
			} else {
				//支付失败
				Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
//				Intent intent = new Intent(this, PayFailActivity.class);
//				startActivity(intent);
				finish();
			}
		}
		else
		{
			Log.i("baseResp.getType()", ">>" + resp.getType());
		}
	}
}