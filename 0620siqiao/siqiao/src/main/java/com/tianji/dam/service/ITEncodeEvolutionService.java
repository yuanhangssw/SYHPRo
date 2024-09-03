package com.tianji.dam.service;

import java.util.List;
import com.tianji.dam.domain.TEncodeEvolution;

/**
 * 层高程Service接口
 * 
 * @author ruoyi
 * @date 2023-11-01
 */
public interface ITEncodeEvolutionService 
{
    /**
     * 查询层高程
     * 
     * @param gid 层高程主键
     * @return 层高程
     */
    public TEncodeEvolution selectTEncodeEvolutionByGid(Long gid);

    /**
     * 查询层高程列表
     * 
     * @param tEncodeEvolution 层高程
     * @return 层高程集合
     */
    public List<TEncodeEvolution> selectTEncodeEvolutionList(TEncodeEvolution tEncodeEvolution);

    /**
     * 新增层高程
     * 
     * @param tEncodeEvolution 层高程
     * @return 结果
     */
    public int insertTEncodeEvolution(TEncodeEvolution tEncodeEvolution);

    /**
     * 修改层高程
     * 
     * @param tEncodeEvolution 层高程
     * @return 结果
     */
    public int updateTEncodeEvolution(TEncodeEvolution tEncodeEvolution);

    /**
     * 批量删除层高程
     * 
     * @param gids 需要删除的层高程主键集合
     * @return 结果
     */
    public int deleteTEncodeEvolutionByGids(Long[] gids);

    /**
     * 删除层高程信息
     * 
     * @param gid 层高程主键
     * @return 结果
     */
    public int deleteTEncodeEvolutionByGid(Long gid);
}
