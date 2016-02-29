package com.qgd.commons.tv.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 作者：ethan on 2015/12/23 19:55
 * 邮箱：ethan.chen@fm2020.com
 */
public class DateUtil {
    private static SimpleDateFormat sf = null;
    private static String defaultFormat = "yyyy-MM-dd HH:mm:ss";

    /*获取系统时间 格式为："yyyy/MM/dd "*/
    public static String getCurrentDate() {
        Date d = new Date();
        sf = new SimpleDateFormat(defaultFormat);
        return sf.format(d);
    }
     public static String getCurrentDate(String format) {
        Date d = new Date();
        sf = new SimpleDateFormat(format);
        return sf.format(d);
    }

    /*时间戳转换成字符窜*/
    public static String getDateToString(long time) {
        Date d = new Date(time);
        sf = new SimpleDateFormat(defaultFormat);
        return sf.format(d);
    }



    /*将字符串转为时间戳*/
    public static long getStringToDateLong(String time) {
        return getStringToDateLong(time, defaultFormat);
    }
    public static Date getStringToDate(String time) {
        return getStringToDate(time, defaultFormat);
    }

    public static long getStringToDateLong(String time, String format) {
        sf = new SimpleDateFormat(format);
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }
    public static Date getStringToDate(String time,String format) {
        sf = new SimpleDateFormat(format);
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 解析北京时间字符串
     * @param time
     * @param format
     * @return
     */
    public static Date parseGmt8Time(String time, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        try {
            return df.parse(time);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public static long getCurrentDateLong(){
        return new Date().getTime();
    }
}

