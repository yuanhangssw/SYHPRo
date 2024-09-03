package com.tianji.dam.thread;

import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.domain.THistoryPic;
import com.tianji.dam.mapper.THistoryPicMapper;
import com.tianji.dam.service.RollingDataService;

import java.util.Date;
import java.util.concurrent.Callable;

public class AutoHistoryThread implements Callable<Integer> {

    private Integer damid;
    private Integer reporttype;
    private boolean isclear;

    public AutoHistoryThread(Integer damid, Integer reporttype, boolean isclear) {

        this.damid = damid;
        this.reporttype = reporttype;
        this.isclear = isclear;
    }

    @Override
    public Integer call() {
        System.out.println("开始平面分析。。");
        long g2time1 =System.currentTimeMillis();
        RollingDataService rollingDataService = BeanContext.getApplicationContext().getBean(RollingDataService.class);

        THistoryPicMapper historyPicMapper = BeanContext.getApplicationContext().getBean(THistoryPicMapper.class);
        JSONObject jsonObject2 = rollingDataService.getHistoryPicMultiThreadingByZhuangByTodNews(damid + "", reporttype);
       // JSONObject jsonObject2 = rollingDataService.getHistoryPicMultiThreadingByZhuangByTod("admin", damid + "");
        THistoryPic historyPic = new THistoryPic();

        if (isclear) {
            historyPicMapper.deleteTHistoryPicByDamAndType(damid, reporttype);
        }


        historyPic.setDamid(damid.longValue());
        historyPic.setHtype(reporttype.longValue());
        historyPic.setCreateTime(new Date());
      //  historyPic.setFreedom1(jsonObject2.getString("matriss"));
        jsonObject2.remove("matriss");
        historyPic.setContent(jsonObject2.toJSONString());
        historyPicMapper.insertTHistoryPic(historyPic);
        long g2time2 =System.currentTimeMillis();
        System.out.println("平面分析耗时："+(g2time2-g2time1)/1000);
        System.gc();

        return 1;
    }
}
