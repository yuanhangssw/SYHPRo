package com.tianji.dam.mileageutil;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {
    /**
     * 里程数值转里程字符串
     * @param mile:里程值
     * @param dkName:里程冠号
     */
    public static String mileage2Dk(double mile,String dkName) {
        int h = (int) mile / 1000;
        double l = mile - h * 1000;
        String s = String.format(Locale.SIMPLIFIED_CHINESE,"%s%d+%.2f",dkName,h,l);
        s = s.replaceAll("0+?$", "");//去掉后面无用的零
        s = s.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
        return s;
    }

    /**
     * 里程字符串转里程
     * @param dk:里程字符串 要求格式为L0+000
     * @return
     */
    public static double dk2Mileage(String dk){
        if(dk == null || dk.equals("")){
            return 0;
        }
        String regex = "^([A-Z,a-z]*)([0-9]+)\\+(([0-9]+)(\\.[0-9]+)?)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(dk);
        if(matcher.find()){
            int h = Integer.parseInt(matcher.group(2));
            float l = Float.parseFloat(matcher.group(3));
            double milaege = h * 1000 + l;
            return milaege;
        }else{
            throw new IllegalArgumentException("格式不匹配");
        }
    }
}
