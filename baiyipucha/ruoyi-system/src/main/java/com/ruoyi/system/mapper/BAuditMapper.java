package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BAudit;

/**
 * 审核记录Mapper接口
 * 
 * @author ruoyi
 * @date 2024-03-05
 */
public interface BAuditMapper 
{
    /**
     * 查询审核记录
     * 
     * @param id 审核记录主键
     * @return 审核记录
     */
    public BAudit selectBAuditById(Long id);

    /**
     * 查询审核记录列表
     * 
     * @param bAudit 审核记录
     * @return 审核记录集合
     */
    public List<BAudit> selectBAuditList(BAudit bAudit);

    /**
     * 新增审核记录
     * 
     * @param bAudit 审核记录
     * @return 结果
     */
    public int insertBAudit(BAudit bAudit);

    /**
     * 修改审核记录
     * 
     * @param bAudit 审核记录
     * @return 结果
     */
    public int updateBAudit(BAudit bAudit);

    /**
     * 删除审核记录
     * 
     * @param id 审核记录主键
     * @return 结果
     */
    public int deleteBAuditById(Long id);

    /**
     * 批量删除审核记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBAuditByIds(Long[] ids);




}
