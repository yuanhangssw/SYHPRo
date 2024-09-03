package com.tianji.dam.mapper;


import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.RollingData;
import com.tianji.dam.domain.T1;
import com.tianji.dam.domain.TrackStatistic;
import com.tianji.dam.domain.vo.RollingDataListVo;
import com.tianji.dam.domain.vo.T1RestAPI;
import com.tianji.dam.domain.vo.T1VO;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 原始轨迹数据信息接口
 */
@Mapper
@Component
@DataSource(value = DataSourceType.SLAVE)
public interface T1Mapper {
    /**
     * 查询
     *
     * @param vo
     * @return
     */
    List<T1> select(T1VO vo);

    /**
     * 数据归档查询
     *
     * @param vo
     * @return
     */
    List<T1> selectByRanges(T1VO vo);

    @Select("select id,ZhuangX,ZhuangY,Elevation as elevation from `${tablename}` Where TimeStamp between ${actualstarttime} and ${actualendtime} order by `Timestamp`")
    List<T1> selectByTime(@Param("tablename") String tablename, @Param("actualstarttime") long actualstarttime, @Param("actualendtime") long actualendtime);

    @Select("select id,VehicleID,ZhuangX,ZhuangY,Elevation as elevation,coordX,coordY,BeforeCoordLX,BeforeCoordLY,BeforeCoordRX,BeforeCoordRY,coordLX,coordLY,coordRX,coordRY from `${tablename}` Where Elevation between ${begin} and ${end} order by `Timestamp`")
    List<T1> selectByEvolution(@Param("tablename") String tablename, @Param("begin") double begin, @Param("end") double end);

    @Select("SELECT max(gaocheng)  elevation FROM `t_damsconstruction` where  `status` =8")
    T1 selectmaxevolution(@Param("tablename") String tablename, @Param("actualstarttime") long actualstarttime, @Param("actualendtime") long actualendtime);
    @Select("select max(Elevation) as elevation from ylj_t_1 ")
    Double maxe();

    /**
     * 复制原始表数据到对应的单元数据表
     *
     * @param vo
     * @return
     */
    Integer addTableData(T1VO vo);

    /**
     * 根据高程 以及车辆对应的起始日期查询原始数据
     *
     * @return
     */
    List<RollingData> getRollingData();


    List<T1> selectPage(T1VO vo);

    List<T1> selectCangData(T1VO vo);
    @Select("select * from  t_1 where  VehicleID = #{VehicleID} ORDER BY `Timestamp` desc limit 0,1")
    T1RestAPI selecttop1(@Param("VehicleID") int VehicleID);

    List<T1RestAPI> selectrest(T1VO param);


    @Select("select * from (select `Timestamp`,avg(speed) speed  from t_1 where VehicleID = #{VehicleID} and Timestamp BETWEEN #{begintime} and #{endtime}  and speed >0  group by `Timestamp`   ORDER BY `Timestamp` desc limit 50   ) t order by  t.TIMESTAMP asc")
    List<T1> selecttimespeed(@Param("VehicleID") int VehicleID, @Param("begintime") long begintime, @Param("endtime") long endtime);

    @Select("select * from t_new_data where VehicleID = #{VehicleID}")
    List<T1> selecttimespeedlast(@Param("VehicleID") int VehicleID);

    @Select("select * from t_new_data   where VehicleID = #{VehicleID} ")
    T1 selectcarlast(@Param("VehicleID") int VehicleID);

    @Select("select * from t_new_data order by Timestamp desc limit 1")
    T1 selectlasttime();

    void updateBatch(T1 t1);


    @Select("select * from t_1  order by Timestamp desc limit 0,100 ")
    public List<T1> selectzhuangxyisnull();

    @Select("select * from t_damsconstruction where tablename is not null")
    public List<DamsConstruction> getalloldtable();

    @Select("ALTER TABLE `${tablename}` ADD COLUMN houdu double NULL")
    public void updatetable(@Param("tablename") String tablename);

    @Select("select * from `${tablename}`  where zhuanghao is null ")
    public List<T1> getupdata(@Param("tablename") String tablename);

    @Select("update `${tablename}` set zhuanghao=#{zhuanghao},pianju=#{pianju} where id =#{id}")
    public void updatetabledata(@Param("id") String id, @Param("zhuanghao") double zhuanghao, @Param("pianju") double pianju, @Param("tablename") String tablename);


    public int insertrollingdatabatch(RollingDataListVo vo);

    @Select("select sum(timepass) timepass from t_car_online_record where  carID=#{carid} and   onlinetime>=#{times}")
    public Integer getcaronlinecount(@Param("carid") int carid, @Param("times") String times);

    @Select("select count(1) from t_1 where zhuangX is null ")
    public Integer gett1count();

    @Select("select * from t_1 where  zhuangX is null  order by Timestamp limit 1000")
    public List<T1> gett1pages();

    public void upt1zhuangxybatch(List<T1> all);

    @Select("select VehicleID,coordX,coordY,BeforeCoordLX,BeforeCoordLY,BeforeCoordRX,BeforeCoordRY,coordLX,coordLY,coordRX,coordRY from `${tablename}` where  `Timestamp` BETWEEN #{begintime} and #{endtime} order by `Timestamp` asc")
    public List<T1> selectbasedata(@Param("tablename") String tablename, @Param("begintime") Long begintime, @Param("endtime") Long endtime);
    @Select("select id,coordX,coordY,BeforeCoordLX,BeforeCoordLY,BeforeCoordRX,BeforeCoordRY,coordLX,coordLY,coordRX,coordRY,Elevation from `${tablename}` where  Timestamp BETWEEN #{begin} and #{end} order by `Timestamp` asc")
    public List<T1> selectallbasedata(@Param("tablename") String tablename,@Param("begin") Long begin,@Param("end")Long end);

    @Select("SELECT MIN(`Timestamp`)  Timestamp FROM `${tablename}` ")
    public List<T1> selectcarfirstdata(@Param("tablename") String tablename);
    @Select("select tablename from t_damsconstruction where id=#{id}")
    public String selecttablename(@Param("id") int id);

    @Select("SELECT * FROM `${tablename}` WHERE VehicleID IN (3,4,5,6)")
    public List<RollingData> getalldata_3456(@Param("tablename") String tablename);

    @Select("SELECT * FROM `${tablename}` WHERE VehicleID IN (1,2)")
    public List<RollingData> getalldata_12(@Param("tablename") String tablename);
    @Delete("delete from `${tablename}` WHERE VehicleID IN (3,4,5,6)")
    public int  delte_3456(@Param("tablename") String tablename);
    @Delete("delete from `${tablename}` WHERE VehicleID IN (1,2)")
    public int  delte_12(@Param("tablename") String tablename);
    @Select("select tablename from t_damsconstruction where tablename is not null")
    public List<String> getalltable();

    @Select("SELECT\n" +
            "\ta.* ,b.Remark\n" +
            "FROM\n" +
            "\t(\n" +
            "\tSELECT\n" +
            "\t\tVehicleID,\n" +
            "\t\tFROM_UNIXTIME( `Timestamp` / 1000, '%Y-%m-%d' ) AS date, FROM_UNIXTIME( MIN( `Timestamp` ) / 1000, '%H:%i:%s' ) AS startTime,\n" +
            "\t\tFROM_UNIXTIME( MAX( `Timestamp` ) / 1000, '%H:%i:%s' ) AS endTime,\n" +
            "\t\tMAX( Elevation ) AS maxElevation,\n" +
            "\t\tMIN( Elevation ) AS minElevation\n" +
            "\tFROM\n" +
            "\t\tylj_t_1 \n" +
            "\tGROUP BY\n" +
            "\t\tdate,\n" +
            "\t\tVehicleID \n" +
            "\t) a \n" +
            "\tLEFT JOIN t_car b  on a.VehicleID = b.CarID\n" +
            "ORDER BY\n" +
            "\ta.date desc ")
    public List<TrackStatistic> getdayevolution();

    @Select("SELECT DISTINCT VehicleID FROM  `${tablename}` ")
    List<String> selectCangCar(@Param("tablename") String tablename);
}
