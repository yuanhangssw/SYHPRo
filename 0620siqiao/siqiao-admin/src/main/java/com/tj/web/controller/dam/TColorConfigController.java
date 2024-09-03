package com.tj.web.controller.dam;

import com.github.pagehelper.PageHelper;
import com.tianji.dam.domain.TColorConfig;
import com.tianji.dam.domain.vo.TColorConfigVO;
import com.tianji.dam.service.TColorConfigService;
import com.tj.common.annotation.Log;
import com.tj.common.annotation.RepeatSubmit;
import com.tj.common.core.controller.BaseController;
import com.tj.common.core.domain.AjaxResult;
import com.tj.common.core.page.TableDataInfo;
import com.tj.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Api(value = "配色设置controller", tags = {"配色设置操作接口"})
@RestController
@RequestMapping("/reservoir/color")
public class TColorConfigController extends BaseController {

    @Autowired
    private TColorConfigService configService;

    @GetMapping("/list")
    @ApiOperation(value = "查询配色设置", response = TColorConfig.class, nickname = "list")
    public AjaxResult list(TColorConfig param) {
        
        List<TColorConfig> list = configService.select(param);
        return AjaxResult.success(list);
    }

    @GetMapping("/pageList")
    @ApiOperation(value = "分页查询配色设置", response = TColorConfig.class, nickname = "pageList")
    public TableDataInfo pageList(TColorConfigVO param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize(), "");
        List<TColorConfig> list = configService.select(param);
        return getDataTable(list);
    }

    /*@PreAuthorize("@ss.hasPermi('system:config:query')")*/
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "根据id获取配色详细信息", response = TColorConfig.class, nickname = "getInfo")
    @ApiImplicitParam(name = "id", value = "配色编号", required = true, dataType = "Long")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(configService.select(id));
    }

    @Log(title = "配色设置管理", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    @ApiOperation(value = "新增配色", response = TColorConfig.class, nickname = "add")
    public AjaxResult add(@Validated @RequestBody TColorConfig vo) {
        return toAjax(configService.addOrUpdate(vo));
    }

    @Log(title = "配色设置管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "修改配色", response = TColorConfig.class, nickname = "edit")
    public AjaxResult edit(@Validated @RequestBody TColorConfig vo) {
        return toAjax(configService.addOrUpdate(vo));
    }

    @Log(title = "配色设置管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除配色", response = TColorConfig.class, nickname = "remove")
    public AjaxResult remove(@PathVariable("id") Long id) {
        return toAjax(configService.delete(id));
    }
}
