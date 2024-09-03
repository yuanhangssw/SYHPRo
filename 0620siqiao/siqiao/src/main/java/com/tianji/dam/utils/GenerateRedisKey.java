package com.tianji.dam.utils;

import com.tj.common.utils.DateUtils;

/**
 * 生成Redis的Key
 */
public class GenerateRedisKey {
    private static final String NORMALRID = "riddata_m_";
    private static final String TASKKEY = "TempRealCang_";

    /**
     * 用于实时监控中返回当天数据中对应Rid数据
     */
    public static String realTimeRidKey(long rid, String cartype) {
        String timekey_rid = NORMALRID + DateUtils.getDate();
        return timekey_rid + "_" + cartype + "_" + rid;
    }

    public static String realTimeAllRidKey(String cartype) {
        String timekey_rid = NORMALRID + DateUtils.getDate();
        return timekey_rid + "_" + cartype + "_*";
    }

    /**
     * 用于实时监控中返回当天所有Rid数据
     */
    public static String realTimeAllRidKeybycartype(String cartype) {
        String timekey_rid = NORMALRID + DateUtils.getDate();
        return timekey_rid + "_" + cartype + "*";
    }

    public static String realTimeAllRidKeybycartype_time(String time, String cartype) {
        String timekey_rid = NORMALRID + time;
        return timekey_rid + "_" + cartype + "*";
    }

    public static String taskrediskeysbycartype(String rediskey, String cartype) {

        return TASKKEY + cartype + "_" + rediskey + "_" + "*";
    }
    public static String taskrediskeysbycartypeCang(String rediskey, String cartype) {

        return TASKKEY + cartype + "==" + rediskey + "==" + "*";
    }
    /**
     * 用于获取该用户在Redis中该仓位下对应Rid数据
     */
    public static String realTimeStorehouseKey(String userName, String storehosue, long rid) {
        return userName + "_" + storehosue + "_" + rid;
    }

    /**
     * 用于获取该用户在Redis中该仓位下所有Rid数据
     */
    public static String realTimeStorehouseLikeKey(String userName, String storehosue) {
        return userName + "_" + storehosue + "_" + "*";
    }

    /**
     * 根据Redis中的key获取该用户在Redis中该仓位下的Rid
     */
    public static String splitRealTimeStorehouse(String key) {
        String[] array = key.split("_");
        if (array.length == 3) {
            return array[2];
        }
        return null;
    }

    public static String splitRealTimeStorehouse2(String key) {
        String[] array = key.split("_");
        if (array.length == 5) {
            return array[4];
        }
        if (array.length == 3) {
            return array[2];
        }
        if (array.length == 4) {
            return array[3];
        }
        return null;
    }

    public static String splitRealTimeStorehouse3(String key) {
        String[] array = key.split("_");
        if (array.length == 4) {
            return array[3];
        } else if (array.length == 5) {
            return array[4];
        }
        return null;
    }
    public static String splitRealTimeStorehouseCang(String key) {
        String[] array = key.split("==");
                return   array[2];

    }

    public static String realTimeAllRidKey2(String date, String cartype) {
        String timekey_rid = NORMALRID + date;
        return timekey_rid + "_" + cartype + "_" + "*";
    }


    public static String realTimeRidKey2(String date, long rid, String cartype) {
        String timekey_rid = NORMALRID + date;
        return timekey_rid + "_" + cartype + "_" + rid;
    }
}
