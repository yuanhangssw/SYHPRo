package com.tianji.dam.thread;

import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.domain.RollingResult;
import com.tianji.dam.domain.TReportSave;
import com.tianji.dam.mapper.TReportSaveMapper;
import com.tianji.dam.service.DownloadService;
import com.tianji.dam.service.RollingDataService;
import com.tj.common.utils.DateUtils;

import java.util.Map;
import java.util.concurrent.Callable;

public class AutoReportThread implements Callable<Integer> {

    private Integer damid;
    private Integer reporttype;
    private boolean isclear;

    public AutoReportThread(Integer damid, Integer reporttype, boolean isclear) {

        this.damid = damid;
        this.reporttype = reporttype;
        this.isclear = isclear;
    }

    @Override
    public Integer call() {

        RollingDataService rollingDataService = BeanContext.getApplicationContext().getBean(RollingDataService.class);

        DownloadService downloadService = BeanContext.getApplicationContext().getBean(DownloadService.class);
        TReportSaveMapper reportSaveMapper = BeanContext.getApplicationContext().getBean(TReportSaveMapper.class);

        String userName = "abc";
        RollingResult rollingresult = null;
        Map<String, Object> data = null;
        try {
            long g2time1 =System.currentTimeMillis();
            System.out.println("开始自动生成报告数据。。");
            JSONObject jsonObject2 = rollingDataService.unitreport_track_zhuanghao(userName, damid + "", reporttype);   //绘制历史碾压轨迹图
            String bsae64_string = jsonObject2.getString("base64");
            rollingresult = (RollingResult) jsonObject2.get("rollingResult");

            data = downloadService.exportService_data(damid + "", bsae64_string.substring(22), rollingresult, reporttype);
            data.put("width_bi", jsonObject2.get("width_bi"));
            data.put("height_bi", jsonObject2.get("height_bi"));
            data.put("angele", jsonObject2.get("angele"));


            data.put("avgthick", jsonObject2.get("avghoudu"));
            data.put("maxthick", jsonObject2.get("maxhoudu"));
            data.put("minthick", jsonObject2.get("minhoudu"));


            Object jsonsave = JSONObject.toJSON(data);

            TReportSave reportSave = new TReportSave();
            reportSave.setTypes(reporttype.longValue());
            reportSave.setDamgid(damid.longValue());
            reportSave.setBase64(jsonsave.toString());
            reportSave.setCreateTime(DateUtils.getNowDate());
            reportSave.setCreateBy("admin_auto");
            if (isclear) {
                reportSaveMapper.deleteTReportSaveById(damid.longValue(), reporttype);
            }
            reportSaveMapper.insertTReportSave(reportSave);
            long g2time2 =System.currentTimeMillis();
            System.out.println("报告耗时："+(g2time2-g2time1)/1000);
        } catch (Exception e) {
            e.printStackTrace();

        }
        System.gc();
        return 1;
    }
}
