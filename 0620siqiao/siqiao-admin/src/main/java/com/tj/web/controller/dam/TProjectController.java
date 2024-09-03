package com.tj.web.controller.dam;


import java.io.InputStream;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tianji.dam.domain.TAnalysisConfig;
import com.tianji.dam.domain.TProject;
import com.tianji.dam.domain.vo.ProjectimportVo;
import com.tianji.dam.service.TAnalysisConfigService;
import com.tianji.dam.service.TProjectService;
import com.tj.common.annotation.Log;
import com.tj.common.annotation.RepeatSubmit;
import com.tj.common.core.controller.BaseController;
import com.tj.common.core.domain.AjaxResult;
import com.tj.common.enums.BusinessType;
import com.tj.common.utils.poi.ExcelUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@CrossOrigin
@Api(value="工程信息controller",tags={"工程信息操作接口"})
@RestController
@RequestMapping("/reservoir/project")
public class TProjectController  extends BaseController{

    @Autowired
    private TProjectService projectService;
    @Autowired
    private TAnalysisConfigService configService;
    
    /*@PreAuthorize("@ss.hasPermi('system:config:query')")*/
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "根据id获取工程详细信息", response = TProject.class, nickname = "getInfo")
    @ApiImplicitParam(name = "id", value = "工程编号", required = true, dataType = "Long")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return AjaxResult.success(projectService.select(id));
    }

    @Log(title = "工程信息管理", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    @ApiOperation(value = "新增工程", response = TProject.class, nickname = "add")
    public AjaxResult add(@Validated @RequestBody TProject vo)
    {
        return toAjax(projectService.addOrUpdate(vo));
    }

    @Log(title = "工程信息管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "修改工程", response = TProject.class, nickname = "edit")
    public AjaxResult edit(@Validated @RequestBody TProject vo)
    {
        return toAjax(projectService.addOrUpdate(vo));
    }
    
    @RequestMapping(value="/excelImport",method=RequestMethod.POST)
    public int  importpointsfromexcel(MultipartFile file) {
    	
    	ExcelUtil<?>  u = new ExcelUtil<>(ProjectimportVo.class);
    	  InputStream is;
		try {
			is = file.getInputStream();
			List<?> all =		 u.importExcel(is);
			  System.out.println(all);
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	 
    	
    	
    	
    	return 1;
    	
    }
    /**
     * 获取系统的所有点位
     * @return
     */
    @RequestMapping("/getallpoint")
    public AjaxResult getallpoint() {
    	return  AjaxResult.success( projectService.getallpoint());
    }
    @RequestMapping("/getconfig")
    public AjaxResult getInfolast()
    {
        return AjaxResult.success(configService.select(new TAnalysisConfig()));
    }
    
}
