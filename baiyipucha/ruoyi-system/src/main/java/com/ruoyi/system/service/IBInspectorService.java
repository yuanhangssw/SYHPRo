package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BInspector;

/**
 * 巡查员用户Service接口
 * 
 * @author ruoyi
 * @date 2024-03-05
 */
public interface IBInspectorService 
{
    /**
     * 查询巡查员用户
     * 
     * @param id 巡查员用户主键
     * @return 巡查员用户
     */
    public BInspector selectBInspectorById(Long id);

    /**
     * 查询巡查员用户列表
     * 
     * @param bInspector 巡查员用户
     * @return 巡查员用户集合
     */
    public List<BInspector> selectBInspectorList(BInspector bInspector);

    /**
     * 新增巡查员用户
     * 
     * @param bInspector 巡查员用户
     * @return 结果
     */
    public int insertBInspector(BInspector bInspector);

    /**
     * 修改巡查员用户
     * 
     * @param bInspector 巡查员用户
     * @return 结果
     */
    public int updateBInspector(BInspector bInspector);

    /**
     * 批量删除巡查员用户
     * 
     * @param ids 需要删除的巡查员用户主键集合
     * @return 结果
     */
    public int deleteBInspectorByIds(Long[] ids);

    /**
     * 删除巡查员用户信息
     * 
     * @param id 巡查员用户主键
     * @return 结果
     */
    public int deleteBInspectorById(Long id);
}
