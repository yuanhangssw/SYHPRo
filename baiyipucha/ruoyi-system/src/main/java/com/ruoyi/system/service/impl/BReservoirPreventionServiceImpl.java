package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.mapper.BReservoirPreventionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.BReservoirPrevention;
import com.ruoyi.system.service.IBReservoirPreventionService;

/**
 * 水库防治情况Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-04-08
 */
@Service
public class BReservoirPreventionServiceImpl implements IBReservoirPreventionService 
{
    @Autowired
    private BReservoirPreventionMapper bReservoirPreventionMapper;

    /**
     * 查询水库防治情况
     * 
     * @param id 水库防治情况主键
     * @return 水库防治情况
     */
    @Override
    public BReservoirPrevention selectBReservoirPreventionById(Long id)
    {
        return bReservoirPreventionMapper.selectBReservoirPreventionById(id);
    }

    /**
     * 查询水库防治情况列表
     * 
     * @param bReservoirPrevention 水库防治情况
     * @return 水库防治情况
     */
    @Override
    public List<BReservoirPrevention> selectBReservoirPreventionList(BReservoirPrevention bReservoirPrevention)
    {
        return bReservoirPreventionMapper.selectBReservoirPreventionList(bReservoirPrevention);
    }

    /**
     * 新增水库防治情况
     * 
     * @param bReservoirPrevention 水库防治情况
     * @return 结果
     */
    @Override
    public int insertBReservoirPrevention(BReservoirPrevention bReservoirPrevention)
    {
        bReservoirPrevention.setCreateTime(DateUtils.getNowDate());
        return bReservoirPreventionMapper.insertBReservoirPrevention(bReservoirPrevention);
    }

    /**
     * 修改水库防治情况
     * 
     * @param bReservoirPrevention 水库防治情况
     * @return 结果
     */
    @Override
    public int updateBReservoirPrevention(BReservoirPrevention bReservoirPrevention)
    {
        return bReservoirPreventionMapper.updateBReservoirPrevention(bReservoirPrevention);
    }

    /**
     * 批量删除水库防治情况
     * 
     * @param ids 需要删除的水库防治情况主键
     * @return 结果
     */
    @Override
    public int deleteBReservoirPreventionByIds(Long[] ids)
    {
        return bReservoirPreventionMapper.deleteBReservoirPreventionByIds(ids);
    }

    /**
     * 删除水库防治情况信息
     * 
     * @param id 水库防治情况主键
     * @return 结果
     */
    @Override
    public int deleteBReservoirPreventionById(Long id)
    {
        return bReservoirPreventionMapper.deleteBReservoirPreventionById(id);
    }
}
