package com.tianji.dam.service;



import java.util.List;

import com.tianji.dam.domain.DistanceTime;
import com.tianji.dam.domain.QueryConditions;

public interface DistanceService {


    /**
     * 查询某个车辆的工作量
     * @param qc
     * @return
     */
    List<DistanceTime> findACar(QueryConditions qc);

    /**
     * 查询日工作量
     * @param qc
     * @return
     */
    List<DistanceTime> findDaily(QueryConditions qc);

    /**
     * 查询周工作量
     * @param qc
     * @return
     */
    List<DistanceTime> findWeekly(QueryConditions qc);
}
