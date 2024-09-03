package com.tianji.dam.service.impl;

import com.tianji.dam.domain.DayConstructionInfo;
import com.tianji.dam.mapper.DayConstructionInfoMapper;
import com.tianji.dam.service.IDayConstructionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 日施工数据Service业务层处理
 * 
 * @author liyan
 * @date 2023-11-30
 */
@Service
public class DayConstructionInfoServiceImpl implements IDayConstructionInfoService 
{
    @Autowired
    private DayConstructionInfoMapper dayConstructionInfoMapper;

    /**
     * 查询日施工数据
     * 
     * @param gid 日施工数据主键
     * @return 日施工数据
     */
    @Override
    public DayConstructionInfo selectDayConstructionInfoByGid(Long gid)
    {
        return dayConstructionInfoMapper.selectDayConstructionInfoByGid(gid);
    }

    /**
     * 查询日施工数据列表
     * 
     * @param dayConstructionInfo 日施工数据
     * @return 日施工数据
     */
    @Override
    public List<DayConstructionInfo> selectDayConstructionInfoList(DayConstructionInfo dayConstructionInfo)
    {
        return dayConstructionInfoMapper.selectDayConstructionInfoList(dayConstructionInfo);
    }

    /**
     * 新增日施工数据
     * 
     * @param dayConstructionInfo 日施工数据
     * @return 结果
     */
    @Override
    public int insertDayConstructionInfo(DayConstructionInfo dayConstructionInfo)
    {
        return dayConstructionInfoMapper.insertDayConstructionInfo(dayConstructionInfo);
    }

    /**
     * 修改日施工数据
     * 
     * @param dayConstructionInfo 日施工数据
     * @return 结果
     */
    @Override
    public int updateDayConstructionInfo(DayConstructionInfo dayConstructionInfo)
    {
        return dayConstructionInfoMapper.updateDayConstructionInfo(dayConstructionInfo);
    }

    /**
     * 批量删除日施工数据
     * 
     * @param gids 需要删除的日施工数据主键
     * @return 结果
     */
    @Override
    public int deleteDayConstructionInfoByGids(Long[] gids)
    {
        return dayConstructionInfoMapper.deleteDayConstructionInfoByGids(gids);
    }

    /**
     * 删除日施工数据信息
     * 
     * @param gid 日施工数据主键
     * @return 结果
     */
    @Override
    public int deleteDayConstructionInfoByGid(Long gid)
    {
        return dayConstructionInfoMapper.deleteDayConstructionInfoByGid(gid);
    }
}
