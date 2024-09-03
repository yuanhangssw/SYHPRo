package com.tianji.dam.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.domain.Car;
import com.tianji.dam.domain.RollingData;
import com.tianji.dam.domain.TDayTask;
import com.tianji.dam.domain.TaskCarRecord;
import com.tianji.dam.mapper.CarMapper;
import com.tianji.dam.mapper.RollingDataMapper;
import com.tianji.dam.mapper.TDayTaskMapper;
import com.tianji.dam.mapper.TaskCarRecordMapper;
import com.tianji.dam.service.ITaskCarRecordService;
import com.tj.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * 作业任务车辆记录Service业务层处理
 *
 * @author liyan
 * @date 2022-10-31
 */
@Service
public class TaskCarRecordServiceImpl implements ITaskCarRecordService {
    @Autowired
    private TaskCarRecordMapper taskCarRecordMapper;
    @Autowired
    private RollingDataMapper rollingDataMapper;
    @Autowired
    private TDayTaskMapper taskMapper;
    @Autowired
    private CarMapper carMapper;

    /**
     * 查询作业任务车辆记录
     *
     * @param id 作业任务车辆记录主键
     * @return 作业任务车辆记录
     */
    @Override
    public TaskCarRecord selectTaskCarRecordById(Long id) {
        return taskCarRecordMapper.selectTaskCarRecordById(id);
    }

    /**
     * 查询作业任务车辆记录列表
     *
     * @param taskCarRecord 作业任务车辆记录
     * @return 作业任务车辆记录
     */
    @Override
    public List<TaskCarRecord> selectTaskCarRecordList(TaskCarRecord taskCarRecord) {
        return taskCarRecordMapper.selectTaskCarRecordList(taskCarRecord);
    }

    /**
     * 新增作业任务车辆记录
     *
     * @param taskCarRecord 作业任务车辆记录
     * @return 结果
     */
    @Override
    public int insertTaskCarRecord(TaskCarRecord taskCarRecord) {
        taskCarRecord.setJoinTime(DateUtils.getNowDate());
        return taskCarRecordMapper.insertTaskCarRecord(taskCarRecord);
    }

    /**
     * 修改作业任务车辆记录
     *
     * @param taskCarRecord 作业任务车辆记录
     * @return 结果
     */
    @Override
    public int updateTaskCarRecord(TaskCarRecord taskCarRecord) {
        return taskCarRecordMapper.updateTaskCarRecord(taskCarRecord);
    }

    /**
     * 批量删除作业任务车辆记录
     *
     * @param ids 需要删除的作业任务车辆记录主键
     * @return 结果
     */
    @Override
    public int deleteTaskCarRecordByIds(Long[] ids) {
        return taskCarRecordMapper.deleteTaskCarRecordByIds(ids);
    }

    /**
     * 删除作业任务车辆记录信息
     *
     * @param id 作业任务车辆记录主键
     * @return 结果
     */
    @Override
    public int deleteTaskCarRecordById(Long id) {
        return taskCarRecordMapper.deleteTaskCarRecordById(id);
    }

    @Override
    public void gettpranges(Long taskid) {

        //查询所有车辆摊铺车辆的历史数据
        TDayTask task = taskMapper.selectTDayTaskById(taskid);
        String tablename = task.getFreedom2();
        List<Car> alltp = carMapper.getCarbyType(2);

        List<RollingData> rollingDataList_all = new LinkedList<>();

        for (Car car : alltp) {
            List<RollingData> all = rollingDataMapper.getAllRollingDataByVehicleID(tablename, car.getCarID().toString());
            rollingDataList_all.addAll(all);
        }
        if (rollingDataList_all.size() > 0) {
            Double max_x = rollingDataList_all.stream().max(Comparator.comparing(RollingData::getCoordX)).get().getCoordX();
            Double min_x = rollingDataList_all.stream().min(Comparator.comparing(RollingData::getCoordX)).get().getCoordX();
            Double max_y = rollingDataList_all.stream().max(Comparator.comparing(RollingData::getCoordY)).get().getCoordY();
            Double min_y = rollingDataList_all.stream().min(Comparator.comparing(RollingData::getCoordY)).get().getCoordY();

            JSONArray ranges = new JSONArray();
            JSONObject point1 = new JSONObject();
            JSONObject point2 = new JSONObject();
            JSONObject point3 = new JSONObject();
            JSONObject point4 = new JSONObject();

            point1.put("x", min_x);
            point1.put("y", min_y);
            ranges.add(point1);
            point2.put("x", min_x);
            point2.put("y", max_y);
            ranges.add(point2);
            point3.put("x", max_x);
            point3.put("y", max_y);
            ranges.add(point3);
            point4.put("x", max_x);
            point4.put("y", min_y);
            ranges.add(point4);
            task.setXbegin(min_x.toString());
            task.setXend(max_x.toString());
            task.setYbegin(min_y.toString());
            task.setYend(max_y.toString());
            task.setRanges(ranges.toJSONString());
            taskMapper.updateTDayTask(task);
        }


    }
}
