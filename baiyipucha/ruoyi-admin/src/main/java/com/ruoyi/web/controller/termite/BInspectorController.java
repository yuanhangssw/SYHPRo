package com.ruoyi.web.controller.termite;

import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.SecurityUtils;
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
import com.ruoyi.system.domain.BInspector;
import com.ruoyi.system.service.IBInspectorService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import sun.security.provider.MD5;

/**
 * 巡查员用户Controller
 *
 * @author ruoyi
 * @date 2024-03-05
 */
@RestController
@RequestMapping("/system/inspector")
public class BInspectorController extends BaseController {
    @Autowired
    private IBInspectorService bInspectorService;

    /**
     * 查询巡查员用户列表
     */
//    @PreAuthorize("@ss.hasPermi('system:inspector:list')")
    @GetMapping("/list")
    public TableDataInfo list(BInspector bInspector) {
        startPage();
        List<BInspector> list = bInspectorService.selectBInspectorList(bInspector);
        return getDataTable(list);
    }

    /**
     * 导出巡查员用户列表
     */
//    @PreAuthorize("@ss.hasPermi('system:inspector:export')")
//    @Log(title = "巡查员用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BInspector bInspector) {
        List<BInspector> list = bInspectorService.selectBInspectorList(bInspector);
        ExcelUtil<BInspector> util = new ExcelUtil<BInspector>(BInspector.class);
        util.exportExcel(response, list, "巡查员用户数据");
    }

    /**
     * 获取巡查员用户详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:inspector:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(bInspectorService.selectBInspectorById(id));
    }

    /**
     * 新增巡查员用户
     */
//    @PreAuthorize("@ss.hasPermi('system:inspector:add')")
//    @Log(title = "巡查员用户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BInspector bInspector) {
        bInspector.setDeptId(SecurityUtils.getDeptId());
        bInspector.setUserId(SecurityUtils.getUserId());
        bInspector.setPassword(Base64.getEncoder().encodeToString(bInspector.getPassword().getBytes()));
        return toAjax(bInspectorService.insertBInspector(bInspector));
    }

    /**
     * 修改巡查员用户
     */
//    @PreAuthorize("@ss.hasPermi('system:inspector:edit')")
//    @Log(title = "巡查员用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BInspector bInspector) {
        bInspector.setDeptId(SecurityUtils.getDeptId());
        bInspector.setUserId(SecurityUtils.getUserId());
        bInspector.setPassword(Base64.getEncoder().encodeToString(bInspector.getPassword().getBytes()));
        return toAjax(bInspectorService.updateBInspector(bInspector));
    }

    /**
     * 删除巡查员用户
     */
//    @PreAuthorize("@ss.hasPermi('system:inspector:remove')")
//    @Log(title = "巡查员用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bInspectorService.deleteBInspectorByIds(ids));
    }

    @PostMapping("/wxlogin")
    public AjaxResult wxlogin(@RequestBody BInspector inspector) {
        BInspector bInspector = new BInspector();
        byte[] decodedBytes = Base64.getDecoder().decode(inspector.getPhone());
        String decodedString = new String(decodedBytes);
        bInspector.setPhone(decodedString);
        List<BInspector> inspectorList = bInspectorService.selectBInspectorList(bInspector);
        if (inspectorList.size() > 0) {
            if (inspector.getPassword().equals(inspectorList.get(0).getPassword())) {
                inspectorList.get(0).setPhone(decodedString);
                return AjaxResult.success(inspectorList.get(0));
            } else {
                return AjaxResult.error("密码错误.");
            }
        } else {
            return AjaxResult.error("用户未注册,请联系主管单位.");
        }
    }
}
