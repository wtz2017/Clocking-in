package com.test.clocking_in.data;

/**
 * Created by WTZ on 2016/12/14.
 */

public class DateTimeRecord {
    boolean isWeekSummary;// 是否为周总结
    String dateTime;
    int weeks;
    int weekday;
    long milliseconds;
    int weekWorkTime;
    int weekSummaryIndex;

    public int getWeekSummaryIndex() {
        return weekSummaryIndex;
    }

    public void setWeekSummaryIndex(int weekSummaryIndex) {
        this.weekSummaryIndex = weekSummaryIndex;
    }

    public int getWeekWorkTime() {
        return weekWorkTime;
    }

    public void setWeekWorkTime(int weekWorkTime) {
        this.weekWorkTime = weekWorkTime;
    }

    public boolean isWeekSummary() {
        return isWeekSummary;
    }

    public void setWeekSummary(boolean weekSummary) {
        isWeekSummary = weekSummary;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getWeeks() {
        return weeks;
    }

    public void setWeeks(int weeks) {
        this.weeks = weeks;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }

}
