package com.work.nzqpdaili.wxapi;



import com.work.nzqpdaili.utils.LogCatUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/11/29.
 */

public class GetWechatJson {

    private static final String TAG = "hezb";
    /**
     * 获取token等信息的地址
     * 传递参数: appid, secret, code, grant_type = authorization_code
     * 返回参数：access_token, expires_in, refresh_token, openid, scope, unionid
     */
    private static final String TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";

    /**
     * 获取用户信息的地址
     * 传递参数：access_token, openid
     * 返回参数：openid, nickname, sex, province, city, country, headimgurl, privilege, unionid
     */
    private static final String USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo";

    /**
     * 获取微信ID
     */
    public static String getWXOpenID(String code) {
        return getJsonResultByUrlPath(TOKEN_URL + "?appid=" + WXConfig.APP_ID + "&secret=" + WXConfig.APP_SECRET + "&code=" + code + "&grant_type=authorization_code");
    }

    /**
     * 获取微信用户信息
     */
    public static String getWXUserinfo(String openid, String access_token) {
        return getJsonResultByUrlPath(USERINFO_URL + "?access_token=" + access_token + "&openid=" + openid);
    }
//    @Override
//    public void run() {
//        super.run();
//
//        try {
//
//            JSONObject jsonObject = new JSONObject(jsonResult);
//            String accessToken = jsonObject.optString("access_token", "");
//            String openid = jsonObject.optString("openid", "");
//
//            if (!TextUtils.isEmpty(accessToken) && !TextUtils.isEmpty(openid)) {
//                String userInfo = getJsonResultByUrlPath(context, USERINFO_URL + "?access_token=" + accessToken + "&openid=" + openid);
//                if (listener != null && !TextUtils.isEmpty(userInfo)) {
//                    listener.onComplete(userInfo);
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }

    /**
     * 网路请求
     */
    private static String getJsonResultByUrlPath(String urlPath) {
        LogCatUtils.e("微信请求链接", urlPath);
        String result = "";
        URL url;
        try {
            url = new URL(urlPath);
            HttpURLConnection urlConn = (HttpURLConnection) url
                    .openConnection();  //创建一个HTTP连接
            InputStreamReader in = new InputStreamReader(
                    urlConn.getInputStream()); // 获得读取的内容
            BufferedReader buffer = new BufferedReader(in); // 获取输入流对象
            String inputLine = null;
            //通过循环逐行读取输入流中的内容
            while ((inputLine = buffer.readLine()) != null) {
                result += inputLine + "\n";
            }
            in.close(); //关闭字符输入流对象
            urlConn.disconnect();   //断开连接
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogCatUtils.e("微信请求结果", result);
        return result;
    }
}
