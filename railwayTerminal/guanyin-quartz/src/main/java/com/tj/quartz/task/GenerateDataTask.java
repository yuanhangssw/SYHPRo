package com.tj.quartz.task;

import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.THistoryPic;
import com.tianji.dam.domain.TRepairData;
import com.tianji.dam.domain.TReportSave;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.mapper.THistoryPicMapper;
import com.tianji.dam.mapper.TRepairDataMapper;
import com.tianji.dam.mapper.TReportSaveMapper;
import com.tianji.dam.thread.AutoHistoryThread;
import com.tianji.dam.thread.AutoHistoryThread_PIDCeng;
import com.tianji.dam.thread.AutoReportThread;
import com.tianji.dam.utils.RedisUtil;
import com.tj.common.utils.DateUtils;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("Generatedata")
public class GenerateDataTask {

    /**
     *  自动报告
     * @param dtype 1压实 2摊铺
     */
    public void autoreport(Integer dtype) {
        System.out.println("定时报表任务开始。。");
        Integer[] reporttype = {dtype};
        TDamsconstructionMapper mapper = BeanContext.getApplicationContext().getBean(TDamsconstructionMapper.class);
        TReportSaveMapper reportSaveMapper = BeanContext.getApplicationContext().getBean(TReportSaveMapper.class);
        TRepairDataMapper repairDataMapper = BeanContext.getApplicationContext().getBean(TRepairDataMapper.class);
        List<DamsConstruction> allcang = mapper.findAll();
        for (DamsConstruction damsConstruction : allcang) {
            Integer damid = damsConstruction.getId();
            for (Integer type : reporttype) {
                //如果仓位有新增的补录数据、（当天、前一天） 则需要从新生成一次。
                TRepairData repairData = new TRepairData();
                repairData.setCartype(type);
                repairData.setDamsid(damid);
                List<TRepairData> allrepair =     repairDataMapper.selectTRepairDatas(repairData);
                int    rday=0;
                for (TRepairData tRepairData : allrepair) {
                    long  repairtimel  =   Long.valueOf(tRepairData.getRepairTime()) ;
                    Date repaird = new Date();
                    repaird.setTime(repairtimel);
                    Calendar cl = Calendar.getInstance();
                    cl.setTime(repaird);
                    rday =    cl.get(Calendar.DAY_OF_YEAR);
                    break;

                }

                Calendar  cl = Calendar.getInstance();
                int nowday =    cl.get(Calendar.DAY_OF_YEAR);
                if(nowday-rday<=1){
                    AutoReportThread autoReportThread = new AutoReportThread(damid, type, true);
                    autoReportThread.call();
                    try {
                        Thread.sleep(2 * 1000);
                    } catch (InterruptedException e) {
                        System.out.println("平面分析补录数据生成出现错误：");
                        e.printStackTrace();
                    }
                    continue;
                }


                TReportSave savereport = reportSaveMapper.seletbyDamAndType(damid.longValue(), type.longValue());
                if (null == savereport && damsConstruction.getStatus() != 88) {
                    AutoReportThread autoReportThread = new AutoReportThread(damid, type, false);
//                    Thread reportthread = new Thread(autoReportThread);
//                    reportthread.start();
                    autoReportThread.call();

                } else if (savereport != null && damsConstruction.getStatus() == 8) {//开仓的仓位每天生成一次。
                    AutoReportThread autoReportThread = new AutoReportThread(damid, type, true);
//                    Thread reportthread = new Thread(autoReportThread);
//                    reportthread.start();
                    autoReportThread.call();

                }
                try {
                    Thread.sleep(2 * 1000);
                } catch (InterruptedException e) {
                    System.out.println("平面分析补录数据生成出现错误：");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 定时平面分析
     * @param dtype 1压实 2摊铺
     */
    public void autoHistoryceng(Integer dtype) {
        System.out.println("定时层分析任务开始。。");
        Integer[] reporttype = {dtype};
        TDamsconstructionMapper mapper = BeanContext.getApplicationContext().getBean(TDamsconstructionMapper.class);
        THistoryPicMapper historyPicMapper = BeanContext.getApplicationContext().getBean(THistoryPicMapper.class);
        TRepairDataMapper  repairDataMapper = BeanContext.getApplicationContext().getBean(TRepairDataMapper.class);

         Map<String,String> pidceng=new HashMap<>();



        List<DamsConstruction> allcang = mapper.findAll();
           allcang.forEach(d->{
                  pidceng.put(d.getPid()+","+d.getEngcode(),d.getStatus()+"");
       });

        pidceng.forEach((k,v)->{
                Integer pid = Integer.valueOf(k.split(",")[0]);
                Integer ceng = Integer.valueOf(k.split(",")[1]);
                if(Integer.valueOf(v)==8){
                    for (Integer type : reporttype) {
                    AutoHistoryThread_PIDCeng autoReportThread = new AutoHistoryThread_PIDCeng(pid,ceng,type, true);
                        autoReportThread.call();
                    }
                    try {
                        Thread.sleep(2 * 1000);
                    } catch (InterruptedException e) {
                        System.out.println("平面分析补录数据生成出现错误：");
                        e.printStackTrace();
                    }

                }

        });


    }


    public void autoHistory(Integer dtype) {
        System.out.println("定时平面分析任务开始。。");
        Integer[] reporttype = {dtype};
        TDamsconstructionMapper mapper = BeanContext.getApplicationContext().getBean(TDamsconstructionMapper.class);
        THistoryPicMapper historyPicMapper = BeanContext.getApplicationContext().getBean(THistoryPicMapper.class);
        TRepairDataMapper  repairDataMapper = BeanContext.getApplicationContext().getBean(TRepairDataMapper.class);

        List<DamsConstruction> allcang = mapper.findAll();

        for (DamsConstruction damsConstruction : allcang) {
            Integer damid = damsConstruction.getId();
            for (Integer type : reporttype) {

                try {
                    THistoryPic historyPic = new THistoryPic();
                    historyPic.setDamid(damid.longValue());
                    historyPic.setHtype(type.longValue());

                    List<THistoryPic> savereport = historyPicMapper.selectTHistoryPicList(historyPic);

                    if (savereport.size() == 0 && damsConstruction.getStatus() != 88) {

                        AutoHistoryThread autoReportThread = new AutoHistoryThread(damid, type, false);
                        autoReportThread.call();
//                        Thread reportthread = new Thread(autoReportThread);
//                        reportthread.start();

                    } else if ( damsConstruction.getStatus() == 8) {//开仓的仓位每天生成一次。
                        AutoHistoryThread autoReportThread = new AutoHistoryThread(damid, type, true);
                        autoReportThread.call();
//                        Thread reportthread = new Thread(autoReportThread);
//                        reportthread.start();

                    }
                    try {
                        Thread.sleep(2 * 1000);
                    } catch (InterruptedException e) {
                        System.out.println("平面分析补录数据生成出现错误：");
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    System.out.println("定时平面分析出现错误。");
                    e.printStackTrace();
                } finally {
                    System.gc();

                }
            }
        }
    }



    /**
     * 定时清理redis数据
     */
    public void clearRedisOldData() {
           String NORMALRID = "riddata_m_";
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
}
