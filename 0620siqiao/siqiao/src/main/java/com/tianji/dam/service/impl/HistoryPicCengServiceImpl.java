package com.tianji.dam.service.impl;

import java.util.List;
import com.tj.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tianji.dam.mapper.HistoryPicCengMapper;
import com.tianji.dam.domain.HistoryPicCeng;
import com.tianji.dam.service.IHistoryPicCengService;

/**
 * 层位分析Service业务层处理
 * 
 * @author liyan
 * @date 2024-04-01
 */
@Service
public class HistoryPicCengServiceImpl implements IHistoryPicCengService 
{
    @Autowired
    private HistoryPicCengMapper historyPicCengMapper;

    /**
     * 查询层位分析
     * 
     * @param id 层位分析主键
     * @return 层位分析
     */
    @Override
    public HistoryPicCeng selectHistoryPicCengById(Long id)
    {
        return historyPicCengMapper.selectHistoryPicCengById(id);
    }

    /**
     * 查询层位分析列表
     * 
     * @param historyPicCeng 层位分析
     * @return 层位分析
     */
    @Override
    public List<HistoryPicCeng> selectHistoryPicCengList(HistoryPicCeng historyPicCeng)
    {
        return historyPicCengMapper.selectHistoryPicCengList(historyPicCeng);
    }

    /**
     * 新增层位分析
     * 
     * @param historyPicCeng 层位分析
     * @return 结果
     */
    @Override
    public int insertHistoryPicCeng(HistoryPicCeng historyPicCeng)
    {
        historyPicCeng.setCreateTime(DateUtils.getNowDate());
        return historyPicCengMapper.insertHistoryPicCeng(historyPicCeng);
    }

    /**
     * 修改层位分析
     * 
     * @param historyPicCeng 层位分析
     * @return 结果
     */
    @Override
    public int updateHistoryPicCeng(HistoryPicCeng historyPicCeng)
    {
        return historyPicCengMapper.updateHistoryPicCeng(historyPicCeng);
    }

    /**
     * 批量删除层位分析
     * 
     * @param ids 需要删除的层位分析主键
     * @return 结果
     */
    @Override
    public int deleteHistoryPicCengByIds(Long[] ids)
    {
        return historyPicCengMapper.deleteHistoryPicCengByIds(ids);
    }

    /**
     * 删除层位分析信息
     * 
     * @param id 层位分析主键
     * @return 结果
     */
    @Override
    public int deleteHistoryPicCengById(Long id)
    {
        return historyPicCengMapper.deleteHistoryPicCengById(id);
    }
}
