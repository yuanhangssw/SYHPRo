package com.tianji.dam.service;

import com.tianji.dam.domain.TaskCarRecord;

import java.util.List;

/**
 * 作业任务车辆记录Service接口
 *
 * @author liyan
 * @date 2022-10-31
 */
public interface ITaskCarRecordService {
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
     * 批量删除作业任务车辆记录
     *
     * @param ids 需要删除的作业任务车辆记录主键集合
     * @return 结果
     */
    public int deleteTaskCarRecordByIds(Long[] ids);

    /**
     * 删除作业任务车辆记录信息
     *
     * @param id 作业任务车辆记录主键
     * @return 结果
     */
    public int deleteTaskCarRecordById(Long id);

    public void gettpranges(Long taskid);
}
