package com.tj.web.controller.dam;

import com.tianji.dam.domain.TProjectUpfile;
import com.tianji.dam.service.ITProjectUpfileService;
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
 * 项目试验文件Controller
 * 
 * @author liyan
 * @date 2022-06-14
 */
@RestController
@RequestMapping("/dam/projectfile")
public class TProjectUpfileController extends BaseController
{
    @Autowired
    private ITProjectUpfileService tProjectUpfileService;

    /**
     * 查询项目试验文件列表
     */
    @PreAuthorize("@ss.hasPermi('dam:projectfile:list')")
    @GetMapping("/list")
    public TableDataInfo list(TProjectUpfile tProjectUpfile)
    {
        startPage();
        tProjectUpfile.setDelflag("N");
        List<TProjectUpfile> list = tProjectUpfileService.selectTProjectUpfileList(tProjectUpfile);
        return getDataTable(list);
    }

    /**
     * 导出项目试验文件列表
     */
    @PreAuthorize("@ss.hasPermi('dam:projectfile:export')")
    @Log(title = "项目试验文件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TProjectUpfile tProjectUpfile)
    {
        List<TProjectUpfile> list = tProjectUpfileService.selectTProjectUpfileList(tProjectUpfile);
        ExcelUtil<TProjectUpfile> util = new ExcelUtil<TProjectUpfile>(TProjectUpfile.class);
        util.exportExcel(response, list, "项目试验文件数据");
    }

    /**
     * 获取项目试验文件详细信息
     */
    @PreAuthorize("@ss.hasPermi('dam:projectfile:query')")
    @GetMapping(value = "/{gid}")
    public AjaxResult getInfo(@PathVariable("gid") Long gid)
    {
        return AjaxResult.success(tProjectUpfileService.selectTProjectUpfileByGid(gid));
    }

    /**
     * 新增项目试验文件
     */
    @PreAuthorize("@ss.hasPermi('dam:projectfile:add')")
    @Log(title = "项目试验文件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TProjectUpfile tProjectUpfile)
    {
        return toAjax(tProjectUpfileService.insertTProjectUpfile(tProjectUpfile));
    }

    /**
     * 修改项目试验文件
     */
    @PreAuthorize("@ss.hasPermi('dam:projectfile:edit')")
    @Log(title = "项目试验文件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TProjectUpfile tProjectUpfile)
    {
        return toAjax(tProjectUpfileService.updateTProjectUpfile(tProjectUpfile));
    }

    /**
     * 删除项目试验文件
     */
    @PreAuthorize("@ss.hasPermi('dam:projectfile:remove')")
    @Log(title = "项目试验文件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{gids}")
    public AjaxResult remove(@PathVariable Long[] gids)
    {
        return toAjax(tProjectUpfileService.deleteTProjectUpfileByGids(gids));
    }
}
