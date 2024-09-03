package com.tj.web.controller.dam;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tianji.dam.domain.TAnalysisConfig;
import com.tianji.dam.service.TAnalysisConfigService;
import com.tj.common.annotation.Log;
import com.tj.common.annotation.RepeatSubmit;
import com.tj.common.core.controller.BaseController;
import com.tj.common.core.domain.AjaxResult;
import com.tj.common.enums.BusinessType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@CrossOrigin
@Api(value="分析设置controller",tags={"分析设置操作接口"})
@RestController
@RequestMapping("/reservoir/analysis")
public class TAnalysisConfigController extends BaseController{

    @Autowired
    private TAnalysisConfigService configService;

    /*@PreAuthorize("@ss.hasPermi('system:config:query')")*/
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "根据id获取分析详细信息", response = TAnalysisConfig.class, nickname = "getInfo")
    @ApiImplicitParam(name = "id", value = "分析编号", required = true, dataType = "Long")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return AjaxResult.success(configService.select(id));
    }
    @RequestMapping("")
    @ApiOperation(value = "获取最新的配置", response = TAnalysisConfig.class, nickname = "getInfolast")
    @ResponseBody
    public AjaxResult getInfolast()
    {
        return AjaxResult.success(configService.select(new TAnalysisConfig()));
    }
    
    

    @Log(title = "分析设置管理", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    @ApiOperation(value = "新增分析", response = TAnalysisConfig.class, nickname = "add")
    public AjaxResult add(@Validated @RequestBody TAnalysisConfig vo)
    {
        return toAjax(configService.addOrUpdate(vo));
    }

    @Log(title = "分析设置管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "修改分析", response = TAnalysisConfig.class, nickname = "edit")
    public AjaxResult edit(@Validated @RequestBody TAnalysisConfig vo)
    {
        return toAjax(configService.addOrUpdate(vo));
    }

    @GetMapping(value = "reset/{type}")
    @ApiOperation(value = "重置分析设置", response = TAnalysisConfig.class, nickname = "reset")
    @ApiImplicitParam(name = "type", value = "类型(1分析，2四参数)", required = true, dataType = "Long")
    public AjaxResult reset(@PathVariable Long type)
    {
        TAnalysisConfig config = configService.select(1L);
        if(type==1){
            TAnalysisConfig vo = new TAnalysisConfig(config);
            return toAjax(configService.addOrUpdate(vo));
        }else{
            TAnalysisConfig vo = new TAnalysisConfig(config,true);
            return toAjax(configService.addOrUpdate(vo));
        }
    }
}
