package com.tj.web.controller.dam;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.tianji.dam.domain.Car;
import com.tianji.dam.domain.Material;
import com.tianji.dam.domain.TDesign;
import com.tianji.dam.service.MaterialService;
import com.tj.common.annotation.Log;
import com.tj.common.annotation.RepeatSubmit;
import com.tj.common.core.controller.BaseController;
import com.tj.common.core.domain.AjaxResult;
import com.tj.common.core.page.TableDataInfo;
import com.tj.common.enums.BusinessType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@CrossOrigin
@Api(value="车辆信息controller",tags={"材料列表接口"})
@RestController
@RequestMapping("/reservoir/material")
public class TmaterialController extends BaseController {

    @Autowired
    private MaterialService  service;
/**
 *  材料管理功能 所有的实体类、使用的都是 ruoyi-test 包中的类
 *   
 * @param param
 * @return
 */
    
    
    @RequestMapping("/search")
    @ApiOperation(value = "获取材料列表", response = Car.class, nickname = "getInfosearch")
    public TableDataInfo getInfosearch(@RequestBody Map<String,Object> param)
    {
    	Material  vo = new Material();
    	  if(param.containsKey("materialname")&&null!=param.get("materialname")&&!"".equals(param.get("materialname"))) {
    		  vo.setMaterialname(param.get("materialname").toString());
    	  }
    	
    	  if(param.containsKey("page")&&param.containsKey("limit")) {
    		  
    		    PageHelper.startPage(Integer.valueOf(param.get("page").toString()), Integer.valueOf(param.get("limit").toString()), "");
    		    
    		  List<Material> all = service.findMaterial(vo);  
    		  
    		  TableDataInfo  r = getDataTable(all);
    		  return r;
    	  }else {
    		    PageHelper.startPage(1, 10, "");
      		  List<Material> all = service.findMaterial(vo);  
      		  
      		  TableDataInfo  r = getDataTable(all);
      		  return r;
    	 }
    }
    @GetMapping
    @ApiOperation(value = "获取材料列表", response = Car.class, nickname = "getInfo")
    public AjaxResult getInfo()
    {
    		 return AjaxResult.success(service.findMaterial());
    }
    
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "根据id获取材料详细信息", response = TDesign.class, nickname = "getInfo")
    @ApiImplicitParam(name = "id", value = "材料id", required = true, dataType = "Long")
    public AjaxResult getInfo(@PathVariable Integer id)
    {
        return AjaxResult.success(service.selectMaterialByID(id));
    }
    
    @Log(title = "材料管理", businessType = BusinessType.INSERT)
    @RequestMapping("/add")
    @RepeatSubmit
    @ApiOperation(value = "新增材料", response = Material.class, nickname = "add")
    public AjaxResult add(@Validated @RequestBody Material vo)
    {
        return toAjax(service.insertMaterial(vo));
    }
  
    @Log(title = "材料管理", businessType = BusinessType.UPDATE)
    @RequestMapping("/edit")
    @ApiOperation(value = "修改材料", response = Material.class, nickname = "edit")
    public AjaxResult edit(@Validated @RequestBody Material vo)
    {
        return toAjax(service.updateMaterial(vo));
    }

    @Log(title = "材料管理", businessType = BusinessType.DELETE)
    @RequestMapping("/delete")
    @ApiOperation(value = "删除材料", response = Material.class, nickname = "remove")
    public AjaxResult remove(@RequestBody Map<String,Integer> param)
    {
    	Integer gid = param.get("id");
    	int r = service.deleteMaterial(gid);
        return toAjax(r);
    }
    
    
    
    
    
    
}

