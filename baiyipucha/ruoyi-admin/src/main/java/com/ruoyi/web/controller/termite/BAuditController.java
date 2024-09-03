package com.ruoyi.web.controller.termite;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.BPatrol;
import com.ruoyi.system.mapper.BAuditMapper;
import com.ruoyi.system.service.IBPatrolService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
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
import com.ruoyi.system.domain.BAudit;
import com.ruoyi.system.service.IBAuditService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 审核记录Controller
 * 
 * @author ruoyi
 * @date 2024-03-05
 */
@RestController
@RequestMapping("/system/audit")
public class BAuditController extends BaseController
{
    @Autowired
    private IBAuditService bAuditService;
    @Autowired
    private ISysDeptService  deptService;

    @Autowired
    private IBPatrolService  patrolService;

    /**
     * 查询审核记录列表
     */
    //@PreAuthorize("@ss.hasPermi('system:audit:list')")
    @GetMapping("/list")
    public TableDataInfo list(BAudit bAudit)
    {
        startPage();
        List<BAudit> list = bAuditService.selectBAuditList(bAudit);
        return getDataTable(list);
    }

    /**
     * 通过当前用户所在部门获取审核信息列表
     * @return
     */
    @GetMapping("/checklist")
    public TableDataInfo getListByUserdept(BAudit bAudit){

       Long deptid =   SecurityUtils.getDeptId();
         bAudit.setCurrentDept(deptid);
         bAudit.setDataState(1l);

        startPage();
        List<BAudit> list = bAuditService.selectBAuditList(bAudit);
        return getDataTable(list);
    }



    /**
     * 导出审核记录列表
     */
    //@PreAuthorize("@ss.hasPermi('system:audit:export')")
    @Log(title = "审核记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BAudit bAudit)
    {
        List<BAudit> list = bAuditService.selectBAuditList(bAudit);
        ExcelUtil<BAudit> util = new ExcelUtil<BAudit>(BAudit.class);
        util.exportExcel(response, list, "审核记录数据");
    }

    /**
     * 获取审核记录详细信息
     */
   // @PreAuthorize("@ss.hasPermi('system:audit:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bAuditService.selectBAuditById(id));
    }

    /**
     * 新增审核记录
     */
    //@PreAuthorize("@ss.hasPermi('system:audit:add')")
    @Log(title = "审核记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BAudit bAudit)
    {
        return toAjax(bAuditService.insertBAudit(bAudit));
    }

    /**
     * 修改审核记录
     */
   // @PreAuthorize("@ss.hasPermi('system:audit:edit')")
    @Log(title = "审核记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BAudit bAudit)
    {

        //审核的时候需要根据下一级的审核部门生成新的审核记录如果是未通过则不进行生成
        if(bAudit.getAuditStatus()==2){
            BPatrol  bPatrol= patrolService.selectBPatrolById(bAudit.getPatrolId()) ;
            bPatrol.setAuditStatus("4");
            bPatrol.setFreedom2(DateUtils.getTime());
            patrolService.updateBPatrol(bPatrol);
            bAudit.setDataState(2l);
        }
        else {
            try {
                if (100 != bAudit.getSubordinateDept()&&0!=bAudit.getSubordinateDept()) {
                    SysDept nextdept = deptService.selectDeptById(bAudit.getSubordinateDept());
                    Long nextparent = nextdept.getParentId();
                    BAudit newa = new BAudit();
                    newa.setPatrolId(bAudit.getPatrolId());
                    newa.setCurrentDept(nextdept.getDeptId());
                    newa.setSubordinateDept(nextparent);
                    newa.setDataState(1l);
                    bAuditService.insertBAudit(newa);
                }
                    if (bAudit.getAuditStatus() == 1) {
                        BPatrol bPatrol = patrolService.selectBPatrolById(bAudit.getPatrolId());
                        bPatrol.setAuditStatus("3");
                        bPatrol.setFreedom2(DateUtils.getTime());
                        bAudit.setDataState(3l);
                        patrolService.updateBPatrol(bPatrol);

                    }

            } catch (Exception e) {
                System.out.println("生成审核失败");
            }
        }

        return toAjax(bAuditService.updateBAudit(bAudit));
    }

    /**
     * 删除审核记录
     */
    //@PreAuthorize("@ss.hasPermi('system:audit:remove')")
    @Log(title = "审核记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bAuditService.deleteBAuditByIds(ids));
    }
}
