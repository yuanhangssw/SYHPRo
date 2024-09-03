package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BInspectorUnitMapper;
import com.ruoyi.system.domain.BInspectorUnit;
import com.ruoyi.system.service.IBInspectorUnitService;

/**
 * 巡查用户和巡查单元关联Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-03-05
 */
@Service
public class BInspectorUnitServiceImpl implements IBInspectorUnitService 
{
    @Autowired
    private BInspectorUnitMapper bInspectorUnitMapper;

    /**
     * 查询巡查用户和巡查单元关联
     * 
     * @param id 巡查用户和巡查单元关联主键
     * @return 巡查用户和巡查单元关联
     */
    @Override
    public BInspectorUnit selectBInspectorUnitById(Long id)
    {
        return bInspectorUnitMapper.selectBInspectorUnitById(id);
    }

    /**
     * 查询巡查用户和巡查单元关联列表
     * 
     * @param bInspectorUnit 巡查用户和巡查单元关联
     * @return 巡查用户和巡查单元关联
     */
    @Override
    public List<BInspectorUnit> selectBInspectorUnitList(BInspectorUnit bInspectorUnit)
    {
        return bInspectorUnitMapper.selectBInspectorUnitList(bInspectorUnit);
    }

    /**
     * 新增巡查用户和巡查单元关联
     * 
     * @param bInspectorUnit 巡查用户和巡查单元关联
     * @return 结果
     */
    @Override
    public int insertBInspectorUnit(BInspectorUnit bInspectorUnit)
    {
        return bInspectorUnitMapper.insertBInspectorUnit(bInspectorUnit);
    }

    /**
     * 修改巡查用户和巡查单元关联
     * 
     * @param bInspectorUnit 巡查用户和巡查单元关联
     * @return 结果
     */
    @Override
    public int updateBInspectorUnit(BInspectorUnit bInspectorUnit)
    {
        return bInspectorUnitMapper.updateBInspectorUnit(bInspectorUnit);
    }

    /**
     * 批量删除巡查用户和巡查单元关联
     * 
     * @param ids 需要删除的巡查用户和巡查单元关联主键
     * @return 结果
     */
    @Override
    public int deleteBInspectorUnitByIds(Long[] ids)
    {
        return bInspectorUnitMapper.deleteBInspectorUnitByIds(ids);
    }

    /**
     * 删除巡查用户和巡查单元关联信息
     * 
     * @param id 巡查用户和巡查单元关联主键
     * @return 结果
     */
    @Override
    public int deleteBInspectorUnitById(Long id)
    {
        return bInspectorUnitMapper.deleteBInspectorUnitById(id);
    }

    @Override
    public int deleteBInspectorUnit(Long inspector, Long projectId) {
        return bInspectorUnitMapper.deleteBInspectorUnit(inspector, projectId);
    }
}
