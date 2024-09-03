package com.tianji.dam.mapper;

import com.tianji.dam.domain.DayVolume;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 每日方量Mapper接口
 * 
 * @author liyan
 * @date 2023-12-12
 */
public interface DayVolumeMapper 
{
    /**
     * 查询每日方量
     * 
     * @param gid 每日方量主键
     * @return 每日方量
     */
    public DayVolume selectDayVolumeByGid(Long gid);

    /**
     * 查询每日方量列表
     * 
     * @param dayVolume 每日方量
     * @return 每日方量集合
     */
    public List<DayVolume> selectDayVolumeList(DayVolume dayVolume);
    public List<DayVolume> selectlast30( );

    /**
     * 新增每日方量
     * 
     * @param dayVolume 每日方量
     * @return 结果
     */
    public int insertDayVolume(DayVolume dayVolume);

    /**
     * 修改每日方量
     * 
     * @param dayVolume 每日方量
     * @return 结果
     */
    public int updateDayVolume(DayVolume dayVolume);

    /**
     * 删除每日方量
     * 
     * @param gid 每日方量主键
     * @return 结果
     */
    public int deleteDayVolumeByGid(Long gid);

    /**
     * 批量删除每日方量
     * 
     * @param gids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDayVolumeByGids(Long[] gids);

    @Select("select totalvolume from t_day_volume where days<=#{param1} order by days desc limit 1")
    Double selecttotalvolume(@Param("day") String day);
    @Select("select volume from t_day_volume where days=#{param1} order by days desc limit 1")
    Double selectvolume(@Param("day") String day);
}
