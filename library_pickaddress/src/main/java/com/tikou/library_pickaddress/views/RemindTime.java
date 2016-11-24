package com.tikou.library_pickaddress.views;

import com.tikou.library_pickaddress.StringUtils;

/**
 * Created by Administrator on 2015/12/25.
 */
public class RemindTime {

    String type_text;


    String hour_text;


    String minute_text;


    public RemindTime() {
    }

    public RemindTime(String type_text, String hour_text, String minute_text) {
        this.type_text = type_text;
        this.hour_text = hour_text;
        this.minute_text = minute_text;
    }

    public String getType_text() {
        return type_text;
    }

    public void setType_text(String type_text) {
        this.type_text = type_text;
    }


    public String getHour_text() {
        return hour_text;
    }

    public void setHour_text(String hour_text) {
        this.hour_text = hour_text;
    }

    public String getMinute_text() {
        return minute_text;
    }

    public void setMinute_text(String minute_text) {
        this.minute_text = minute_text;
    }


    public String getTotalTime() {
        try {
            int hour = Integer.parseInt(hour_text.replace("时", ""));
            String minute = minute_text.replace("分", "");

            if (StringUtils.isEqualSt(type_text, "下午")) {
                hour = hour + 12 == 24 ? 0 : hour + 12;
            }
            return (hour > 9 ? String.valueOf(hour) : "0" + String.valueOf(hour)) + ":" + minute;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "";
        }
    }


}
