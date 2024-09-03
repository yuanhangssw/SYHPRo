package com.tj.web.controller.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Testsm {

    public static void main(String[] args) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr1 = "2023-11-12 06:00:00.000";
        String dateStr2 = "2023-11-12 00:00:00.000";

        try {
            Date date1 = format.parse(dateStr1);
            Date date2 = format.parse(dateStr2);

            getDifference(date1, date2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void getDifference(Date date1, Date date2) {
        long diff = date2.getTime() - date1.getTime();

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        System.out.println("两个时间相差: ");
        System.out.println(diffDays + " 天, ");
        System.out.println(diffHours + " 小时, ");
        System.out.println(diffMinutes + " 分钟, ");
        System.out.println(diffSeconds + " 秒.");
    }


}
