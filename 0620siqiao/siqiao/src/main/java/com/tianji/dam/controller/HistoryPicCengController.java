package com.tianji.dam.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tj.common.annotation.Log;
import com.tj.common.core.controller.BaseController;
import com.tj.common.core.domain.AjaxResult;
import com.tj.common.enums.BusinessType;
import com.tianji.dam.domain.HistoryPicCeng;
import com.tianji.dam.service.IHistoryPicCengService;
import com.tj.common.utils.poi.ExcelUtil;
import com.tj.common.core.page.TableDataInfo;

/**
 * 层位分析Controller
 * 
 * @author liyan
 * @date 2024-04-01
 */
@RestController
@RequestMapping("/dam/ceng")
public class HistoryPicCengController extends BaseController
{
    @Autowired
    private IHistoryPicCengService historyPicCengService;

    /**
     * 查询层位分析列表
     */
    @PreAuthorize("@ss.hasPermi('dam:ceng:list')")
    @GetMapping("/list")
    public TableDataInfo list(HistoryPicCeng historyPicCeng)
    {
        startPage();
        List<HistoryPicCeng> list = historyPicCengService.selectHistoryPicCengList(historyPicCeng);
        return getDataTable(list);
    }

    /**
     * 导出层位分析列表
     */
    @PreAuthorize("@ss.hasPermi('dam:ceng:export')")
    @Log(title = "层位分析", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HistoryPicCeng historyPicCeng)
    {
        List<HistoryPicCeng> list = historyPicCengService.selectHistoryPicCengList(historyPicCeng);
        ExcelUtil<HistoryPicCeng> util = new ExcelUtil<HistoryPicCeng>(HistoryPicCeng.class);
        util.exportExcel(response, list, "层位分析数据");
    }

    /**
     * 获取层位分析详细信息
     */
    @PreAuthorize("@ss.hasPermi('dam:ceng:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(historyPicCengService.selectHistoryPicCengById(id));
    }

    /**
     * 新增层位分析
     */
    @PreAuthorize("@ss.hasPermi('dam:ceng:add')")
    @Log(title = "层位分析", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HistoryPicCeng historyPicCeng)
    {
        return toAjax(historyPicCengService.insertHistoryPicCeng(historyPicCeng));
    }

    /**
     * 修改层位分析
     */
    @PreAuthorize("@ss.hasPermi('dam:ceng:edit')")
    @Log(title = "层位分析", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HistoryPicCeng historyPicCeng)
    {
        return toAjax(historyPicCengService.updateHistoryPicCeng(historyPicCeng));
    }

    /**
     * 删除层位分析
     */
    @PreAuthorize("@ss.hasPermi('dam:ceng:remove')")
    @Log(title = "层位分析", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(historyPicCengService.deleteHistoryPicCengByIds(ids));
    }
}
