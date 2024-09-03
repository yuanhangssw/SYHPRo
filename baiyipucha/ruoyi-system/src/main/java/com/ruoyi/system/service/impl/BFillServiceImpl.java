package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BFillMapper;
import com.ruoyi.system.domain.BFill;
import com.ruoyi.system.service.IBFillService;

/**
 * 填报登记Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-03-07
 */
@Service
public class BFillServiceImpl implements IBFillService 
{
    @Autowired
    private BFillMapper bFillMapper;

    /**
     * 查询填报登记
     * 
     * @param id 填报登记主键
     * @return 填报登记
     */
    @Override
    public BFill selectBFillById(Long id)
    {
        return bFillMapper.selectBFillById(id);
    }

    /**
     * 查询填报登记列表
     * 
     * @param bFill 填报登记
     * @return 填报登记
     */
    @Override
    public List<BFill> selectBFillList(BFill bFill)
    {
        return bFillMapper.selectBFillList(bFill);
    }

    /**
     * 新增填报登记
     * 
     * @param bFill 填报登记
     * @return 结果
     */
    @Override
    public int insertBFill(BFill bFill)
    {
        bFill.setCreateTime(DateUtils.getNowDate());
        return bFillMapper.insertBFill(bFill);
    }

    /**
     * 修改填报登记
     * 
     * @param bFill 填报登记
     * @return 结果
     */
    @Override
    public int updateBFill(BFill bFill)
    {
        return bFillMapper.updateBFill(bFill);
    }

    /**
     * 批量删除填报登记
     * 
     * @param ids 需要删除的填报登记主键
     * @return 结果
     */
    @Override
    public int deleteBFillByIds(Long[] ids)
    {
        return bFillMapper.deleteBFillByIds(ids);
    }

    /**
     * 删除填报登记信息
     * 
     * @param id 填报登记主键
     * @return 结果
     */
    @Override
    public int deleteBFillById(Long id)
    {
        return bFillMapper.deleteBFillById(id);
    }
}
