package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BFill;

/**
 * 填报登记Mapper接口
 * 
 * @author ruoyi
 * @date 2024-03-07
 */
public interface BFillMapper 
{
    /**
     * 查询填报登记
     * 
     * @param id 填报登记主键
     * @return 填报登记
     */
    public BFill selectBFillById(Long id);

    /**
     * 查询填报登记列表
     * 
     * @param bFill 填报登记
     * @return 填报登记集合
     */
    public List<BFill> selectBFillList(BFill bFill);

    /**
     * 新增填报登记
     * 
     * @param bFill 填报登记
     * @return 结果
     */
    public int insertBFill(BFill bFill);

    /**
     * 修改填报登记
     * 
     * @param bFill 填报登记
     * @return 结果
     */
    public int updateBFill(BFill bFill);

    /**
     * 删除填报登记
     * 
     * @param id 填报登记主键
     * @return 结果
     */
    public int deleteBFillById(Long id);

    /**
     * 批量删除填报登记
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBFillByIds(Long[] ids);
}
