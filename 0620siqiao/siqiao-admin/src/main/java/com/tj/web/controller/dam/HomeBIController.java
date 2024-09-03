package com.tj.web.controller.dam;

import com.alibaba.fastjson.JSONArray;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.domain.*;
import com.tianji.dam.mapper.*;
import com.tianji.dam.service.CarService;
import com.tianji.dam.service.ITSpeedWarmingService;
import com.tianji.dam.utils.RedisUtil;
import com.tj.common.core.domain.AjaxResult;
import com.tj.common.core.domain.entity.SysDictData;
import com.tj.common.utils.DateUtils;
import com.tj.system.mapper.SysDictDataMapper;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("homeBI")
public class HomeBIController {


    @Autowired
    private T1Mapper t1Service;
    @Autowired
    private CarService carservice;
    @Autowired
    private TDamsconstructionMapper constamapper;
    @Autowired
    private CarService carService;
    @Autowired
    private ITSpeedWarmingService tSpeedWarmingService;
    @Autowired
    private DayConstructionInfoMapper dayConstructionInfoMapper;
    @Autowired
    TDamsconstructionMapper  damsconstructionMapper;
    @Autowired
    DayVolumeMapper dayVolumeMapper;
    @Autowired
    SysDictDataMapper sysDictDataMapper;
    @RequestMapping("/carinfo")
    @ResponseBody
    public AjaxResult getcarinfo() {
        Map<String, Integer> carrollingtimes = StoreHouseMap.getCarrollingtimes();
        List<Car> carList = carService.findCar();

        return AjaxResult.success(carList);
    }


    /**
     * 获取统计数据、施工面积、动静碾占比、碾压遍数占比
     *
     * @return
     */
    @RequestMapping("/nyinfo")
    @ResponseBody
    public AjaxResult getthree() {
        {
            Map<String, Object> result = new HashMap<>();
            RedisUtil redis = BeanContext.getApplicationContext().getBean(RedisUtil.class);
            result = (Map<String, Object>) redis.get("homeBI_THREEDATA");
            Integer todayold = (Integer) redis.get("day_"+DateUtils.getDate());
            return AjaxResult.success(result);

        }
    }
    @RequestMapping("/nydayarea")
    @ResponseBody
    public AjaxResult gettodayarea() {
        {

            Map<String, Object> result = new HashMap<>();
            RedisUtil redis = BeanContext.getApplicationContext().getBean(RedisUtil.class);
           // Integer todayold = (Integer) redis.get("day_"+DateUtils.getDate());
            List<Long> todayold = (List<Long>) redis.get("day_"+DateUtils.getDate());
            Integer datatotal =0;
            if(null==todayold){
                datatotal =0;
                todayold=new ArrayList<>();
            }
             //这里累计的是格子 扫描RID 是10*10 也就是100个平方
            Double  totalarea =    todayold.size() *100.0;

            List<DayConstructionInfo> last_30=    dayConstructionInfoMapper.selectdayevolution();
                    Map<String,List<Object>>  last30 =new HashMap<>();
                        List<String> xkey =new ArrayList<>();
                        List<Double> ykey =new ArrayList<>();

            for (DayConstructionInfo constructionInfo : last_30) {
                xkey.add(constructionInfo.getDay());
                ykey.add(constructionInfo.getTodayevolution().doubleValue());

            }
         Double maxevolution =     ykey.stream().max(Double::compareTo).get().doubleValue();

            List<DayVolume> alldaydata=  dayVolumeMapper.selectlast30();

            List<Double> vykey =new ArrayList<>();
          Collections.reverse(xkey);
          Collections.reverse(ykey);

              Map<String,Double> vmap =new HashMap<>();
            for (DayVolume dayvolume : alldaydata) {
               String day=   DateUtils.parseDateToStr("yyyy-MM-dd",dayvolume.getDays());
                Double volume =dayvolume.getTotalvolume().doubleValue();
                vmap.put(day,volume);
            }

            for (String s : xkey) {
                if(vmap.containsKey(s)){
                   double va= vmap.get(s);
                    vykey.add(va);
                }else{
                    Double totalvolume = dayVolumeMapper.selecttotalvolume( s);
                    vykey.add(totalvolume);
                }
            }
            last30.put("xkey", Collections.singletonList(xkey));
            last30.put("ykey", Collections.singletonList(ykey));
            last30.put("vykey",Collections.singletonList(vykey));
            Double  totalfl =dayConstructionInfoMapper.gettotalfl();

            result.put("todayarea",totalarea);
            result.put("last30",last30);
            result.put("maxevolution",maxevolution);
            result.put("totalfl",totalfl);
            String daynow =DateUtils.getDate();
            List<SysDictData> all =   sysDictDataMapper.selectDictDataByType("volume");
            String sjtotal ="";
            for (SysDictData sysDictData : all) {
                  String  key =      sysDictData.getDictValue();
                  String  value = sysDictData.getDictLabel();
                  if(key.equals("total")){
                        sjtotal = value;
                  }
            }
            Double todayvolume =    dayVolumeMapper.selectvolume(daynow);
            Double totalvolume=   dayVolumeMapper.selecttotalvolume(daynow);
            result.put("sjfl",sjtotal);
            result.put("todayvolume",todayvolume);
            result.put("totalvolume",totalvolume);

            return AjaxResult.success(result);

        }
    }




    /**
     * 获取填写的每日填筑方量
     * @return
     */
    public AjaxResult getdayvolume(){

        dayVolumeMapper.selectlast30();
        return  AjaxResult.success();

    }

    /**
     * 当日碾压的
     *
     * @return
     */
    @RequestMapping("/getevolution")
    @ResponseBody
    public AjaxResult getevolution() {
        String begins = DateUtils.getDate() + " 00:00:00";
        String ends = DateUtils.getDate() + " 23:59:59";
        Long begintime = DateUtils.parseDate(begins).getTime();
        Long endtime = DateUtils.parseDate(ends).getTime();
        T1 t1 = t1Service.selectmaxevolution("ylj_t_1", begintime, endtime);
        if (null == t1) {
            t1 = new T1();
            t1.setElevation(0.0);
        }
        return AjaxResult.success(t1.getElevation());
    }


    /**
     * 获取车辆当前所在仓位-返回仓位id-名称-详细信息
     * @return
     */
   @RequestMapping("/carincang")
   public AjaxResult getcarincang(){


       TaskCarRecordMapper taskCarRecordMapper = BeanContext.getApplicationContext().getBean(TaskCarRecordMapper.class);
        TDamsconstructionMapper  damsconstructionMapper = BeanContext.getApplicationContext().getBean(TDamsconstructionMapper.class);
        CarMapper  carMapper = BeanContext.getApplicationContext().getBean(CarMapper.class);
       TAnalysisConfigMapper  tanalysisConfigMapper  = BeanContext.getApplicationContext().getBean(TAnalysisConfigMapper.class);

       TAnalysisConfig t = tanalysisConfigMapper.selectMaxIdOne();
       Double x = t.getX();
       Double y = t.getY();

       TaskCarRecord taskCarRecord = new TaskCarRecord();
         taskCarRecord.setStatus(0L);


         Map<String,Object> canginfo = new HashMap<>();
        List<String> opencanglist = new ArrayList<>();

        List<TaskCarRecord> allcang =    taskCarRecordMapper.selectTaskCarRecordList(taskCarRecord);

                allcang.forEach(record -> {
                    Long cangid =record.getTaskId();
                    String carid =record.getCarId().toString();
                  DamsConstruction tdamsconstruction=  damsconstructionMapper.selectByPrimaryKey(cangid.intValue());
                  DamsConstruction dam_parent=  damsconstructionMapper.selectByPrimaryKey(tdamsconstruction.getPid());
                  Car car =  carMapper.selectByPrimaryKey(record.getCarId().intValue());
                  opencanglist.add(tdamsconstruction.getTitle());
                    Map<String,Object> cangdetail =new HashMap<>();
                    cangdetail.put("unit",dam_parent.getTitle());
                    cangdetail.put("ceng",tdamsconstruction.getEngcode());
                    cangdetail.put("carname",car.getRemark());
                    cangdetail.put("cangid",cangid);
                    List<Coordinate> list = JSONArray.parseArray(tdamsconstruction.getRanges(), Coordinate.class);
                    Coordinate[] pointList = new Coordinate[list.size() + 1];
                    list.toArray(pointList);
                    pointList[list.size()] = pointList[0];
                    GeometryFactory gf = new GeometryFactory();
                    Geometry edgePoly = gf.createPolygon(pointList);
                    double area = edgePoly.getArea();
                    Double total1area = x * y * area;
                    String finalarea = new BigDecimal(total1area / 10000.0).setScale(1, RoundingMode.HALF_UP).toString();

                    cangdetail.put("area",finalarea);

                    canginfo.put(tdamsconstruction.getTitle(),cangdetail);


                });




    return  AjaxResult.success(canginfo);
   }


    /**
     * 获取合格数量-所有开仓数量
     * @return
     */
   @RequestMapping("/getclosecangtotal")
   public AjaxResult getclosecangtotal(){
       TDamsconstructionMapper  damsconstructionMapper = BeanContext.getApplicationContext().getBean(TDamsconstructionMapper.class);

     List<DamsConstruction> allopen =  damsconstructionMapper.findByStatus(2);

       Map<Integer, Integer> reult =new HashMap<>();
       Map<Integer, List<DamsConstruction>> carsByPid = allopen.stream().collect(Collectors.groupingBy(DamsConstruction::getPid));

       for (Integer i : carsByPid.keySet()) {
           reult.put(i,carsByPid.get(i).size());
       }

       reult.put(0,allopen.size());

       return AjaxResult.success(reult);


   }



    @PostMapping("/ttttt")
    public void getTest(@RequestBody Map<String, String> o) {

        System.out.println(o.toString());
    }


    private String formatTime(int minuteStr) {
        String resultStr = "";
        if (minuteStr != 0) {
            if (minuteStr / 60 == 0) {
                resultStr = minuteStr % 60 + "分";
            } else {
                if (minuteStr % 60 == 0) {
                    resultStr = minuteStr / 60 + "小时";
                } else {
                    resultStr = (minuteStr / 60 + "小时" + minuteStr % 60 + "分");
                }
            }
        } else {
            resultStr = "0小时";
        }
        return resultStr;
    }


}
