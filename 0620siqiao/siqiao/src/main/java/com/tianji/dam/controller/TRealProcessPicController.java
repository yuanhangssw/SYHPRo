package com.tianji.dam.controller;

import com.tianji.dam.domain.TRealProcessPic;
import com.tianji.dam.mapper.TRealProcessPicMapper;
import com.tianji.dam.service.ITRealProcessPicService;
import com.tj.common.annotation.Log;
import com.tj.common.core.controller.BaseController;
import com.tj.common.core.domain.AjaxResult;
import com.tj.common.core.page.TableDataInfo;
import com.tj.common.enums.BusinessType;
import com.tj.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 实景图管理Controller
 * 
 * @author liyan
 * @date 2023-12-28
 */
@RestController
@RequestMapping("/dam/realpic")
public class TRealProcessPicController extends BaseController
{
    @Autowired
    private ITRealProcessPicService tRealProcessPicService;
    @Autowired
    private TRealProcessPicMapper  mapper;

    /**
     * 查询实景图管理列表
     */

    @GetMapping("/list")
    public TableDataInfo list(TRealProcessPic tRealProcessPic)
    {
        startPage();
        List<TRealProcessPic> list = tRealProcessPicService.selectTRealProcessPicList(tRealProcessPic);
        return getDataTable(list);
    }

    @GetMapping("/getlastpic")
    public AjaxResult getlastpic(){
        return AjaxResult.success(mapper.selectlast());
    }

    /**
     * 导出实景图管理列表
     */

    @Log(title = "实景图管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TRealProcessPic tRealProcessPic)
    {
        List<TRealProcessPic> list = tRealProcessPicService.selectTRealProcessPicList(tRealProcessPic);
        ExcelUtil<TRealProcessPic> util = new ExcelUtil<TRealProcessPic>(TRealProcessPic.class);
        util.exportExcel(response, list, "实景图管理数据");
    }

    /**
     * 获取实景图管理详细信息
     */

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tRealProcessPicService.selectTRealProcessPicById(id));
    }

    /**
     * 新增实景图管理
     */

    @Log(title = "实景图管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TRealProcessPic tRealProcessPic)
    {
        return toAjax(tRealProcessPicService.insertTRealProcessPic(tRealProcessPic));
    }

    /**
     * 修改实景图管理
     */

    @Log(title = "实景图管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TRealProcessPic tRealProcessPic)
    {
        return toAjax(tRealProcessPicService.updateTRealProcessPic(tRealProcessPic));
    }

    /**
     * 删除实景图管理
     */

    @Log(title = "实景图管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tRealProcessPicService.deleteTRealProcessPicByIds(ids));
    }
}
