package com.tianji.dam.service.impl;

import java.util.List;
import com.tj.common.utils.DateUtils;
import com.tj.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tianji.dam.mapper.TProjectUpfileMapper;
import com.tianji.dam.domain.TProjectUpfile;
import com.tianji.dam.service.ITProjectUpfileService;

/**
 * 项目试验文件Service业务层处理
 * 
 * @author liyan
 * @date 2022-06-14
 */
@Service
public class TProjectUpfileServiceImpl implements ITProjectUpfileService 
{
    @Autowired
    private TProjectUpfileMapper tProjectUpfileMapper;

    /**
     * 查询项目试验文件
     * 
     * @param gid 项目试验文件主键
     * @return 项目试验文件
     */
    @Override
    public TProjectUpfile selectTProjectUpfileByGid(Long gid)
    {
        return tProjectUpfileMapper.selectTProjectUpfileByGid(gid);
    }

    /**
     * 查询项目试验文件列表
     * 
     * @param tProjectUpfile 项目试验文件
     * @return 项目试验文件
     */
    @Override
    public List<TProjectUpfile> selectTProjectUpfileList(TProjectUpfile tProjectUpfile)
    {
        return tProjectUpfileMapper.selectTProjectUpfileList(tProjectUpfile);
    }

    /**
     * 新增项目试验文件
     * 
     * @param tProjectUpfile 项目试验文件
     * @return 结果
     */
    @Override
    public int insertTProjectUpfile(TProjectUpfile tProjectUpfile)
    {
        tProjectUpfile.setCreateTime(DateUtils.getNowDate());
        tProjectUpfile.setDelflag("N");
        tProjectUpfile.setProjectid(1l);
        tProjectUpfile.setCreateBy(SecurityUtils.getUsername());
        return tProjectUpfileMapper.insertTProjectUpfile(tProjectUpfile);
    }

    /**
     * 修改项目试验文件
     * 
     * @param tProjectUpfile 项目试验文件
     * @return 结果
     */
    @Override
    public int updateTProjectUpfile(TProjectUpfile tProjectUpfile)
    {
        return tProjectUpfileMapper.updateTProjectUpfile(tProjectUpfile);
    }

    /**
     * 批量删除项目试验文件
     * 
     * @param gids 需要删除的项目试验文件主键
     * @return 结果
     */
    @Override
    public int deleteTProjectUpfileByGids(Long[] gids)
    {
        return tProjectUpfileMapper.deleteTProjectUpfileByGids(gids);
    }

    /**
     * 删除项目试验文件信息
     * 
     * @param gid 项目试验文件主键
     * @return 结果
     */
    @Override
    public int deleteTProjectUpfileByGid(Long gid)
    {
        return tProjectUpfileMapper.deleteTProjectUpfileByGid(gid);
    }
}
