package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BPatrolFileMapper;
import com.ruoyi.system.domain.BPatrolFile;
import com.ruoyi.system.service.IBPatrolFileService;

/**
 * 巡查文件Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-03-05
 */
@Service
public class BPatrolFileServiceImpl implements IBPatrolFileService 
{
    @Autowired
    private BPatrolFileMapper bPatrolFileMapper;

    /**
     * 查询巡查文件
     * 
     * @param id 巡查文件主键
     * @return 巡查文件
     */
    @Override
    public BPatrolFile selectBPatrolFileById(Long id)
    {
        return bPatrolFileMapper.selectBPatrolFileById(id);
    }

    /**
     * 查询巡查文件列表
     * 
     * @param bPatrolFile 巡查文件
     * @return 巡查文件
     */
    @Override
    public List<BPatrolFile> selectBPatrolFileList(BPatrolFile bPatrolFile)
    {
        return bPatrolFileMapper.selectBPatrolFileList(bPatrolFile);
    }

    /**
     * 新增巡查文件
     * 
     * @param bPatrolFile 巡查文件
     * @return 结果
     */
    @Override
    public int insertBPatrolFile(BPatrolFile bPatrolFile)
    {
        bPatrolFile.setCreateTime(DateUtils.getNowDate());
        return bPatrolFileMapper.insertBPatrolFile(bPatrolFile);
    }

    /**
     * 修改巡查文件
     * 
     * @param bPatrolFile 巡查文件
     * @return 结果
     */
    @Override
    public int updateBPatrolFile(BPatrolFile bPatrolFile)
    {
        return bPatrolFileMapper.updateBPatrolFile(bPatrolFile);
    }

    /**
     * 批量删除巡查文件
     * 
     * @param ids 需要删除的巡查文件主键
     * @return 结果
     */
    @Override
    public int deleteBPatrolFileByIds(Long[] ids)
    {
        return bPatrolFileMapper.deleteBPatrolFileByIds(ids);
    }

    /**
     * 删除巡查文件信息
     * 
     * @param id 巡查文件主键
     * @return 结果
     */
    @Override
    public int deleteBPatrolFileById(Long id)
    {
        return bPatrolFileMapper.deleteBPatrolFileById(id);
    }
}
