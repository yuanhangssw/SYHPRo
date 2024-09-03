package com.ruoyi.web.controller.termite.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MileageUtils {

    public static void main(String[] args) {
        mtolist1("DL1+200.16", "DL23+325.25");
        List<String> result = mtolist("K1+200.16", "K22+325.25",2);
        for (String str : result) {
            System.out.println(str);
        }

    }

    public static  List<String> mtolist1(String start, String end){

       return mtolist( start,  end,  1);
    }


    /**
     * 根据传入的开始结束里程，拆分成list
     * @param start 开始桩号
     * @param end 结束桩号
     * @param step     间隔公里
     * @return List的桩号集合1公里1条
     */
    public static List<String> mtolist(String start, String end, int step) {
        List<String> result = new ArrayList<>();

        Pattern pattern = Pattern.compile("(\\D+)(\\d+)\\+(\\d+(\\.\\d+)?)");
        Matcher matcherStart = pattern.matcher(start);
        Matcher matcherEnd = pattern.matcher(end);

        if (matcherStart.matches() && matcherEnd.matches()) {
            String startKey = matcherStart.group(1);
            int startNum = Integer.parseInt(matcherStart.group(2));
            String startValue = "+" + matcherStart.group(3);

            String endKey = matcherEnd.group(1);
            int endNum = Integer.parseInt(matcherEnd.group(2));
            String endValue = "+" + matcherEnd.group(3);

            result.add(startKey + startNum + startValue + "-" + startKey + (startNum + step) + "+00");

            for (int i = startNum + step; i + step < endNum; i += step) {
                result.add(startKey + i + "+00" + "-" + startKey + (i + step) + "+00");
            }

            int prev = Math.max(startNum, endNum - step);
            result.add(startKey + prev + "+00" + "-" + endKey + endNum + "+00");

            if (endNum > startNum) {
                result.add(endKey + endNum + "+00" + "-" + endKey + endNum + endValue);
            }
        }

        return result;
    }
}
