package com.tianji.dam.bean;

import com.tianji.dam.domain.*;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GlobCache {
    ConcurrentHashMap<Long, MatrixItem[][]> cacheData = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<Long, MatrixItem[][]> t1data2RedisCache = new ConcurrentHashMap<>();

    public static final Map<String, ConcurrentHashMap<Long, MatrixItem[][]>> t1T0redishistorycarCachData = new ConcurrentHashMap<>();
    public static final Map<String, ConcurrentHashMap<Long, MatrixItemRedisReal[][]>> t1T0redishistorycarCachDataCang = new ConcurrentHashMap<>();
    public static final String[] cartableprfix = {"", "ylj", "tpj", "ssc"};
    public static final Map<String, Integer> cartypecolorindex = new HashMap<>();
    public static final Integer[] carcoloconfigtype = {0, 1, 44, 55};

    public static final ConcurrentHashMap<Integer, TDayTask> cartaskmap = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<Integer, DamsConstruction> cartaskmapcang = new ConcurrentHashMap<>();

    public static final ConcurrentHashMap<String, Long> carhistorybacktime = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, Integer> carspeedwarning = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, Integer> houduwarning = new ConcurrentHashMap<>();
    public static Long carspeedtime = 0l;
    public static Long carhoudutime = 0l;

    public static final Map<Long, List<TColorConfig>> typecolors = new HashMap();
    public static final Map<String, Map<Integer, Color>> rtimecolors = new HashMap<>();
    public static final Map<Integer, DamsConstruction> daminfomap = new HashMap<>();
    public static final ConcurrentHashMap<String, ConcurrentHashMap<Long, MatrixItem[][]>> historycars = new ConcurrentHashMap<>();

    public static final Map<String, List<Float>> encode_gc = new ConcurrentHashMap<>();

    public static boolean iscaradd = false;
    public static Map<String, String> daytaskbaseevolution = new HashMap<>();


    public static final Map<String, Long> speedWarningNumber = new ConcurrentHashMap<>();


    static {
        cartypecolorindex.put("ylj", 1);
        cartypecolorindex.put("tpj", 44);
        cartypecolorindex.put("ssc", 3);
        t1T0redishistorycarCachData.put("ylj", new ConcurrentHashMap<>());
        t1T0redishistorycarCachData.put("tpj", new ConcurrentHashMap<>());
        t1T0redishistorycarCachData.put("ssc", new ConcurrentHashMap<>());
    }


    public static Map<String, MatrixItemNews[][]> CANGITEMS = new HashMap<>();
}
