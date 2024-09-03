package com.tianji.dam.service;

import java.util.List;
import com.tianji.dam.domain.THistoryPic;

/**
 * 平面分析历史Service接口
 * 
 * @author liyan
 * @date 2022-12-15
 */
public interface ITHistoryPicService 
{
    /**
     * 查询平面分析历史
     * 
     * @param id 平面分析历史主键
     * @return 平面分析历史
     */
    public THistoryPic selectTHistoryPicById(Long id);

    /**
     * 查询平面分析历史列表
     * 
     * @param tHistoryPic 平面分析历史
     * @return 平面分析历史集合
     */
    public List<THistoryPic> selectTHistoryPicList(THistoryPic tHistoryPic);

    /**
     * 新增平面分析历史
     * 
     * @param tHistoryPic 平面分析历史
     * @return 结果
     */
    public int insertTHistoryPic(THistoryPic tHistoryPic);

    /**
     * 修改平面分析历史
     * 
     * @param tHistoryPic 平面分析历史
     * @return 结果
     */
    public int updateTHistoryPic(THistoryPic tHistoryPic);

    /**
     * 批量删除平面分析历史
     * 
     * @param ids 需要删除的平面分析历史主键集合
     * @return 结果
     */
    public int deleteTHistoryPicByIds(Long[] ids);

    /**
     * 删除平面分析历史信息
     * 
     * @param id 平面分析历史主键
     * @return 结果
     */
    public int deleteTHistoryPicById(Long id);
}
