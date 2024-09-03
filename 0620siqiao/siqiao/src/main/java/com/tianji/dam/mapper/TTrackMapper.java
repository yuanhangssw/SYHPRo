package com.tianji.dam.mapper;


import com.tianji.dam.domain.*;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
@DataSource(value = DataSourceType.SLAVE)
public interface TTrackMapper {
    /**
     * 统计轨迹数据量
     *
     * @param example
     * @return
     */
    long countByExample(TTrackExample example);

    /**
     * 删除轨迹
     *
     * @param example
     * @return
     */
    int deleteByExample(TTrackExample example);

    /**
     * 根据主键删除轨迹
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 新增轨迹
     *
     * @param record
     * @return
     */
    int insert(TTrack record);

    /**
     * 新增轨迹（字段可选）
     *
     * @param record
     * @return
     */
    int insertSelective(TTrack record);

    /**
     * 查询轨迹
     *
     * @param example
     * @return
     */
    List<TTrack> selectByExample(TTrackExample example);

    /**
     * 根据主键查询轨迹
     *
     * @param id
     * @return
     */
    TTrack selectByPrimaryKey(Integer id);

    /**
     * 更新轨迹（字段可选）
     *
     * @param record
     * @param example
     * @return
     */
    int updateByExampleSelective(@Param("record") TTrack record, @Param("example") TTrackExample example);

    /**
     * 更新轨迹
     *
     * @param record
     * @param example
     * @return
     */
    int updateByExample(@Param("record") TTrack record, @Param("example") TTrackExample example);

    /**
     * 根据主键更新轨迹（字段可选）
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(TTrack record);

    /**
     * 根据主键更新轨迹
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(TTrack record);

    /**
     * 轨迹表和车辆信息表联表查询
     *
     * @param qc
     * @return
     */
    @Select("<script>select * from t_1,t_car\n" +
            "where t_1.VehicleID=t_car.CarID\n" +
            "<when test='qc.startTime != null'> and t_1.`Timestamp` &gt;= ${qc.startTime}</when>\n" +
            "<when test='qc.endTime != null'> and t_1.`Timestamp` &lt;= ${qc.endTime} </when>\n" +
            "<when test='qc.startElevation != null'> and t_1.Elevation &gt;= ${qc.startElevation}</when>\n" +
            "<when test='qc.endElevation != null'> and t_1.Elevation &lt;= ${qc.endElevation}</when>\n" +
            "<when test='qc.carId != 0'>and t_1.VehicleID = ${qc.carId} </when>" +
            "</script>")
    List<TrackCar> selectTrackCar(@Param("qc") QueryConditions qc);

    /**
     * 单车-查询超限速度及其时间
     *
     * @param thSpeed
     * @param qc
     * @return
     */
    @Select("<script>select DATE_FORMAT(FROM_UNIXTIME(`Timestamp` / 1000),'%Y-%m-%d %H:%i:%s') as 'dateTime', Speed as 'overSpeed', VehicleID from t_1 \n" +
            "where Speed>${thSpeed}\n" +
            "<when test='qc.carId != 0'>and VehicleID=${qc.carId}</when>\n" +
            "<when test='qc.startTime != null'> and t_1.`Timestamp` &gt;= ${qc.startTime}</when>\n" +
            "<when test='qc.endTime != null'> and t_1.`Timestamp` &lt;= ${qc.endTime} </when>\n" +
            "<when test='qc.startElevation != null'> and t_1.Elevation &gt;= ${qc.startElevation}</when>\n" +
            "<when test='qc.endElevation != null'> and t_1.Elevation &lt;= ${qc.endElevation}</when></script>")
    List<DateSpeed> selectOverSpeed(@Param("thSpeed") Float thSpeed, @Param("qc") QueryConditions qc);

    /**
     * 日-查询超限速度
     *
     * @param thSpeed
     * @param qc
     * @return
     */
    @Select("<script>select DATE_FORMAT(FROM_UNIXTIME(`Timestamp` / 1000),'%Y-%m-%d %H:%i:%s') as 'dateTime', Speed as 'overSpeed', VehicleID from t_1 \n" +
            "where Speed>${thSpeed}\n" +
            "<when test='qc.startTimeStr != null'> and t_1.`Timestamp` &gt;= UNIX_TIMESTAMP('${qc.startTimeStr}')*1000</when>\n" +
            "<when test='qc.endTimeStr != null'> and t_1.`Timestamp` &lt;= UNIX_TIMESTAMP('${qc.endTimeStr}')*1000 </when>\n" +
            "<when test='qc.startElevation != null'> and t_1.Elevation &gt;= ${qc.startElevation}</when>\n" +
            "<when test='qc.endElevation != null'> and t_1.Elevation &lt;= ${qc.endElevation}</when></script>")
    List<DateSpeed> selectDayOverSpeed(@Param("thSpeed") Float thSpeed, @Param("qc") QueryConditions qc);

    /**
     * 周-查询各车辆超限速度
     *
     * @param thSpeed
     * @param qc
     * @return
     */
    @Select("<script>select DATE_FORMAT(FROM_UNIXTIME(`Timestamp` / 1000),'%Y-%m-%d %H:%i:%s') as 'dateTime', Speed as 'overSpeed', VehicleID from t_1 \n" +
            "where Speed>${thSpeed}\n" +
            "<when test='qc.startTime != null'> and t_1.`Timestamp` &gt;= ${qc.startTime}</when>\n" +
            "<when test='qc.endTime != null'> and t_1.`Timestamp` &lt;= ${qc.endTime} </when>\n" +
            "<when test='qc.startElevation != null'> and t_1.Elevation &gt;= ${qc.startElevation}</when>\n" +
            "<when test='qc.endElevation != null'> and t_1.Elevation &lt;= ${qc.endElevation}</when></script>")
    List<DateSpeed> selectWeekOverSpeed(@Param("thSpeed") Float thSpeed, @Param("qc") QueryConditions qc);

    /**
     * 单车-统计高程和时间范围内VechicleId,日期，开始结束时间，最大最小高程，上传数据量
     *
     * @param qc
     * @return
     */
    @Select("<script>" +
            "select a.* from (select VehicleID,FROM_UNIXTIME(`Timestamp`/1000,'%Y-%m-%d') as date,\n" +
            "FROM_UNIXTIME(MIN(`Timestamp`)/1000,'%H:%i:%s') as startTime,\n" +
            "FROM_UNIXTIME(MAX(`Timestamp`)/1000,'%H:%i:%s') as endTime,\n" +
            "MAX(Elevation) as maxElevation,MIN(Elevation) as minElevation,count(*) as count from ${qc.tablename} \n" +
            "where " +
            "VehicleID=${qc.carId}\n" +
            "<when test='qc.startTime != null'> and `Timestamp` &gt;= ${qc.startTime}</when>\n" +
            "<when test='qc.endTime != null'> and `Timestamp` &lt;= ${qc.endTime} </when>\n" +
            "<when test='qc.startElevation != null'> and Elevation &gt;= ${qc.startElevation}</when>\n" +
            "<when test='qc.endElevation != null'> and Elevation &lt;= ${qc.endElevation}</when>\n" +
            "GROUP BY date,VehicleID ) a  order by a.date asc " +
            "</script>")
    List<TrackStatistic> selectStatistic(@Param("qc") QueryConditions qc);

    /**
     * 日-统计高程和时间范围内VechicleId,日期，开始结束时间，最大最小高程，上传数据量
     *
     * @param qc
     * @return
     */
    @Select("<script>" +
            "select VehicleID,FROM_UNIXTIME(`Timestamp`/1000,'%Y-%m-%d') as date,\n" +
            "FROM_UNIXTIME(MIN(`Timestamp`)/1000,'%H:%i:%s') as startTime,\n" +
            "FROM_UNIXTIME(MAX(`Timestamp`)/1000,'%H:%i:%s') as endTime,\n" +
            "MAX(Elevation) as maxElevation,MIN(Elevation) as minElevation,count(*) as count from ${qc.tablename}  \n" +
            "where 1=1" +
            "<when test='qc.startTimeStr != null'> and `Timestamp` &gt;= UNIX_TIMESTAMP('${qc.startTimeStr}')*1000</when>\n" +
            "<when test='qc.endTimeStr != null'> and `Timestamp` &lt;= UNIX_TIMESTAMP('${qc.endTimeStr}')*1000 </when>\n" +
            "<when test='qc.startElevation != null'> and Elevation &gt;= ${qc.startElevation}</when>\n" +
            "<when test='qc.endElevation != null'> and Elevation &lt;= ${qc.endElevation}</when>\n" +
            "GROUP BY date,VehicleID" +
            "</script>")
    List<TrackStatistic> selectDayStatistic(@Param("qc") QueryConditions qc);

    /**
     * 周-统计高程和时间范围内VechicleId,日期，开始结束时间，最大最小高程，上传数据量
     *
     * @param qc
     * @return
     */
    @Select("<script>" +
            "select VehicleID,FROM_UNIXTIME(`Timestamp`/1000,'%Y-%m-%d') as date,\n" +
            "FROM_UNIXTIME(MIN(`Timestamp`)/1000,'%H:%i:%s') as startTime,\n" +
            "FROM_UNIXTIME(MAX(`Timestamp`)/1000,'%H:%i:%s') as endTime,\n" +
            "MAX(Elevation) as maxElevation,MIN(Elevation) as minElevation,count(*) as count from t_1 \n" +
            "where 1=1" +
            "<when test='qc.startTime != null'> and `Timestamp` &gt;= ${qc.startTime}</when>\n" +
            "<when test='qc.endTime != null'> and `Timestamp` &lt;= ${qc.endTime} </when>\n" +
            "<when test='qc.startElevation != null'> and Elevation &gt;= ${qc.startElevation}</when>\n" +
            "<when test='qc.endElevation != null'> and Elevation &lt;= ${qc.endElevation}</when>\n" +
            "GROUP BY date,VehicleID" +
            "</script>")
    List<TrackStatistic> selectWeekStatistic(@Param("qc") QueryConditions qc);
}