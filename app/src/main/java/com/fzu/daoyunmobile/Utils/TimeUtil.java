package com.fzu.daoyunmobile.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    /**
     * 转换jsonformat的时间
     *
     * @param time
     * @return
     */
    public static String covertJsonFormatTime(String time) {
        if (time.length() > 10)
            return time.substring(0, time.length() - 10).replace('T', ' ');
        return getNowTimeString();
    }

    /**
     * @return 获取当前时间
     */
    public static String getNowTimeString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Date d = new Date();
        return df.format(d);
    }

}
