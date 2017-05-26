package com.test.clocking_in.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {

    /**
     * @param format
     *            e.g. "yy-MM-dd_HH-mm-ss"
     * @return DateTime
     */
    public static String getCurrentDateTime(String format) {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat(format);
        String nowTime = df.format(date);
        return nowTime;
    }

    /**
     * @param format
     *            e.g. "yy-MM-dd_HH-mm-ss"
     * @return DateTime
     */
    public static String getSpecifiedDateTime(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        String nowTime = df.format(date);
        return nowTime;
    }

    /**
     * @param dateString
     *            e.g. "2016‐03‐03 14:28:02"
     * @param format
     *            e.g. "yyyy-MM-dd HH:mm:ss"
     * @return Date
     */
    public static Date changeStringToDate(String dateString, String format) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(dateString);
        } catch (Exception e) {
        }
        return date;
    }

    /**
     * 获取指定时间对应的毫秒数
     * 
     * @param time
     *            "HH:mm:ss"
     * @return
     */
    public static long getTimeMillis(String time) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
            Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);
            return curDate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取指定日期时间对应的毫秒数
     *
     * @param dateTime
     *            "2016-12-08 12:00:00"
     * @param format
     *            "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static long getDateTimeMillis(String dateTime, String format) {
        Date date = DateTimeUtil.changeStringToDate(dateTime, format);
        if (date == null) {
            return 0;
        }
        return date.getTime();
    }

    public static int getWeekOfYear(Calendar calendar) {
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day==1) {
            // Calendar的周一实际为我们中国人的上周的星期七
            week=week-1;
        }
        return week;
    }

    /**
     * 参数举例
     * String format = "yyyy-MM-dd";
     * String dateString = "2016-12-8";
     */
    public static int getWeekOfYear(String dateString, String format) {
        Date date = DateTimeUtil.changeStringToDate(dateString, format);
        if (date == null) {
            return 0;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day==1) {
            // Calendar的周一实际为我们中国人的上周的星期七
            week=week-1;
        }
        return week;
    }

    public static int getWeekOfMonth(Calendar calendar) {
        int week = calendar.get(Calendar.WEEK_OF_MONTH);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day==1) {
            // Calendar的周一实际为我们中国人的上周的星期七
            week=week-1;
        }
        return week;
    }

    /**
     * 参数举例
     * String format = "yyyy-MM-dd";
     * String dateString = "2016-12-8";
     */
    public static int getWeekOfMonth(String dateString, String format) {
        Date date = DateTimeUtil.changeStringToDate(dateString, format);
        if (date == null) {
            return 0;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(Calendar.WEEK_OF_MONTH);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day==1) {
            // Calendar的周一实际为我们中国人的上周的星期七
            week=week-1;
        }
        return week;
    }

    public static int getDayOfWeek(Calendar calendar) {
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day==1) {
            // Calendar的周一实际为我们中国人的上周的星期七
            day=7;
        } else {
            day=day-1;
        }
        return day;
    }

    /**
     * 参数举例
     * String format = "yyyy-MM-dd";
     * String dateString = "2016-12-8";
     */
    public static int getDayOfWeek(String dateString, String format) {
        Date date = DateTimeUtil.changeStringToDate(dateString, format);
        if (date == null) {
            return 0;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day==1) {
            // Calendar的周一实际为我们中国人的上周的星期七
            day=7;
        } else {
            day=day-1;
        }
        return day;
    }
}
