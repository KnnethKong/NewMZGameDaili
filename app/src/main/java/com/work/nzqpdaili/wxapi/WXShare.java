package com.work.nzqpdaili.wxapi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.work.nzqpdaili.R;
import com.work.nzqpdaili.utils.PreferenceUtils;
import com.work.nzqpdaili.utils.Util;


/**
 * Created by range on 2017/12/14.
 */

public class WXShare {
    private IWXAPI api;
    public void share(Activity activity,int i,String name,String id,String type,String url){
        api = WXAPIFactory.createWXAPI(activity, "wx287651ff7b2915ae");
        api.registerApp("wx287651ff7b2915ae");

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = name+" 俱乐部ID: "+id;
        String s="";
        if(type.equals("1")){
            s="(普通)";
        }else {
            s="(高级)";
        }
        msg.description = "我在"+ PreferenceUtils.getPrefString(activity, "quyum", "")+"创建了 "+name+s+" 俱乐部ID: "+id+",加入俱乐部等你来战！ ";
        Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.llogoi);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 90, 90, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        if(i==1){
            req.scene =  SendMessageToWX.Req.WXSceneSession;
        }else {
            req.scene =  SendMessageToWX.Req.WXSceneTimeline;
        }

        api.sendReq(req);
    }
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
