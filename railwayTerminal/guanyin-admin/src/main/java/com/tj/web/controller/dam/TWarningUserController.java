package com.tj.web.controller.dam;

import com.tianji.dam.domain.TWarningUser;
import com.tianji.dam.service.ITWarningUserService;
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
 * 预警用户维护Controller
 * 
 * @author liyan
 * @date 2022-06-09
 */
@RestController
@RequestMapping("/warninguser/warninguser")
public class TWarningUserController extends BaseController
{
    @Autowired
    private ITWarningUserService tWarningUserService;

    /**
     * 查询预警用户维护列表
     */
    @PreAuthorize("@ss.hasPermi('warninguser:warninguser:list')")
    @GetMapping("/list")
    public TableDataInfo list(TWarningUser tWarningUser)
    {
        startPage();
        List<TWarningUser> list = tWarningUserService.selectTWarningUserList(tWarningUser);
        return getDataTable(list);
    }

    /**
     * 导出预警用户维护列表
     */
    @PreAuthorize("@ss.hasPermi('warninguser:warninguser:export')")
    @Log(title = "预警用户维护", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TWarningUser tWarningUser)
    {
        List<TWarningUser> list = tWarningUserService.selectTWarningUserList(tWarningUser);
        ExcelUtil<TWarningUser> util = new ExcelUtil<TWarningUser>(TWarningUser.class);
        util.exportExcel(response, list, "预警用户维护数据");
    }

    /**
     * 获取预警用户维护详细信息
     */
    @PreAuthorize("@ss.hasPermi('warninguser:warninguser:query')")
    @GetMapping(value = "/{userGid}")
    public AjaxResult getInfo(@PathVariable("userGid") String userGid)
    {
        return AjaxResult.success(tWarningUserService.selectTWarningUserByUserGid(userGid));
    }

    /**
     * 新增预警用户维护
     */
    @PreAuthorize("@ss.hasPermi('warninguser:warninguser:add')")
    @Log(title = "预警用户维护", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TWarningUser tWarningUser)
    {
        return toAjax(tWarningUserService.insertTWarningUser(tWarningUser));
    }

    /**
     * 修改预警用户维护
     */
    @PreAuthorize("@ss.hasPermi('warninguser:warninguser:edit')")
    @Log(title = "预警用户维护", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TWarningUser tWarningUser)
    {
        return toAjax(tWarningUserService.updateTWarningUser(tWarningUser));
    }

    /**
     * 删除预警用户维护
     */
    @PreAuthorize("@ss.hasPermi('warninguser:warninguser:remove')")
    @Log(title = "预警用户维护", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userGids}")
    public AjaxResult remove(@PathVariable String[] userGids)
    {
        return toAjax(tWarningUserService.deleteTWarningUserByUserGids(userGids));
    }
}
