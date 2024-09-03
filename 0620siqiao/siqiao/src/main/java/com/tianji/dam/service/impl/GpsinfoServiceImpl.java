package com.tianji.dam.service.impl;

import com.tianji.dam.domain.Gpsinfo;
import com.tianji.dam.mapper.GpsinfoMapper;
import com.tianji.dam.service.IGpsinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 人员定位信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-10-19
 */
@Service
public class GpsinfoServiceImpl implements IGpsinfoService 
{
    @Autowired
    private GpsinfoMapper gpsinfoMapper;

    /**
     * 查询人员定位信息
     * 
     * @param id 人员定位信息主键
     * @return 人员定位信息
     */
    @Override
    public Gpsinfo selectGpsinfoById(Long id)
    {
        return gpsinfoMapper.selectGpsinfoById(id);
    }

    /**
     * 查询人员定位信息列表
     * 
     * @param gpsinfo 人员定位信息
     * @return 人员定位信息
     */
    @Override
    public List<Gpsinfo> selectGpsinfoList(Gpsinfo gpsinfo)
    {
        return gpsinfoMapper.selectGpsinfoList(gpsinfo);
    }

    /**
     * 新增人员定位信息
     * 
     * @param gpsinfo 人员定位信息
     * @return 结果
     */
    @Override
    public int insertGpsinfo(Gpsinfo gpsinfo)
    {
        return gpsinfoMapper.insertGpsinfo(gpsinfo);
    }

    /**
     * 修改人员定位信息
     * 
     * @param gpsinfo 人员定位信息
     * @return 结果
     */
    @Override
    public int updateGpsinfo(Gpsinfo gpsinfo)
    {
        return gpsinfoMapper.updateGpsinfo(gpsinfo);
    }

    /**
     * 批量删除人员定位信息
     * 
     * @param ids 需要删除的人员定位信息主键
     * @return 结果
     */
    @Override
    public int deleteGpsinfoByIds(Long[] ids)
    {
        return gpsinfoMapper.deleteGpsinfoByIds(ids);
    }

    /**
     * 删除人员定位信息信息
     * 
     * @param id 人员定位信息主键
     * @return 结果
     */
    @Override
    public int deleteGpsinfoById(Long id)
    {
        return gpsinfoMapper.deleteGpsinfoById(id);
    }
}
