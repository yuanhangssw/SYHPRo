package com.tianji.dam.service.serviceimpl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tianji.dam.domain.DistanceTime;
import com.tianji.dam.domain.QueryConditions;
import com.tianji.dam.mapper.DistanceMapper;
import com.tianji.dam.service.DistanceService;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;

@Service
@DataSource(value = DataSourceType.SLAVE)
public class DistanceServiceImpl implements DistanceService {
    @Autowired
    private DistanceMapper distanceMapper;
 

    /**
     * 查询某辆车的工作量
     * @param qc
     * @return
     */
    @Override
    public List<DistanceTime> findACar(QueryConditions qc) {
        return distanceMapper.selectACar(qc);
    }


    /**
     * 查询日工作量
     * @param qc
     * @return
     */
    @Override
    public List<DistanceTime> findDaily(QueryConditions qc) {
        return distanceMapper.selectDaily(qc);
    }

    /**
     * 查询周工作量
     * @param qc
     * @return
     */
    @Override
    public List<DistanceTime> findWeekly(QueryConditions qc) {
        return distanceMapper.selectWeekly(qc);
    }
}
