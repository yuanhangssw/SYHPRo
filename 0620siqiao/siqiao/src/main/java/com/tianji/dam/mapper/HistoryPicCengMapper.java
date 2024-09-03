package com.tianji.dam.mapper;

import java.util.List;
import com.tianji.dam.domain.HistoryPicCeng;

/**
 * 层位分析Mapper接口
 * 
 * @author liyan
 * @date 2024-04-01
 */
public interface HistoryPicCengMapper 
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
     * 删除层位分析
     * 
     * @param id 层位分析主键
     * @return 结果
     */
    public int deleteHistoryPicCengById(Long id);

    /**
     * 批量删除层位分析
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteHistoryPicCengByIds(Long[] ids);

    void deleteTHistoryPicBycengAndType(Integer ceng, Integer reporttype);
}
