package com.tianji.dam.service.serviceimpl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tianji.dam.domain.DateSpeed;
import com.tianji.dam.domain.QueryConditions;
import com.tianji.dam.domain.Sysconfig;
import com.tianji.dam.domain.TrackCar;
import com.tianji.dam.domain.TrackStatistic;
import com.tianji.dam.mapper.SysconfigMapper;
import com.tianji.dam.mapper.TTrackMapper;
import com.tianji.dam.service.TrackService;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;

@Service("tTrackServiceImpl")
@DataSource(value = DataSourceType.SLAVE)
public class TrackServiceImpl implements TrackService {
    @Autowired
    private TTrackMapper tTrackMapper;
    @Autowired
    private SysconfigMapper sysconfigMapper;

    /**
     * 查询轨迹信息和车辆基础信息
     * @param qc
     * @return
     */
    @Override
    public List<TrackCar> findTrackCar(QueryConditions qc) {
        return tTrackMapper.selectTrackCar(qc);
    }

    /**
     * 单车-查询超限速度及其时间
     * @param qc
     * @return
     */
    @Override
    public List<DateSpeed> findOverSpeed(QueryConditions qc) {
        Sysconfig sysconfig = sysconfigMapper.selectBySyskey(qc.getSys_key());
        return tTrackMapper.selectOverSpeed(Float.valueOf(sysconfig.getSysKeyvalue()), qc);
    }

    /**
     * 日-查询一天中所有车辆的超限速度
     * @param qc
     * @return
     */
    @Override
    public List<DateSpeed> findDayOverSpeed(QueryConditions qc) {
        Sysconfig sysconfig = sysconfigMapper.selectBySyskey(qc.getSys_key());
        return tTrackMapper.selectDayOverSpeed(Float.valueOf(sysconfig.getSysKeyvalue()), qc);
    }

    /**
     * 周-查询各车辆超限速度及其时间
     * @param qc
     * @return
     */
    @Override
    public List<DateSpeed> findWeekOverSpeed(QueryConditions qc) {
        Sysconfig sysconfig = sysconfigMapper.selectBySyskey(qc.getSys_key());
        return tTrackMapper.selectWeekOverSpeed(Float.valueOf(sysconfig.getSysKeyvalue()), qc);
    }

    /**
     * 单车-查询工作量统计中的表格内容
     * @param qc
     * @return
     */
    @Override
    public List<TrackStatistic> findStatistics(QueryConditions qc) {
        return tTrackMapper.selectStatistic(qc);
    }

    /**
     * 日-查询工作量统计中的表格内容
     * @param qc
     * @return
     */
    @Override
    public List<TrackStatistic> findDayStatistics(QueryConditions qc) {
        return tTrackMapper.selectDayStatistic(qc);
    }

    /**
     * 周-查询工作量统计中的表格内容
     * @param qc
     * @return
     */
    @Override
    public List<TrackStatistic> findWeekStatistics(QueryConditions qc) {
        return tTrackMapper.selectWeekStatistic(qc);
    }
}
