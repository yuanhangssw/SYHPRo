package com.tianji.dam.mapper;
 

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.geo.Distance;
import org.springframework.stereotype.Component;

import com.tianji.dam.domain.DistanceSum;
import com.tianji.dam.domain.DistanceTime;
import com.tianji.dam.domain.QueryConditions;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;

@Mapper
@Component
@DataSource(value = DataSourceType.SLAVE)
public interface DistanceMapper {
    /**
     * 根据主键删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 新增数据
     * @param record
     * @return
     */
    int insert(Distance record);

    /**
     * 新增数据（字段可选）
     * @param record
     * @return
     */
    int insertSelective(Distance record);

    /**
     * 根据主键查询数据
     * @param id
     * @return
     */
    Distance selectByPrimaryKey(Integer id);

    /**
     * 根据主键更新数据（字段可选）
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Distance record);

    /**
     * 根据主键更新数据
     * @param record
     * @return
     */
    int updateByPrimaryKey(Distance record);



    /**
     * 所有车辆工作量统计
     * @param qc
     * @return
     */
    @Select("<script>select\n" +
            "   carId,sum(distance) as sum\n" +
            "   from t_distance where 1=1\n" +
            "   <when test='qc.startTimeStr != null'> and date &gt;= ${qc.startTimeStr}</when>\n" +
            "   <when test='qc.endTimeStr != null'> and date &lt;= ${qc.endTimeStr} </when>\n" +
            "   group by carId" +
            "</script>")
    List<DistanceSum> selectCars(@Param("qc") QueryConditions qc);

    /**
     * 时间范围内单个车辆每天的工作量
     */
    @Select("<script>select date,carId,distance\n" +
            "from t_distance\n" +
            "where carId=${qc.carId} " +
            "<when test='qc.startTimeStr != null'> and date &gt;= ${qc.startTimeStr}</when>\n" +
            "<when test='qc.endTimeStr != null'> and date &lt;= ${qc.endTimeStr} </when>" +
            "</script>")
    List<DistanceTime> selectACar(@Param("qc") QueryConditions qc);

    /**
     * 日工作量统计-查询这一天的各个车辆的工作量
     */
    @Select("select\n" +
            "   date,carId,distance\n" +
            "   from t_distance where date=${qc.startTimeStr}")
    List<DistanceTime> selectDaily(@Param("qc") QueryConditions qc);

    /**
     * 周工作量统计，查询一周内每天各个小
     */
    @Select("<script>select date,carId,distance from t_distance where 1=1\n" +
            "   <when test='qc.startTimeStr != null'> and date &gt;= ${qc.startTimeStr}</when>\n" +
            "   <when test='qc.endTimeStr != null'> and date &lt;= ${qc.endTimeStr} </when>\n" +
            "</script>")
    List<DistanceTime> selectWeekly(@Param("qc") QueryConditions qc);
}