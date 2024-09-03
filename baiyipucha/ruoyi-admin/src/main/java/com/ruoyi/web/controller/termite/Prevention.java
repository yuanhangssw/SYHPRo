package com.ruoyi.web.controller.termite;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.BProject;
import com.ruoyi.system.service.IBProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/prevention/prevent")
public class Prevention extends BaseController {

    @Autowired
    private IBProjectService bProjectService;

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        List<Map<String,Object>> list = bProjectService.selectBProjectListJoinPrevent(id);
        return AjaxResult.success(list);
    }



}
