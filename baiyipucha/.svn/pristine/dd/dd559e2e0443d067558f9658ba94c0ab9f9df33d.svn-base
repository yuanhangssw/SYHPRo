package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BDykePreventionMapper;
import com.ruoyi.system.domain.BDykePrevention;
import com.ruoyi.system.service.IBDykePreventionService;

/**
 * 堤防防治情况Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-04-08
 */
@Service
public class BDykePreventionServiceImpl implements IBDykePreventionService 
{
    @Autowired
    private BDykePreventionMapper bDykePreventionMapper;

    /**
     * 查询堤防防治情况
     * 
     * @param id 堤防防治情况主键
     * @return 堤防防治情况
     */
    @Override
    public BDykePrevention selectBDykePreventionById(Long id)
    {
        return bDykePreventionMapper.selectBDykePreventionById(id);
    }

    /**
     * 查询堤防防治情况列表
     * 
     * @param bDykePrevention 堤防防治情况
     * @return 堤防防治情况
     */
    @Override
    public List<BDykePrevention> selectBDykePreventionList(BDykePrevention bDykePrevention)
    {
        return bDykePreventionMapper.selectBDykePreventionList(bDykePrevention);
    }

    /**
     * 新增堤防防治情况
     * 
     * @param bDykePrevention 堤防防治情况
     * @return 结果
     */
    @Override
    public int insertBDykePrevention(BDykePrevention bDykePrevention)
    {
        bDykePrevention.setCreateTime(DateUtils.getNowDate());
        return bDykePreventionMapper.insertBDykePrevention(bDykePrevention);
    }

    /**
     * 修改堤防防治情况
     * 
     * @param bDykePrevention 堤防防治情况
     * @return 结果
     */
    @Override
    public int updateBDykePrevention(BDykePrevention bDykePrevention)
    {
        return bDykePreventionMapper.updateBDykePrevention(bDykePrevention);
    }

    /**
     * 批量删除堤防防治情况
     * 
     * @param ids 需要删除的堤防防治情况主键
     * @return 结果
     */
    @Override
    public int deleteBDykePreventionByIds(Long[] ids)
    {
        return bDykePreventionMapper.deleteBDykePreventionByIds(ids);
    }

    /**
     * 删除堤防防治情况信息
     * 
     * @param id 堤防防治情况主键
     * @return 结果
     */
    @Override
    public int deleteBDykePreventionById(Long id)
    {
        return bDykePreventionMapper.deleteBDykePreventionById(id);
    }
}
