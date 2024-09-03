package com.tianji.dam.service.impl;

import com.github.pagehelper.PageInfo;
import com.tianji.dam.domain.Dam;
import com.tianji.dam.domain.Row;
import com.tianji.dam.domain.TDayTask;
import com.tianji.dam.domain.vo.ResponseData;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.mapper.TDayTaskMapper;
import com.tianji.dam.mapper.TableMapper;
import com.tianji.dam.service.ITDayTaskService;
import com.tj.common.constant.HttpStatus;
import com.tj.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 作业任务Service业务层处理
 *
 * @author liyan
 * @date 2022-10-31
 */
@Service
public class TDayTaskServiceImpl implements ITDayTaskService {
    @Autowired
    private TDayTaskMapper tDayTaskMapper;
    @Autowired
    private TableMapper tableMapper;
    @Autowired
    private TDamsconstructionMapper tDamsconstructionMapper;

    /**
     * 查询作业任务
     *
     * @param id 作业任务主键
     * @return 作业任务
     */
    @Override
    public TDayTask selectTDayTaskById(Long id) {
        return tDayTaskMapper.selectTDayTaskById(id);
    }

    /**
     * 查询作业任务列表
     *
     * @param tDayTask 作业任务
     * @return 作业任务
     */
    @Override
    public List<TDayTask> selectTDayTaskList(TDayTask tDayTask) {
        return tDayTaskMapper.selectTDayTaskList(tDayTask);
    }

    /**
     * 新增作业任务
     *
     * @param tDayTask 作业任务
     * @return 结果
     */
    @Override
    public int insertTDayTask(TDayTask tDayTask) {
        tDayTask.setCreateTime(DateUtils.getNowDate());
        tDayTask.setNyStatus("Y");
        String rediskey = java.util.UUID.randomUUID().toString().replace("-", "");
        String taskdatatable = "task_data_" + DateUtils.dateTimeNow("yyyy_MM_dd_HH_mm_ss");

        tDayTask.setFreedom1(rediskey);
        tDayTask.setFreedom2(taskdatatable);
        if (null == tDayTask.getBaseEvolution()) {
            tDayTask.setBaseEvolution(0.0);
        }
        tableMapper.createVehicleDateTable(taskdatatable);

        tDayTaskMapper.insertTDayTask(tDayTask);
        return tDayTask.getId().intValue();
    }

    /**
     * 修改作业任务
     *
     * @param tDayTask 作业任务
     * @return 结果
     */
    @Override
    public int updateTDayTask(TDayTask tDayTask) {
        return tDayTaskMapper.updateTDayTask(tDayTask);
    }

    /**
     * 批量删除作业任务
     *
     * @param ids 需要删除的作业任务主键
     * @return 结果
     */
    @Override
    public int deleteTDayTaskByIds(Long[] ids) {
        return tDayTaskMapper.deleteTDayTaskByIds(ids);
    }

    /**
     * 删除作业任务信息
     *
     * @param id 作业任务主键
     * @return 结果
     */
    @Override
    public int deleteTDayTaskById(Long id) {
        return tDayTaskMapper.deleteTDayTaskById(id);
    }

    /*
     * 查询分布工程及分布工程下的单个工程
     * */
    @Override
    public ResponseData getList() {
        List<Row> rows = tDamsconstructionMapper.selectTDamsconstructionByStatus4();

        ResponseData responseData = new ResponseData();
        List<Row> rowList = new ArrayList<>();
        //查询分部工程下的单个工程
        rows.forEach(r -> {

            List<Dam> damList = tDamsconstructionMapper.selectByid(r.getId());
            List<Dam> list = new ArrayList<>(damList);
            r.setDams(list);
            rowList.add(r);
        });
        responseData.setRows(rowList);
        responseData.setCode(HttpStatus.SUCCESS);
        responseData.setMsg("查询成功");
        responseData.setTotal(new PageInfo(rows).getTotal());
        return responseData;
    }
}
