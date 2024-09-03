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
import com.ruoyi.system.domain.BPatrolFile;
import com.ruoyi.system.service.IBPatrolFileService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 巡查文件Controller
 * 
 * @author ruoyi
 * @date 2024-03-05
 */
@RestController
@RequestMapping("/bPatrol/file")
public class BPatrolFileController extends BaseController
{
    @Autowired
    private IBPatrolFileService bPatrolFileService;

    /**
     * 查询巡查文件列表
     */
    @PreAuthorize("@ss.hasPermi('system:file:list')")
    @GetMapping("/list")
    public TableDataInfo list(BPatrolFile bPatrolFile)
    {
        startPage();
        List<BPatrolFile> list = bPatrolFileService.selectBPatrolFileList(bPatrolFile);
        return getDataTable(list);
    }

    /**
     * 导出巡查文件列表
     */
    @PreAuthorize("@ss.hasPermi('system:file:export')")
    @Log(title = "巡查文件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BPatrolFile bPatrolFile)
    {
        List<BPatrolFile> list = bPatrolFileService.selectBPatrolFileList(bPatrolFile);
        ExcelUtil<BPatrolFile> util = new ExcelUtil<BPatrolFile>(BPatrolFile.class);
        util.exportExcel(response, list, "巡查文件数据");
    }

    /**
     * 获取巡查文件详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:file:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bPatrolFileService.selectBPatrolFileById(id));
    }

    /**
     * 新增巡查文件
     */
    @PreAuthorize("@ss.hasPermi('system:file:add')")
    @Log(title = "巡查文件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BPatrolFile bPatrolFile)
    {
        return toAjax(bPatrolFileService.insertBPatrolFile(bPatrolFile));
    }

    /**
     * 修改巡查文件
     */
    @PreAuthorize("@ss.hasPermi('system:file:edit')")
    @Log(title = "巡查文件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BPatrolFile bPatrolFile)
    {
        return toAjax(bPatrolFileService.updateBPatrolFile(bPatrolFile));
    }

    /**
     * 删除巡查文件
     */
    @PreAuthorize("@ss.hasPermi('system:file:remove')")
    @Log(title = "巡查文件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bPatrolFileService.deleteBPatrolFileByIds(ids));
    }
}
