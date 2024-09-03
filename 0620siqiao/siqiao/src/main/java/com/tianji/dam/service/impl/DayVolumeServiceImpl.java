package com.tianji.dam.service.impl;

import com.tianji.dam.domain.DayVolume;
import com.tianji.dam.mapper.DayVolumeMapper;
import com.tianji.dam.service.IDayVolumeService;
import com.tj.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 每日方量Service业务层处理
 * 
 * @author liyan
 * @date 2023-12-12
 */
@Service
public class DayVolumeServiceImpl implements IDayVolumeService 
{
    @Autowired
    private DayVolumeMapper dayVolumeMapper;

    /**
     * 查询每日方量
     * 
     * @param gid 每日方量主键
     * @return 每日方量
     */
    @Override
    public DayVolume selectDayVolumeByGid(Long gid)
    {
        return dayVolumeMapper.selectDayVolumeByGid(gid);
    }

    /**
     * 查询每日方量列表
     * 
     * @param dayVolume 每日方量
     * @return 每日方量
     */
    @Override
    public List<DayVolume> selectDayVolumeList(DayVolume dayVolume)
    {
        return dayVolumeMapper.selectDayVolumeList(dayVolume);
    }

    /**
     * 新增每日方量
     * 
     * @param dayVolume 每日方量
     * @return 结果
     */
    @Override
    public int insertDayVolume(DayVolume dayVolume)
    {
        dayVolume.setCreateTime(DateUtils.getNowDate());
        return dayVolumeMapper.insertDayVolume(dayVolume);
    }

    /**
     * 修改每日方量
     * 
     * @param dayVolume 每日方量
     * @return 结果
     */
    @Override
    public int updateDayVolume(DayVolume dayVolume)
    {
        return dayVolumeMapper.updateDayVolume(dayVolume);
    }

    /**
     * 批量删除每日方量
     * 
     * @param gids 需要删除的每日方量主键
     * @return 结果
     */
    @Override
    public int deleteDayVolumeByGids(Long[] gids)
    {
        return dayVolumeMapper.deleteDayVolumeByGids(gids);
    }

    /**
     * 删除每日方量信息
     * 
     * @param gid 每日方量主键
     * @return 结果
     */
    @Override
    public int deleteDayVolumeByGid(Long gid)
    {
        return dayVolumeMapper.deleteDayVolumeByGid(gid);
    }



}
