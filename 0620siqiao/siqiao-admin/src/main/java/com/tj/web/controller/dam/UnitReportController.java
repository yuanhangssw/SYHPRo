package com.tj.web.controller.dam;

import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.domain.RollingResult;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.service.DownloadService;
import com.tianji.dam.service.RollingDataService;
import com.tj.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RequestMapping("/unitreport")
@Controller

public class UnitReportController {
    @Autowired
    private RollingDataService rollingDataService;
    @Autowired
    private DownloadService downloadService;
    @Autowired
    private TDamsconstructionMapper tDamsconstructionMapper;

    /**
     * 普通仓位的报表
     *
     * @param request
     * @param response
     * @param tableName
     * @return
     */
    @RequestMapping("/getdata/{tableName}/{cartype}")
    @ResponseBody
    public AjaxResult getreportdata(HttpServletRequest request, HttpServletResponse response, @PathVariable("tableName") String tableName, @PathVariable("cartype") Integer cartype) {
        String userName = "abc";
        RollingResult rollingresult = null;
        Map<String, Object> data = null;
        try {
            JSONObject jsonObject2 = rollingDataService.unitreport_track_zhuanghao(userName, tableName, cartype);   //绘制历史碾压轨迹图
            String bsae64_string = jsonObject2.getString("base64");
            rollingresult = (RollingResult) jsonObject2.get("rollingResult");
            data = downloadService.exportService_data(tableName, bsae64_string.substring(22), rollingresult, cartype);
            data.put("width_bi", jsonObject2.get("width_bi"));
            data.put("height_bi", jsonObject2.get("height_bi"));
            data.put("angele", jsonObject2.get("angele"));
            return AjaxResult.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(e.getLocalizedMessage());
        }
    }

    /**
     * 铁路 桩号偏距 生成仓位的报表
     *
     * @param request
     * @param response
     * @param tableName
     * @return
     */

    @RequestMapping("/getdata_z/{tableName}")
    @ResponseBody
    public AjaxResult getreportdata_zhuanghao(HttpServletRequest request, HttpServletResponse response, @PathVariable("tableName") String tableName) {
        String userName = "abc";
        RollingResult rollingresult = null;
        Map<String, Object> data = null;
        try {
            JSONObject jsonObject2 = rollingDataService.unitreport_track_zhuanghao(userName, tableName, 1);   //绘制历史碾压轨迹图
            String bsae64_string = jsonObject2.getString("base64");
            rollingresult = (RollingResult) jsonObject2.get("rollingResult");
            data = downloadService.exportService_data(tableName, bsae64_string.substring(22), rollingresult, 1);
            data.put("width_bi", jsonObject2.get("width_bi"));
            data.put("height_bi", jsonObject2.get("height_bi"));
            data.put("angele", jsonObject2.get("angele"));
            return AjaxResult.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(e.getLocalizedMessage());
        }
    }

    /**
     * 压实程度
     *
     * @param tableName
     * @return
     */
    @RequestMapping("/getdata_chengdu/{tableName}/{rollingtime}")
    @ResponseBody
    public AjaxResult getreportdata_chengdu(@PathVariable("tableName") String tableName, @PathVariable("rollingtime") int rollingtime) {
        String userName = "abc";
        RollingResult rollingresult = null;
        Map<String, Object> data = null;
        try {
            JSONObject jsonObject2 = rollingDataService.unitreport_track_chengdu(userName, tableName, rollingtime);   //绘制历史碾压轨迹图
            String bsae64_string = jsonObject2.getString("base64");
            rollingresult = (RollingResult) jsonObject2.get("rollingResult");
            data = downloadService.exportService_chengdu(tableName, "", rollingresult);
            data.put("datalist", jsonObject2.get("rollingtimes2"));
            data.put("width_bi", jsonObject2.get("width_bi"));
            data.put("height_bi", jsonObject2.get("height_bi"));
            data.put("angele", jsonObject2.get("angele"));

            data.put("finalarea", jsonObject2.get("finalarea"));
            data.put("targetvalue", jsonObject2.get("targetvalue"));
            data.put("normalvalue", "");
            data.put("workfrequency", "");
            data.put("maxvalue", jsonObject2.get("maxvalue"));
            data.put("minvalu", jsonObject2.get("minvalu"));
            data.put("jicha", jsonObject2.get("jicha"));
            data.put("avg", jsonObject2.get("avg"));
            data.put("biaozhun", jsonObject2.get("biaozhun"));
            data.put("xishu", jsonObject2.get("xishu"));
            data.put("pass", jsonObject2.get("pass"));
            data.put("passarea", jsonObject2.get("passarea"));
            data.put("bianshu", rollingtime);
            data.put("kuandu", jsonObject2.get("kuandu"));
            data.put("begidate", jsonObject2.get("begidate"));
            data.put("enddate", jsonObject2.get("enddate"));
            return AjaxResult.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(e.getLocalizedMessage());
        }
    }


    /**
     * 相关校验报告
     *
     * @param tableName
     * @return
     */
    @RequestMapping("/getdata_verification/{tableName}")
    @ResponseBody
    public AjaxResult getreport_verification(@PathVariable("tableName") String tableName) {
        JSONObject jsonObject2 = rollingDataService.unitreport_verification(tableName);   //绘制历史碾压轨迹图
        return AjaxResult.success(jsonObject2);
    }

    /**
     * 过程控制报告
     * 用平面分析的模式先生成数据，然后将单元的所有遍数数据取出来。
     *
     * @param tableName
     * @return
     */
    @RequestMapping("/getdata_process/{tableName}")
    @ResponseBody
    public AjaxResult getreport_process(@PathVariable("tableName") String tableName) {

        try {
            JSONObject jsonObject2 = rollingDataService.unitreport_process(tableName);   //绘制历史碾压轨迹图
            return AjaxResult.success(jsonObject2);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }


    }

}
