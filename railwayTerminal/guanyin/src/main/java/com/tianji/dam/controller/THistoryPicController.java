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
import com.tianji.dam.domain.THistoryPic;
import com.tianji.dam.service.ITHistoryPicService;
import com.tj.common.utils.poi.ExcelUtil;
import com.tj.common.core.page.TableDataInfo;

/**
 * 平面分析历史Controller
 * 
 * @author liyan
 * @date 2022-12-15
 */
@RestController
@RequestMapping("/dam/historypic")
public class THistoryPicController extends BaseController
{
    @Autowired
    private ITHistoryPicService tHistoryPicService;

    /**
     * 查询平面分析历史列表
     */
    @PreAuthorize("@ss.hasPermi('dam:historypic:list')")
    @GetMapping("/list")
    public TableDataInfo list(THistoryPic tHistoryPic)
    {
        startPage();
        List<THistoryPic> list = tHistoryPicService.selectTHistoryPicList(tHistoryPic);
        return getDataTable(list);
    }

    /**
     * 导出平面分析历史列表
     */
    @PreAuthorize("@ss.hasPermi('dam:historypic:export')")
    @Log(title = "平面分析历史", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, THistoryPic tHistoryPic)
    {
        List<THistoryPic> list = tHistoryPicService.selectTHistoryPicList(tHistoryPic);
        ExcelUtil<THistoryPic> util = new ExcelUtil<THistoryPic>(THistoryPic.class);
        util.exportExcel(response, list, "平面分析历史数据");
    }

    /**
     * 获取平面分析历史详细信息
     */
    @PreAuthorize("@ss.hasPermi('dam:historypic:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tHistoryPicService.selectTHistoryPicById(id));
    }

    /**
     * 新增平面分析历史
     */
    @PreAuthorize("@ss.hasPermi('dam:historypic:add')")
    @Log(title = "平面分析历史", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody THistoryPic tHistoryPic)
    {
        return toAjax(tHistoryPicService.insertTHistoryPic(tHistoryPic));
    }

    /**
     * 修改平面分析历史
     */
    @PreAuthorize("@ss.hasPermi('dam:historypic:edit')")
    @Log(title = "平面分析历史", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody THistoryPic tHistoryPic)
    {
        return toAjax(tHistoryPicService.updateTHistoryPic(tHistoryPic));
    }

    /**
     * 删除平面分析历史
     */
    @PreAuthorize("@ss.hasPermi('dam:historypic:remove')")
    @Log(title = "平面分析历史", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tHistoryPicService.deleteTHistoryPicByIds(ids));
    }
}
