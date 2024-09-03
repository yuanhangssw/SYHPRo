package com.tianji.dam.service;

import java.util.List;
import com.tianji.dam.domain.DayVolume;

/**
 * 每日方量Service接口
 * 
 * @author liyan
 * @date 2023-12-12
 */
public interface IDayVolumeService 
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
     * 批量删除每日方量
     * 
     * @param gids 需要删除的每日方量主键集合
     * @return 结果
     */
    public int deleteDayVolumeByGids(Long[] gids);

    /**
     * 删除每日方量信息
     * 
     * @param gid 每日方量主键
     * @return 结果
     */
    public int deleteDayVolumeByGid(Long gid);
}
