package com.tianji.dam.domain;

import com.tianji.dam.model.RealTimeRedisDataModel;
import com.tianji.dam.thread.HistoryCarDataTaskBlockingList;
import com.tianji.dam.thread.HistoryDataTaskBlockingList;
import com.tianji.dam.thread.RealTimeRedisRightPopTaskBlockingZhuang;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class StoreHouseMap {
    private static final Map<String, int[][]> storeHouses;

    public static Map<String, int[][]> getStoreHouses() {
        return storeHouses;
    }


    public static Map<String, Integer> carrollingtimes;

    static {
        storeHouses = new HashMap<String, int[][]>();
        carrollingtimes = new HashMap<>();
    }

    public static Map<String, Integer> getCarrollingtimes() {
        return carrollingtimes;
    }

    public static void setCarrollingtimes(Map<String, Integer> carrollingtimes) {
        StoreHouseMap.carrollingtimes = carrollingtimes;
    }

    private static final Map<String, List<String>> storehouseIdUsernameListMap;

    public static Map<String, List<String>> getStorehouseIdUsernameListMap() {
        return storehouseIdUsernameListMap;
    }

    static {
        storehouseIdUsernameListMap = new HashMap<String, List<String>>();
    }

    private static final Map<String, List<RealTimeRedisRightPopTaskBlockingZhuang>> realTimeRedisRightPopTaskBlockingZhuangList;
	public static Map<String, List<RealTimeRedisRightPopTaskBlockingZhuang>> getRealTimeRedisRightPopTaskBlockingZhuangList() {
		return realTimeRedisRightPopTaskBlockingZhuangList;
	}
	static {
		realTimeRedisRightPopTaskBlockingZhuangList = new HashMap<String, List<RealTimeRedisRightPopTaskBlockingZhuang>>();
	}


	private static final Map<String, MatrixItem[][]> storeHouses2RollingData;
	public static Map<String, MatrixItem[][]> getStoreHouses2RollingData() {

		
		return storeHouses2RollingData;
	}
	static {
		storeHouses2RollingData = new HashMap<>();
		storeHouses2RollingDataNews = new HashMap<>();
	}

	private static final Map<String, MatrixItemNews[][]> storeHouses2RollingDataNews;

	public static Map<String, MatrixItemNews[][]> getStoreHouses2RollingDataNews() {


		return storeHouses2RollingDataNews;
	}


	private static final Map<String, MatrixItem[][]> storeHouses2RollingDataCang;
	public static Map<String, MatrixItem[][]> getStoreHouses2RollingDataCang() {


		return storeHouses2RollingDataCang;
	}
	static {
		storeHouses2RollingDataCang = new HashMap<>();
	}



	private static volatile Map<String, StorehouseRange> shorehouseRange;
	public static Map<String, StorehouseRange> getShorehouseRange() {
		return shorehouseRange;
	}
	static {
		shorehouseRange = new ConcurrentHashMap<>();
	}

	private static volatile Map<String, RollingResult> rollingResultMap;

	private static volatile Map<String, RollingResult> websockforhistorymap;

	public static Map<String, RollingResult> getRollingResultMap() {
		return rollingResultMap;
	}
	static {
		rollingResultMap = new HashMap<String, RollingResult>();
	}

	//历史
	private static final Map<String, List<HistoryDataTaskBlockingList>> historyDataTaskBlockingList;
	public static Map<String, List<HistoryDataTaskBlockingList>> getHistoryDataTaskBlockingList() {
		return historyDataTaskBlockingList;
	}
	static {
		historyDataTaskBlockingList = new HashMap<String, List<HistoryDataTaskBlockingList>>();
	}
	//历史播放速度
	private static final Map<String, Long> historyDataSpeed;
	public static Map<String, Long> getHistoryDataSpeed() {
		return historyDataSpeed;
	}
	static {
		historyDataSpeed = new ConcurrentHashMap<String, Long>();
	}
	
	//历史播放速度
		private static final Map<String, Long> historycarDataSpeed;
		public static Map<String, Long> getHistorycarDataSpeed() {
			return historycarDataSpeed;
		}
		static {
			historycarDataSpeed = new ConcurrentHashMap<String, Long>();
		}
	
	

	private static final Map<String, MatrixItem[][]> history2RollingData;
	public static Map<String, MatrixItem[][]> getHistory2RollingData() {
		return history2RollingData;
	}
	static {
		history2RollingData = new ConcurrentHashMap<String, MatrixItem[][]>();
	}

	private static final Map<String, List<String>> historyUsernameListMap;
	public static Map<String, List<String>> getHistoryUsernameListMap() {
		return historyUsernameListMap;
	}
	static {
		historyUsernameListMap = new ConcurrentHashMap<String, List<String>>();
	}

	private static volatile Map<String, StorehouseRange> historyRange;
	public static Map<String, StorehouseRange> getHistoryRange() {
		return historyRange;
	}
	static {
		historyRange = new ConcurrentHashMap<String, StorehouseRange>();
	}

	private static volatile Map<String, RollingResult> historyResultMap;
	public static Map<String, RollingResult> getHistoryResultMap() {
		return historyResultMap;
	}
	static {
		historyResultMap = new ConcurrentHashMap<String, RollingResult>();
	}

	//历史 自定义车
	private static final Map<String, List<HistoryCarDataTaskBlockingList>> historyCarDataTaskBlockingList;
	public static Map<String, List<HistoryCarDataTaskBlockingList>> getHistoryCarDataTaskBlockingList() {
		return historyCarDataTaskBlockingList;
	}
	static {
		historyCarDataTaskBlockingList = new ConcurrentHashMap<String, List<HistoryCarDataTaskBlockingList>>();
	}


	//实时数据缓存 替代redis
	private static final Map<String, Queue<RollingData>> realTimeDataList;
	public static Map<String, Queue<RollingData>> getRealTimeDataList() {
		return realTimeDataList;
	}
	static {
		realTimeDataList = new ConcurrentHashMap<String, Queue<RollingData>>();
	}

	/*private static final Map<String, List<RollingData>> realTimeDataList;
	public static Map<String, List<RollingData>> getRealTimeDataList() {
		return realTimeDataList;
	}
	static {
		realTimeDataList = new HashMap<String, List<RollingData>>();
	}*/

	private static final Map<String, RealTimeRedisDataModel> realTimeRedisDataModelMap;
	public static Map<String, RealTimeRedisDataModel> getRealTimeRedisDataModelMap() {
		return realTimeRedisDataModelMap;
	}
	static {
		realTimeRedisDataModelMap = new ConcurrentHashMap<String, RealTimeRedisDataModel>();
	}




	//实时监控 仓位数据缓存 <仓ID,Map<瓦片ID,瓦片数据集>>
	private static final HashMap<String,ConcurrentHashMap<Long, MatrixItem[][]>> realTimeStorehouseDataMap;
	public static HashMap<String, ConcurrentHashMap<Long, MatrixItem[][]>> getRealTimeStorehouseDataMap() {
		return realTimeStorehouseDataMap;
	}
	public static ConcurrentHashMap<Long, MatrixItem[][]> getRealTimeStorehouseDataItem(String storehouseName) {
		ConcurrentHashMap<Long, MatrixItem[][]> matrixitems = realTimeStorehouseDataMap.get(storehouseName);
		if(matrixitems == null) {
			matrixitems = new ConcurrentHashMap<Long, MatrixItem[][]>();
			realTimeStorehouseDataMap.put(storehouseName, matrixitems);
		}
		return matrixitems;
	}
	static {
		realTimeStorehouseDataMap = new HashMap<String, ConcurrentHashMap<Long, MatrixItem[][]>>();
	}
	public static void clearstoreHouses2RollingData() {
		
		
	Set<String> keys =	storeHouses2RollingData.keySet();
	 for (String string : keys) {
		 MatrixItem[][] allolddata =	 storeHouses2RollingData.get(string);
		 for (int i = 0; i < allolddata.length; i++) {
			 for (int j = 0; j < allolddata[i].length; j++) {
				 allolddata[i][j]= null;
			}
		}
	}
			storeHouses2RollingData.clear();
			System.gc();
		
	}



	private static final ConcurrentHashMap<Long, MatrixItem[][]> historycarCachData;
	static {
		historycarCachData = new  ConcurrentHashMap<Long, MatrixItem[][]>();
	}

	public static ConcurrentHashMap  gethistorycarCachData(){

		 return historycarCachData;
	};


	private static final ConcurrentHashMap<Long, MatrixItem[][]> t1T0redishistorycarCachData;
	static {
		t1T0redishistorycarCachData = new  ConcurrentHashMap<Long, MatrixItem[][]>();
	}

	public static ConcurrentHashMap  gett1T0redishistorycarCachData(){

		return t1T0redishistorycarCachData;
	};
	public static void cleart1T0redishistorycarCachData(){
		t1T0redishistorycarCachData.clear();;
	}


}
