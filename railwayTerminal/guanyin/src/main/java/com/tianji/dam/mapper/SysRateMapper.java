package com.tianji.dam.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.tianji.dam.domain.RollingDataJson;
import com.tianji.dam.domain.SysRate;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;
   
@Mapper
@Component
@DataSource(value = DataSourceType.SLAVE)
public interface SysRateMapper {
    /*
     * 查询某车 or大于某数的所有数据*/
    @Select("select `CoordX`, `CoordY`, `Latitude`, `Longitude`, `Elevation`, `Timestamp`, `LengthVehicleID`, `VehicleID`, `Speed`, `LayerID`, `IsForward`, `IsVibrate`, `VibrateValue`, `Frequency`, `Acceleration`, `Amplitude`, `Satellites`, `materialname`,`ishistory`, `OrderNum`, `Angle`, `CoordLX`, `CoordLY`, `CoordRX`, `CoordRY` from `${tableName}` where orderNum> ${orderNum} AND LayerID = ${layerId} And materialname = ${materialname} And VehicleID = ${vehicleID} ORDER BY `Timestamp` LIMIT 1000")
    public List<RollingDataJson> selectSysRate(@Param("tableName") String tableName, @Param("vehicleID") String vehicleID, @Param("layerId") int layerId, @Param("materialname") int materialname, @Param("orderNum") int orderNum);

    /*
     * 查询某车 or大于某数的所有数据*/
    @Select("SELECT DISTINCT VehicleID from `${tableName}`")
    public List<RollingDataJson> selectVehicleID(@Param("tableName") String tableName);

    @Select("SELECT DISTINCT CarID from t_car where CarID <> ${CarID}")
    public List<String> selectCars(@Param("CarID") String carId);

    /*插入SysRate记录*/
    @Insert("insert into `${tableName}`(uuid,ownerCar,otherCar,orderNum) values (#{sysRate.uuid},#{sysRate.ownerCar},#{sysRate.otherCar},#{sysRate.orderNum}) ")
    public int insertSysRate(@Param("tableName") String tableName, @Param("sysRate") SysRate sysRate);

    /*插入SysRate记录*/
    @InsertProvider(type = SysRateProvider.class, method = "insertAll")
    public int insertAllSysRate(@Param("uuid") String uuid, @Param("ownerCar") String ownerCar, @Param("otherCar") String otherCar, @Param("list") List<RollingDataJson> list);


    @Select("select `CoordX`, `CoordY`, `Latitude`, `Longitude`, `Elevation`, `Timestamp`, `LengthVehicleID`, `VehicleID`, `Speed`, `LayerID`, `IsForward`, `IsVibrate`, `VibrateValue`, `Frequency`, `Acceleration`, `Amplitude`, `Satellites`, `materialname`,`ishistory`, `OrderNum`, `Angle`, `CoordLX`, `CoordLY`, `CoordRX`, `CoordRY` from `${tableName}` where " +
            "orderNum not in (Select orderNum From sysrate Where ownerCar = ${ownerCar} and otherCar = ${otherCar} and uuid = '${uuid}') AND VehicleID = ${ownerCar} ORDER BY `OrderNum`")
    public List<RollingDataJson> selectAllRollingData(@Param("tableName") String tableName, @Param("uuid") String uuid, @Param("ownerCar") String ownerCar, @Param("otherCar") String otherCar, @Param("layerId") int layerId, @Param("materialname") int materialname);


    @Select("select `CoordX`, `CoordY`, `Latitude`, `Longitude`, `Elevation`, `Timestamp`, `LengthVehicleID`, `VehicleID`, `Speed`, `LayerID`, `IsForward`, `IsVibrate`, `VibrateValue`, `Frequency`, `Acceleration`, `Amplitude`, `Satellites`, `materialname`,`ishistory`, `OrderNum`, `Angle`, `CoordLX`, `CoordLY`, `CoordRX`, `CoordRY` from `${tableName}` where " +
            "orderNum not in (Select orderNum From sysrate Where ownerCar = ${ownerCar} and otherCar = ${otherCar} and uuid = '${uuid}') AND VehicleID = ${ownerCar} ORDER BY `OrderNum` LIMIT ${limit}")
    public List<RollingDataJson> selectAllUnsysRollingData(@Param("tableName") String tableName, @Param("uuid") String uuid, @Param("ownerCar") String ownerCar, @Param("otherCar") String otherCar, @Param("limit") int limit);


    @Select("select count(*) from ${tableName} where " +
        "orderNum not in (Select orderNum From sysrate Where ownerCar = ${ownerCar} and otherCar = ${otherCar} and uuid = '${uuid}') AND VehicleID = ${ownerCar}")
    public int selectUnSysCount(@Param("tableName") String tableName, @Param("uuid") String uuid, @Param("ownerCar") String ownerCar, @Param("otherCar") String otherCar, @Param("layerId") int layerId, @Param("materialname") int materialname);
}
