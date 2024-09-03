package com.tj.quartz.task;

import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.DayConstructionInfo;
import com.tianji.dam.domain.MatrixItem;
import com.tianji.dam.domain.T1;
import com.tianji.dam.mapper.DayConstructionInfoMapper;
import com.tianji.dam.mapper.T1Mapper;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.utils.GenerateRedisKey;
import com.tianji.dam.utils.RedisUtil;
import com.tj.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component("BItask")
public class BITask {

    @Autowired
    DayConstructionInfoMapper  dayConstructionInfoMapper;
    @Autowired
    private T1Mapper t1Service;
    @Autowired
    TDamsconstructionMapper damsconstructionMapper;

    public  void    getdayarea(){
        RedisUtil redis = BeanContext.getApplicationContext().getBean(RedisUtil.class);
        List<Long> todayold = (List<Long>) redis.get("day_"+DateUtils.getDate());
        Integer datatotal =0;
        if(null==todayold){
            datatotal =0;
            todayold=new ArrayList<>();
        }
        //这里累计的是格子 每个格子是10*10cm  0.01平方
        Double  totalarea =    todayold.size() *100.0;


        Double maxevolution =0.0;

        List<Double> gaochenglist =new ArrayList<>();
        List<DamsConstruction> allopen =    damsconstructionMapper.findByStatus(8);
        //获取所有开仓的列表=按照分区累计

        for (DamsConstruction damsConstruction : allopen) {
             Double gaocheng =       damsConstruction.getGaocheng();
            gaochenglist.add(gaocheng);
        }
        if(gaochenglist.size()>0){
            maxevolution =   gaochenglist.stream().max(Double::compareTo).get().doubleValue();
        }
        //查询之前的高程作为当前高程
        if(maxevolution.intValue() ==0){
            int dayadd=-1;
            Date beforeone =    DateUtils.addDays(DateUtils.getNowDate(),dayadd);
            DayConstructionInfo dayConstructionInfo = new DayConstructionInfo();
            dayConstructionInfo.setDay(DateUtils.parseDateToStr("yyyy-MM-dd",beforeone));
            List<DayConstructionInfo> allold =  dayConstructionInfoMapper.selectDayConstructionInfoList(dayConstructionInfo);
                while (allold.size()==0){
                    dayadd--;
                     beforeone =    DateUtils.addDays(DateUtils.getNowDate(),dayadd);
                    dayConstructionInfo.setDay(DateUtils.parseDateToStr("yyyy-MM-dd",beforeone));
                    allold =  dayConstructionInfoMapper.selectDayConstructionInfoList(dayConstructionInfo);
                }
             maxevolution = allold.get(0).getTodayevolution().doubleValue();
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



}
