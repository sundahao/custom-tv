package com.qgd.commons.tv.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TimeUtils
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-8-24
 */
public class TimeUtils {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE    = new SimpleDateFormat("yyyy-MM-dd");

    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * long time to string
     * 
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     * 
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * get current time in milliseconds
     * 
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     * 
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     * 
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    public static String time2Str(long duration){
        if (duration <= 0) {
            return "00:00:00";
        }
        // 忽略更小的时间上的误差
        long s = duration / 1000;

        String time = "";
        // 首先计算有多少小时
        long h = s / (60 * 60);
        if(h <= 0){
            time = "00:";
        }else if(h < 10){
            time = "0" + h + ":";
        }else{
            time = h + ":";
        }

        // 计算分钟
        long m = (s - h * (3600)) / 60;
        if(m <= 0){
            time = time + "00:";
        }else if(m < 10){
            time = time + "0" + m + ":";
        }else{
            time = time + m + ":";
        }

        // 计算秒
        long ss = s - h * (3600) - m * 60;
        if(ss < 10){
            time = time + "0" + ss;
        }else{
            time = time + ss;
        }
        return time;
    }
}
