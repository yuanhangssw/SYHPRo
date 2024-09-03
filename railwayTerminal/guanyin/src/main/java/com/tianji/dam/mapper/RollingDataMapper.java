package com.tianji.dam.mapper;


import com.tianji.dam.domain.CarTime;
import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.RollingData;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
@DataSource(value = DataSourceType.SLAVE)
public interface RollingDataMapper {
    /*
     *查询车开始结束碾压时间*/
    @Select("select VehicleID , MAX(DATE_FORMAT(FROM_UNIXTIME(`Timestamp`/1000),'%Y-%m-%d %h:%i:%s')) as endTime, MIN(DATE_FORMAT(FROM_UNIXTIME(`Timestamp`/1000),'%Y-%m-%d %h:%i:%s')) as startTime\n" +
            "from ${tablename} GROUP BY VehicleID")
    public List<CarTime> getTime(@Param("tablename") String tablename);

    /*
     *查询每层包含的单元工程tableName*/
    @Select("SELECT id FROM t_damsconstruction WHERE engcode='${tableName}'")
    public List<String> getUnittableName(@Param("tableName") String tableName);

    /*
     * 查询某张表中的所有数据 */
    @Select("select Acceleration,Frequency,Speed,`Timestamp`,VehicleID,VibrateValue,Elevation,ZhuangX,ZhuangY,CoordX,CoordY,zhuanghao,pianju,houdu from `${tableName}` ORDER BY `Timestamp`")
    public List<RollingData> getAllRollingData(@Param("tableName") String tableName);

    /*
     * 查询某张表中的所有数据*/
    @Select("select Acceleration,Frequency,Speed,`Timestamp`,VehicleID,VibrateValue,Elevation,ZhuangX,ZhuangY,CoordX,CoordY,Angle,zhuanghao,pianju,houdu,CurrentEvolution,CoordLX,CoordLY,CoordRX,CoordRY,BeforeCoordLX,BeforeCoordLY,BeforeCoordRX,BeforeCoordRY,BeforeElevation,qhangle,zyangle from `${tableName}` where VehicleID = ${vehicleID} ORDER BY `Timestamp` ")
    public List<RollingData> getAllRollingDataByVehicleID(@Param("tableName") String tableName, @Param("vehicleID") String vehicleID);
    @Select("select vehicleID,coordX,coordY,`Timestamp` from `${tableName}` where VehicleID = ${vehicleID} ORDER BY `Timestamp` ")
    public List<Map<String,Object>> getsimpledataVehicleID(@Param("tableName") String tableName, @Param("vehicleID") String vehicleID);

    /*
     * 根据仓位空间查询某张表中的所有数据*/
    @Select("select Acceleration,Frequency,Speed,`Timestamp`,VehicleID,VibrateValue,Elevation,ZhuangX,ZhuangY,CoordX,CoordY,zhuanghao,pianju,houdu,CurrentEvolution,BeforeCoordLX,BeforeCoordLY,BeforeCoordRX,BeforeCoordRY,BeforeElevation,qhangle,zyangle from `${tableName}`  where Elevation BETWEEN ${zbegin} AND ${zend} AND ZhuangX BETWEEN ${xbegin} AND ${xend} AND ZhuangY BETWEEN ${ybegin} AND ${yend} AND materialname=${mat} GROUP BY Elevation,ZhuangX,ZhuangY,`Timestamp` ORDER BY `Timestamp`")
    public List<RollingData> getAllRollingDataByCube(@Param("tableName") String tableName, @Param("vehicleID") String vehicleID, @Param("xbegin") Double xbegin, @Param("xend") Double xend, @Param("ybegin") Double ybegin, @Param("yend") Double yend, @Param("zbegin") Double zbegin, @Param("zend") Double zend, @Param("mat") String mat);

    @Select("select * from t_1 where VehicleID = ${vehicleID} and Elevation BETWEEN ${bottom} and ${top} ORDER BY `Timestamp` ")
    public List<RollingData> getAllRollingDataByVehicleIDByEla(@Param("vehicleID") String vehicleID, @Param("bottom") Double bottom, @Param("top") Double top);

    @Select("select Acceleration,Frequency,Speed,`Timestamp`,VehicleID,VibrateValue,Elevation,ZhuangX,ZhuangY,CoordX,CoordY,zhuanghao,pianju,houdu,CurrentEvolution,BeforeCoordLX,BeforeCoordLY,BeforeCoordRX,BeforeCoordRY,BeforeElevation,qhangle,zyangle from `${tableName}` where VehicleID = ${vehicleID} and  materialname=${mat} and Elevation BETWEEN ${bottom} and ${top} ORDER BY `Timestamp` ")
    List<RollingData> getAllRollingDataByVehicleIDByElaAndRange(@Param("tableName") String tableName, @Param("vehicleID") String vehicleID, @Param("bottom") Double bottom, @Param("top") Double top, @Param("mat") String mat);

    @Select("select Acceleration,Frequency,Speed,`Timestamp`,VehicleID,VibrateValue,Elevation,ZhuangX,ZhuangY,CoordX,CoordY,zhuanghao,pianju,houdu,CurrentEvolution,BeforeCoordLX,BeforeCoordLY,BeforeCoordRX,BeforeCoordRY,BeforeElevation,qhangle,zyangle from `${tableName}` where  TimeStamp BETWEEN ${startTime} and ${endTime} ORDER BY `Timestamp` ")
    List<RollingData> getDataByTimeStamp(@Param("tableName") String tableName, @Param("startTime") long startTime, @Param("endTime") long endTime);

    @Select("select Acceleration,Frequency,Speed,`Timestamp`,VehicleID,VibrateValue,Elevation,ZhuangX,ZhuangY,CoordX,CoordY,zhuanghao,pianju,houdu,CurrentEvolution,BeforeCoordLX,BeforeCoordLY,BeforeCoordRX,BeforeCoordRY,BeforeElevation,qhangle,zyangle from `${tableName}` where  VehicleID = ${carId} and materialname=${mat} and TimeStamp BETWEEN ${startTime} and ${endTime} ORDER BY `Timestamp` ")
    List<RollingData> getDataByTimeStampAndCarId(@Param("tableName") String tableName, @Param("carId") String carId, @Param("mat") String mat, @Param("startTime") long startTime, @Param("endTime") long endTime);


    /*
     * 根据仓位空间查询某张表中的所有数据*/
    @Select("select Acceleration,Frequency,Speed,`Timestamp`,VehicleID,VibrateValue,Elevation,ZhuangX,ZhuangY,CoordX,CoordY,zhuanghao,pianju,houdu,CurrentEvolution,BeforeCoordLX,BeforeCoordLY,BeforeCoordRX,BeforeCoordRY,BeforeElevation,qhangle,zyangle from `${tableName}`  where ZhuangX BETWEEN ${xbegin} AND ${xend} AND ZhuangY BETWEEN ${ybegin} AND ${yend} AND materialname=${mat} GROUP BY Elevation,ZhuangX,ZhuangY,`Timestamp` ORDER BY `Timestamp`")
    public List<RollingData> getAllRollingDataByCube2(@Param("tableName") String tableName, @Param("vehicleID") String vehicleID, @Param("xbegin") Double xbegin, @Param("xend") Double xend, @Param("ybegin") Double ybegin, @Param("yend") Double yend, @Param("zbegin") Double zbegin, @Param("zend") Double zend, @Param("mat") String mat);


    @Select("select Acceleration,Frequency,Speed,`Timestamp`,VehicleID,VibrateValue,Elevation,ZhuangX,ZhuangY,CoordX,CoordY,Angle,Amplitude,zhuanghao,pianju,houdu,CurrentEvolution,BeforeCoordLX,BeforeCoordLY,BeforeCoordRX,BeforeCoordRY,BeforeElevation,qhangle,zyangle from `${tableName}` where VehicleID = ${vehicleID} ORDER BY `Timestamp` limit 0,1")
    public List<RollingData> getAllRollingDataByVehicleIDLimit1(@Param("tableName") String tableName, @Param("vehicleID") String vehicleID);

    @Select("select Acceleration,Frequency,Speed,`Timestamp`,VehicleID,VibrateValue,Elevation,ZhuangX,ZhuangY,CoordX,CoordY,Angle,Amplitude,zhuanghao,pianju,houdu,CurrentEvolution,BeforeCoordLX,BeforeCoordLY,BeforeCoordRX,BeforeCoordRY,BeforeElevation,qhangle,zyangle from `${tableName}` where VehicleID = ${vehicleID} and `Timestamp` > ${timestamp} ORDER BY `Timestamp` limit ${pageSize}")
    public List<RollingData> getAllRollingDataByVehicleIDLimit2(@Param("tableName") String tableName, @Param("vehicleID") String vehicleID, @Param("timestamp") Long timestamp, @Param("pageSize") int pageSize);


    /*
     * 查询某张表中的时间范围筛选数据*/
    @Select("select Acceleration,Frequency,Speed,`Timestamp`,VehicleID,VibrateValue,Elevation,ZhuangX,ZhuangY,CoordX,CoordY,Angle,Amplitude,zhuanghao,pianju,houdu,CurrentEvolution,BeforeCoordLX,BeforeCoordLY,BeforeCoordRX,BeforeCoordRY,BeforeElevation,qhangle,zyangle from `${tableName}` where  TimeStamp BETWEEN ${beginTimestamp} and ${endTimestamp} ORDER BY `Timestamp` ")
    public List<RollingData> getAllRollingDataByVehicleIDAndDateRange(@Param("tableName") String tableName, @Param("vehicleID") String vehicleID, @Param("beginTimestamp") Long beginTimestamp, @Param("endTimestamp") Long endTimestamp);

    /*
     * 查询某张表中的时间筛选数据*/
    @Select("select Acceleration,Frequency,Speed,`Timestamp`,VehicleID,VibrateValue,Elevation,ZhuangX,ZhuangY,CoordX,CoordY,CoordLX,CoordLY,CoordRX,CoordRY,Angle,Amplitude,zhuanghao,pianju,houdu,CurrentEvolution,BeforeCoordLX,BeforeCoordLY,BeforeCoordRX,BeforeCoordRY,BeforeElevation,qhangle,zyangle from `${tableName}` where VehicleID = ${vehicleID}   ORDER BY `Timestamp` ")
    public List<RollingData> getAllRollingDataByVehicleIDAndDate(@Param("tableName") String tableName, @Param("vehicleID") String vehicleID, @Param("beginTimestamp") Long beginTimestamp, @Param("endTimestamp") Long endTimestamp);


    /*
     * 查询某张表中的时间范围筛选数据*/
    @Select("select Acceleration,Frequency,Speed,`Timestamp`,VehicleID,VibrateValue,Elevation,ZhuangX,ZhuangY,CoordX,CoordY,Angle,Amplitude,zhuanghao,pianju,houdu,CurrentEvolution,BeforeCoordLX,BeforeCoordLY,BeforeCoordRX,BeforeCoordRY,BeforeElevation,qhangle,zyangle from `${tableName}` where VehicleID = ${vehicleID}  and TimeStamp BETWEEN ${beginTimestamp} and ${endTimestamp} ORDER BY `Timestamp` ")
    public List<RollingData> getAllRollingDataByDateRange(@Param("tableName") String tableName, @Param("vehicleID") String vehicleID, @Param("beginTimestamp") Long beginTimestamp, @Param("endTimestamp") Long endTimestamp);


    @Select("select Latitude,Longitude from `${tableName}` where VehicleID = ${vehicleID}  and TimeStamp BETWEEN ${beginTimestamp} and ${endTimestamp} ORDER BY `Timestamp` asc ")
    public List<RollingData> getRollingLatLngDataByDateRange(@Param("tableName") String tableName, @Param("vehicleID") String vehicleID, @Param("beginTimestamp") Long beginTimestamp, @Param("endTimestamp") Long endTimestamp);

        @Select("SELECT AVG(Elevation)  FROM ${param1} ")
        Double getavgevolution(String tablename);

        @Select("SELECT tablename,gaocheng FROM t_damsconstruction where engcode = #{param1} order by gaocheng  ")
        List<DamsConstruction> getDamsConstructionsbyencode(Integer encode);

}
