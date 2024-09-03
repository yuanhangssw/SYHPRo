package com.tianji.dam.thread;

import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.domain.HistoryPicCeng;
import com.tianji.dam.domain.THistoryPic;
import com.tianji.dam.mapper.HistoryPicCengMapper;
import com.tianji.dam.mapper.THistoryPicMapper;
import com.tianji.dam.service.RollingDataService;

import java.util.Date;
import java.util.concurrent.Callable;

public class AutoHistoryThread_PIDCeng implements Callable<Integer> {

    private Integer pid;
    private Integer ceng;
    private Integer reporttype;
    private boolean isclear;

    public AutoHistoryThread_PIDCeng(Integer pid,Integer ceng, Integer reporttype, boolean isclear) {

        this.pid = pid;
        this.ceng = ceng;
        this.reporttype = reporttype;
        this.isclear = isclear;
    }

    @Override
    public Integer call() {
        System.out.println("开始平面分析。。");
        long g2time1 =System.currentTimeMillis();
        RollingDataService rollingDataService = BeanContext.getApplicationContext().getBean(RollingDataService.class);

        HistoryPicCengMapper historyPicMapper = BeanContext.getApplicationContext().getBean(HistoryPicCengMapper.class);
        JSONObject jsonObject2 = rollingDataService.getHistoryPicbyalltabledata(pid, ceng, reporttype);
        jsonObject2.remove("matriss");
       // JSONObject jsonObject2 = rollingDataService.getHistoryPicMultiThreadingByZhuangByTod("admin", damid + "");
        HistoryPicCeng historyPic = new HistoryPicCeng();

        if (isclear) {
            historyPicMapper.deleteTHistoryPicBycengAndType(ceng, reporttype);
        }
        historyPic.setPid(Long.valueOf(pid));
        historyPic.setCeng(Long.valueOf(ceng));
        historyPic.setHtype(reporttype.longValue());
        historyPic.setCreateTime(new Date());
      //  historyPic.setFreedom1(jsonObject2.getString("matriss"));

        historyPic.setContent(jsonObject2.toJSONString());
        historyPicMapper.insertHistoryPicCeng(historyPic);
        long g2time2 =System.currentTimeMillis();
        System.out.println("平面分析耗时："+(g2time2-g2time1)/1000);
        System.gc();

        return 1;
    }
}
