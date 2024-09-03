package com.tj.web.controller.dam;

import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.StoreHouseMap;
import com.tianji.dam.domain.THistoryPic;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.mapper.THistoryPicMapper;
import com.tianji.dam.service.CarService;
import com.tianji.dam.service.HistoryDataService;
import com.tianji.dam.service.RollingDataService;
import com.tj.common.utils.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class RollingDataController {

    @Autowired
    private TDamsconstructionMapper damsConstructionMapper;
    @Autowired
    private RollingDataService rollingDataService;
    @Autowired
    private THistoryPicMapper historyPicMapper;


    @Autowired
    private HistoryDataService historyDataService;

    //历史播放速度
    private static Map<String, Long> historySpeed = StoreHouseMap.getHistoryDataSpeed();
    //历史播放速度
    private static Map<String, Long> historycarSpeed = StoreHouseMap.getHistorycarDataSpeed();

    @Autowired
    private CarService carService;
    /**
     * 平面分析-单元工程
     */
    @GetMapping("/getHistoryPicByZhuangByTod/{tableName}/{cartype}")
    @ResponseBody
    public JSONObject getHistoryPicByZhuangByTod(HttpServletRequest request, @PathVariable("tableName") String tableName, @PathVariable("cartype") Integer cartype)  {
        /*取得当前的用户，后面根据用户名放置网格内存*/

        //首先查询是否存在已经保存的 存在这直接使用已经保存的
        THistoryPic select = new THistoryPic();
        select.setDamid(Long.valueOf(tableName));
        select.setHtype(cartype.longValue());
        List<THistoryPic> selects = historyPicMapper.selectTHistoryPicList(select);
        if (!selects.isEmpty()) {
            String res = selects.get(0).getContent();

            return JSONObject.parseObject(res);
        }

        return rollingDataService.getHistoryPicMultiThreadingByZhuangByTodNews(tableName, cartype);
    }

    @GetMapping("/getHistoryPicByceng/{pid}/{ceng}/{cartype}")
    @ResponseBody
    public JSONObject getHistoryPicByceng(HttpServletRequest request, @PathVariable("pid") Integer pid, @PathVariable("ceng") Integer ceng,@PathVariable("cartype") Integer cartype)  {
        /*取得当前的用户，后面根据用户名放置网格内存*/

        //首先查询是否存在已经保存的 存在这直接使用已经保存的
//        THistoryPic select = new THistoryPic();
//        select.setDamid(Long.valueOf(tableName));
//        select.setHtype(cartype.longValue());
//        List<THistoryPic> selects = historyPicMapper.selectTHistoryPicList(select);
//        if (!selects.isEmpty()) {
//            String res = selects.get(0).getContent();
//
//            return JSONObject.parseObject(res);
//        }

        return rollingDataService.getHistoryPicbyalltabledata(pid, ceng,cartype);
    }



    /**
     * 历史回放单元工程
     *
     * @param request 请求
     * @param storehouseName 仓位
     * @param timestamp  web主键
     * @param beginTimestamp 开始日期
     * @param endTimestamp 结束日期
     * @return 返回仓位边界等数据
     */
    @GetMapping("/historyData/{storehouseName}/{timestamp}/{beginTimestamp}/{endTimestamp}/{cartype}")
    @ResponseBody
    public JSONObject historyData(HttpServletRequest request, @PathVariable String storehouseName, @PathVariable String timestamp, @PathVariable Long beginTimestamp, @PathVariable Long endTimestamp, @PathVariable int cartype) {
        String userName = "admin";
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
     * @param request 请求
     * @param beginTimestamp 开始时间
     * @param endTimestamp 结束时间
     * @param vehicleID  车辆
     * @param elevation 高程
     * @return 结果
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



    /**
     * 仓位回放-获取仓位数据时间范围。
     * @param request 请求
     * @param storehouseName  仓位
     * @param cartype  类型 1 压实 2 摊铺
     * @return 边界等信息
     */
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

    /**
     * 仓位回放-回放速度设置
     * @param key 仓位、主键 组合键
     * @param millisecond 速度
     * @return  设置结果
     */
    /*key = 仓id:车id:时间戳*/
    @ApiOperation(value = "设置历史播放速度  key = 仓id:车id:时间戳(和开启通道时一致) ")
    @GetMapping("/setHistorySpeed/{key}/{millisecond}")
    @ResponseBody
    public JSONObject setHistorySpeed(@PathVariable String key, @PathVariable Long millisecond) {
        JSONObject result = new JSONObject();
        String key1 = key.split(":")[0];
        String key2 = key.split(":")[1];
            String k = key1 +  ":" + key2;
            historySpeed.put(k, millisecond);

        result.put("200", "设置成功");
        return result;
    }

    /**
     * 车辆回放-回放速度设置
     * @param key 组合主键
     * @param millisecond 速度
     * @return 结果
     */
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

    /**
     * 离开回放页面清理线程
     * @param request  请求
     * @param timestamp  主键
     * @return 结果
     */
    @GetMapping("/clearMatrix/{timestamp}")
    public String clearMatrix(HttpServletRequest request, @PathVariable String timestamp) {
        /*取得当前的用户，后面根据用户名放置网格内存*/
        return rollingDataService.clearMatrix(timestamp);
    }

    @GetMapping("/clearMatrix")
    public String clearMatrix2(HttpServletRequest request) {
        /*取得当前的用户，后面根据用户名放置网格内存*/
        String userName = "";
        return rollingDataService.clearMatrix(userName);
    }
    /**
     * 离开回放时清理历史数据
     * @param request  请求
     * @param timestamp 主键
     * @return 结果
     */
    @GetMapping("/clearHistoryMatrix/{timestamp}")
    public String clearHistoryMatrix(HttpServletRequest request, @PathVariable String timestamp) {
        /*取得当前的用户，后面根据用户名放置网格内存*/
        return historyDataService.clearMatrix(timestamp);
    }

    @GetMapping("/clearHistoryMatrix")
    public String clearHistoryMatrix2(HttpServletRequest request) {
        /*取得当前的用户，后面根据用户名放置网格内存*/
        String userName = "timestamp";
        return historyDataService.clearMatrix(userName);
    }

}
