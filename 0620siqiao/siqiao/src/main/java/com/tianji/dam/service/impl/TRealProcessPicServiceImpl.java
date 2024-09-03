package com.tianji.dam.service.impl;

import java.util.List;
import com.tj.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tianji.dam.mapper.TRealProcessPicMapper;
import com.tianji.dam.domain.TRealProcessPic;
import com.tianji.dam.service.ITRealProcessPicService;

/**
 * 实景图管理Service业务层处理
 * 
 * @author liyan
 * @date 2023-12-28
 */
@Service
public class TRealProcessPicServiceImpl implements ITRealProcessPicService 
{
    @Autowired
    private TRealProcessPicMapper tRealProcessPicMapper;

    /**
     * 查询实景图管理
     * 
     * @param id 实景图管理主键
     * @return 实景图管理
     */
    @Override
    public TRealProcessPic selectTRealProcessPicById(Long id)
    {
        return tRealProcessPicMapper.selectTRealProcessPicById(id);
    }

    /**
     * 查询实景图管理列表
     * 
     * @param tRealProcessPic 实景图管理
     * @return 实景图管理
     */
    @Override
    public List<TRealProcessPic> selectTRealProcessPicList(TRealProcessPic tRealProcessPic)
    {
        return tRealProcessPicMapper.selectTRealProcessPicList(tRealProcessPic);
    }

    /**
     * 新增实景图管理
     * 
     * @param tRealProcessPic 实景图管理
     * @return 结果
     */
    @Override
    public int insertTRealProcessPic(TRealProcessPic tRealProcessPic)
    {
        tRealProcessPic.setCreateTime(DateUtils.getNowDate());
        return tRealProcessPicMapper.insertTRealProcessPic(tRealProcessPic);
    }

    /**
     * 修改实景图管理
     * 
     * @param tRealProcessPic 实景图管理
     * @return 结果
     */
    @Override
    public int updateTRealProcessPic(TRealProcessPic tRealProcessPic)
    {
        return tRealProcessPicMapper.updateTRealProcessPic(tRealProcessPic);
    }

    /**
     * 批量删除实景图管理
     * 
     * @param ids 需要删除的实景图管理主键
     * @return 结果
     */
    @Override
    public int deleteTRealProcessPicByIds(Long[] ids)
    {
        return tRealProcessPicMapper.deleteTRealProcessPicByIds(ids);
    }

    /**
     * 删除实景图管理信息
     * 
     * @param id 实景图管理主键
     * @return 结果
     */
    @Override
    public int deleteTRealProcessPicById(Long id)
    {
        return tRealProcessPicMapper.deleteTRealProcessPicById(id);
    }
}
