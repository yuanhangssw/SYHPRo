package com.tianji.dam.mapper;

import java.util.List;
import com.tianji.dam.domain.TProjectUpfile;

/**
 * 项目试验文件Mapper接口
 * 
 * @author liyan
 * @date 2022-06-14
 */
public interface TProjectUpfileMapper 
{
    /**
     * 查询项目试验文件
     * 
     * @param gid 项目试验文件主键
     * @return 项目试验文件
     */
    public TProjectUpfile selectTProjectUpfileByGid(Long gid);

    /**
     * 查询项目试验文件列表
     * 
     * @param tProjectUpfile 项目试验文件
     * @return 项目试验文件集合
     */
    public List<TProjectUpfile> selectTProjectUpfileList(TProjectUpfile tProjectUpfile);

    /**
     * 新增项目试验文件
     * 
     * @param tProjectUpfile 项目试验文件
     * @return 结果
     */
    public int insertTProjectUpfile(TProjectUpfile tProjectUpfile);

    /**
     * 修改项目试验文件
     * 
     * @param tProjectUpfile 项目试验文件
     * @return 结果
     */
    public int updateTProjectUpfile(TProjectUpfile tProjectUpfile);

    /**
     * 删除项目试验文件
     * 
     * @param gid 项目试验文件主键
     * @return 结果
     */
    public int deleteTProjectUpfileByGid(Long gid);

    /**
     * 批量删除项目试验文件
     * 
     * @param gids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTProjectUpfileByGids(Long[] gids);
}
