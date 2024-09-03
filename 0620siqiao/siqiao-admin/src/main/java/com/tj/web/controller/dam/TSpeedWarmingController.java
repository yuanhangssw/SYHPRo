package com.tj.web.controller.dam;

import com.tianji.dam.domain.SmsSendRecord;
import com.tianji.dam.domain.TSpeedWarming;
import com.tianji.dam.mapper.SmsSendRecordMapper;
import com.tianji.dam.service.ITSpeedWarmingService;
import com.tianji.dam.service.impl.SmsSendRecordServiceImpl;
import com.tj.common.annotation.Log;
import com.tj.common.core.controller.BaseController;
import com.tj.common.core.domain.AjaxResult;
import com.tj.common.core.page.TableDataInfo;
import com.tj.common.enums.BusinessType;
import com.tj.common.utils.DateUtils;
import com.tj.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 预警信息Controller
 * 
 * @author tianji_liyan
 * @date 2021-12-28
 */
@RestController
@RequestMapping("/dam/speedwarming")
public class TSpeedWarmingController extends BaseController
{
    @Autowired
    private ITSpeedWarmingService tSpeedWarmingService;

    @Autowired
    private SmsSendRecordServiceImpl smsSendRecordService;

    /**
     * 查询预警信息列表
     */
    @PreAuthorize("@ss.hasPermi('dam:speedwarming:list')")
    @GetMapping("/list")
    public TableDataInfo list(TSpeedWarming tSpeedWarming)
    {
        startPage();
        List<TSpeedWarming> list = tSpeedWarmingService.selectTSpeedWarmingList(tSpeedWarming);

            TableDataInfo t =getDataTable(list);

        return t;
    }

    /**
     * 导出预警信息列表
     */
    @PreAuthorize("@ss.hasPermi('dam:speedwarming:export')")
    @Log(title = "预警信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TSpeedWarming tSpeedWarming)
    {
        List<TSpeedWarming> list = tSpeedWarmingService.selectTSpeedWarmingList(tSpeedWarming);
        ExcelUtil<TSpeedWarming> util = new ExcelUtil<TSpeedWarming>(TSpeedWarming.class);
        util.exportExcel(response, list, "预警信息数据");
    }

    /**
     * 获取预警信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('dam:speedwarming:query')")
    @GetMapping(value = "/{gid}")
    public AjaxResult getInfo(@PathVariable("gid") String gid)
    {
        return AjaxResult.success(tSpeedWarmingService.selectTSpeedWarmingByGid(gid));
    }

    /**
     * 新增预警信息
     */
    @PreAuthorize("@ss.hasPermi('dam:speedwarming:add')")
    @Log(title = "预警信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TSpeedWarming tSpeedWarming)
    {
        return toAjax(tSpeedWarmingService.insertTSpeedWarming(tSpeedWarming));
    }

    /**
     * 修改预警信息
     */
    @PreAuthorize("@ss.hasPermi('dam:speedwarming:edit')")
    @Log(title = "预警信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TSpeedWarming tSpeedWarming)
    {
        return toAjax(tSpeedWarmingService.updateTSpeedWarming(tSpeedWarming));
    }

    /**
     * 删除预警信息
     */
    @PreAuthorize("@ss.hasPermi('dam:speedwarming:remove')")
    @Log(title = "预警信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{gids}")
    public AjaxResult remove(@PathVariable String[] gids)
    {
        return toAjax(tSpeedWarmingService.deleteTSpeedWarmingByGids(gids));
    }

    /*
     * 处理预警信息
     * freedom3
     * */
    @Log(title = "预警信息", businessType = BusinessType.UPDATE)
    @PostMapping("/updateStatusFreedom3")
    public AjaxResult updateStatusFreedom3(@RequestBody Map<String,Object> param){

        String ids = param.get("ids").toString();
                while(ids.startsWith("[")){
                    ids = ids.substring(1);
        }
                while (ids.endsWith("]")){
                    ids =ids.substring(0,ids.length()-1);
                }

        String username =param.get("username").toString();
            String[] allid =ids.split(",");
            if(allid.length==0){
                return AjaxResult.error();
            }
            for (String s : allid) {
                  s= s.trim();
                SmsSendRecord smsSendRecord=new SmsSendRecord();
                Long l = Long.valueOf(s);
                smsSendRecord.setGid(l);
                smsSendRecord.setFreedom3("2");
                smsSendRecord.setFreedom4(username);
                smsSendRecordService.updateSmsSendRecord(smsSendRecord);



            }
        return AjaxResult.success();

    }


    /*
     * 处理预警信息
     * freedom2：1：未处理 2：已处理
     * */
    @Log(title = "预警信息", businessType = BusinessType.UPDATE)
    @PostMapping("/updateStatus")
    public AjaxResult updateStatus(@RequestBody Map<String,Object> param){

        String ids = param.get("ids").toString();
        while(ids.startsWith("[")){
            ids = ids.substring(1);
        }
        while (ids.endsWith("]")){
            ids =ids.substring(0,ids.length()-1);
        }

        String username =param.get("username").toString();
        String[] allid =ids.split(",");
        if(allid.length==0){
            return AjaxResult.error();
        }
        for (String s : allid) {
            s= s.trim();

            TSpeedWarming warming =new TSpeedWarming();
            warming.setGid(s);
            warming.setFreedom3("2");
            warming.setFreedom4(username);
            warming.setFreedom5(DateUtils.getTime());
            tSpeedWarmingService.updateTSpeedWarming(warming);
        }
        return AjaxResult.success();

    }


}

