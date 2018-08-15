package com.work.nzqpdaili.utils;

/**
 * Created by cn on 2016/8/8.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 验证工具
 *
 * @author Vayne
 */
public class VerifyUtils {
    /**
     * 保留两位小数
     * */
    public static String get2Places(double double_value) {
        try {
            DecimalFormat df = new DecimalFormat("#####0.00");
            return df.format(double_value);
        } catch (Exception ex) {
            return "0.00";
        }
    }
    /**
     * 验证输入是否为空
     *
     * @return
     */
    public static boolean verifyInputIsNull(EditText et) {
        if (et.getText() != null && !et.getText().toString().equals("")) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16时09分00秒"）
     *
     * @param time
     * @return
     */
    public static String times(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

    /**
     * 检查字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        boolean flag = true;
        if (str == null || "".equals(str) || str.equals("null")) {
            flag = false;
        }
        return flag;
    }

    /**
     * 请求成功判断 ps:判断JSON 字符串 中的 status 是10001
     *
     * @param str
     * @return
     */
//    public static boolean is10001(String response) {
//        if (JSONUtils.getJsonStr(response, "status").equals("10001")) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//    public static boolean is10004(String response) {
//        if (JSONUtils.getJsonStr(response, "status").equals("10004")) {
//            return true;
//        } else {
//            return false;
//        }
//    }
    /**
     * 请求成功判断 ps:判断JSON 字符串 中的 status 是10001
     *
     * @param str
     * @return
     */
//    public static boolean is10006(String response) {
//        if (JSONUtils.getJsonStr(response, "status").equals("10006")) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    /**
     * 判断是否有网
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Button倒计时
     *
     * @param btn
     */
    public static void startBtnTimer(final Button btn) {
        CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                btn.setText(millisUntilFinished / 1000 + "秒");
                btn.setClickable(false);// 防止重复点击
            }

            @Override
            public void onFinish() {
                // 可以在这做一些操作,如果没有获取到验证码再去请求服务器
                btn.setClickable(true);// 防止重复点击
                btn.setText("获取验证码");
//                btn.setBackgroundResource(R.drawable.button3);
            }
        };
        countDownTimer.start();
    }

    /**
     * 转化Html中的代替符
     *
     * @param notice
     * @return
     */
    public static String convertHtml(String notice) {

        notice = notice.replaceAll("&quot;", "\"");
        notice = notice.replaceAll("&lt;", "<");
        notice = notice.replaceAll("&gt;", ">");
        notice = notice.replaceAll("&amp;", "&");
        notice = notice.replaceAll("&apos;", "'");

        // LogUtils.e("HTML:"+notice);
        return notice;
    }

    public static boolean verifyPwd(String pwd) {
        Pattern pat = Pattern.compile("[\\da-zA-Z]{6,20}");
        Pattern patno = Pattern.compile(".*\\d.*");
        Pattern paten = Pattern.compile(".*[a-zA-Z].*");
        Matcher mat = pat.matcher(pwd);
        Matcher matno = patno.matcher(pwd);
        Matcher maten = paten.matcher(pwd);
        return matno.matches() && maten.matches() && mat.matches();
    }

//    public static boolean isLogin(Context context) {
//        if (!SharedPreferencesUtils.getString(context,
//                SharedPreferencesUtils.USER_ID, "").equals("")
//                && !SharedPreferencesUtils.getString(context,
//                SharedPreferencesUtils.USER_TOKEN, "").equals("")) {
//            return true;
//        } else {
//            return false;
//        }
//    }
    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
		/*
		移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		联通：130、131、132、152、155、156、185、186
		电信：133、153、180、189、（1349卫通）
		总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		*/
        String telRegex = "[1][34587]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }
    public static String[] isQianmi(String mi) {

        if (mi == null || Float.parseFloat(mi) == 0) {
            return new String[] { "0km", "0" };
        }
        if (Float.parseFloat(mi) < 1000) {
            return new String[] {( mi + "m"), "0" };
        }
        float f = Float.parseFloat(mi) * 1f / 1000;
        if (f > 3) {
            // map.put(String.valueOf(Math.round(f * 10) / 10.0) + "km", true);
            return new String[] {
                    (String.valueOf(Math.round(f * 10) / 10.0) + "km"), "1" };
        } else {
            return new String[] {
                    (String.valueOf(Math.round(f * 10) / 10.0) + "km"), "0" };
        }
    }
}

