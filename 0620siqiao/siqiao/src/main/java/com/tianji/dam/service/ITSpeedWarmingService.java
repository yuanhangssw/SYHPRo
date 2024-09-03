package com.tianji.dam.service;

import com.tianji.dam.domain.TSpeedWarming;

import java.util.List;

/**
 * 预警信息Service接口
 * 
 * @author tianji_liyan
 * @date 2021-12-28
 */
public interface ITSpeedWarmingService 
{
    /**
     * 查询预警信息
     * 
     * @param gid 预警信息主键
     * @return 预警信息
     */
    public TSpeedWarming selectTSpeedWarmingByGid(String gid);

    /**
     * 查询预警信息列表
     * 
     * @param tSpeedWarming 预警信息
     * @return 预警信息集合
     */
    public List<TSpeedWarming> selectTSpeedWarmingList(TSpeedWarming tSpeedWarming);

    /**
     * 新增预警信息
     * 
     * @param tSpeedWarming 预警信息
     * @return 结果
     */
    public int insertTSpeedWarming(TSpeedWarming tSpeedWarming);

    /**
     * 修改预警信息
     * 
     * @param tSpeedWarming 预警信息
     * @return 结果
     */
    public int updateTSpeedWarming(TSpeedWarming tSpeedWarming);

    /**
     * 批量删除预警信息
     * 
     * @param gids 需要删除的预警信息主键集合
     * @return 结果
     */
    public int deleteTSpeedWarmingByGids(String[] gids);

    /**
     * 删除预警信息信息
     * 
     * @param gid 预警信息主键
     * @return 结果
     */
    public int deleteTSpeedWarmingByGid(String gid);

	public List<TSpeedWarming> selectTSpeedWarmingList2(TSpeedWarming tsw);

    int updateStatus(String ids);
}
