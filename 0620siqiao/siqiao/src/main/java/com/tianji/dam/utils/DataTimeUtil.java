package com.tianji.dam.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataTimeUtil {

    public static long timeToStamp(String timers) {
        Date d = new Date();
        long timeStemp = 0;
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            d = sf.parse(timers);// 日期转换为时间戳
        } catch (ParseException e) {
            e.printStackTrace();
        }
        timeStemp = d.getTime();
        return timeStemp;
    }
    

    public static long getdate2dateminute(Date d1, Date d2) {
		long seconds = (d2.getTime()-d1.getTime())/(1000*60);
    	return seconds;
	}
    
    public static long getdate2datedays(Date d1, Date d2) {
		long seconds = (d2.getTime()-d1.getTime())/(1000*60*60*24);
    	return seconds;
	}
    
    
    public static long getdate2datedays(String d1, String d2) throws ParseException {
    	
    	 SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	 Date    d11 = sf.parse(d1);// 日期转换为时间戳
    	
    	 Date    d22 = sf.parse(d2);// 日期转换为时间戳
    	 
		long seconds = (d22.getTime()-d11.getTime())/(1000*60*60*24);
    	return seconds;
	}
     

}
  