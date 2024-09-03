package com.ruoyi.web.controller.termite;

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
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.BPatrolUnit;
import com.ruoyi.system.service.IBPatrolUnitService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 巡查单元Controller
 * 
 * @author ruoyi
 * @date 2024-03-05
 */
@RestController
@RequestMapping("/bPatrol/unit")
public class BPatrolUnitController extends BaseController
{
    @Autowired
    private IBPatrolUnitService bPatrolUnitService;

    /**
     * 查询巡查单元列表
     */
    @PreAuthorize("@ss.hasPermi('system:unit:list')")
    @GetMapping("/list")
    public TableDataInfo list(BPatrolUnit bPatrolUnit)
    {
        startPage();
        List<BPatrolUnit> list = bPatrolUnitService.selectBPatrolUnitList(bPatrolUnit);
        return getDataTable(list);
    }

    /**
     * 导出巡查单元列表
     */
    @PreAuthorize("@ss.hasPermi('system:unit:export')")
    @Log(title = "巡查单元", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BPatrolUnit bPatrolUnit)
    {
        List<BPatrolUnit> list = bPatrolUnitService.selectBPatrolUnitList(bPatrolUnit);
        ExcelUtil<BPatrolUnit> util = new ExcelUtil<BPatrolUnit>(BPatrolUnit.class);
        util.exportExcel(response, list, "巡查单元数据");
    }

    /**
     * 获取巡查单元详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:unit:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bPatrolUnitService.selectBPatrolUnitById(id));
    }

    /**
     * 新增巡查单元
     */
    @PreAuthorize("@ss.hasPermi('system:unit:add')")
    @Log(title = "巡查单元", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BPatrolUnit bPatrolUnit)
    {
        return toAjax(bPatrolUnitService.insertBPatrolUnit(bPatrolUnit));
    }

    /**
     * 修改巡查单元
     */
    @PreAuthorize("@ss.hasPermi('system:unit:edit')")
    @Log(title = "巡查单元", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BPatrolUnit bPatrolUnit)
    {
        return toAjax(bPatrolUnitService.updateBPatrolUnit(bPatrolUnit));
    }

    /**
     * 删除巡查单元
     */
    @PreAuthorize("@ss.hasPermi('system:unit:remove')")
    @Log(title = "巡查单元", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bPatrolUnitService.deleteBPatrolUnitByIds(ids));
    }
}
