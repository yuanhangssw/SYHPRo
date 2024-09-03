package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BPatrolUnitMapper;
import com.ruoyi.system.domain.BPatrolUnit;
import com.ruoyi.system.service.IBPatrolUnitService;

/**
 * 巡查单元Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-03-05
 */
@Service
public class BPatrolUnitServiceImpl implements IBPatrolUnitService 
{
    @Autowired
    private BPatrolUnitMapper bPatrolUnitMapper;

    /**
     * 查询巡查单元
     * 
     * @param id 巡查单元主键
     * @return 巡查单元
     */
    @Override
    public BPatrolUnit selectBPatrolUnitById(Long id)
    {
        return bPatrolUnitMapper.selectBPatrolUnitById(id);
    }

    /**
     * 查询巡查单元列表
     * 
     * @param bPatrolUnit 巡查单元
     * @return 巡查单元
     */
    @Override
    public List<BPatrolUnit> selectBPatrolUnitList(BPatrolUnit bPatrolUnit)
    {
        return bPatrolUnitMapper.selectBPatrolUnitList(bPatrolUnit);
    }

    /**
     * 新增巡查单元
     * 
     * @param bPatrolUnit 巡查单元
     * @return 结果
     */
    @Override
    public int insertBPatrolUnit(BPatrolUnit bPatrolUnit)
    {
        bPatrolUnit.setCreateTime(DateUtils.getNowDate());
        return bPatrolUnitMapper.insertBPatrolUnit(bPatrolUnit);
    }

    /**
     * 修改巡查单元
     * 
     * @param bPatrolUnit 巡查单元
     * @return 结果
     */
    @Override
    public int updateBPatrolUnit(BPatrolUnit bPatrolUnit)
    {
        bPatrolUnit.setUpdateTime(DateUtils.getNowDate());
        return bPatrolUnitMapper.updateBPatrolUnit(bPatrolUnit);
    }

    /**
     * 批量删除巡查单元
     * 
     * @param ids 需要删除的巡查单元主键
     * @return 结果
     */
    @Override
    public int deleteBPatrolUnitByIds(Long[] ids)
    {
        return bPatrolUnitMapper.deleteBPatrolUnitByIds(ids);
    }

    /**
     * 删除巡查单元信息
     * 
     * @param id 巡查单元主键
     * @return 结果
     */
    @Override
    public int deleteBPatrolUnitById(Long id)
    {
        return bPatrolUnitMapper.deleteBPatrolUnitById(id);
    }

    @Override
    public void deleteBPatrolUnitByProject(Long projectId) {
         bPatrolUnitMapper.deleteBPatrolUnitByProject(projectId);
    }
}
