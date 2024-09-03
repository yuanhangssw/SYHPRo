package com.ruoyi.web.controller.termite;

import java.util.Date;
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
import com.ruoyi.system.domain.BFill;
import com.ruoyi.system.service.IBFillService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 填报登记Controller
 * 
 * @author ruoyi
 * @date 2024-03-07
 */
@RestController
@RequestMapping("/system/fill")
public class BFillController extends BaseController
{
    @Autowired
    private IBFillService bFillService;

    /**
     * 查询填报登记列表
     */
    //@PreAuthorize("@ss.hasPermi('system:fill:list')")
    @GetMapping("/list")
    public TableDataInfo list(BFill bFill)
    {
        //startPage();
        List<BFill> list = bFillService.selectBFillList(bFill);
        return getDataTable(list);
    }

    /**
     * 导出填报登记列表
     */
    //@PreAuthorize("@ss.hasPermi('system:fill:export')")
    @Log(title = "填报登记", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BFill bFill)
    {
        List<BFill> list = bFillService.selectBFillList(bFill);
        ExcelUtil<BFill> util = new ExcelUtil<BFill>(BFill.class);
        util.exportExcel(response, list, "填报登记数据");
    }

    /**
     * 获取填报登记详细信息
     */
    //@PreAuthorize("@ss.hasPermi('system:fill:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bFillService.selectBFillById(id));
    }

    /**
     * 新增填报登记
     */
    //@PreAuthorize("@ss.hasPermi('system:fill:add')")
    @Log(title = "填报登记", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BFill bFill)
    {
        bFill.setCreateTime(new Date());
        return toAjax(bFillService.insertBFill(bFill));
    }

    /**
     * 修改填报登记
     */
   // @PreAuthorize("@ss.hasPermi('system:fill:edit')")
    @Log(title = "填报登记", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BFill bFill)
    {
        bFill.setCreateTime(new Date());
        return toAjax(bFillService.updateBFill(bFill));
    }

    /**
     * 删除填报登记
     */
   // @PreAuthorize("@ss.hasPermi('system:fill:remove')")
    @Log(title = "填报登记", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bFillService.deleteBFillByIds(ids));
    }
}
