package com.tianji.dam.analysis;


import com.tianji.dam.domain.MatrixItem;
import com.tianji.dam.domain.StorehouseRange;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StoreHouseMapAnalysis {
	private static final Map<String, int[][]> storeHouses;
	public static Map<String, int[][]> getStoreHouses() {
		return storeHouses;
	}
	static {
		storeHouses = new HashMap<>();
	}

	private static final Map<String, List<String>> storehouseIdUsernameListMap;
	public static Map<String, List<String>> getStorehouseIdUsernameListMap() {
		return storehouseIdUsernameListMap;
	}
	static {
		storehouseIdUsernameListMap = new HashMap<String, List<String>>();
	}

	private static final Map<String, List> realTimeRedisRightPopTaskBlockingZhuangList;
	public static Map<String, List> getRealTimeRedisRightPopTaskBlockingZhuangList() {
		return realTimeRedisRightPopTaskBlockingZhuangList;
	}
	static {
		realTimeRedisRightPopTaskBlockingZhuangList = new HashMap<String, List>();
	}


	private static final Map<String, MatrixItem[][]> storeHouses2RollingData;
	public static Map<String, MatrixItem[][]> getStoreHouses2RollingData() {
		return storeHouses2RollingData;
	}
	static {
		storeHouses2RollingData = new HashMap<String, MatrixItem[][]>();
	}

	private static volatile Map<String, StorehouseRange> shorehouseRange;
	public static Map<String, StorehouseRange> getShorehouseRange() {
		return shorehouseRange;
	}
	static {
		shorehouseRange = new HashMap<String, StorehouseRange>();
	}

}
