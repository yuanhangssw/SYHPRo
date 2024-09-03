package com.tianji.dam.mapper;

import java.util.List;
import com.tianji.dam.domain.TDayTask;

/**
 * 作业任务Mapper接口
 * 
 * @author liyan
 * @date 2022-10-31
 */
public interface TDayTaskMapper 
{
    /**
     * 查询作业任务
     * 
     * @param id 作业任务主键
     * @return 作业任务
     */
    public TDayTask selectTDayTaskById(Long id);

    /**
     * 查询作业任务列表
     * 
     * @param tDayTask 作业任务
     * @return 作业任务集合
     */
    public List<TDayTask> selectTDayTaskList(TDayTask tDayTask);

    /**
     * 新增作业任务
     * 
     * @param tDayTask 作业任务
     * @return 结果
     */
    public int insertTDayTask(TDayTask tDayTask);

    /**
     * 修改作业任务
     * 
     * @param tDayTask 作业任务
     * @return 结果
     */
    public int updateTDayTask(TDayTask tDayTask);

    /**
     * 删除作业任务
     * 
     * @param id 作业任务主键
     * @return 结果
     */
    public int deleteTDayTaskById(Long id);

    /**
     * 批量删除作业任务
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTDayTaskByIds(Long[] ids);
}
