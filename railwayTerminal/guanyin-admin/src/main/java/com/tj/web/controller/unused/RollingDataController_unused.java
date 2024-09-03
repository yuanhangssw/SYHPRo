package com.tj.web.controller.unused;

import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.domain.*;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.mapper.THistoryPicMapper;
import com.tianji.dam.mapper.TableMapper;
import com.tianji.dam.service.CarService;
import com.tianji.dam.service.HistoryDataService;
import com.tianji.dam.unused.RollingDataService;
import com.tj.common.utils.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/unused")
public class RollingDataController_unused {

    @Autowired
    private TDamsconstructionMapper damsConstructionMapper;
    @Autowired
    private RollingDataService rollingDataService;
    @Autowired
    private THistoryPicMapper historyPicMapper;
    @Autowired
    private TableMapper tableMapper;

    @GetMapping("/getMatrixItemBylonLat/{longitude}/{latitude}")
    @ResponseBody
    public MatrixItem getMatrixItemBylonLat(@PathVariable("longitude") double longitude, @PathVariable("latitude") double latitude) {
        return rollingDataService.getMatrixItemBylonLat(longitude, latitude);
    }

    @GetMapping("/rollingDataPositon/{tableName}")
    @ResponseBody
    public List<ArrayList> rollingDataPositon(@PathVariable("tableName") String tableName) {
        List<RollingData> rollingDataList = rollingDataService.getAllRollingData(tableName);
        List<ArrayList> list = new ArrayList();
        for (RollingData rollingData : rollingDataList) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(rollingData.getTimestamp());
            arrayList.add(rollingData.getElevation());
            list.add(arrayList);
        }
        return list;
    }


    /*多线程获得历史碾压结果*/
    @GetMapping("/getHistoryPicByZhuang/{tableName}")
    @ResponseBody
    public JSONObject getHistoryPicByZhuang(HttpServletRequest request, @PathVariable("tableName") String tableName) throws ExecutionException, InterruptedException, IOException {
        /*取得当前的用户，后面根据用户名放置网格内存*/
        String userName = SecurityUtils.getUsername();
        JSONObject jsonObject2 = rollingDataService.getHistoryPicMultiThreadingByZhuang(userName, tableName);
        return jsonObject2;
    }

    @GetMapping("/getHistoryPicByZhuangByTodforReal/{tableName}")
    @ResponseBody
    public JSONObject getHistoryPicByZhuangByTodforReal(HttpServletRequest request, @PathVariable("tableName") String tableName) throws ExecutionException, InterruptedException, IOException {
        /*取得当前的用户，后面根据用户名放置网格内存*/
        String userName = SecurityUtils.getUsername();
        JSONObject jsonObject2 = rollingDataService.getHistoryPicMultiThreadingByZhuangByTodforReal(userName, tableName);
        return jsonObject2;
    }

    /**
     * 实时监控-进入页面时加载当天的数据显示--
     * 2022-10-11 14:03:59 改为通过websoket获取
     */
//    @GetMapping("/getHistoryPicByZhuangByTodbeforeReal")
//    @ResponseBody
//    public List<JSONObject> getHistoryPicByZhuangByTodbeforeReal(HttpServletRequest request) throws ExecutionException, InterruptedException, IOException {
//        /*取得当前的用户，后面根据用户名放置网格内存*/
//        String userName = SecurityUtils.getUsername();
//        List<JSONObject> jsonObject2 = rollingDataService.getHistoryPicMultiThreadingByZhuangByTodbeforeReal();
//        return jsonObject2;
//    }


    /**
     * 平面分析-单元工程
     */
    @GetMapping("/getHistoryPicByZhuangByTod/{tableName}/{cartype}")
    @ResponseBody
    public JSONObject getHistoryPicByZhuangByTod(HttpServletRequest request, @PathVariable("tableName") String tableName, @PathVariable("cartype") Integer cartype) throws ExecutionException, InterruptedException, IOException {
        /*取得当前的用户，后面根据用户名放置网格内存*/
        String userName = SecurityUtils.getUsername();

        //首先查询是否存在已经保存的 存在这直接使用已经保存的
        THistoryPic select = new THistoryPic();
        select.setDamid(Long.valueOf(tableName));
        select.setHtype(cartype.longValue());
        List<THistoryPic> selects = historyPicMapper.selectTHistoryPicList(select);
        if (selects.size() > 0) {
            String res = selects.get(0).getContent();

            return JSONObject.parseObject(res);
        }

        JSONObject jsonObject2 = rollingDataService.getHistoryPicMultiThreadingByZhuangByTod(tableName, cartype);
        return jsonObject2;
    }

    @GetMapping("/getHistoryPicByZhuangByEla/{bottom}/{top}")
    @ResponseBody
    public JSONObject getHistoryPicByZhuangByEla(@PathVariable("bottom") String bottom, @PathVariable("top") String top) throws ExecutionException, InterruptedException {
        return rollingDataService.getHistoryPicByElvacation(bottom, top);
    }

    @GetMapping("/getHistoryPicByZhuangByElaAndRange/{range}/{bottom}/{top}/{mat}")
    @ResponseBody
    public JSONObject getHistoryPicByZhuangByElaAndRange(@PathVariable("range") String range, @PathVariable("bottom") String bottom, @PathVariable("top") String top, @PathVariable("mat") String mat) throws ExecutionException, InterruptedException {
        return rollingDataService.getHistoryPicByElvacationAndRange(range, bottom, top, mat);
    }

    @GetMapping("/realTimeByZhuangByElaAndRange/{range}/{bottom}/{top}/{timestamp}/{mat}")
    @ResponseBody
    public JSONObject realTimeByZhuangByElaAndRange(@PathVariable("range") String range, @PathVariable("bottom") String bottom, @PathVariable("top") String top, @PathVariable("timestamp") String timestamp, @PathVariable("mat") String mat) throws ExecutionException, InterruptedException {
        return rollingDataService.realTimeByZhuangByElaAndRange(range, bottom, top, timestamp, mat);
    }


    @GetMapping("/killThread/{threadName}/{tableName}")
    public String killThread(@PathVariable("threadName") String threadName, @PathVariable("tableName") String tableName) {
        return rollingDataService.killThread(threadName, tableName);
    }

    @GetMapping("/killThreadByThreadName/{timestamp}/{ThreadName}")
    public String killThreadByThreadName(@PathVariable("timestamp") String timestamp, @PathVariable("ThreadName") String ThreadName) {
        return rollingDataService.killThreadByThreadName(timestamp, ThreadName);
    }

    @GetMapping("/getAllThread")
    public List getAllThread() {
        return rollingDataService.getAllThread();
    }

    @GetMapping("/realTimeZhuang/{storehouseName}/{timestamp}")
    @ResponseBody
    public JSONObject realTimeZhuang(HttpServletRequest request, @PathVariable String storehouseName, @PathVariable String timestamp) {
        String userName = SecurityUtils.getUsername();
        //   String userName = "test";
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.parseInt(storehouseName));
        String tablename = damsConstruction.getTablename();
        try {
            return rollingDataService.realTimeZhuang(String.valueOf(damsConstruction.getId()), userName, tablename, timestamp);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    @GetMapping("/realTimeZhuang/{storehouseName}/{timestamp}/{carId}")
    @ResponseBody
    public JSONObject realTimeZhuang(HttpServletRequest request, @PathVariable String storehouseName, @PathVariable String timestamp, @PathVariable String carId) {
        String userName = SecurityUtils.getUsername();
        // String userName = "test";
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.parseInt(storehouseName));
        String tablename = damsConstruction.getTablename();
        try {
            System.out.println("实时监控查询:" + carId);
            return rollingDataService.realTimeZhuang(String.valueOf(damsConstruction.getId()), userName, tablename, timestamp, carId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    @GetMapping("/drawPic/{storehouesId}")
    @ResponseBody
    public String drawPic(@PathVariable("storehouesId") String storehouesId) {
        return rollingDataService.drawPic(storehouesId);
    }

    @GetMapping("/deleteStorehouseMatrix/{storehouseStr}")
    public String deleteStorehouseMatrix(@PathVariable("storehouseStr") String storehouseStr) {
        return rollingDataService.deleteStorehouseMatrix(storehouseStr);
    }


    @GetMapping("/createTable")
    public String createTable() {
        for (int i = 0; i < 209; i++) {
            String databaseName = "t_" + String.valueOf(27 + i);
            tableMapper.createVehicleDateTable(databaseName);
        }
        return "ok";
    }

    @GetMapping("/getAllPic")
    public void getAllPic() throws ExecutionException, InterruptedException {
        rollingDataService.getAllPic();
    }


    @GetMapping("/getPicByLayerMat")
    public void getPicByLayerMat() throws ExecutionException, InterruptedException {
        try {
            rollingDataService.getPicByLayerMat();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/clearMatrix/{timestamp}")
    public String clearMatrix(HttpServletRequest request, @PathVariable String timestamp) {
        /*取得当前的用户，后面根据用户名放置网格内存*/
        String userName = timestamp;
        return rollingDataService.clearMatrix(userName);
    }

    @GetMapping("/clearMatrix")
    public String clearMatrix2(HttpServletRequest request) {
        /*取得当前的用户，后面根据用户名放置网格内存*/
        String userName = "";
        return rollingDataService.clearMatrix(userName);
    }

    @Autowired
    private HistoryDataService historyDataService;

    /**
     * 历史回放单元工程
     *
     * @param request
     * @param storehouseName
     * @param timestamp
     * @param beginTimestamp
     * @param endTimestamp
     * @return
     */
    @GetMapping("/historyData/{storehouseName}/{timestamp}/{beginTimestamp}/{endTimestamp}/{cartype}")
    @ResponseBody
    public JSONObject historyData(HttpServletRequest request, @PathVariable String storehouseName, @PathVariable String timestamp, @PathVariable Long beginTimestamp, @PathVariable Long endTimestamp, @PathVariable int cartype) {
        String userName = SecurityUtils.getUsername();
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.parseInt(storehouseName));
        String tablename = damsConstruction.getTablename();
        try {
            return historyDataService.historyData(String.valueOf(damsConstruction.getId()), userName, tablename, timestamp, beginTimestamp, endTimestamp, cartype);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * 历史回放-自定义车辆
     *
     * @param request
     * @param beginTimestamp
     * @param endTimestamp
     * @param vehicleID
     * @param elevation
     * @return
     */
    @GetMapping("/historyCarData/{beginTimestamp}/{endTimestamp}/{vehicleID}/{elevation}")
    @ResponseBody
    public JSONObject historyCarData(HttpServletRequest request, @PathVariable String beginTimestamp, @PathVariable String endTimestamp, @PathVariable String vehicleID, @PathVariable Double elevation) {
        try {
            return historyDataService.historyCar(beginTimestamp, endTimestamp, vehicleID, elevation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject result = new JSONObject();
        result.put("datasize", 0);
        return result;

    }

    @GetMapping("/clearHistoryMatrix/{timestamp}")
    public String clearHistoryMatrix(HttpServletRequest request, @PathVariable String timestamp) {
        /*取得当前的用户，后面根据用户名放置网格内存*/
        String userName = timestamp;
        return historyDataService.clearMatrix(userName);
    }

    @GetMapping("/clearHistoryMatrix")
    public String clearHistoryMatrix2(HttpServletRequest request) {
        /*取得当前的用户，后面根据用户名放置网格内存*/
        String userName = "timestamp";
        return historyDataService.clearMatrix(userName);
    }

    @ApiOperation(value = "根据仓位id获取时间信息")
    @GetMapping("/getTimestampById/{storehouseName}/{cartype}")
    @ResponseBody
    public Map<String, Long> getTimestampById(HttpServletRequest request, @PathVariable String storehouseName, @PathVariable Integer cartype) {
        try {
            return historyDataService.getTimestampById(storehouseName, cartype);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //历史播放速度
    private static Map<String, Long> historySpeed = StoreHouseMap.getHistoryDataSpeed();
    //历史播放速度
    private static Map<String, Long> historycarSpeed = StoreHouseMap.getHistorycarDataSpeed();

    @Autowired
    private CarService carService;

    /*key = 仓id:车id:时间戳*/
    @ApiOperation(value = "设置历史播放速度  key = 仓id:车id:时间戳(和开启通道时一致) ")
    @GetMapping("/setHistorySpeed/{key}/{millisecond}")
    @ResponseBody
    public JSONObject setHistorySpeed(@PathVariable String key, @PathVariable Long millisecond) {
        JSONObject result = new JSONObject();
        String key1 = key.split(":")[0];
        String key2 = key.split(":")[1];
        List<Car> carList = carService.findCar();
        for (Car oneCar : carList) {
            Integer driverID = oneCar.getCarID();
            String k = key1 + ":" + driverID + ":" + key2;
            historySpeed.put(k, millisecond);
        }
        result.put("200", "设置成功");
        return result;
    }


    /*key = 仓id:车id:时间戳*/
    @ApiOperation(value = "设置历史播放速度  key = 仓id:车id:时间戳(和开启通道时一致) ")
    @GetMapping("/setHistorycarSpeed/{key}/{millisecond}")
    @ResponseBody
    public JSONObject setHistorycarSpeed(@PathVariable String key, @PathVariable Long millisecond) {
        JSONObject result = new JSONObject();

        historycarSpeed.put(key, millisecond);

        result.put("200", "设置成功");
        return result;
    }


}
