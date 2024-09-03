package com.ruoyi.web.controller.termite;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.BDykePreventionMapper;
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
import com.ruoyi.system.domain.BDykePrevention;
import com.ruoyi.system.service.IBDykePreventionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 堤防防治情况Controller
 * 
 * @author
 * @date 2024-04-08
 */
@RestController
@RequestMapping("/system/dfprevention")
public class BDykePreventionController extends BaseController
{
    @Autowired
    private IBDykePreventionService bDykePreventionService;

    @Autowired
    private BDykePreventionMapper bDykePreventionMapper;

    /**
     * 查询堤防防治情况列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BDykePrevention bDykePrevention)
    {
        startPage();
        List<BDykePrevention> list = bDykePreventionService.selectBDykePreventionList(bDykePrevention);
        return getDataTable(list);
    }

    /**
     * 导出堤防防治情况列表
     */
    @Log(title = "堤防防治情况", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BDykePrevention bDykePrevention)
    {
        List<BDykePrevention> list = bDykePreventionService.selectBDykePreventionList(bDykePrevention);
        ExcelUtil<BDykePrevention> util = new ExcelUtil<BDykePrevention>(BDykePrevention.class);
        util.exportExcel(response, list, "堤防防治情况数据");
    }

    /**
     * 获取堤防防治情况详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bDykePreventionService.selectBDykePreventionById(id));
    }

    /**
     * 新增堤防防治情况
     */
    @Log(title = "堤防防治情况", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BDykePrevention bDykePrevention)
    {
        Long unitId = bDykePrevention.getUnitId();
        Long detrimentType = bDykePrevention.getDetrimentType();
        String i= bDykePreventionMapper.selectBReservoirPreventionByUnitId(unitId, detrimentType);
        if(!StringUtils.isNull(i)){
            return AjaxResult.error("新增失败！");
        }
        else {
            return toAjax(bDykePreventionService.insertBDykePrevention(bDykePrevention));
        }

    }

    /**
     * 修改堤防防治情况
     */
    @Log(title = "堤防防治情况", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BDykePrevention bDykePrevention)
    {
        Long unitId = bDykePrevention.getUnitId();
        Long detrimentType = bDykePrevention.getDetrimentType();
        String i= bDykePreventionMapper.selectBReservoirPreventionByUnitId(unitId,detrimentType);
 //       if(!StringUtils.isNull(i)){
 //           return AjaxResult.error("修改失败！");
 //       }
   //     else {
            return toAjax(bDykePreventionService.updateBDykePrevention(bDykePrevention));
   //     }
    }

    /**
     * 删除堤防防治情况
     */
    @Log(title = "堤防防治情况", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bDykePreventionService.deleteBDykePreventionByIds(ids));
    }
}
