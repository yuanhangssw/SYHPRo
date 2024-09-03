package com.tianji.dam.service.impl;

import java.util.List;
import com.tj.common.utils.DateUtils;
import com.tj.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.LinkedList;import java.util.ArrayList;
import com.tj.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.tianji.dam.domain.TDamsconstrctionReportDetail;
import com.tianji.dam.mapper.TDamsconstructionReportMapper;
import com.tianji.dam.domain.TDamsconstructionReport;
import com.tianji.dam.service.ITDamsconstructionReportService;

/**
 * 单元试验报告Service业务层处理
 * 
 * @author liyan
 * @date 2022-06-10
 */
@Service
public class TDamsconstructionReportServiceImpl implements ITDamsconstructionReportService 
{
    @Autowired
    private TDamsconstructionReportMapper tDamsconstructionReportMapper;

    /**
     * 查询单元试验报告
     * 
     * @param gid 单元试验报告主键
     * @return 单元试验报告
     */
    @Override
    public TDamsconstructionReport selectTDamsconstructionReportByGid(Long gid)
    {
        return tDamsconstructionReportMapper.selectTDamsconstructionReportByGid(gid);
    }

    /**
     * 查询单元试验报告列表
     * 
     * @param tDamsconstructionReport 单元试验报告
     * @return 单元试验报告
     */
    @Override
    public List<TDamsconstructionReport> selectTDamsconstructionReportList(TDamsconstructionReport tDamsconstructionReport)
    {
        return tDamsconstructionReportMapper.selectTDamsconstructionReportList(tDamsconstructionReport);
    }

    /**
     * 新增单元试验报告
     * 
     * @param tDamsconstructionReport 单元试验报告
     * @return 结果
     */
    @Transactional
    @Override
    public int insertTDamsconstructionReport(TDamsconstructionReport tDamsconstructionReport)
    {
        tDamsconstructionReport.setCreateTime(DateUtils.getNowDate());
        tDamsconstructionReport.setDelflag("N");
        tDamsconstructionReport.setType(1l);
        tDamsconstructionReport.setCreateBy(SecurityUtils.getUsername());
        int rows = tDamsconstructionReportMapper.insertTDamsconstructionReport(tDamsconstructionReport);
        insertTDamsconstrctionReportDetail(tDamsconstructionReport);
        return rows;
    }

    /**
     * 修改单元试验报告
     * 
     * @param tDamsconstructionReport 单元试验报告
     * @return 结果
     */
    @Transactional
    @Override
    public int updateTDamsconstructionReport(TDamsconstructionReport tDamsconstructionReport)
    {
        tDamsconstructionReportMapper.deleteTDamsconstrctionReportDetailByReportGid(tDamsconstructionReport.getGid());
        insertTDamsconstrctionReportDetail(tDamsconstructionReport);
        return tDamsconstructionReportMapper.updateTDamsconstructionReport(tDamsconstructionReport);
    }

    /**
     * 批量删除单元试验报告
     * 
     * @param gids 需要删除的单元试验报告主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteTDamsconstructionReportByGids(Long[] gids)
    {
        tDamsconstructionReportMapper.deleteTDamsconstrctionReportDetailByReportGids(gids);
        return tDamsconstructionReportMapper.deleteTDamsconstructionReportByGids(gids);
    }

    /**
     * 删除单元试验报告信息
     * 
     * @param gid 单元试验报告主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteTDamsconstructionReportByGid(Long gid)
    {
        tDamsconstructionReportMapper.deleteTDamsconstrctionReportDetailByReportGid(gid);
        return tDamsconstructionReportMapper.deleteTDamsconstructionReportByGid(gid);
    }

    /**
     * 新增试验报告详情信息
     * 
     * @param tDamsconstructionReport 单元试验报告对象
     */
    public void insertTDamsconstrctionReportDetail(TDamsconstructionReport tDamsconstructionReport)
    {
        List<TDamsconstrctionReportDetail> tDamsconstrctionReportDetailList = tDamsconstructionReport.gettDamsconstrctionReportDetailList();
        Long gid = tDamsconstructionReport.getGid();
        if (StringUtils.isNotNull(tDamsconstrctionReportDetailList))
        {
            List<TDamsconstrctionReportDetail> list = new LinkedList<TDamsconstrctionReportDetail>();
            for (TDamsconstrctionReportDetail tDamsconstrctionReportDetail : tDamsconstrctionReportDetailList)
            {
                tDamsconstrctionReportDetail.setReportGid(gid);
                list.add(tDamsconstrctionReportDetail);
            }
            if (list.size() > 0)
            {
                tDamsconstructionReportMapper.batchTDamsconstrctionReportDetail(list);
            }
        }
    }
}
