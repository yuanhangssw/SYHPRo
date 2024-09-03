package com.tj.web.controller.dam;

import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.domain.*;
import com.tianji.dam.domain.vo.ReportSaveVO;
import com.tianji.dam.mapper.CangAllitemsMapper;
import com.tianji.dam.mapper.CarMapper;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.mapper.TReportSaveMapper;
import com.tianji.dam.service.DownloadService;
import com.tianji.dam.service.ITReportSaveService;
import com.tianji.dam.service.RollingDataService;
import com.tianji.dam.service.TDamsconstructionService;
import com.tj.common.annotation.Log;
import com.tj.common.core.controller.BaseController;
import com.tj.common.core.domain.AjaxResult;
import com.tj.common.core.page.TableDataInfo;
import com.tj.common.enums.BusinessType;
import com.tj.common.utils.poi.ExcelUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//https://szdb.shdctech.com:8202/#/compaction/unitreport/unitreport

/**
 * 报表保存Controller
 *
 * @author liyan
 * @date 2022-10-14
 */
@RestController
@RequestMapping("/dam/reportsave")
public class TReportSaveController extends BaseController {
    @Autowired
    private ITReportSaveService tReportSaveService;
    @Autowired
    private RollingDataService rollingDataService;
    @Autowired
    private DownloadService downloadService;

    @Autowired
    private TDamsconstructionService tDamsconstructionService;
    @Autowired
    private TDamsconstructionMapper damsconstructionMapper;
    @Autowired
    CangAllitemsMapper cangAllitemsMapper;
    @Autowired
    CarMapper carMapper;

    @Autowired
    TReportSaveMapper tReportSaveMapper;

    /**
     * 查询报表保存列表
     */

    @GetMapping("/list")
    public TableDataInfo list(TReportSave tReportSave) {
        startPage();
        List<TReportSave> list = tReportSaveService.selectTReportSaveList(tReportSave);
        return getDataTable(list);
    }

    /**
     * 导出报表保存列表
     */

    @Log(title = "报表保存", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AreaReportInfo areaReportInfo) {
        startPage();
        areaReportInfo.setTypes(1l);
        List<TReportSave> list = tReportSaveService.selectTReportSaveListByAreaReportInfo(areaReportInfo);
        List<ReportSaveVO> allvo = new ArrayList<>();
        for (TReportSave save : list) {
            String savedata = save.getBase64();
            ReportSaveVO one = JSONObject.parseObject(savedata, ReportSaveVO.class);
            DamsConstruction damsConstruction = damsconstructionMapper.selectByPrimaryKey(save.getDamgid().intValue());
            one.setId(save.getDamgid().intValue());
            one.setMaterialname(damsConstruction.getMaterialname());
            one.setVolume(new BigDecimal(one.getFinalarea() * one.getHoudu() / 100.0).setScale(2, RoundingMode.DOWN).doubleValue());
            allvo.add(one);
        }

        ExcelUtil<ReportSaveVO> util = new ExcelUtil<ReportSaveVO>(ReportSaveVO.class);
        util.exportExcel(response, allvo, "报表保存数据");
    }

    /**
     * 获取报表保存详细信息
     */

    @GetMapping(value = "/{id}/{types}")
    public AjaxResult getInfo(@PathVariable("id") Long id, @PathVariable("types") Long types) {
//        String userName = "abc";
//        RollingResult rollingresult = null;
//        Map<String, Object> data = null;
//        try {
//            JSONObject jsonObject2 = rollingDataService.unitreport_track_zhuanghao(userName, id + "", types.intValue());   //绘制历史碾压轨迹图
//            String bsae64_string = jsonObject2.getString("base64");
//
//            rollingresult = (RollingResult) jsonObject2.get("rollingResult");
//            data = downloadService.exportService_data(id + "", bsae64_string.substring(22), rollingresult, types.intValue());
//            data.put("width_bi", jsonObject2.get("width_bi"));
//            data.put("height_bi", jsonObject2.get("height_bi"));
//            data.put("angele", jsonObject2.get("angele"));
//
//            data.put("avgthick", jsonObject2.get("avghoudu"));
//            data.put("maxthick", jsonObject2.get("maxhoudu"));
//            data.put("minthick", jsonObject2.get("minhoudu"));
//
//            return AjaxResult.success(data);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return AjaxResult.error(e.getLocalizedMessage());
//        }
        List<DamsConstruction> allchild = new ArrayList<>();
        List<TReportSave> allreportsave = new ArrayList<>();
        for (DamsConstruction damsConstruction : allchild) {

            TReportSave one = tReportSaveService.seletbyDamAndType(Long.valueOf(damsConstruction.getId()), 2l);
            allreportsave.add(one);

        }

/*
        TReportSave tReportSave = tReportSaveService.seletbyDamAndType(id, types);
        String base64 = tReportSave.getBase64();
        // 解析JSON字符串
        JSONObject jsonObject = JSONObject.parseObject(base64);
        if (jsonObject != null) {
            double beforegaocheng = jsonObject.getDouble("beforegaocheng");
            System.out.println(beforegaocheng);
        }*/


        return AjaxResult.success(tReportSaveService.seletbyDamAndType(id, types));
    }


    /**
     * //2023年9月13日09:34:42   新加
     * 获取报表保存详细信息     报表查看
     */

    @GetMapping(value = "/area/{id}/{pageno}/{pagesize}/{ceng}")
    public AjaxResult getReportInfo(@PathVariable("id") Long id, @PathVariable("pageno") int pageno, @PathVariable("pagesize") int pagesize, @PathVariable("ceng") int ceng) {
        //!！！！  实体类 TReportSave   中添加了字段  titile

        //设置分页数据
        int start = (pageno - 1) * pagesize;
        int end = pagesize;


        //1.根据父id查询child   这里算的是有多少个仓位   一个仓位对应一个报告
        List<DamsConstruction> allchild = new ArrayList<>();
        int countTotal = tDamsconstructionService.selectCountNumber(id);                                           //查询有多少条数据
        allchild = tDamsconstructionService.selectChildById(id, start, end);     //仓位需要分页

        List<TReportSave> allreportsave = new ArrayList<>();
        for (DamsConstruction damsConstruction : allchild) {

            TReportSave one = tReportSaveService.seletbyDamById(Long.valueOf(damsConstruction.getId()), 1l);//遍历每一个仓找到对应报告   因为只有压实
            if (one == null) {
                continue;
            }
            allreportsave.add(one);
        }

        Map<String, Object> map = new HashMap();
        map.put("dataList", allreportsave);
        map.put("countTotal", countTotal);

        return AjaxResult.success(map);
    }

    @PostMapping(value = "/area/getReportInfo")
    public TableDataInfo getAreaReportInfo(AreaReportInfo areaReportInfo) {
        startPage();
        areaReportInfo.setTypes(1l);
        List<TReportSave> list = tReportSaveService.selectTReportSaveListByAreaReportInfo(areaReportInfo);
        return getDataTable(list);
    }

    @PostMapping(value = "/area/exportReportInfo")
    public void getAreaReportInfo(HttpServletResponse response, AreaReportInfo areaReportInfo) {
        startPage();
        areaReportInfo.setTypes(1l);
        List<TReportSave> list = tReportSaveService.selectTReportSaveListByAreaReportInfo(areaReportInfo);
        List<ReportSaveVO> allvo = new ArrayList<>();
        for (TReportSave save : list) {
            String savedata = save.getBase64();
            ReportSaveVO one = JSONObject.parseObject(savedata, ReportSaveVO.class);
            allvo.add(one);
        }

        ExcelUtil<ReportSaveVO> util = new ExcelUtil<ReportSaveVO>(ReportSaveVO.class);
        util.exportExcel(response, allvo, "报表保存数据");

    }


    @GetMapping("/getpointdatalist")
    public List<RollingData> getpointdatalist(String damid, double x, double y) {
        List<RollingData> results = new ArrayList<>();

        String[] damidss = damid.split(",");


        for (String s : damidss) {
            Integer damidi = Integer.valueOf(s);
            DamsConstruction damsConstruction = damsconstructionMapper.selectByPrimaryKey(damidi);

            String tablename = "cang_ceng_" + damsConstruction.getPid() + "_" + damsConstruction.getEngcode();
            int ishave = cangAllitemsMapper.checktable(tablename);
            if (ishave == 0) {
                cangAllitemsMapper.createtable(tablename);
            }
            List<CangAllitems> cangoneitemss = cangAllitemsMapper.getcanglasttems(tablename, (int) x, (int) y);

            if (null == cangoneitemss) {
                continue;
            } else {
                CangAllitems cangoneitem = cangoneitemss.get(0);
                RollingData r = new RollingData();
                r.setTablename(damsConstruction.getTitle());
                r.setElevation((float) (cangoneitem.getPz() / 100.0));
                r.setVibrateValue(Double.valueOf(cangoneitem.getVcv() / 100.0));
                r.setSpeed((float) (cangoneitem.getSpeed() / 100.0));
                r.setTimestamp(cangoneitem.getTimes());
                results.add(r);
            }

        }
        return results;

    }


    /**
     * 新增报表保存
     */

    @Log(title = "报表保存", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TReportSave tReportSave) {
        return toAjax(tReportSaveService.insertTReportSave(tReportSave));
    }

    /**
     * 修改报表保存
     */

    @Log(title = "报表保存", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TReportSave tReportSave) {
        return toAjax(tReportSaveService.updateTReportSave(tReportSave));
    }

    /**
     * 删除报表保存
     */

    @Log(title = "报表删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tReportSaveService.deleteTReportSaveByIds(ids));
    }


    @GetMapping("/listByBase64")
    public void listByBase64(HttpServletResponse response) throws IOException {

        // 假设从数据库获取的base64List
        List<Map<String, String>> base64List = tReportSaveMapper.listByBase64();

        // 创建工作簿
        Workbook workbook = new XSSFWorkbook();
        // 创建工作表
        Sheet sheet = workbook.createSheet("Report");

        // 创建表头
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("startendtime");
        headerRow.createCell(1).setCellValue("partname");
        headerRow.createCell(2).setCellValue("engcode");
        headerRow.createCell(3).setCellValue("areaname");
        headerRow.createCell(4).setCellValue("houdu");
        headerRow.createCell(5).setCellValue("avgspeed");

        // 填充数据
        int rowNum = 1;
        for (Map<String, String> map : base64List) {
            String base64 = map.get("base64");
            String engcode = map.get("engcode");

            // 将base64字符串转换为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(base64);
            String startendtime = jsonObject.getString("startendtime");
            String partname = jsonObject.getString("partname");
            String areaname = jsonObject.getString("areaname");
            String houdu = jsonObject.getString("houdu");
            String avgspeed = jsonObject.getString("avgspeed");

            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(startendtime);
            row.createCell(1).setCellValue(partname);
            row.createCell(2).setCellValue("第" + engcode + "层");
            row.createCell(3).setCellValue(areaname);
            row.createCell(4).setCellValue(houdu != null ? houdu : ""); // 如果houdu不存在，填空字符串
            row.createCell(5).setCellValue(avgspeed);
        }

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=report.xlsx");

        // 输出到浏览器
        try {
            workbook.write(response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭工作簿
            workbook.close();
        }
    }


}
