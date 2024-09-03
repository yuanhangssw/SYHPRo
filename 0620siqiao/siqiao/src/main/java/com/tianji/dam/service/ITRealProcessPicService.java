package com.tianji.dam.service;

import java.util.List;
import com.tianji.dam.domain.TRealProcessPic;

/**
 * 实景图管理Service接口
 * 
 * @author liyan
 * @date 2023-12-28
 */
public interface ITRealProcessPicService 
{
    /**
     * 查询实景图管理
     * 
     * @param id 实景图管理主键
     * @return 实景图管理
     */
    public TRealProcessPic selectTRealProcessPicById(Long id);

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
     * 批量删除实景图管理
     * 
     * @param ids 需要删除的实景图管理主键集合
     * @return 结果
     */
    public int deleteTRealProcessPicByIds(Long[] ids);

    /**
     * 删除实景图管理信息
     * 
     * @param id 实景图管理主键
     * @return 结果
     */
    public int deleteTRealProcessPicById(Long id);
}
