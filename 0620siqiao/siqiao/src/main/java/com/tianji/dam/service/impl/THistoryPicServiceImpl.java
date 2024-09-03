package com.tianji.dam.service.impl;

import java.util.List;
import com.tj.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tianji.dam.mapper.THistoryPicMapper;
import com.tianji.dam.domain.THistoryPic;
import com.tianji.dam.service.ITHistoryPicService;

/**
 * 平面分析历史Service业务层处理
 * 
 * @author liyan
 * @date 2022-12-15
 */
@Service
public class THistoryPicServiceImpl implements ITHistoryPicService 
{
    @Autowired
    private THistoryPicMapper tHistoryPicMapper;

    /**
     * 查询平面分析历史
     * 
     * @param id 平面分析历史主键
     * @return 平面分析历史
     */
    @Override
    public THistoryPic selectTHistoryPicById(Long id)
    {
        return tHistoryPicMapper.selectTHistoryPicById(id);
    }

    /**
     * 查询平面分析历史列表
     * 
     * @param tHistoryPic 平面分析历史
     * @return 平面分析历史
     */
    @Override
    public List<THistoryPic> selectTHistoryPicList(THistoryPic tHistoryPic)
    {
        return tHistoryPicMapper.selectTHistoryPicList(tHistoryPic);
    }

    /**
     * 新增平面分析历史
     * 
     * @param tHistoryPic 平面分析历史
     * @return 结果
     */
    @Override
    public int insertTHistoryPic(THistoryPic tHistoryPic)
    {
        tHistoryPic.setCreateTime(DateUtils.getNowDate());
        return tHistoryPicMapper.insertTHistoryPic(tHistoryPic);
    }

    /**
     * 修改平面分析历史
     * 
     * @param tHistoryPic 平面分析历史
     * @return 结果
     */
    @Override
    public int updateTHistoryPic(THistoryPic tHistoryPic)
    {
        return tHistoryPicMapper.updateTHistoryPic(tHistoryPic);
    }

    /**
     * 批量删除平面分析历史
     * 
     * @param ids 需要删除的平面分析历史主键
     * @return 结果
     */
    @Override
    public int deleteTHistoryPicByIds(Long[] ids)
    {
        return tHistoryPicMapper.deleteTHistoryPicByIds(ids);
    }

    /**
     * 删除平面分析历史信息
     * 
     * @param id 平面分析历史主键
     * @return 结果
     */
    @Override
    public int deleteTHistoryPicById(Long id)
    {
        return tHistoryPicMapper.deleteTHistoryPicById(id);
    }
}
