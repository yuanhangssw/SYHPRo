package com.tianji.dam.service.impl;

import java.util.List;
import com.tj.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tianji.dam.mapper.TEncodeEvolutionMapper;
import com.tianji.dam.domain.TEncodeEvolution;
import com.tianji.dam.service.ITEncodeEvolutionService;

/**
 * 层高程Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-01
 */
@Service
public class TEncodeEvolutionServiceImpl implements ITEncodeEvolutionService 
{
    @Autowired
    private TEncodeEvolutionMapper tEncodeEvolutionMapper;

    /**
     * 查询层高程
     * 
     * @param gid 层高程主键
     * @return 层高程
     */
    @Override
    public TEncodeEvolution selectTEncodeEvolutionByGid(Long gid)
    {
        return tEncodeEvolutionMapper.selectTEncodeEvolutionByGid(gid);
    }

    /**
     * 查询层高程列表
     * 
     * @param tEncodeEvolution 层高程
     * @return 层高程
     */
    @Override
    public List<TEncodeEvolution> selectTEncodeEvolutionList(TEncodeEvolution tEncodeEvolution)
    {
        return tEncodeEvolutionMapper.selectTEncodeEvolutionList(tEncodeEvolution);
    }

    /**
     * 新增层高程
     * 
     * @param tEncodeEvolution 层高程
     * @return 结果
     */
    @Override
    public int insertTEncodeEvolution(TEncodeEvolution tEncodeEvolution)
    {
        tEncodeEvolution.setCreateTime(DateUtils.getNowDate());
        return tEncodeEvolutionMapper.insertTEncodeEvolution(tEncodeEvolution);
    }

    /**
     * 修改层高程
     * 
     * @param tEncodeEvolution 层高程
     * @return 结果
     */
    @Override
    public int updateTEncodeEvolution(TEncodeEvolution tEncodeEvolution)
    {
        tEncodeEvolution.setUpdateTime(DateUtils.getNowDate());
        return tEncodeEvolutionMapper.updateTEncodeEvolution(tEncodeEvolution);
    }

    /**
     * 批量删除层高程
     * 
     * @param gids 需要删除的层高程主键
     * @return 结果
     */
    @Override
    public int deleteTEncodeEvolutionByGids(Long[] gids)
    {
        return tEncodeEvolutionMapper.deleteTEncodeEvolutionByGids(gids);
    }

    /**
     * 删除层高程信息
     * 
     * @param gid 层高程主键
     * @return 结果
     */
    @Override
    public int deleteTEncodeEvolutionByGid(Long gid)
    {
        return tEncodeEvolutionMapper.deleteTEncodeEvolutionByGid(gid);
    }
}
