package com.tj.quartz.task;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 过滤池集合
 */
public class FilterMap {
    private  static ConcurrentHashMap<String,LatLngfilter> map = new ConcurrentHashMap<>();

    /**
     * 返回对应Key的过滤器
     * @param key
     * @return
     */
    public static LatLngfilter getFilter(String key){
        if(!map.containsKey(key)){
            LatLngfilter latLngfilter = new LatLngfilter(key);
            map.put(key,latLngfilter);
        }
        return map.get(key);
    }
}
