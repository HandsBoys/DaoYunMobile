package com.fzu.daoyunmobile.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间处理工具类
 */
public class TimeUtil {
    /**
     * 转换jsonformat的时间
     *
     * @param time
     * @return
     */
    public static String covertJsonFormatTime(String time) {
        if (!time.equals("null") || time.length() > 10)
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

    /**
     * str转换为Date
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static Date strConvertDate(String time) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.parse(time);
    }

    /**
     * 计算分钟数
     *
     * @param s
     * @param e
     * @return
     */
    public static long figMinute(Date s, Date e) {
        long start = s.getTime();

        long end = e.getTime();

        long min = (end - start) / (1000 * 60);
        return min;

    }

}
