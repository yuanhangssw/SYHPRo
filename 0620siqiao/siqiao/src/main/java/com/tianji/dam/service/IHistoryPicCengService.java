package com.tianji.dam.service;

import java.util.List;
import com.tianji.dam.domain.HistoryPicCeng;

/**
 * 层位分析Service接口
 * 
 * @author liyan
 * @date 2024-04-01
 */
public interface IHistoryPicCengService 
{
    /**
     * 查询层位分析
     * 
     * @param id 层位分析主键
     * @return 层位分析
     */
    public HistoryPicCeng selectHistoryPicCengById(Long id);

    /**
     * 查询层位分析列表
     * 
     * @param historyPicCeng 层位分析
     * @return 层位分析集合
     */
    public List<HistoryPicCeng> selectHistoryPicCengList(HistoryPicCeng historyPicCeng);

    /**
     * 新增层位分析
     * 
     * @param historyPicCeng 层位分析
     * @return 结果
     */
    public int insertHistoryPicCeng(HistoryPicCeng historyPicCeng);

    /**
     * 修改层位分析
     * 
     * @param historyPicCeng 层位分析
     * @return 结果
     */
    public int updateHistoryPicCeng(HistoryPicCeng historyPicCeng);

    /**
     * 批量删除层位分析
     * 
     * @param ids 需要删除的层位分析主键集合
     * @return 结果
     */
    public int deleteHistoryPicCengByIds(Long[] ids);

    /**
     * 删除层位分析信息
     * 
     * @param id 层位分析主键
     * @return 结果
     */
    public int deleteHistoryPicCengById(Long id);
}
