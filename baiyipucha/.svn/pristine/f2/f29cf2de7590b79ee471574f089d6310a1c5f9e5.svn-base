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
import com.ruoyi.system.domain.BPatrolType;
import com.ruoyi.system.service.IBPatrolTypeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 巡查类型Controller
 * 
 * @author ruoyi
 * @date 2024-03-05
 */
@RestController
@RequestMapping("/bPatrol/type")
public class BPatrolTypeController extends BaseController
{
    @Autowired
    private IBPatrolTypeService bPatrolTypeService;

    /**
     * 查询巡查类型列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BPatrolType bPatrolType)
    {
        startPage();
        List<BPatrolType> list = bPatrolTypeService.selectBPatrolTypeList(bPatrolType);
        return getDataTable(list);
    }

    /**
     * 导出巡查类型列表
     */
    @PreAuthorize("@ss.hasPermi('system:type:export')")
    @Log(title = "巡查类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BPatrolType bPatrolType)
    {
        List<BPatrolType> list = bPatrolTypeService.selectBPatrolTypeList(bPatrolType);
        ExcelUtil<BPatrolType> util = new ExcelUtil<BPatrolType>(BPatrolType.class);
        util.exportExcel(response, list, "巡查类型数据");
    }

    /**
     * 获取巡查类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:type:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bPatrolTypeService.selectBPatrolTypeById(id));
    }

    /**
     * 新增巡查类型
     */
    @PreAuthorize("@ss.hasPermi('system:type:add')")
    @Log(title = "巡查类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BPatrolType bPatrolType)
    {
        return toAjax(bPatrolTypeService.insertBPatrolType(bPatrolType));
    }

    /**
     * 修改巡查类型
     */
    @PreAuthorize("@ss.hasPermi('system:type:edit')")
    @Log(title = "巡查类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BPatrolType bPatrolType)
    {
        return toAjax(bPatrolTypeService.updateBPatrolType(bPatrolType));
    }

    /**
     * 删除巡查类型
     */
    @PreAuthorize("@ss.hasPermi('system:type:remove')")
    @Log(title = "巡查类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bPatrolTypeService.deleteBPatrolTypeByIds(ids));
    }
}
