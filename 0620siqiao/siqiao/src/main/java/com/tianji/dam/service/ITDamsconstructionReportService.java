package com.tianji.dam.service;

import com.tianji.dam.domain.TDamsconstructionReport;

import java.util.List;

/**
 * 单元试验报告Service接口
 * 
 * @author liyan
 * @date 2022-06-10
 */
public interface ITDamsconstructionReportService 
{
    /**
     * 查询单元试验报告
     * 
     * @param gid 单元试验报告主键
     * @return 单元试验报告
     */
    public TDamsconstructionReport selectTDamsconstructionReportByGid(Long gid);

    /**
     * 查询单元试验报告列表
     * 
     * @param tDamsconstructionReport 单元试验报告
     * @return 单元试验报告集合
     */
    public List<TDamsconstructionReport> selectTDamsconstructionReportList(TDamsconstructionReport tDamsconstructionReport);

    /**
     * 新增单元试验报告
     * 
     * @param tDamsconstructionReport 单元试验报告
     * @return 结果
     */
    public int insertTDamsconstructionReport(TDamsconstructionReport tDamsconstructionReport);

    /**
     * 修改单元试验报告
     * 
     * @param tDamsconstructionReport 单元试验报告
     * @return 结果
     */
    public int updateTDamsconstructionReport(TDamsconstructionReport tDamsconstructionReport);

    /**
     * 批量删除单元试验报告
     * 
     * @param gids 需要删除的单元试验报告主键集合
     * @return 结果
     */
    public int deleteTDamsconstructionReportByGids(Long[] gids);

    /**
     * 删除单元试验报告信息
     * 
     * @param gid 单元试验报告主键
     * @return 结果
     */
    public int deleteTDamsconstructionReportByGid(Long gid);

}
