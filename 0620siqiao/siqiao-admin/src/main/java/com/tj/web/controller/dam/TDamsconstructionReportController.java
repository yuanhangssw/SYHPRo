package com.tj.web.controller.dam;

import com.tianji.dam.domain.TDamsconstructionReport;
import com.tianji.dam.service.ITDamsconstructionReportService;
import com.tj.common.annotation.Log;
import com.tj.common.core.controller.BaseController;
import com.tj.common.core.domain.AjaxResult;
import com.tj.common.core.page.TableDataInfo;
import com.tj.common.enums.BusinessType;
import com.tj.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 单元试验报告Controller
 * 
 * @author liyan
 * @date 2022-06-10
 */
@RestController
@RequestMapping("/dam/report")
public class TDamsconstructionReportController extends BaseController
{
    @Autowired
    private ITDamsconstructionReportService tDamsconstructionReportService;

    /**
     * 查询单元试验报告列表
     */
    @PreAuthorize("@ss.hasPermi('dam:report:list')")
    @GetMapping("/list")
    public TableDataInfo list(TDamsconstructionReport tDamsconstructionReport)
    {
        startPage();
        List<TDamsconstructionReport> list = tDamsconstructionReportService.selectTDamsconstructionReportList(tDamsconstructionReport);
        return getDataTable(list);
    }

    /**
     * 导出单元试验报告列表
     */
    @PreAuthorize("@ss.hasPermi('dam:report:export')")
    @Log(title = "单元试验报告", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TDamsconstructionReport tDamsconstructionReport)
    {
        List<TDamsconstructionReport> list = tDamsconstructionReportService.selectTDamsconstructionReportList(tDamsconstructionReport);
        ExcelUtil<TDamsconstructionReport> util = new ExcelUtil<TDamsconstructionReport>(TDamsconstructionReport.class);
        util.exportExcel(response, list, "单元试验报告数据");
    }

    /**
     * 获取单元试验报告详细信息
     */
    @PreAuthorize("@ss.hasPermi('dam:report:query')")
    @GetMapping(value = "/{gid}")
    public AjaxResult getInfo(@PathVariable("gid") Long gid)
    {
        return AjaxResult.success(tDamsconstructionReportService.selectTDamsconstructionReportByGid(gid));
    }

    /**
     * 新增单元试验报告
     */
    @PreAuthorize("@ss.hasPermi('dam:report:add')")
    @Log(title = "单元试验报告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TDamsconstructionReport tDamsconstructionReport)
    {
        return toAjax(tDamsconstructionReportService.insertTDamsconstructionReport(tDamsconstructionReport));
    }

    /**
     * 修改单元试验报告
     */
    @PreAuthorize("@ss.hasPermi('dam:report:edit')")
    @Log(title = "单元试验报告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TDamsconstructionReport tDamsconstructionReport)
    {
        return toAjax(tDamsconstructionReportService.updateTDamsconstructionReport(tDamsconstructionReport));
    }

    /**
     * 删除单元试验报告
     */
    @PreAuthorize("@ss.hasPermi('dam:report:remove')")
    @Log(title = "单元试验报告", businessType = BusinessType.DELETE)
	@DeleteMapping("/{gids}")
    public AjaxResult remove(@PathVariable Long[] gids)
    {

        return toAjax(tDamsconstructionReportService.deleteTDamsconstructionReportByGids(gids));
    }

}
