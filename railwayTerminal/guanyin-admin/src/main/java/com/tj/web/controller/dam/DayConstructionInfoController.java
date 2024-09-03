package com.tj.web.controller.dam;

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
import com.tianji.dam.domain.DayConstructionInfo;
import com.tianji.dam.service.IDayConstructionInfoService;
import com.tj.common.utils.poi.ExcelUtil;
import com.tj.common.core.page.TableDataInfo;

/**
 * 日施工数据Controller
 * 
 * @author liyan
 * @date 2023-11-30
 */
@RestController
@RequestMapping("/dam/construction")
public class DayConstructionInfoController extends BaseController
{
    @Autowired
    private IDayConstructionInfoService dayConstructionInfoService;

    /**
     * 查询日施工数据列表
     */
    @PreAuthorize("@ss.hasPermi('dam:construction:list')")
    @GetMapping("/list")
    public TableDataInfo list(DayConstructionInfo dayConstructionInfo)
    {
        startPage();
        List<DayConstructionInfo> list = dayConstructionInfoService.selectDayConstructionInfoList(dayConstructionInfo);
        return getDataTable(list);
    }

    /**
     * 导出日施工数据列表
     */
    @PreAuthorize("@ss.hasPermi('dam:construction:export')")
    @Log(title = "日施工数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DayConstructionInfo dayConstructionInfo)
    {
        List<DayConstructionInfo> list = dayConstructionInfoService.selectDayConstructionInfoList(dayConstructionInfo);
        ExcelUtil<DayConstructionInfo> util = new ExcelUtil<DayConstructionInfo>(DayConstructionInfo.class);
        util.exportExcel(response, list, "日施工数据数据");
    }

    /**
     * 获取日施工数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('dam:construction:query')")
    @GetMapping(value = "/{gid}")
    public AjaxResult getInfo(@PathVariable("gid") Long gid)
    {
        return AjaxResult.success(dayConstructionInfoService.selectDayConstructionInfoByGid(gid));
    }

    /**
     * 新增日施工数据
     */
    @PreAuthorize("@ss.hasPermi('dam:construction:add')")
    @Log(title = "日施工数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DayConstructionInfo dayConstructionInfo)
    {
        return toAjax(dayConstructionInfoService.insertDayConstructionInfo(dayConstructionInfo));
    }

    /**
     * 修改日施工数据
     */
    @PreAuthorize("@ss.hasPermi('dam:construction:edit')")
    @Log(title = "日施工数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DayConstructionInfo dayConstructionInfo)
    {
        return toAjax(dayConstructionInfoService.updateDayConstructionInfo(dayConstructionInfo));
    }

    /**
     * 删除日施工数据
     */
    @PreAuthorize("@ss.hasPermi('dam:construction:remove')")
    @Log(title = "日施工数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{gids}")
    public AjaxResult remove(@PathVariable Long[] gids)
    {
        return toAjax(dayConstructionInfoService.deleteDayConstructionInfoByGids(gids));
    }
}
