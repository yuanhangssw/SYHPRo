package com.tianji.dam.mapper;

import com.tianji.dam.domain.TRealProcessPic;

import java.util.List;

/**
 * 实景图管理Mapper接口
 * 
 * @author liyan
 * @date 2023-12-28
 */
public interface TRealProcessPicMapper 
{
    /**
     * 查询实景图管理
     * 
     * @param id 实景图管理主键
     * @return 实景图管理
     */
    public TRealProcessPic selectTRealProcessPicById(Long id);
    TRealProcessPic selectlast();

    /**
     * 查询实景图管理列表
     * 
     * @param tRealProcessPic 实景图管理
     * @return 实景图管理集合
     */
    public List<TRealProcessPic> selectTRealProcessPicList(TRealProcessPic tRealProcessPic);

    /**
     * 新增实景图管理
     * 
     * @param tRealProcessPic 实景图管理
     * @return 结果
     */
    public int insertTRealProcessPic(TRealProcessPic tRealProcessPic);

    /**
     * 修改实景图管理
     * 
     * @param tRealProcessPic 实景图管理
     * @return 结果
     */
    public int updateTRealProcessPic(TRealProcessPic tRealProcessPic);

    /**
     * 删除实景图管理
     * 
     * @param id 实景图管理主键
     * @return 结果
     */
    public int deleteTRealProcessPicById(Long id);

    /**
     * 批量删除实景图管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTRealProcessPicByIds(Long[] ids);


}
