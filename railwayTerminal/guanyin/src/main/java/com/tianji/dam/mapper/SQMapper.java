package com.tianji.dam.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface SQMapper {


    @Select("SELECT DISTINCT Idcard from gpsinfo")
    public List<String> getdevicelist();
    @Select("SELECT  Idcard,`status`,time from gpsinfo  where Idcard=#{idcard}  order by `time` desc limit 1")
    public Map<String, Object> getdevicestatus(@Param("idcard") String s);

    @Select("select Idcard gpsSn,latitude lat,longitude lng,electricity electric, DATE_FORMAT(time, '%Y-%m-%d %H:%i:%s') uploadTime,speed from gpsinfo where Idcard=#{idcard} order by time desc limit 1")
    Map<String,Object> getdeviveinfobyid(@Param("idcard") String s);
}
