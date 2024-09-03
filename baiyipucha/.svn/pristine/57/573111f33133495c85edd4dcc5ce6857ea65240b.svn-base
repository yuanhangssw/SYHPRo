package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BPatrolMapper;
import com.ruoyi.system.domain.BPatrol;
import com.ruoyi.system.service.IBPatrolService;

/**
 * 巡查Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-03-05
 */
@Service
public class BPatrolServiceImpl implements IBPatrolService 
{
    @Autowired
    private BPatrolMapper bPatrolMapper;

    /**
     * 查询巡查
     * 
     * @param id 巡查主键
     * @return 巡查
     */
    @Override
    public BPatrol selectBPatrolById(Long id)
    {
        return bPatrolMapper.selectBPatrolById(id);
    }

    /**
     * 查询巡查列表
     * 
     * @param bPatrol 巡查
     * @return 巡查
     */
    @Override
    public List<BPatrol> selectBPatrolList(BPatrol bPatrol)
    {
        return bPatrolMapper.selectBPatrolList(bPatrol);
    }

    /**
     * 新增巡查
     * 
     * @param bPatrol 巡查
     * @return 结果
     */
    @Override
    public int insertBPatrol(BPatrol bPatrol)
    {
        return bPatrolMapper.insertBPatrol(bPatrol);
    }

    /**
     * 修改巡查
     * 
     * @param bPatrol 巡查
     * @return 结果
     */
    @Override
    public int updateBPatrol(BPatrol bPatrol)
    {
        return bPatrolMapper.updateBPatrol(bPatrol);
    }

    /**
     * 批量删除巡查
     * 
     * @param ids 需要删除的巡查主键
     * @return 结果
     */
    @Override
    public int deleteBPatrolByIds(Long[] ids)
    {
        return bPatrolMapper.deleteBPatrolByIds(ids);
    }

    /**
     * 删除巡查信息
     * 
     * @param id 巡查主键
     * @return 结果
     */
    @Override
    public int deleteBPatrolById(Long id)
    {
        return bPatrolMapper.deleteBPatrolById(id);
    }
}
