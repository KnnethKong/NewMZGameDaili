package com.work.nzqpdaili.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by range on 2017/12/18.
 */

public class TimeUtil {
    public static Long GetTime() {
        Calendar c = Calendar.getInstance();
        String year = c.get(Calendar.YEAR) + "";
        String month = c.get(Calendar.MONTH)+1 + "";
        String day = c.get(Calendar.DAY_OF_MONTH) + "";
        String hour = c.get(Calendar.HOUR_OF_DAY) + "";
        String time=year+"."+month+"."+day+" "+hour;
        return Long.parseLong(data(time));
    }
    /**
     * 掉此方法输入所要转换的时间输入例如（"2014年06月14日16时09分00秒"）返回时间戳
     *
     * @param time
     * @return
     */
    public  static  String data(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy.MM.dd HH",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }
}
