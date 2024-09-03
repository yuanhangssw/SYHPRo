package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BInspectorMapper;
import com.ruoyi.system.domain.BInspector;
import com.ruoyi.system.service.IBInspectorService;

/**
 * 巡查员用户Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-03-05
 */
@Service
public class BInspectorServiceImpl implements IBInspectorService 
{
    @Autowired
    private BInspectorMapper bInspectorMapper;

    /**
     * 查询巡查员用户
     * 
     * @param id 巡查员用户主键
     * @return 巡查员用户
     */
    @Override
    public BInspector selectBInspectorById(Long id)
    {
        return bInspectorMapper.selectBInspectorById(id);
    }

    /**
     * 查询巡查员用户列表
     * 
     * @param bInspector 巡查员用户
     * @return 巡查员用户
     */
    @Override
    public List<BInspector> selectBInspectorList(BInspector bInspector)
    {
        return bInspectorMapper.selectBInspectorList(bInspector);
    }

    /**
     * 新增巡查员用户
     * 
     * @param bInspector 巡查员用户
     * @return 结果
     */
    @Override
    public int insertBInspector(BInspector bInspector)
    {
        return bInspectorMapper.insertBInspector(bInspector);
    }

    /**
     * 修改巡查员用户
     * 
     * @param bInspector 巡查员用户
     * @return 结果
     */
    @Override
    public int updateBInspector(BInspector bInspector)
    {
        return bInspectorMapper.updateBInspector(bInspector);
    }

    /**
     * 批量删除巡查员用户
     * 
     * @param ids 需要删除的巡查员用户主键
     * @return 结果
     */
    @Override
    public int deleteBInspectorByIds(Long[] ids)
    {
        return bInspectorMapper.deleteBInspectorByIds(ids);
    }

    /**
     * 删除巡查员用户信息
     * 
     * @param id 巡查员用户主键
     * @return 结果
     */
    @Override
    public int deleteBInspectorById(Long id)
    {
        return bInspectorMapper.deleteBInspectorById(id);
    }
}
