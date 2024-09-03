package com.tianji.dam.thread;

import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.RollingResult;
import com.tianji.dam.domain.TReportSave;
import com.tianji.dam.mapper.TDamsconstructionMapper;
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


    private TDamsconstructionMapper tDamsconstructionMapper;


    public AutoReportThread(Integer damid, Integer reporttype, boolean isclear) {

        this.damid = damid;
        this.reporttype = reporttype;
        this.isclear = isclear;
    }

    @Override
    public Integer call() {
        tDamsconstructionMapper = BeanContext.getApplicationContext().getBean(TDamsconstructionMapper.class);

        RollingDataService rollingDataService = BeanContext.getApplicationContext().getBean(RollingDataService.class);

        DownloadService downloadService = BeanContext.getApplicationContext().getBean(DownloadService.class);
        TReportSaveMapper reportSaveMapper = BeanContext.getApplicationContext().getBean(TReportSaveMapper.class);

        String userName = "abc";
        RollingResult rollingresult = null;
        Map<String, Object> data = null;
        try {
            long g2time1 = System.currentTimeMillis();
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

            //添加代码   把厚度拿出来作对比看是否合格
            if (null != jsonObject2.get("houdu")) {
                double houdu = jsonObject2.getDouble("houdu");
                DamsConstruction damsConstruction = tDamsconstructionMapper.getDamconstructionById(damid);
                double target_houdu = 0;

                int pid = damsConstruction.getPid();
                if (pid == 11 || pid == 12) {
                    target_houdu = 120d;
                } else if (pid == 13 || pid == 14) {
                    target_houdu = 40d;

                } else if (pid == 15) {
                    target_houdu = 20d;
                }


                //是否符合质量要求
                if (houdu > target_houdu) {
                    data.put("houduQuality", 1);//不符合质量要求
                } else {
                    data.put("houduQuality", 0);//符合质量要求
                }

            }

            data.put("houdu", jsonObject2.getLong("houdu"));

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
            long g2time2 = System.currentTimeMillis();
            System.out.println("报告耗时：" + (g2time2 - g2time1) / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.gc();
        return 1;
    }


}
