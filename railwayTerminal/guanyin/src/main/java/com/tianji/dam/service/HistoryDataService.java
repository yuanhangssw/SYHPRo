package com.tianji.dam.service;


import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.domain.*;
import com.tianji.dam.domain.vo.T1VO;
import com.tianji.dam.mapper.*;
import com.tianji.dam.thread.HistoryCarDataTaskBlockingList;
import com.tianji.dam.thread.HistoryDataTaskBlockingList;
import com.tianji.dam.utils.DataTimeUtil;
import com.tianji.dam.utils.MapUtil;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;
import com.tj.common.utils.RGBHexUtil;
import com.tj.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.osgeo.proj4j.ProjCoordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.*;
import java.util.concurrent.ExecutionException;


@Service
@Slf4j
@DataSource(value = DataSourceType.SLAVE)
public class HistoryDataService {


    @Autowired
    private CarMapper carMapper;
    @Autowired
    private static StoreHouseMap storeHouseMap;
    @Autowired
    private TDamsconstructionMapper damsConstructionMapper;
    @Autowired
    private T1Mapper t1Mapper;

    private static Map<String, List<HistoryDataTaskBlockingList>> threadMapHistory = storeHouseMap.getHistoryDataTaskBlockingList();
    private static Map<String, MatrixItem[][]> storeHouseMaps2RollingDataHistory = storeHouseMap.getHistory2RollingData();
    private static Map<String, StorehouseRange> shorehouseRangeHistory = storeHouseMap.getHistoryRange();
    private static Map<String, RollingResult> rollingResultMapHistory = storeHouseMap.getHistoryResultMap();

    private static Map<String, List<HistoryCarDataTaskBlockingList>> threadMapHistoryCar = storeHouseMap.getHistoryCarDataTaskBlockingList();

    public static final int CORECOUNT = 1;
    @Autowired
    private RollingDataMapper rollingDataMapper;
    private static Object obj1 = new Object();//锁
    Double division = 1d;


    @Autowired
    private TRepairDataMapper repairDataMapper;

    /**
     * 历史回放-单元工程
     */
    public JSONObject historyData(String id, String userName, String tablename, String timestamp, Long beginTimestamp, Long endTimestamp, int cartype) throws InterruptedException, ExecutionException {
        //获得时间戳
        String rollingResultKey = timestamp;
        //0.获得工作仓的范围
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(id));
        //工作仓 数据
        String tableName = damsConstruction.getTablename();
        String range = tableName.split("_")[0] + "_" + tableName.split("_")[1];
        //1.根据工作仓初设化网格
        //直接根据桩号获得范围

        List<Car> carList = carMapper.findCar();
        JSONObject result = new JSONObject();
        //2 获得数据库中的数据
        long start = System.currentTimeMillis();
        String tableName2 = GlobCache.cartableprfix[cartype] + "_" + tableName;

        //测试 用于直接返回所有数据。
//        if(true) {
//            result.put("alldata",allcar);
//            result.put("tablename", id);
//            result.put("carList", carList);
//            result.put("range", range);
//            //边界
//
//
//            List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
//            List<Coordinate> listcoord =new LinkedList<>();
//            Mileage  mileage = Mileage.getmileage();
//
//            for (Coordinate coordinate : list) {
//             double[] coord =     mileage.pixels2Coord(1,coordinate.x,coordinate.y);
//
//                 listcoord.add(new Coordinate(coord[0],coord[1]));
//
//            }
//            listcoord.add(listcoord.get(0));
//            result.put("ranges", listcoord);
//
//            return result;
//        }

        Double max_x = damsConstruction.getXend();
        Double min_x = damsConstruction.getXbegin();
        Double max_y = damsConstruction.getYend();
        Double min_y = damsConstruction.getYbegin();
        BigDecimal minOfxList = BigDecimal.valueOf(min_x);
        BigDecimal maxOfxList = BigDecimal.valueOf(max_x);
        BigDecimal minOfyList = BigDecimal.valueOf(min_y);
        BigDecimal maxOfyList = BigDecimal.valueOf(max_y);
        StorehouseRange storehouseRange = new StorehouseRange();
        storehouseRange.setMinOfxList(minOfxList.doubleValue());
        storehouseRange.setMaxOfxList(maxOfxList.doubleValue());
        storehouseRange.setMinOfyList(minOfyList.doubleValue());
        storehouseRange.setMaxOfyList(maxOfyList.doubleValue());
        RollingDataRange rollingDataRange = new RollingDataRange();
        rollingDataRange.setMaxCoordY(maxOfyList.doubleValue());
        rollingDataRange.setMinCoordY(minOfyList.doubleValue());
        rollingDataRange.setMaxCoordX(maxOfxList.doubleValue());
        rollingDataRange.setMinCoordX(minOfxList.doubleValue());


        int xSize = (int) Math.abs(max_x - min_x);
        int ySize = (int) Math.abs(max_y - min_y);
        //多用户访问 内存是否含有本工作仓的信息
        //所有车
        //判断工作仓数据是否存在

        //  if(!isHave){//不存在则进行
        shorehouseRangeHistory.put(id, storehouseRange);
        //storehouse.setState(1);
        MatrixItem[][] matrix = new MatrixItem[xSize][ySize];
        //  for (int i = 0; i < xSize; i++) {
//            for (int j = 0; j < ySize; j++) {
//                matrix[i][j] = new MatrixItem();
//                matrix[i][j].setRollingTimes(0);
//            }
//        }
        storeHouseMaps2RollingDataHistory.put(timestamp + "_" + tableName, matrix);


        ProjCoordinate projCoordinate1 = new ProjCoordinate(rollingDataRange.getMinCoordY(), rollingDataRange.getMinCoordX(), 0);
        ProjCoordinate projCoordinate2 = new ProjCoordinate(rollingDataRange.getMaxCoordY(), rollingDataRange.getMaxCoordX(), 0);

        List<HistoryDataTaskBlockingList> threadList = new ArrayList<>();
        //将rollingResult放入到全局变量中 key为时间戳
        //   rollingResultMapHistory.put(rollingResultKey, rollingResult);
        // if(!isHave){


        List<T1> orderlist = t1Mapper.selectcarfirstdata(tableName2);
        for (int i = 0; i < orderlist.size(); i++) {
            HistoryDataTaskBlockingList historyDataTaskBlockingList = new HistoryDataTaskBlockingList();
            historyDataTaskBlockingList.setRollingResultKey(rollingResultKey);
            historyDataTaskBlockingList.setKey(id);
            historyDataTaskBlockingList.setWebsoketid(timestamp);
            historyDataTaskBlockingList.setCartype(cartype);

            Double gaocheng = 0d;
            historyDataTaskBlockingList.setHeight(gaocheng);
            historyDataTaskBlockingList.setStorehouseId(timestamp + "_" + tablename);
            historyDataTaskBlockingList.setBeginTimestamp(beginTimestamp);
            historyDataTaskBlockingList.setEndTimestamp(endTimestamp);
            Thread thread1 = new Thread(historyDataTaskBlockingList);
            threadList.add(historyDataTaskBlockingList);

            thread1.start();
            Set set = new HashSet();
            set.add(timestamp);
            break;
        }
        threadMapHistory.put(timestamp, threadList);

        result.put("tablename", id);
        result.put("rollingDataRange", rollingDataRange);
        result.put("pointLeftBottom", projCoordinate1);
        result.put("pointRightTop", projCoordinate2);
        result.put("height", damsConstruction.getGaocheng());
        result.put("threadList", threadList);
        result.put("carList", carList);
        result.put("range", range);
        //边界
        result.put("ranges", damsConstruction.getRanges());

        //绘制补录数据
        List<JSONObject> images = new LinkedList<>();
        result.put("images", images);
        return result;
    }

    @Autowired
    TColorConfigMapper colorConfigMapper;

    public Map<Integer, Color> getColorMap(Long type) {
        Map<Integer, Color> colorMap = new HashMap<>();
        TColorConfig vo = new TColorConfig();
        vo.setType(type);//碾压遍次
        List<TColorConfig> colorConfigs = colorConfigMapper.select(vo);
        for (TColorConfig color : colorConfigs) {
            if (color.getNum().intValue() == 0) {
                colorMap.put(0, new Color(255, 255, 255, 0));
            } else {
                int[] rgb = RGBHexUtil.hex2RGB(color.getColor());
                colorMap.put(Integer.valueOf(String.valueOf(color.getNum())), new Color(rgb[0], rgb[1], rgb[2]));
            }
        }
        return colorMap;
    }

    public Color getColorByCount2(Integer count, Map<Integer, Color> colorMap) {
        Color color = colorMap.get(count);
        if (StringUtils.isNotNull(color)) {
            return color;
        } else {
            if (count.intValue() > 0) {
                Integer maxKey = (Integer) MapUtil.getMaxKey(colorMap);//取最大key
                return colorMap.get(maxKey);
            } else if (count.intValue() == 0) {
                return new Color(255, 255, 255, 0);
            }
        }
        return null;
    }

    private void calculateRollingtimes(Integer rollingTimes, RollingResult rollingResult) {
        if (rollingTimes.equals(0)) {
            rollingResult.setTime0(rollingResult.getTime0() + 1);
        }
        if (rollingTimes.equals(1)) {
            rollingResult.setTime1(rollingResult.getTime1() + 1);
        }
        if (rollingTimes.equals(2)) {
            rollingResult.setTime2(rollingResult.getTime2() + 1);
        }
        if (rollingTimes.equals(3)) {
            rollingResult.setTime3(rollingResult.getTime3() + 1);
        }
        if (rollingTimes.equals(4)) {
            rollingResult.setTime4(rollingResult.getTime4() + 1);
        }
        if (rollingTimes.equals(5)) {
            rollingResult.setTime5(rollingResult.getTime5() + 1);
        }
        if (rollingTimes.equals(6)) {
            rollingResult.setTime6(rollingResult.getTime6() + 1);
        }
        if (rollingTimes.equals(7)) {
            rollingResult.setTime7(rollingResult.getTime7() + 1);
        }
        if (rollingTimes.equals(8)) {
            rollingResult.setTime8(rollingResult.getTime8() + 1);
        }
        if (rollingTimes.equals(9)) {
            rollingResult.setTime9(rollingResult.getTime9() + 1);
        }
        if (rollingTimes.equals(10)) {
            rollingResult.setTime10(rollingResult.getTime10() + 1);
        }
        if (rollingTimes.equals(11)) {
            rollingResult.setTime11(rollingResult.getTime11() + 1);
        }
        if (rollingTimes > 11) {
            rollingResult.setTime11Up(rollingResult.getTime11Up() + 1);
        }
    }

    /**
     * 自定义车辆历史
     * 开始结束日期 高程 车辆id
     */
    public JSONObject historyCar(String beginTimestamp, String endTimestamp, String vehicleID, Double elevation) {
        JSONObject result = new JSONObject();
        //创建一个线程
        HistoryCarDataTaskBlockingList historyCarDataTaskBlockingList = new HistoryCarDataTaskBlockingList();

        T1Mapper t1Mapper = BeanContext.getApplicationContext().getBean(T1Mapper.class);
        //筛选出车辆数据
        String vehicleIDf = vehicleID.split(",")[0];

        String carids = "";
        String firstVehicle = "";
        if (vehicleIDf.contains("-")) {
            String[] caridss = vehicleIDf.split("-");
            for (String s : caridss) {
                if ("".equals(firstVehicle)) {
                    firstVehicle = s.trim();
                }
                carids += "'" + s + "',";
            }
            while (carids.endsWith(",")) {
                carids = carids.substring(0, carids.length() - 1);
            }
        } else {
            carids = vehicleIDf;
            firstVehicle = vehicleIDf;
        }

        String key = vehicleID.split(",")[1];
        Car car = carMapper.selectByPrimaryKey(Integer.valueOf(firstVehicle));
        historyCarDataTaskBlockingList.setKey(key);
        historyCarDataTaskBlockingList.setGcarId(vehicleIDf);
        historyCarDataTaskBlockingList.setCartype(car.getType());
        T1VO vo = new T1VO();
        vo.setBeginTimestamp(beginTimestamp);
        vo.setEndTimestamp(endTimestamp);
        vo.setVehicleID(carids);
        vo.setSort(1);
        vo.setBeginTime(DataTimeUtil.timeToStamp(vo.getBeginTimestamp()));
        vo.setEndTime(DataTimeUtil.timeToStamp(vo.getEndTimestamp()));
        vo.setTimestamp(0L);
        vo.setTablename(GlobCache.cartableprfix[car.getType()] + "_t_1");
        /*vo.setElevation(elevation);*/

        List<T1> t1s = t1Mapper.selectPage(vo);
        historyCarDataTaskBlockingList.setVo(vo);
        historyCarDataTaskBlockingList.setAlldata(t1s);
        Thread thread1 = new Thread(historyCarDataTaskBlockingList);
        thread1.start();
        if (!threadMapHistoryCar.containsKey("HISTORYCAR")) {
            threadMapHistoryCar.put("HISTORYCAR", new LinkedList<>());
        }
        threadMapHistoryCar.get("HISTORYCAR").add(historyCarDataTaskBlockingList);

        result.put("datasize", t1s.size());
        //t1s.clear();
        t1s = null;
        return result;
    }

    public String clearMatrix(String userName) {
        /*清除storehouseRange*/
        /*清除storehouseMaos2RollingData*/
        try {
            Iterator<String> iterator = shorehouseRangeHistory.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String[] split1 = key.split("-");
                String userNameOfMatrix = split1[0];
                if (userName.equalsIgnoreCase(userNameOfMatrix)) {
                    iterator.remove();
                }
            }
            Iterator<String> iterator2 = storeHouseMaps2RollingDataHistory.keySet().iterator();
            while (iterator2.hasNext()) {
                String key = iterator2.next();
                String[] split1 = key.split("_");
                String userNameOfMatrix = split1[0];
                if (userName.equalsIgnoreCase(userNameOfMatrix)) {
                    iterator2.remove();
                }
            }

            Iterator<String> iterator3 = threadMapHistory.keySet().iterator();

            while (iterator3.hasNext()) {
                String key = iterator3.next();
                if (userName.equals(key)) {

                    List<HistoryDataTaskBlockingList> all = threadMapHistory.get(key);

                    for (HistoryDataTaskBlockingList historyDataTaskBlockingList : all) {
                        historyDataTaskBlockingList.stopThread(true);
                        System.out.println(historyDataTaskBlockingList.getWebsoketid() + "停止完成。");
                    }
                    //  threadMapHistory.remove(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "清除该用户对应的内存";
    }

    /**
     * 获取该仓位对应数据的最大、最小日期 Long类型
     *
     * @param id
     * @return
     */
    public Map<String, Long> getTimestampById(String id, int cartype) {
        //0.获得工作仓的范围
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(id));
        JSONObject result = new JSONObject();
        String tableprefix = GlobCache.cartableprfix[cartype];

        Map<String, Long> map = damsConstructionMapper.getTimestampByTablename(tableprefix + "_" + damsConstruction.getTablename());
        return map;
    }
}