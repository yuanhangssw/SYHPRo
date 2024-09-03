package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BPatrolTypeMapper;
import com.ruoyi.system.domain.BPatrolType;
import com.ruoyi.system.service.IBPatrolTypeService;

/**
 * 巡查类型Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-03-05
 */
@Service
public class BPatrolTypeServiceImpl implements IBPatrolTypeService 
{
    @Autowired
    private BPatrolTypeMapper bPatrolTypeMapper;

    /**
     * 查询巡查类型
     * 
     * @param id 巡查类型主键
     * @return 巡查类型
     */
    @Override
    public BPatrolType selectBPatrolTypeById(Long id)
    {
        return bPatrolTypeMapper.selectBPatrolTypeById(id);
    }

    /**
     * 查询巡查类型列表
     * 
     * @param bPatrolType 巡查类型
     * @return 巡查类型
     */
    @Override
    public List<BPatrolType> selectBPatrolTypeList(BPatrolType bPatrolType)
    {
        return bPatrolTypeMapper.selectBPatrolTypeList(bPatrolType);
    }

    /**
     * 新增巡查类型
     * 
     * @param bPatrolType 巡查类型
     * @return 结果
     */
    @Override
    public int insertBPatrolType(BPatrolType bPatrolType)
    {
        bPatrolType.setCreateTime(DateUtils.getNowDate());
        return bPatrolTypeMapper.insertBPatrolType(bPatrolType);
    }

    /**
     * 修改巡查类型
     * 
     * @param bPatrolType 巡查类型
     * @return 结果
     */
    @Override
    public int updateBPatrolType(BPatrolType bPatrolType)
    {
        bPatrolType.setUpdateTime(DateUtils.getNowDate());
        return bPatrolTypeMapper.updateBPatrolType(bPatrolType);
    }

    /**
     * 批量删除巡查类型
     * 
     * @param ids 需要删除的巡查类型主键
     * @return 结果
     */
    @Override
    public int deleteBPatrolTypeByIds(Long[] ids)
    {
        return bPatrolTypeMapper.deleteBPatrolTypeByIds(ids);
    }

    /**
     * 删除巡查类型信息
     * 
     * @param id 巡查类型主键
     * @return 结果
     */
    @Override
    public int deleteBPatrolTypeById(Long id)
    {
        return bPatrolTypeMapper.deleteBPatrolTypeById(id);
    }
}
