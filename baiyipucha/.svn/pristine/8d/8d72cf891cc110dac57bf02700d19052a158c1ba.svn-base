package com.ruoyi.web.controller.termite;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.system.domain.vo.InspectorUnitVo;
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
import com.ruoyi.system.domain.BInspectorUnit;
import com.ruoyi.system.service.IBInspectorUnitService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 巡查用户和巡查单元关联Controller
 *
 * @author ruoyi
 * @date 2024-03-05
 */
@RestController
@RequestMapping("/bInspectorUnit/unit")
public class BInspectorUnitController extends BaseController {
    @Autowired
    private IBInspectorUnitService bInspectorUnitService;

    /**
     * 查询巡查用户和巡查单元关联列表
     */
    //@PreAuthorize("@ss.hasPermi('system:unit:list')")
    @GetMapping("/list")
    public TableDataInfo list(BInspectorUnit bInspectorUnit) {
        //startPage();
        List<BInspectorUnit> list = bInspectorUnitService.selectBInspectorUnitList(bInspectorUnit);
        return getDataTable(list);
    }

    /**
     * 导出巡查用户和巡查单元关联列表
     */
    // @PreAuthorize("@ss.hasPermi('system:unit:export')")
    @Log(title = "巡查用户和巡查单元关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BInspectorUnit bInspectorUnit) {
        List<BInspectorUnit> list = bInspectorUnitService.selectBInspectorUnitList(bInspectorUnit);
        ExcelUtil<BInspectorUnit> util = new ExcelUtil<BInspectorUnit>(BInspectorUnit.class);
        util.exportExcel(response, list, "巡查用户和巡查单元关联数据");
    }

    /**
     * 获取巡查用户和巡查单元关联详细信息
     */
    //@PreAuthorize("@ss.hasPermi('system:unit:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(bInspectorUnitService.selectBInspectorUnitById(id));
    }

    /**
     * 新增巡查用户和巡查单元关联
     */
    //@PreAuthorize("@ss.hasPermi('system:unit:add')")
    @Log(title = "巡查用户和巡查单元关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BInspectorUnit bInspectorUnit) {
        return toAjax(bInspectorUnitService.insertBInspectorUnit(bInspectorUnit));
    }

    /**
     * 修改巡查用户和巡查单元关联
     */
    // @PreAuthorize("@ss.hasPermi('system:unit:edit')")
    @Log(title = "巡查用户和巡查单元关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BInspectorUnit bInspectorUnit) {
        return toAjax(bInspectorUnitService.updateBInspectorUnit(bInspectorUnit));
    }

    /**
     * 删除巡查用户和巡查单元关联
     */
    //@PreAuthorize("@ss.hasPermi('system:unit:remove')")
    @Log(title = "巡查用户和巡查单元关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bInspectorUnitService.deleteBInspectorUnitByIds(ids));
    }


    @PostMapping("addInspectorUnit")
    public AjaxResult addInspectorUnit(@RequestBody List<InspectorUnitVo> inspectorUnitVo) {
        int res=0;
        if(inspectorUnitVo.size()>0){
            for (InspectorUnitVo in:inspectorUnitVo) {
                bInspectorUnitService.deleteBInspectorUnit(in.getInspectorId(),in.getProjectId());
                if(in.getUnit().size()>0){
                    for (Long unit:in.getUnit()) {
                        BInspectorUnit bInspectorUnit=new BInspectorUnit();
                        bInspectorUnit.setProjectId(in.getProjectId());
                        bInspectorUnit.setInspector(in.getInspectorId());
                        bInspectorUnit.setPatrolUnitId(unit);
                        res+=bInspectorUnitService.insertBInspectorUnit(bInspectorUnit);
                    }
                }
            }
        }
        return toAjax(res);
    }

}
