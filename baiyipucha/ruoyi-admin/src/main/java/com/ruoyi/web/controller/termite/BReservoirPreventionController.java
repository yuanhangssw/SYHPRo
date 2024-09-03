package com.ruoyi.web.controller.termite;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.BReservoirPreventionMapper;
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
import com.ruoyi.system.domain.BReservoirPrevention;
import com.ruoyi.system.service.IBReservoirPreventionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 水库防治情况Controller
 * 
 * @author
 * @date 2024-04-08
 */
@RestController
@RequestMapping("/system/skprevention")
public class BReservoirPreventionController extends BaseController
{
    @Autowired
    private IBReservoirPreventionService bReservoirPreventionService;

    @Autowired
    private BReservoirPreventionMapper bReservoirPreventionMapper;

    /**
     * 查询水库防治情况列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BReservoirPrevention bReservoirPrevention)
    {
        startPage();
        List<BReservoirPrevention> list = bReservoirPreventionService.selectBReservoirPreventionList(bReservoirPrevention);
        return getDataTable(list);
    }

    /**
     * 导出水库防治情况列表
     */
    @Log(title = "水库防治情况", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BReservoirPrevention bReservoirPrevention)
    {
        List<BReservoirPrevention> list = bReservoirPreventionService.selectBReservoirPreventionList(bReservoirPrevention);
        ExcelUtil<BReservoirPrevention> util = new ExcelUtil<BReservoirPrevention>(BReservoirPrevention.class);
        util.exportExcel(response, list, "水库防治情况数据");
    }

    /**
     * 获取水库防治情况详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bReservoirPreventionService.selectBReservoirPreventionById(id));
    }

    /**
     * 新增水库防治情况
     */
    @Log(title = "水库防治情况", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BReservoirPrevention bReservoirPrevention)
    {
        Long unitId = bReservoirPrevention.getUnitId();
        String detrimentType = bReservoirPrevention.getDetrimentType();
        String i= bReservoirPreventionMapper.selectBReservoirPreventionByUnitId(unitId,detrimentType);
        if(!StringUtils.isNull(i)){
            return AjaxResult.error("插入失败！");
        }
        else {
            return toAjax(bReservoirPreventionService.insertBReservoirPrevention(bReservoirPrevention));
        }
    }

    /**
     * 修改水库防治情况
     */
    @Log(title = "水库防治情况", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BReservoirPrevention bReservoirPrevention)
    {
        Long unitId = bReservoirPrevention.getUnitId();
        String detrimentType = bReservoirPrevention.getDetrimentType();
        String i= bReservoirPreventionMapper.selectBReservoirPreventionByUnitId(unitId, detrimentType);
//        if(!StringUtils.isNull(i)){
//            return AjaxResult.error("修改失败！");
//        }
 //       else {
            return toAjax(bReservoirPreventionService.updateBReservoirPrevention(bReservoirPrevention));
   //     }
    }

    /**
     * 删除水库防治情况
     */
    @Log(title = "水库防治情况", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bReservoirPreventionService.deleteBReservoirPreventionByIds(ids));
    }
}
