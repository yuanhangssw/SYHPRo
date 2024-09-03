package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BPatrolUnitPlaceMapper;
import com.ruoyi.system.domain.BPatrolUnitPlace;
import com.ruoyi.system.service.IBPatrolUnitPlaceService;

/**
 * 普查处管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-04-09
 */
@Service
public class BPatrolUnitPlaceServiceImpl implements IBPatrolUnitPlaceService 
{
    @Autowired
    private BPatrolUnitPlaceMapper bPatrolUnitPlaceMapper;

    /**
     * 查询普查处管理
     * 
     * @param id 普查处管理主键
     * @return 普查处管理
     */
    @Override
    public BPatrolUnitPlace selectBPatrolUnitPlaceById(Long id)
    {
        return bPatrolUnitPlaceMapper.selectBPatrolUnitPlaceById(id);
    }

    /**
     * 查询普查处管理列表
     * 
     * @param bPatrolUnitPlace 普查处管理
     * @return 普查处管理
     */
    @Override
    public List<BPatrolUnitPlace> selectBPatrolUnitPlaceList(BPatrolUnitPlace bPatrolUnitPlace)
    {
        return bPatrolUnitPlaceMapper.selectBPatrolUnitPlaceList(bPatrolUnitPlace);
    }

    /**
     * 新增普查处管理
     * 
     * @param bPatrolUnitPlace 普查处管理
     * @return 结果
     */
    @Override
    public int insertBPatrolUnitPlace(BPatrolUnitPlace bPatrolUnitPlace)
    {
        bPatrolUnitPlace.setCreateTime(new Date());
        return bPatrolUnitPlaceMapper.insertBPatrolUnitPlace(bPatrolUnitPlace);
    }

    /**
     * 修改普查处管理
     * 
     * @param bPatrolUnitPlace 普查处管理
     * @return 结果
     */
    @Override
    public int updateBPatrolUnitPlace(BPatrolUnitPlace bPatrolUnitPlace)
    {
        return bPatrolUnitPlaceMapper.updateBPatrolUnitPlace(bPatrolUnitPlace);
    }

    /**
     * 批量删除普查处管理
     * 
     * @param ids 需要删除的普查处管理主键
     * @return 结果
     */
    @Override
    public int deleteBPatrolUnitPlaceByIds(Long[] ids)
    {
        return bPatrolUnitPlaceMapper.deleteBPatrolUnitPlaceByIds(ids);
    }

    /**
     * 删除普查处管理信息
     * 
     * @param id 普查处管理主键
     * @return 结果
     */
    @Override
    public int deleteBPatrolUnitPlaceById(Long id)
    {
        return bPatrolUnitPlaceMapper.deleteBPatrolUnitPlaceById(id);
    }
}
