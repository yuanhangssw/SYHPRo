package com.tianji.dam.controller;

import com.tianji.dam.domain.DayVolume;
import com.tianji.dam.domain.Sysconfig;
import com.tianji.dam.mapper.DayVolumeMapper;
import com.tianji.dam.mapper.SysconfigMapper;
import com.tianji.dam.service.IDayVolumeService;
import com.tj.common.annotation.Log;
import com.tj.common.core.controller.BaseController;
import com.tj.common.core.domain.AjaxResult;
import com.tj.common.core.page.TableDataInfo;
import com.tj.common.enums.BusinessType;
import com.tj.common.utils.DateUtils;
import com.tj.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 每日方量Controller
 * 
 * @author liyan
 * @date 2023-12-12
 */
@RestController
@RequestMapping("/dam/volume")
public class DayVolumeController extends BaseController
{
    @Autowired
    private IDayVolumeService dayVolumeService;
    @Autowired
    SysconfigMapper sysconfigMapper;
    @Autowired
    private DayVolumeMapper dayVolumeMapper;

    /**
     * 查询每日方量列表
     */

    @GetMapping("/list")
    public TableDataInfo list(DayVolume dayVolume)
    {

        startPage();
        List<DayVolume> list = dayVolumeService.selectDayVolumeList(dayVolume);
        return getDataTable(list);

    }

    /**
     * 查询当前为止的累计方量
     * @return
     */
    @GetMapping("/gettotal")
   public AjaxResult gettotalvolume(){

      Double totalvolume = dayVolumeMapper.selecttotalvolume(DateUtils.getDate());
                if(null==totalvolume){
                     totalvolume =0d;
                }
       return AjaxResult.success(totalvolume);
   }

    @GetMapping("/getstatics")
    public  AjaxResult volumestatics(){

          Map<String,Object> res =new HashMap<>();
        Sysconfig sysconfig= sysconfigMapper.selectBySyskey("sjvolume");
        String sjvolume="";
        String yestodayvolume ="";
        if(null!=sysconfig){
             sjvolume =     sysconfig.getSysKeyvalue();

        }
          DayVolume dayVolume = new DayVolume();
          Date yestoday =   DateUtils.addDays(new Date(),-1);
          String  yestoday_s =    DateUtils.parseDateToStr("yyyy-MM-dd",yestoday);
         dayVolume.setDays(yestoday);

        List<DayVolume> all=     dayVolumeService.selectDayVolumeList(dayVolume);
            if(all.size()==1){
                yestodayvolume =all.get(0).getVolume().toString();
            }
        res.put("sjvolume",sjvolume);
        res.put("yestodayvolume",yestodayvolume);
        return AjaxResult.success(res);

    }


    /**
     * 导出每日方量列表
     */

    @Log(title = "每日方量", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DayVolume dayVolume)
    {
        List<DayVolume> list = dayVolumeService.selectDayVolumeList(dayVolume);
        ExcelUtil<DayVolume> util = new ExcelUtil<DayVolume>(DayVolume.class);
        util.exportExcel(response, list, "每日方量数据");
    }

    /**
     * 获取每日方量详细信息
     */

    @GetMapping(value = "/{gid}")
    public AjaxResult getInfo(@PathVariable("gid") Long gid)
    {
        return AjaxResult.success(dayVolumeService.selectDayVolumeByGid(gid));
    }

    /**
     * 新增每日方量
     */

    @Log(title = "每日方量", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DayVolume dayVolume)
    {
        return toAjax(dayVolumeService.insertDayVolume(dayVolume));
    }

    /**
     * 修改每日方量
     */

    @Log(title = "每日方量", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DayVolume dayVolume)
    {
        return toAjax(dayVolumeService.updateDayVolume(dayVolume));
    }

    /**
     * 删除每日方量
     */

    @Log(title = "每日方量", businessType = BusinessType.DELETE)
	@DeleteMapping("/{gids}")
    public AjaxResult remove(@PathVariable Long[] gids)
    {
        return toAjax(dayVolumeService.deleteDayVolumeByGids(gids));
    }
}
