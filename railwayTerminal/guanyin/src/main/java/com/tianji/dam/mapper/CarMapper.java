package com.tianji.dam.mapper;

import com.tianji.dam.domain.Car;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
@Component
@DataSource(value = DataSourceType.SLAVE)
public interface CarMapper {
    int deleteByPrimaryKey(Integer carID);

    int insert(Car record);

    int insertSelective(Car record);

    Car selectByPrimaryKey(Integer carID);

    int updateByPrimaryKeySelective(Car record);

    int updateByPrimaryKey(Car record);

    List<Car> findCar();

    @Select("update t_car_online_record set offlinetime =#{offlinetime},timepass=#{timepass} where carID=#{carID} and offlinetime is null ")
    void updatecaroffline(@Param("carID") int carID, @Param("offlinetime") Date offlinetime, @Param("timepass") Double timepass);

    @Select("insert into t_car_online_record (carID,onlinetime) values(#{carID},#{time})")
    void insertcaronline(@Param("carID") int carID, @Param("time") Date time);

    @Select("select onlinetime from t_car_online_record where carID=#{carID} and  offlinetime is null  order by onlinetime desc limit 1 ")
    public List<Date> getcaronlinetime(@Param("carID") int carID);

    @Select("select onlinetime,offlinetime from t_car_online_record where carID=#{carID} and  offlinetime is not null and timepass>5  order by offlinetime desc limit 10 ")
    public List<Map<String, LocalDateTime>> getcarofflinetime(@Param("carID") int carID);

    @Select("select carID,remark from t_car where type =#{type}")
    public List<Car> getCarbyType(@Param("type") Integer type);
    @Select("select onlinetime from t_car_online_record where carID=#{carID}  order by onlinetime desc limit 1 ")
    public List<Date> getcaronlinetime2(@Param("carID") int carID);
    @Select("SELECT" +
            "   SUM( TIMESTAMPDIFF( MINUTE, onlinetime, ifnull( offlinetime, NOW( ) ) ) )" +
            "   FROM" +
            "   t_car_online_record" +
            "   WHERE" +
            "   DATE(onlinetime) = CURDATE()" +
            "   AND carID = #{param1}")
        Integer cartodaytimepass(String carid);
}
