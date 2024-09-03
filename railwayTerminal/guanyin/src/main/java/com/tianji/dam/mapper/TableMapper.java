package com.tianji.dam.mapper;


import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.RangeZhuang;
import com.tianji.dam.domain.RollingData;
import com.tianji.dam.domain.TRepairData;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
@DataSource(value = DataSourceType.SLAVE)
public interface TableMapper {
    //生成碾压机械每天的碾压数据 生成每个工作仓数据
    @Update({"CREATE TABLE ${tableName} (" +
            "`id` int(11) NOT NULL AUTO_INCREMENT," +
            "`CoordX` double NULL DEFAULT NULL," +
            "`CoordY` double NULL DEFAULT NULL," +
            "`Latitude` double NULL DEFAULT NULL," +
            "`Longitude` double NULL DEFAULT NULL," +
            "`Elevation` float NULL DEFAULT NULL," +
            "`Timestamp` bigint(20) NULL DEFAULT NULL," +
            "`LengthVehicleID` int(11) NULL DEFAULT NULL," +
            "`VehicleID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL," +
            "`Speed` float NULL DEFAULT NULL," +
            "`LayerID` int(11) NULL DEFAULT NULL," +
            "`IsForward` int(11) NULL DEFAULT NULL," +
            "`IsVibrate` int(11) NULL DEFAULT NULL," +
            "`VibrateValue` double NULL DEFAULT NULL," +
            "`Frequency` double NULL DEFAULT NULL," +
            "`Acceleration` double NULL DEFAULT NULL," +
            "`Amplitude` double NULL DEFAULT NULL," +
            "`Satellites` int(11) NULL DEFAULT NULL," +
            "`ZhuangX` double NULL DEFAULT NULL," +
            "`ZhuangY` double NULL DEFAULT NULL," +
            "`zhuanghao` double NULL DEFAULT NULL," +
            "`pianju` double NULL DEFAULT NULL," +
            "`houdu` double NULL DEFAULT NULL," +
            "`ishistory` int(11) NULL DEFAULT NULL," +
            "`materialname` int(11) NULL DEFAULT NULL," +
            "`OrderNum` int(11) NULL DEFAULT NULL," +
            "`Angle` float NULL DEFAULT NULL," +
            "`CoordLX` double NULL DEFAULT NULL," +
            "`CoordLY` double NULL DEFAULT NULL," +
            "`CoordRX` double NULL DEFAULT NULL," +
            "`CoordRY` double NULL DEFAULT NULL," +
            "`CurrentEvolution` float NULL DEFAULT NULL," +
            "`BeforeCoordLX` double NULL DEFAULT NULL," +
            "`BeforeCoordLY` double NULL DEFAULT NULL," +
            "`BeforeCoordRX` double NULL DEFAULT NULL," +
            "`BeforeCoordRY` double NULL DEFAULT NULL," +
            "`BeforeElevation` double NULL DEFAULT NULL," +
            "`qhangle` double NULL DEFAULT NULL," +
            "`zyangle` double NULL DEFAULT NULL," +
            "`rangeStr` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL," +
            " PRIMARY KEY (`id`) USING BTREE" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8;"})
    public void createVehicleDateTable(@Param("tableName") String tableName);

    @Delete("truncate table `${tableName}` ")
    public int truncateDateTable(@Param("tableName") String tableName);

    @Delete("delete  from   `${tableName}` where VehicleID =#{carid} ")
    public int truncateDateTable2(@Param("tableName") String tableName, @Param("carid") Integer carid);
    @Delete("delete from t_history_pic where damid =#{damid}")
    public int deletesaveold( @Param("damid") Integer damid);

    @Insert("insert into `${tableName}`(CoordX,CoordY,Latitude,Longitude,Elevation,Timestamp," +
            "LengthVehicleID,VehicleID,Speed,LayerID,IsForward,IsVibrate,VibrateValue,Frequency,Acceleration," +
            "Amplitude,Satellites,ZhuangX,ZhuangY,zhuanghao,pianju,houdu,ishistory,materialname,OrderNum,Angle," +
            "CoordLX,CoordLY,CoordRX,CoordRY,rangeStr," +
            "CurrentEvolution,BeforeCoordLX,BeforeCoordLY,BeforeCoordRX,BeforeCoordRY,BeforeElevation,qhangle,zyangle)" +
            " values (#{rollingData.CoordX},#{rollingData.CoordY},#{rollingData.Latitude},#{rollingData.Longitude}," +
            "#{rollingData.Elevation},#{rollingData.Timestamp},#{rollingData.LengthVehicleID},#{rollingData.VehicleID},#{rollingData.Speed},#{rollingData.LayerID},#{rollingData.IsForward}," +
            "#{rollingData.IsVibrate},#{rollingData.VibrateValue},#{rollingData.Frequency},#{rollingData.Acceleration},#{rollingData.Amplitude},#{rollingData.Satellites},#{rollingData.ZhuangX},#{rollingData.ZhuangY},#{rollingData.zhuanghao},#{rollingData.pianju},#{rollingData.houdu},#{rollingData.ishistory},#{rollingData.materialname},#{rollingData.OrderNum},#{rollingData.Angle},#{rollingData.CoordLX}," +
            "#{rollingData.CoordLY},#{rollingData.CoordRX},#{rollingData.CoordRY},#{rollingData.rangeStr}," +
            " #{rollingData.CurrentEvolution},#{rollingData.BeforeCoordLX},#{rollingData.BeforeCoordLY},#{rollingData.BeforeCoordRX},#{rollingData.BeforeCoordRY},#{rollingData.BeforeElevation},#{rollingData.qhangle},#{rollingData.zyangle})")
    public int insertRollingData(@Param("tableName") String tableName, @Param("rollingData") RollingData rollingData);


    public int insertRollingDatabatch();

    @Select("show TABLES like '${tableName}'")
    @ResultType(Map.class)
    public Map<String, String> checkTableExistsWithSchema(@Param("tableName") String tableName);

    @Select("SELECT count(*) from  ${tableName}")
    int getCount(@Param("tableName") String tableName);

    @Select("SELECT min(ZhuangX) as 'xbegin' ,max(ZhuangX)  as 'xend' ,min(ZhuangY)  as 'ybegin' ,max(ZhuangY)  as 'yend'  " +
            "from t_1 where Elevation BETWEEN ${bottom} and ${top} ")
    @ResultType(RangeZhuang.class)
    RangeZhuang getRangeZhuangDataByEla(@Param("bottom") Double bottom, @Param("top") Double top);

    @Select("SELECT count(*) from  ${tableName}  Where  TimeStamp BETWEEN ${startTime} and ${endTime}")
    int getCountByTimeStamp(@Param("tableName") String tableName, @Param("startTime") long startTime, @Param("endTime") long endTime);

    @Select("select DISTINCT materialname from ${tableName}")
    List<Integer> findMaterialList(@Param("tableName") String tablename);

    @Delete("DROP TABLE ${tableName}")
    int dropByTablename(@Param("tableName") String tablename);

    //查询t_1表zhuangy的最大值
    @Select("select max(zhuangy) from t_1")
    double findZhuangYMaxT_1();

    //查询t_1表zhuangy的最小值
    @Select("select min(zhuangy) from t_1")
    double findZhuangYMinT_1();

    //查询t_1表zhuangx的最大值
    @Select("select max(zhuangx) from t_1")
    double findZhuangXMaxT_1();

    //查询t_1表zhuangy的最大值
    @Select("select min(zhuangx) from t_1")
    double findZhuangXMinT_1();

    @Select("SELECT * FROM t_damsconstruction")
    List<DamsConstruction> findIdRangesFromDam();

    @Select("select max(gaocheng) from t_damsconstruction")
    double findMaxGaocheng();

    @Select("select min(gaocheng) from t_damsconstruction")
    double findMinGaocheng();

    @Select("select max(yend) from t_damsconstruction")
    double findMaxYend();

    @Select("select min(ybegin) from t_damsconstruction")
    double findMinYbegin();

    @Select("SELECT cenggao from `t_damsconstruction` where gaocheng=(SELECT MAX(gaocheng) FROM `t_damsconstruction`)")
    double findLastCenggao();

    @Select("select max(xend) from t_damsconstruction")
    double findMaxXend();

    @Select("select min(xbegin) from t_damsconstruction")
    double findMinXbegin();

//    @Select("SELECT" + 
//    		"	min( elevation ) gaocheng_bottom," + 
//    		"	max( elevation ) gaocheng_top," + 
//    		"	avg(elevation)  avgelevation," + 
//    		"	min( ZhuangX ) AS 'xbegin'," + 
//    		"	max( ZhuangX ) AS 'xend'," + 
//    		"	min( ZhuangY ) AS 'ybegin'," + 
//    		"	max( ZhuangY ) AS 'yend'  from ${tableName}")
//   public Map<String,Object> selecttabledata(@Param("tableName")String tablename);
//    


    @Select("SELECT" +
            "	min( elevation ) gaocheng_bottom," +
            "	max( elevation ) gaocheng_top," +
            "	avg(elevation)  avgelevation," +
            "	min( zhuanghao ) AS 'xbegin'," +
            "	max( zhuanghao ) AS 'xend'," +
            "	min( pianju ) AS 'ybegin'," +
            "	min(VehicleID) AS 'VehicleID'," +
            "	max( pianju ) AS 'yend'  from ${tableName}")
    public Map<String, Object> selecttabledata(@Param("tableName") String tablename);


    @Select("SELECT" +
            "	max(VibrateValue)   maxamplitude," +
            "	AVG(VibrateValue)  avgamplitude," +
            "	max(speed)  maxspeed," +
            "	AVG(speed)  avgspeed," +
            "   min(CurrentEvolution) minevolution," +
            "   max(CurrentEvolution) maxevolution," +
            "   AVG(CurrentEvolution) avgevolution," +
            "	max( ZhuangY ) AS 'yend'  from ${tableName} where speed>0 ")
    public Map<String, Object> selecttabledata_0(@Param("tableName") String tablename);

    @Select("select zhuanghao from (" +
            "SELECT zhuanghao FROM ${tableName} where `zhuanghao`!=0 ORDER BY `zhuanghao` asc limit 1" +
            ") t1" +
            " UNION ALL " +
            " SELECT zhuanghao from (" +
            " SELECT zhuanghao FROM ${tableName} ORDER BY `zhuanghao` desc limit 1" +
            ") t2")
    public List<Double> getbegin_endzhuanghao(@Param("tableName") String tablename);


    public Double seletbeforeavgelevation(@Param("tableName") String tablename);

    @Insert("insert into `${tableName}`(tableName, subId, CoordX,CoordY,Latitude,Longitude,Elevation,Timestamp," +
            "LengthVehicleID,VehicleID,Speed,LayerID,IsForward,IsVibrate,VibrateValue,Frequency,Acceleration,Amplitude,Satellites,ZhuangX,ZhuangY,zhuanghao,pianju,ishistory,materialname,OrderNum,Angle,CoordLX,CoordLY,CoordRX,CoordRY,rangeStr) values (#{rollingData.tablename}, #{rollingData.subid}, #{rollingData.CoordX},#{rollingData.CoordY},#{rollingData.Latitude},#{rollingData.Longitude}," +
            "#{rollingData.Elevation},#{rollingData.Timestamp},#{rollingData.LengthVehicleID},#{rollingData.VehicleID},#{rollingData.Speed},#{rollingData.LayerID},#{rollingData.IsForward}," +
            "#{rollingData.IsVibrate},#{rollingData.VibrateValue},#{rollingData.Frequency},#{rollingData.Acceleration},#{rollingData.Amplitude},#{rollingData.Satellites},#{rollingData.ZhuangX},#{rollingData.ZhuangY},#{rollingData.zhuanghao},#{rollingData.pianju},#{rollingData.ishistory},#{rollingData.materialname},#{rollingData.OrderNum},#{rollingData.Angle},#{rollingData.CoordLX}," +
            "#{rollingData.CoordLY},#{rollingData.CoordRX},#{rollingData.CoordRY},#{rollingData.rangeStr}) ")
    public int insertNewRollingData(@Param("tableName") String tableName, @Param("rollingData") RollingData rollingData);


    @Delete("delete from t_new_data where VehicleID = #{VehicleID}")
    int deleteNewRollingData(@Param("VehicleID") String VehicleID);

    @Select("SELECT MAX(id) as id FROM  ${tableName}")
    int getMaxId(@Param("tableName") String tableName);

    @Select("select * from t_repair_data where damsid =#{damsid} and    repairtype is null or repairtype =0")
    List<TRepairData> gettablerepair(@Param("damsid") String damsid);

    @Select("delete from ${tablename} where VehicleID=#{carid} and  Timestamp between #{begintime} and #{endtime}")
    public void deletefromt1(@Param("tablename") String tablename, @Param("begintime") Long begintime, @Param("endtime") Long endtime, @Param("carid") Integer carid);
    @Select("select min(Timestamp) begin,max(Timestamp) end from ${tablename}")
    public Map<String,Object> seleminmaxdate(@Param("tablename") String tablename);
}
