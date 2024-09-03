package com.tianji.dam.service;


import com.tianji.dam.domain.DateSpeed;
import com.tianji.dam.domain.QueryConditions;
import com.tianji.dam.domain.TrackCar;
import com.tianji.dam.domain.TrackStatistic;

import java.util.List;

public interface TrackService {
    /**
     * 查询轨迹信息和车辆基础信息
     * @param qc
     * @return
     */
    List<TrackCar> findTrackCar(QueryConditions qc);

    /**
     * 单车-查询超限速度及其日期
     * @param qc
     * @return
     */
    List<DateSpeed> findOverSpeed(QueryConditions qc);

    /**
     * 日-查询一天中所有车辆的超限速度
     * @param qc
     * @return
     */
    List<DateSpeed> findDayOverSpeed(QueryConditions qc);

    /**
     * 周-查询各车辆超限速度及其日期
     * @param qc
     * @return
     */
    List<DateSpeed> findWeekOverSpeed(QueryConditions qc);

    /**
     * 单车-查询工作量统计中的表格内容
     * @param qc
     * @return
     */
    List<TrackStatistic> findStatistics(QueryConditions qc);

    /**
     * 日-查询工作量统计中的表格内容
     * @param qc
     * @return
     */
    List<TrackStatistic> findDayStatistics(QueryConditions qc);

    /**
     * 周-查询工作量统计中的表格内容
     * @param qc
     * @return
     */
    List<TrackStatistic> findWeekStatistics(QueryConditions qc);
}
