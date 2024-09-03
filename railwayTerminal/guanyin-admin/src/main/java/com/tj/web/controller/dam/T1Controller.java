package com.tj.web.controller.dam;

import com.github.pagehelper.PageHelper;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.bean.SMSBean;
import com.tianji.dam.common.SMSutils;
import com.tianji.dam.domain.*;
import com.tianji.dam.domain.vo.RollingDataListVo;
import com.tianji.dam.domain.vo.T1VO;
import com.tianji.dam.mapper.CarMapper;
import com.tianji.dam.mapper.T1Mapper;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.mapper.TableMapper;
import com.tianji.dam.mileageutil.Mileage;
import com.tianji.dam.service.CarService;
import com.tianji.dam.service.T1Service;
import com.tianji.dam.thread.RedisRightPopTaskBlockingforRealDone2;
import com.tianji.dam.thread.RedisRightPopTaskBlockingforRealDoneCang;
import com.tianji.dam.thread.TransferT1toRedisThread;
import com.tianji.dam.utils.GenerateRedisKey;
import com.tianji.dam.utils.RedisUtil;
import com.tianji.dam.utils.TrackConstant;
import com.tianji.dam.websocket.WebsocketServerForPosition;
import com.tj.common.core.controller.BaseController;
import com.tj.common.core.domain.AjaxResult;
import com.tj.common.core.page.TableDataInfo;
import com.tj.common.utils.DateUtils;
import com.tj.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.*;

@CrossOrigin
@Api(value = "原始轨迹数据controller", tags = {"数据管理操作接口"})
@RestController
@RequestMapping("/reservoir/t1")
public class T1Controller extends BaseController {

    @Autowired
    private T1Service t1Service;
    @Autowired
    private T1Mapper t1Mapper;
    @Autowired
    TableMapper tableMapper;

    @Autowired
    private CarService carservice;
    @Autowired
    private TDamsconstructionMapper tDamsconstructionMapper;


    private static Double x0 = TrackConstant.x0;//北方向
    private static Double y0 = TrackConstant.y0;//东方下

    private static Double alpha = TrackConstant.alpha;//顺时针旋转角度，弧度制d
    private static Double kk = TrackConstant.kk;//缩放比

    @GetMapping("/list")
    @ApiOperation(value = "分页查询数据管理信息", response = T1.class, nickname = "list")
    public TableDataInfo list(T1VO param) {

        if(null!=param.getTablename()){

            if(param.getTablename().length()<2||param.getTablename().length()>8){
                return null;
            }
        }

        if (null == param.getTablename()) {
            param.setTablename(GlobCache.cartableprfix[1] + "_t_1");
        } else {
            param.setTablename(param.getTablename() + "_t_1");
        }

        PageHelper.startPage(param.getPageNum(), param.getPageSize(), "");
        List<T1> list = t1Service.select(param);
        Map<String, String> carmap = new HashMap<>();
        for (T1 t1 : list) {
            if (!carmap.containsKey(t1.getVehicleID())) {
                Car temp = carservice.selectCarByID(Integer.parseInt(t1.getVehicleID()));
                carmap.put(t1.getVehicleID(), temp.getRemark());
            }
            t1.setVehicleID(carmap.get(t1.getVehicleID()));
            ;
        }

        TableDataInfo r = getDataTable(list);
        return r;
    }

    @GetMapping("/test")
    @ApiOperation(value = "测试", response = T1.class, nickname = "list")
    public AjaxResult test() {
        t1Service.getHistoryByCustomCar();
        return AjaxResult.success();
    }

//    @GetMapping("/analysisCar")
//    @ApiOperation(value = "自定义车辆分析", response = T1VO.class, nickname = "list")
//    public AjaxResult analysisCar(String vo) {
//        String param = "";
//        try {
//            param = URLDecoder.decode(vo, "utf-8");
//        } catch (Exception e) {
//        }
//        List<T1VO> vos = JSON.parseArray(param, T1VO.class);
//        return customCarService.analysisCar(vos);
//    }

    /**
     * 重载某天的数据到redis
     * @param time
     * @param carid
     * @param clear
     * @return
     * @throws ParseException
     */

    @GetMapping("/TransferT1toRedisThread")
    public AjaxResult TransferT1toRedisThread(String time, String carid, boolean clear) throws ParseException {

        T1VO param = new T1VO();
        param.setBeginTimestamp(time + " 00:00:00");
        param.setEndTimestamp(time + " 23:59:59");
        param.setSort(1);
        param.setVehicleID(carid);
        Car car = carservice.selectCarByID(Integer.parseInt(carid));

        TransferT1toRedisThread tt = new TransferT1toRedisThread();
        tt.setVos(Collections.singletonList(param));
        tt.setKey(time);
        tt.setIsclear(clear);
        tt.setKeytype(GlobCache.cartableprfix[car.getType()]);
        tt.call();
        return AjaxResult.success();

    }


    /**
     * 重载某个任务（仓）的数据到redis
     */

    @GetMapping("/reloadcangtoredis")
    public AjaxResult  reloadcangtoredis(Integer cangid){


        return  AjaxResult.success();
    }





    /**
     * 实时页面从新加载当天的数据
     */
    @GetMapping("/reloaddatatoredis/{cartype}")
    public void autoredisdata(@PathVariable("cartype") int cartype) {

        RedisUtil redis = BeanContext.getApplicationContext().getBean(RedisUtil.class);
        CarMapper carMapper = BeanContext.getApplicationContext().getBean(CarMapper.class);
        List<Car> carList = carMapper.findCar();
       // String time = DateUtils.getDate();

        //本地测试时使用
        String time = "2023-06-10";
        boolean last = false;

        BlockingQueue<Runnable> executorQueue = new LinkedBlockingQueue<>(50000);
        ExecutorService exec = new ThreadPoolExecutor( 1,4,60, TimeUnit.SECONDS, executorQueue);
        // 定义一个任务集合
        List<TransferT1toRedisThread> tasks = new LinkedList<>();


        for (int i = 0; i < carList.size(); i++) {

            if (carList.get(i).getType() != cartype) {
                continue;
            }

            boolean clear = false;
            if (i == 0) {
                clear = true;
            }
            if (i == carList.size() - 1) {
                last = true;
            }
            Integer carid = carList.get(i).getCarID();


            T1VO param = new T1VO();
            param.setBeginTimestamp(time + " 00:00:00");
            param.setEndTimestamp(time + " 23:59:59");
            param.setSort(1);
            param.setVehicleID(carid.toString());

            TransferT1toRedisThread tt = new TransferT1toRedisThread();
            tt.setVos(Collections.singletonList(param));
            tt.setKey(time);
            tt.setIsclear(clear);

            tt.setKeytype(GlobCache.cartableprfix[cartype]);
            tasks.add(tt);
        }

        try {
            List<Future<Integer>> results = exec.invokeAll(tasks);
            for (Future<Integer> future : results) {
                future.get();
            }
            // 关闭线程池
            exec.shutdown();
        } catch (Exception ie) {
            ie.printStackTrace();
        }

        //所有生成完成以后再将数据设置回redis中
        if(GlobCache.t1T0redishistorycarCachData.size()>0){
            System.out.println("即将保存。" + GlobCache.t1T0redishistorycarCachData.get(GlobCache.cartableprfix[cartype]).entrySet().size() + "条数据");

        for (Map.Entry<Long, MatrixItem[][]> entry : GlobCache.t1T0redishistorycarCachData.get(GlobCache.cartableprfix[cartype]).entrySet()) {
            long rid = entry.getKey();
            MatrixItem[][] value = entry.getValue();
            //本地测试时使用
            time = DateUtils.getDate();
            String redisKey = GenerateRedisKey.realTimeRidKey2(time, rid, GlobCache.cartableprfix[cartype]);
            redis.set(redisKey, value);

        }

        System.out.println("Redis数据保存完成。。");
        }
        GlobCache.t1T0redishistorycarCachData.clear();
        //任务数据从新生成
//        TransferT1toRedisThreadTempCang tempCang = new TransferT1toRedisThreadTempCang();
//        tempCang.setCartype(GlobCache.cartableprfix[cartype]);
//        Thread tcang = new Thread(tempCang);
//        tcang.start();

    }


    @GetMapping("/TransferT1toRedisThreadkill")
    public AjaxResult TransferT1toRedisThreadkill() throws ParseException {

        GlobCache.t1T0redishistorycarCachData.clear();

        return AjaxResult.success();


    }

    @GetMapping("/maket1xy")
    public AjaxResult makezhuangxzhuangy() {

        List<T1> alls = t1Mapper.gett1pages();
        int total = 0;
        while (alls.size() > 0) {


            List<T1> uplist = new LinkedList<>();
            for (T1 rollingData : alls) {

                Double zhuangY = -1 * (((rollingData.getCoordX() - x0) / kk) * Math.cos(alpha) + ((rollingData.getCoordY() - y0) / kk) * Math.sin(alpha));
                Double zhuangX = ((rollingData.getCoordX() - x0) / kk) * Math.sin(alpha) + ((rollingData.getCoordY() - y0) / kk) * Math.cos(alpha);
                rollingData.setZhuangX(zhuangX);
                rollingData.setZhuangY(zhuangY);
                //计算里程和偏距
                try {
                    Mileage m = Mileage.getmileage();
                    double[] value = m.coord2Mileage2(rollingData.getCoordX(), rollingData.getCoordY(), "0");
                    rollingData.setZhuanghao(value[0] + "");
                    rollingData.setPianju(value[1] + "");
                } catch (Exception e) {
                    System.out.println("里程偏距计算错误。");
                }

                uplist.add(rollingData);
            }
            t1Mapper.upt1zhuangxybatch(uplist);
            logger.info("处理完成" + uplist.size() + "条");
            total += uplist.size();
            logger.info("累计完成" + total + "条");
            alls = t1Mapper.gett1pages();
            logger.info("继续处理" + alls.size() + "条");

        }
        return AjaxResult.success(total);
    }

    /**
     * 重传以前先清空某一天的数据。
     *
     * @param date
     * @return
     */
    @GetMapping("/dataclear")
    public AjaxResult dataclear(String date, int carid) {

        try {
         Car car =   carservice.selectCarByID(carid);
            String tablep = GlobCache.cartableprfix[car.getType()];
            String tablename = tablep + "_t_1";


            Date dbegin = DateUtils.parseDate(date + " 00:00:00");
            Date dend = DateUtils.parseDate(date + " 23:59:59");
            Long begintime = dbegin.getTime();
            Long endtime = dend.getTime();

            tableMapper.deletefromt1(tablename, begintime, endtime, carid);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success();
    }

    /**
     * 重传某天的数据。。
     *
     * @param rollingDatas
     * @return
     */


    @PostMapping("/datareload")
    public AjaxResult datareload(@RequestBody List<RollingData> rollingDatas) {

        List<RollingData> t1list = new LinkedList<>();
        System.out.println("获取到数据===" + rollingDatas.size());
        String tablep = "";
        for (RollingData rollingData : rollingDatas) {
            if ("".equals(tablep)) {
                tablep = GlobCache.cartableprfix[Integer.parseInt(rollingData.getVehicleID())];
            }
            if (StringUtils.isNotNull(rollingData)) {
                //System.out.println("排序：："+rollingData.getOrderNum());
                //  System.out.println("time：："+rollingData.getTimestamp());
                Double zhuangY = -1 * (((rollingData.getCoordX() - x0) / kk) * Math.cos(alpha) + ((rollingData.getCoordY() - y0) / kk) * Math.sin(alpha));
                Double zhuangX = ((rollingData.getCoordX() - x0) / kk) * Math.sin(alpha) + ((rollingData.getCoordY() - y0) / kk) * Math.cos(alpha);
                rollingData.setZhuangX(zhuangX);
                rollingData.setZhuangY(zhuangY);

                t1list.add(rollingData);
                if (t1list.size() > 200) {
                    RollingDataListVo savevo = new RollingDataListVo();
                    System.out.println("实时数据向t1表插入" + t1list.size() + "条>>" + DateUtils.getTime());
                    savevo.setTableName(tablep + "_t_1");
                    savevo.setDataList(t1list);
                    t1Mapper.insertrollingdatabatch(savevo);
                    t1list.clear();
                }
            }
        }
        if (t1list.size() > 0) {
            RollingDataListVo savevo = new RollingDataListVo();
            System.out.println("实时数据向t1表插入最后" + t1list.size() + "条数据>>" + DateUtils.getTime());
            savevo.setTableName(tablep + "_t_1");
            savevo.setDataList(t1list);
            t1Mapper.insertrollingdatabatch(savevo);
            t1list.clear();
        }
        return AjaxResult.success();
    }

    //实时页面获取当天的历史数据
    @GetMapping("/gettodaydata")
    public AjaxResult gettodaydata(int cartype) {

        String tablep = GlobCache.cartableprfix[cartype];
        String tablename = tablep + "_t_1";
         String date = DateUtils.getDate();
        //String date = "2023-02-09";
        Date dbegin = DateUtils.parseDate(date + " 00:00:00");
        Date dend = DateUtils.parseDate(date + " 23:59:59");
        Long begintime = dbegin.getTime();
        Long endtime = dend.getTime();

        List<T1> alldata = t1Mapper.selectbasedata(tablename, begintime, endtime);

        return AjaxResult.success(alldata);
    }

    //实时页面获取当天的历史数据
    @GetMapping("/gettodaydataevolution")
    public AjaxResult gettodaydata(int cartype,double begin,double end) {

        String tablep = GlobCache.cartableprfix[cartype];
        String tablename = tablep + "_t_1";
        String date = DateUtils.getDate();
        //String date = "2023-02-09";
        Date dbegin = DateUtils.parseDate(date + " 00:00:00");
        Date dend = DateUtils.parseDate(date + " 23:59:59");
        Long begintime = dbegin.getTime();
        Long endtime = dend.getTime();

        List<T1> alldata = t1Mapper.selectByEvolution(tablename, begin, end);

        return AjaxResult.success(alldata);
    }


    @GetMapping("/getcarfirstdatatime")
    public AjaxResult getcarfirstdatatime(int damid,int cartype){


        String tablepre =  GlobCache.cartableprfix[cartype];
        String tableafter = t1Mapper.selecttablename(damid);
        String tablename=tablepre+"_"+tableafter;

        List<T1> orderlist =    t1Mapper.selectcarfirstdata(tablename);

        return AjaxResult.success(orderlist);


    }

    @GetMapping("/ylje_2_tpj")
    public AjaxResult dddddd(){

        //查询所有的非压实车辆数据，然后将这些数据插入到摊铺表

          List<RollingData> alldata =    t1Mapper.getalldata_3456("ylj_t_1");

          List<RollingData> savelist =new ArrayList<>();
            for(int i=0;i<alldata.size();i=i+2000){

                  int from =i ;
                  int to = i+2000;
                  if(to>alldata.size()){
                      to = alldata.size();
                  }
                savelist.addAll(alldata.subList(from,to));

                for (RollingData rollingData : savelist) {
                    rollingData.setRangeStr("fromylj");
                }
                RollingDataListVo savevo = new RollingDataListVo();
                System.out.println("实时数据向tpj_t1表插入2000条");
                savevo.setTableName( "tpj_t_1");
                savevo.setDataList(savelist);
                t1Mapper.insertrollingdatabatch(savevo);
                savelist.clear();
            }

        System.out.println("插入数据完成。");

        int result =    t1Mapper.delte_3456("ylj_t_1");

        System.out.println("删除"+result+"条数据");
        return AjaxResult.success();

    }

    @GetMapping("/ylje_2_tpj_cang")
    public AjaxResult trans2(int type){
     List<String> tablenames =   t1Mapper.getalltable();
                String pre1 ="ylj_";
                String pre2 ="tpj_";
                if(type==2){
                    pre1 ="tpj_";
                    pre2 ="ylj_";

                }
        for (String tablename : tablenames) {
            List<RollingData> alldata=new ArrayList<>();
            if(1==type){
                alldata =    t1Mapper.getalldata_3456(pre1+tablename);
            }else{
                alldata =    t1Mapper.getalldata_12(pre1+tablename);
            }

            if(alldata.size()>0){
                System.out.println(tablename+"需要转移"+alldata.size()+"条数据");
                List<RollingData> savelist =new ArrayList<>();
                for(int i=0;i<alldata.size();i=i+2000){

                    int from =i ;
                    int to = i+2000;
                    if(to>alldata.size()){
                        to = alldata.size();
                    }
                    savelist.addAll(alldata.subList(from,to));

                    for (RollingData rollingData : savelist) {
                        rollingData.setRangeStr("fromylj");
                    }
                    RollingDataListVo savevo = new RollingDataListVo();
                    System.out.println("向"+pre2+tablename+"表插入"+savelist.size()+"条");
                    savevo.setTableName(pre2+tablename);
                    savevo.setDataList(savelist);
                    t1Mapper.insertrollingdatabatch(savevo);
                    savelist.clear();
                }
                System.out.println(tablename+"插入数据完成。将删除数据");
        if(1==type){
            int result =    t1Mapper.delte_3456(pre1+tablename);

            System.out.println("删除"+result+"条数据");

        }else{
            int result =    t1Mapper.delte_12(pre1+tablename);

            System.out.println("删除"+result+"条数据");

        }

            }

        }

        return  AjaxResult.success()    ;
    }

    @GetMapping("/startathread")
    public void  startathreadbycarid(Integer  carid){

        final String NORMAL = "normaldata_";
        ConcurrentHashMap<String, RedisRightPopTaskBlockingforRealDoneCang> threadRegister =     WebsocketServerForPosition.getThreadRegister();
        CarMapper carMapper = BeanContext.getApplicationContext().getBean(CarMapper.class);

        if (!threadRegister.containsKey(NORMAL + carid)) {
            //启动时传入车辆类型
            Car carinfo = carMapper.selectByPrimaryKey(carid);
            //每一辆车启动一个数据处理线程
            RedisRightPopTaskBlockingforRealDoneCang blocking = new RedisRightPopTaskBlockingforRealDoneCang();
            blocking.setKey(String.valueOf(carid));
            blocking.setKeytype(GlobCache.cartableprfix[carinfo.getType()]);
            Thread thread = new Thread(blocking);
            thread.start();
            threadRegister.put(NORMAL + carid, blocking);
            System.out.println(carid+"车数据处理线程启动成功");
        }else{
            System.out.println(carid+"车数据处理线程无需手动启动");
        }

    }


    @GetMapping("/dayevolution")
    public TableDataInfo getdayevolution(int pageNum,int pageSize){

        PageHelper.startPage(pageNum, pageSize);
        List<TrackStatistic> statisticsList = t1Mapper.getdayevolution();
        return getDataTable(statisticsList);

    }

 @GetMapping("/testsms")
    public AjaxResult success() {

        SMSBean  s =new SMSBean();
          s.setDevicename("123");
          s.setTime(DateUtils.getTime());
          s.setWarning("666");
          s.setUsertel("15320715451");
        SMSutils.send(s);

        return AjaxResult.success();
    }
}
