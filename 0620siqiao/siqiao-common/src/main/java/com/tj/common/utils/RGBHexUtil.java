package com.tj.common.utils;

/**
 * rgb与16进制颜色互相转换
 */
public class RGBHexUtil {

    /**
     * 16进制颜色字符串转换成rgb
     * @param hexStr
     * @return rgb
     */
    public static int[] hex2RGB(String hexStr){
        if(hexStr != null && !"".equals(hexStr) && hexStr.length() == 7){
            int[] rgb = new int[3];
            rgb[0] = Integer.valueOf(hexStr.substring( 1, 3 ), 16);
            rgb[1] = Integer.valueOf(hexStr.substring( 3, 5 ), 16);
            rgb[2] = Integer.valueOf(hexStr.substring( 5, 7 ), 16);
            return rgb;
        }
        return null;
    }

    /**
     * rgb转换成16进制
     * @param r
     * @param g
     * @param b
     * @return
     */
    public static String rgb2Hex(int r,int g,int b){
        return String.format("#%02X%02X%02X", r,g,b);
    }

}
