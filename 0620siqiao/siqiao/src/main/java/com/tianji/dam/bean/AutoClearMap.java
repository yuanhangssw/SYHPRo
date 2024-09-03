package com.tianji.dam.bean;


import com.tianji.dam.domain.Car;
import com.tianji.dam.domain.DayConstructionInfo;
import com.tianji.dam.domain.MatrixItem;
import com.tianji.dam.domain.T1;
import com.tianji.dam.domain.vo.T1VO;
import com.tianji.dam.mapper.CarMapper;
import com.tianji.dam.mapper.DayConstructionInfoMapper;
import com.tianji.dam.mapper.T1Mapper;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.thread.TransferT1toRedisThread;
import com.tianji.dam.utils.GenerateRedisKey;
import com.tianji.dam.utils.RedisUtil;
import com.tj.common.utils.DateUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.util.*;

@Configuration
@EnableScheduling
public class AutoClearMap {

    private static final String NORMALRID = "riddata_m_";

    private DayConstructionInfoMapper dayConstructionInfoMapper;

    TDamsconstructionMapper damsconstructionMapper;

    private T1Mapper t1Service;

    @Scheduled(cron = "00 35 01 * * ? ")
    @Async
    public void clearRedisOldData() {
        RedisUtil redis = BeanContext.getApplicationContext().getBean(RedisUtil.class);
        System.out.println("执行删除2天前数据");
        //  riddata_m_2022-12-14_tpj_111669149726
        //删除三天之前的所有redis数据
        Date before3 = DateUtils.addDays(new Date(), -1);
        Set<String> allbefore7keys = redis.keys(NORMALRID + "*");
        for (String allbefore7key : allbefore7keys) {
            String[] keys2 = allbefore7key.split("_");
            if (keys2.length == 5) {
                String timepas = keys2[2];
                System.out.println("数据时间" + timepas);
                try {
                    if (DateUtils.parseDate(timepas).before(before3)) {
                        redis.del(allbefore7key);
                        System.out.println("删除");
                    } else {
                        System.out.println("不删除");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

//    @Scheduled(cron = "00 10 00 * * ? ")
//    @Async
    public void autoredisdata() {
        System.out.println("0点重新生成当天redis数据，避免跨天影响");
        CarMapper carMapper = BeanContext.getApplicationContext().getBean(CarMapper.class);
        dayConstructionInfoMapper =BeanContext.getApplicationContext().getBean(DayConstructionInfoMapper.class);
        damsconstructionMapper =BeanContext.getApplicationContext().getBean(TDamsconstructionMapper.class);
        t1Service =BeanContext.getApplicationContext().getBean(T1Mapper.class);
        List<Car> carList = carMapper.findCar();
        for (int i = 0; i < carList.size(); i++) {
            boolean clear = i == 0;
            Integer carid = carList.get(i).getCarID();
            String time = DateUtils.getDate();

            T1VO param = new T1VO();
            param.setBeginTimestamp(time + " 00:00:00");
            param.setEndTimestamp(time + " 23:59:59");
            param.setSort(1);
            param.setVehicleID(carid.toString());

            TransferT1toRedisThread tt = new TransferT1toRedisThread();
            tt.setVos(Collections.singletonList(param));
            tt.setKey(time);
            tt.setIsclear(clear);
            tt.setKeytype(GlobCache.cartableprfix[carList.get(i).getType()]);
            tt.call();

        }

    }

    /**
     * 定时获取接口的数据
     */
//    @Scheduled(cron = "0 0/5 * * * ?")
//    @Async
    public void getthreedata() {
        Map<String, Object> result = new HashMap<>();
        try {
            RedisUtil redis = BeanContext.getApplicationContext().getBean(RedisUtil.class);
            Set<String> keys = redis.keys(GenerateRedisKey.realTimeAllRidKey("ylj"));
            System.out.println("大屏接口获取到压路机" + keys.size() + "条");
            int itemcount = 0;
            Map<Integer, Integer> bmap = new HashMap<>();
            Map<String, Integer> vmap = new HashMap<>();
            for (String key : keys) {

                MatrixItem[][] item = (MatrixItem[][]) redis.get(key);

                for (MatrixItem[] matrixItems : item) {
                    for (MatrixItem matrixItem : matrixItems) {
                        if (null != matrixItem) {
                            itemcount++;
                            int rtimes = matrixItem.getRollingTimes() == null ? 0 : matrixItem.getRollingTimes();
                            int vtime = matrixItem.getIsVibrate() == null ? 0 : matrixItem.getIsVibrate();
                            int nvtime = matrixItem.getIsNotVibrate() == null ? 0 : matrixItem.getIsNotVibrate();
                            int rtimes9 = 6;
                            int rtimes5 = 2;
                            if (rtimes <= 2) {
                                if (bmap.containsKey(rtimes5)) {
                                    bmap.put(rtimes5, bmap.get(rtimes5) + 1);
                                } else {
                                    bmap.put(rtimes5, 1);
                                }
                            } else if (rtimes <= 5) {
                                if (bmap.containsKey(rtimes)) {
                                    bmap.put(rtimes, bmap.get(rtimes) + 1);
                                } else {
                                    bmap.put(rtimes, 1);
                                }
                            } else {
                                if (bmap.containsKey(rtimes9)) {
                                    bmap.put(rtimes9, bmap.get(rtimes9) + 1);
                                } else {
                                    bmap.put(rtimes9, 1);
                                }
                            }
                            if (vmap.containsKey("vibrate")) {
                                vmap.put("vibrate", vmap.get("vibrate") + vtime);
                            } else {
                                vmap.put("vibrate", vtime);
                            }
                            if (vmap.containsKey("notvibrate")) {
                                vmap.put("notvibrate", vmap.get("notvibrate") + nvtime);
                            } else {
                                vmap.put("notvibrate", nvtime);
                            }
                        }
                    }
                }
            }

            result.put("area", itemcount / 100.0);
            result.put("bianshu", bmap);
            result.put("Vibrate", vmap);

            redis.set("homeBI_THREEDATA", result);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // @Scheduled(cron = "00 10 06 * * ? ")
   public void dayevolution(){
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

           String begins = DateUtils.getDate() + " 00:00:00";
           String ends = DateUtils.getDate() + " 23:59:59";
           Long begintime = DateUtils.parseDate(begins).getTime();
           Long endtime = DateUtils.parseDate(ends).getTime();
           T1 t1 = t1Service.selectmaxevolution("ylj_t_1", begintime, endtime);
           Double maxevolution =0.0;
           if (null == t1) {
               maxevolution = t1Service.maxe();
           }else{
               maxevolution = t1.getElevation();
           }

           DayConstructionInfo dayConstructionInfo = new DayConstructionInfo();
           dayConstructionInfo.setDay(DateUtils.getDate());
           List<DayConstructionInfo> allold =  dayConstructionInfoMapper.selectDayConstructionInfoList(dayConstructionInfo);
           if(allold.size()==1){
               DayConstructionInfo old = allold.get(0);
               old.setTodayarea(new BigDecimal(totalarea));
               old.setTodayevolution(new BigDecimal(maxevolution));
               dayConstructionInfoMapper.updateDayConstructionInfo(old);
           }else{
               dayConstructionInfo.setDay(DateUtils.getDate());
               dayConstructionInfo.setTodayevolution(new BigDecimal(maxevolution));
               dayConstructionInfo.setTodayarea(new BigDecimal(totalarea));
               dayConstructionInfoMapper.insertDayConstructionInfo(dayConstructionInfo);
           }



       }
   }



}
