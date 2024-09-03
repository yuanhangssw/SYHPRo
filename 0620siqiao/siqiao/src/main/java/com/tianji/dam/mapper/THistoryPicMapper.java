package com.tianji.dam.mapper;

import com.tianji.dam.domain.THistoryPic;

import java.util.List;

/**
 * 平面分析历史Mapper接口
 *
 * @author liyan
 * @date 2022-12-15
 */
public interface THistoryPicMapper {
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
     * 删除平面分析历史
     *
     * @param id 平面分析历史主键
     * @return 结果
     */
    public int deleteTHistoryPicById(Long id);

    public int deleteTHistoryPicByDamAndType(int damid, int type);

    /**
     * 批量删除平面分析历史
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTHistoryPicByIds(Long[] ids);
}
