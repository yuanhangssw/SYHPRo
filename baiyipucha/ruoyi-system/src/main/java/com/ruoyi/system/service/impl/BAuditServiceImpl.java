package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BAuditMapper;
import com.ruoyi.system.domain.BAudit;
import com.ruoyi.system.service.IBAuditService;

/**
 * 审核记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-03-05
 */
@Service
public class BAuditServiceImpl implements IBAuditService 
{
    @Autowired
    private BAuditMapper bAuditMapper;

    /**
     * 查询审核记录
     * 
     * @param id 审核记录主键
     * @return 审核记录
     */
    @Override
    public BAudit selectBAuditById(Long id)
    {
        return bAuditMapper.selectBAuditById(id);
    }

    /**
     * 查询审核记录列表
     * 
     * @param bAudit 审核记录
     * @return 审核记录
     */
    @Override
    public List<BAudit> selectBAuditList(BAudit bAudit)
    {
        return bAuditMapper.selectBAuditList(bAudit);
    }

    /**
     * 新增审核记录
     * 
     * @param bAudit 审核记录
     * @return 结果
     */
    @Override
    public int insertBAudit(BAudit bAudit)
    {
        return bAuditMapper.insertBAudit(bAudit);
    }

    /**
     * 修改审核记录
     * 
     * @param bAudit 审核记录
     * @return 结果
     */
    @Override
    public int updateBAudit(BAudit bAudit)
    {
        return bAuditMapper.updateBAudit(bAudit);
    }

    /**
     * 批量删除审核记录
     * 
     * @param ids 需要删除的审核记录主键
     * @return 结果
     */
    @Override
    public int deleteBAuditByIds(Long[] ids)
    {
        return bAuditMapper.deleteBAuditByIds(ids);
    }

    /**
     * 删除审核记录信息
     * 
     * @param id 审核记录主键
     * @return 结果
     */
    @Override
    public int deleteBAuditById(Long id)
    {
        return bAuditMapper.deleteBAuditById(id);
    }
}
