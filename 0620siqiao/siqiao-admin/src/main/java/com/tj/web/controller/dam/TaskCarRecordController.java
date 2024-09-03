package com.tj.web.controller.dam;

import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.domain.Car;
import com.tianji.dam.domain.TDayTask;
import com.tianji.dam.domain.TaskCarRecord;
import com.tianji.dam.mapper.CarMapper;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.mapper.TaskCarRecordMapper;
import com.tianji.dam.service.ITDayTaskService;
import com.tianji.dam.service.ITaskCarRecordService;
import com.tj.common.annotation.Log;
import com.tj.common.core.controller.BaseController;
import com.tj.common.core.domain.AjaxResult;
import com.tj.common.core.page.TableDataInfo;
import com.tj.common.enums.BusinessType;
import com.tj.common.utils.DateUtils;
import com.tj.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 作业任务车辆记录Controller
 *
 * @author liyan
 * @date 2022-10-31
 */
@RestController
@RequestMapping("/dam/TaskCarRecord")
public class TaskCarRecordController extends BaseController {
    @Autowired
    private ITaskCarRecordService taskCarRecordService;

    @Autowired
    private ITDayTaskService taskService;
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private TDamsconstructionMapper damsconstructionMapper;
    @Resource
    private TaskCarRecordMapper taskCarRecordMapper;

    /**
     * 查询作业任务车辆记录列表
     */

    @GetMapping("/list")
    public TableDataInfo list(TaskCarRecord taskCarRecord) {
        startPage();
        List<TaskCarRecord> list = taskCarRecordService.selectTaskCarRecordList(taskCarRecord);
        return getDataTable(list);
    }

    /**
     * 导出作业任务车辆记录列表
     */

    @Log(title = "作业任务车辆记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TaskCarRecord taskCarRecord) {
        List<TaskCarRecord> list = taskCarRecordService.selectTaskCarRecordList(taskCarRecord);
        ExcelUtil<TaskCarRecord> util = new ExcelUtil<TaskCarRecord>(TaskCarRecord.class);
        util.exportExcel(response, list, "作业任务车辆记录数据");
    }

    /**
     * 获取作业任务车辆记录详细信息
     */

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(taskCarRecordService.selectTaskCarRecordById(id));
    }

    /**
     * 新增作业任务车辆记录
     */

    @Log(title = "作业任务车辆记录", businessType = BusinessType.INSERT)
    @PostMapping("/joinrecord")
    public AjaxResult add(@RequestBody TaskCarRecord taskCarRecord) {

        taskCarRecordMapper.delectByCarId(taskCarRecord.getCarId());

/*        if (!GlobCache.cartaskmapcang.containsKey(taskCarRecord.getCarId().intValue())) {
            DamsConstruction task = damsconstructionMapper.selectByPrimaryKey(taskCarRecord.getTaskId().intValue());
            GlobCache.cartaskmapcang.put(taskCarRecord.getCarId().intValue(), task);
        } else {
            //当前车辆已经加入任务，进行任务的移除
            TaskCarRecord taskCarRecord1 = new TaskCarRecord();
            taskCarRecord1.setStatus(0L);
            taskCarRecord1.setCarId(taskCarRecord.getCarId());
            List<TaskCarRecord> list = taskCarRecordService.selectTaskCarRecordList(taskCarRecord1);
            if (list.size() == 0){
                return error();
            }
            list.forEach(L->{
                L.setAwayTime(DateUtils.getNowDate());
                L.setStatus(1L);
                taskCarRecordService.updateTaskCarRecord(L);
                GlobCache.cartaskmapcang.remove(L.getTaskId().intValue());
            });
            DamsConstruction task = damsconstructionMapper.selectByPrimaryKey(taskCarRecord.getTaskId().intValue());
            GlobCache.cartaskmapcang.put(taskCarRecord.getCarId().intValue(), task);
        }
        GlobCache.iscaradd = false;*/
       // taskCarRecord.setAwayTime(DateUtils.getNowDate());
        taskCarRecord.setStatus(0L);
        if(GlobCache.cartaskmapcang.containsKey(taskCarRecord.getCarId().intValue())){
            GlobCache.cartaskmapcang.remove(taskCarRecord.getCarId().intValue());
        }
        return toAjax(taskCarRecordService.insertTaskCarRecord(taskCarRecord));
    }

    @Log(title = "作业任务车辆记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult addde(@RequestBody TaskCarRecord taskCarRecord) {

        if (!GlobCache.cartaskmap.containsKey(taskCarRecord.getCarId().intValue())) {
            TDayTask task = taskService.selectTDayTaskById(taskCarRecord.getTaskId());
            GlobCache.cartaskmap.put(taskCarRecord.getCarId().intValue(), task);
        } else {
            return AjaxResult.error("车辆已加入任务，请先结束上一个任务");
        }
        return toAjax(taskCarRecordService.insertTaskCarRecord(taskCarRecord));
    }

    @PostMapping("/outrecord")
    public AjaxResult outrecord(@RequestBody TaskCarRecord taskCarRecord) {

        taskCarRecord.setAwayTime(null);
        List<TaskCarRecord> list = taskCarRecordService.selectTaskCarRecordList(taskCarRecord);
        if (list.size() >= 1) {
            TaskCarRecord record = list.get(0);
            record.setAwayTime(DateUtils.getNowDate());
            record.setStatus(1L);
            taskCarRecordService.updateTaskCarRecord(record);
            if (GlobCache.cartaskmapcang.containsKey(taskCarRecord.getCarId().intValue())) {
                GlobCache.cartaskmapcang.remove(taskCarRecord.getCarId().intValue());
            }
        
            //如果这个离开的车辆是该任务的最后一个摊铺车辆，则开始计算任务的区域
            TaskCarRecord query = new TaskCarRecord();
            query.setTaskId(taskCarRecord.getTaskId());
            query.setStatus(0l);
            List<TaskCarRecord> allrecord = taskCarRecordService.selectTaskCarRecordList(query);
            if (allrecord.size() == 1) {
                TaskCarRecord old = allrecord.get(0);
                if (old.getCarId().equals(taskCarRecord.getCarId())) {
                    taskCarRecordService.gettpranges(taskCarRecord.getTaskId());
                }

            }
        } else {
            return AjaxResult.error();
        }

        return AjaxResult.success();
    }

    /**
     * 修改作业任务车辆记录
     */

    @Log(title = "作业任务车辆记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TaskCarRecord taskCarRecord) {

        if (GlobCache.cartaskmap.containsKey(taskCarRecord.getCarId().intValue())) {
            GlobCache.cartaskmap.remove(taskCarRecord.getCarId().intValue());
        }

        TDayTask task = taskService.selectTDayTaskById(taskCarRecord.getTaskId());
        GlobCache.cartaskmap.put(taskCarRecord.getCarId().intValue(), task);

        if (null != taskCarRecord.getAwayTime()) {
            taskCarRecord.setStatus(1l);

            //如果这个离开的车辆是该任务的最后一个摊铺车辆，则开始计算任务的区域
            TaskCarRecord query = new TaskCarRecord();
            query.setTaskId(taskCarRecord.getTaskId());
            query.setStatus(0l);
            List<TaskCarRecord> allrecord = taskCarRecordService.selectTaskCarRecordList(query);
            List<Car> alltpcar = carMapper.getCarbyType(2);
            List<TaskCarRecord> tprecord = new ArrayList<>();
            for (Car car : alltpcar) {
                for (TaskCarRecord record : allrecord) {
                    if (record.getCarId().intValue() == car.getCarID()) {
                        tprecord.add(record);
                    }
                }
            }
            if (tprecord.size() == 1) {
                TaskCarRecord old = tprecord.get(0);
                if (old.getCarId() .equals( taskCarRecord.getCarId())) {
                    taskCarRecordService.gettpranges(taskCarRecord.getTaskId());
                }
            }
        }
        return toAjax(taskCarRecordService.updateTaskCarRecord(taskCarRecord));
    }

    /**
     * 删除作业任务车辆记录
     */

    @Log(title = "作业任务车辆记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(taskCarRecordService.deleteTaskCarRecordByIds(ids));
    }
}
