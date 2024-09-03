package com.tianji.dam.utils;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapUtil {

    /**
     * 求Map<K,V>中Key(键)的最大值
     * @param map
     * @return
     */
    public static Object getMaxKey(Map<Integer, Color> map) {
        if (map == null) return null;
        Set<Integer> set = map.keySet();
        Object[] obj = set.toArray();
        Arrays.sort(obj);
        return obj[obj.length-1];
    }

    public static void main(String[] args) {
        Map<Integer, Color> map = new HashMap<>();
        map.put(1,new Color(1,1,1));
        map.put(2,new Color(2,1,1));
        map.put(3,new Color(3,1,1));
        map.put(4,new Color(4,1,1));

        Integer maxKey = (Integer) getMaxKey(map);
        Color c = map.get(maxKey);
        System.out.println(maxKey);

    }
}
