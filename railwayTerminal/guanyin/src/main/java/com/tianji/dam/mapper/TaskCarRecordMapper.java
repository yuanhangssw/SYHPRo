package com.tianji.dam.mapper;

import java.util.List;
import com.tianji.dam.domain.TaskCarRecord;
import org.apache.ibatis.annotations.Param;

/**
 * 作业任务车辆记录Mapper接口
 * 
 * @author liyan
 * @date 2022-10-31
 */
public interface TaskCarRecordMapper 
{
    public void delectByCarId(Long carId);


    /**
     * 查询作业任务车辆记录
     * 
     * @param id 作业任务车辆记录主键
     * @return 作业任务车辆记录
     */
    public TaskCarRecord selectTaskCarRecordById(Long id);

    /**
     * 查询作业任务车辆记录列表
     * 
     * @param taskCarRecord 作业任务车辆记录
     * @return 作业任务车辆记录集合
     */
    public List<TaskCarRecord> selectTaskCarRecordList(TaskCarRecord taskCarRecord);

    /**
     * 新增作业任务车辆记录
     * 
     * @param taskCarRecord 作业任务车辆记录
     * @return 结果
     */
    public int insertTaskCarRecord(TaskCarRecord taskCarRecord);

    /**
     * 修改作业任务车辆记录
     * 
     * @param taskCarRecord 作业任务车辆记录
     * @return 结果
     */
    public int updateTaskCarRecord(TaskCarRecord taskCarRecord);

    /**
     * 删除作业任务车辆记录
     * 
     * @param id 作业任务车辆记录主键
     * @return 结果
     */
    public int deleteTaskCarRecordById(Long id);

    /**
     * 批量删除作业任务车辆记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTaskCarRecordByIds(Long[] ids);
}
