package com.tianji.dam.mapper;

import java.util.List;
import com.tianji.dam.domain.TDamsconstructionReport;
import com.tianji.dam.domain.TDamsconstrctionReportDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 单元试验报告Mapper接口
 * 
 * @author liyan
 * @date 2022-06-10
 */
public interface TDamsconstructionReportMapper 
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
     * 删除单元试验报告
     * 
     * @param gid 单元试验报告主键
     * @return 结果
     */
    public int deleteTDamsconstructionReportByGid(Long gid);

    /**
     * 批量删除单元试验报告
     * 
     * @param gids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTDamsconstructionReportByGids(Long[] gids);

    /**
     * 批量删除试验报告详情
     * 
     * @param gids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTDamsconstrctionReportDetailByReportGids(Long[] gids);
    
    /**
     * 批量新增试验报告详情
     * 
     * @param tDamsconstrctionReportDetailList 试验报告详情列表
     * @return 结果
     */
    public int batchTDamsconstrctionReportDetail(List<TDamsconstrctionReportDetail> tDamsconstrctionReportDetailList);
    

    /**
     * 通过单元试验报告主键删除试验报告详情信息
     * 
     * @param gid 单元试验报告ID
     * @return 结果
     */
    public int deleteTDamsconstrctionReportDetailByReportGid(Long gid);

    @Select("select param3 from t_damsconstrction_report_detail td left join t_damsconstruction_report tr on tr.gid = td.report_gid where tr.damgid=#{damgid}")
    List<TDamsconstrctionReportDetail> getreportdetail(@Param("damgid") Integer  damgid );
}
