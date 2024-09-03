package com.tj.web.controller.dam;

import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.Material;
import com.tianji.dam.domain.TDayTask;
import com.tianji.dam.domain.vo.ResponseData;
import com.tianji.dam.mapper.MaterialMapper;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.service.ITDayTaskService;
import com.tianji.dam.utils.GenerateRedisKey;
import com.tianji.dam.utils.RedisUtil;
import com.tj.common.annotation.Log;
import com.tj.common.core.controller.BaseController;
import com.tj.common.core.domain.AjaxResult;
import com.tj.common.core.page.TableDataInfo;
import com.tj.common.enums.BusinessType;
import com.tj.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * 作业任务Controller
 *
 * @author liyan
 * @date 2022-10-31
 */
@RestController
@RequestMapping("/dam/Task")
public class TDayTaskController extends BaseController {
    @Autowired
    private ITDayTaskService tDayTaskService;
    @Autowired
    TDamsconstructionMapper damsconstructionMapper;
    @Autowired
    MaterialMapper materialMapper;
    /**
     * 查询作业任务列表
     */

    @GetMapping("/list")
    public TableDataInfo list(TDayTask tDayTask) {
        startPage();
        //List<TDayTask> list = tDayTaskService.selectTDayTaskList(tDayTask);

       // JSONObject.parseObject("");

      List<DamsConstruction> list =  damsconstructionMapper.findByStatus(8);

        return getDataTable(list);
    }
    @GetMapping("/getList")
    public ResponseData getList() {

        return tDayTaskService.getList();
    }
    /*
     * 查询t_material表
     * */
    @GetMapping("/getMaterialAll")
    public TableDataInfo getMaterialAll(){
        startPage();

        List<Material> list = materialMapper.findAllMaterial();
        return getDataTable(list);
    }

    /**
     * 导出作业任务列表
     */

    @Log(title = "作业任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TDayTask tDayTask) {
        List<TDayTask> list = tDayTaskService.selectTDayTaskList(tDayTask);
        ExcelUtil<TDayTask> util = new ExcelUtil<TDayTask>(TDayTask.class);
        util.exportExcel(response, list, "作业任务数据");
    }

    /**
     * 获取作业任务详细信息
     */

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {


        //需要获取该任务的作业范围、然后展示摊铺均匀度、碾压合格度
        //首先获取数据集合、然后计算出摊铺的范围，以摊铺范围确定仓位（预留）

        return AjaxResult.success(tDayTaskService.selectTDayTaskById(id));
    }

    @GetMapping("/closetask")
    public AjaxResult closetask(Long id, String checkman) {

        try {
            DamsConstruction task = damsconstructionMapper.selectByPrimaryKey(id.intValue());
            RedisUtil redis = BeanContext.getApplicationContext().getBean(RedisUtil.class);
                String key=  GenerateRedisKey.taskrediskeysbycartypeCang(task.getTablename(), "ylj");
                Set<String> alldeletekey = redis.keys(key);
                for (String allbefore7key : alldeletekey) {
                    redis.del(allbefore7key);
                }

        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
        return AjaxResult.success();
    }

    /**
     * 新增作业任务
     */

    @Log(title = "新增作业任务", businessType = BusinessType.INSERT)
    @PostMapping("/create")
    public AjaxResult add(@RequestBody TDayTask tDayTask) {

        return AjaxResult.success(tDayTaskService.insertTDayTask(tDayTask));
    }

    /**
     * 修改作业任务
     */

    @Log(title = "修改作业任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TDayTask tDayTask) {
        return toAjax(tDayTaskService.updateTDayTask(tDayTask));
    }

    /**
     * 删除作业任务
     */

    @Log(title = "删除任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tDayTaskService.deleteTDayTaskByIds(ids));
    }

}
