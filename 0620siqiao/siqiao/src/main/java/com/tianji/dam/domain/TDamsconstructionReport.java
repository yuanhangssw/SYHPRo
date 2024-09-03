package com.tianji.dam.domain;

import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.tj.common.annotation.Excel;
import com.tj.common.core.domain.BaseEntity;

/**
 * 单元试验报告对象 t_damsconstruction_report
 * 
 * @author liyan
 * @date 2022-06-10
 */
public class TDamsconstructionReport extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long gid;

    /** 报告名称 */
    @Excel(name = "报告名称")
    private String title;

    /** 试验类型 */
    @Excel(name = "试验类型")
    private Long type;

    /** 所属 */
    @Excel(name = "所属")
    private Long damgid;

    /** 报告时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报告时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reportTime;

    /** 报告附件地址 */
    private String reportPath;

    /** 状态 */
    private String delflag;

    /** $column.columnComment */
    private String freedom1;

    /** $column.columnComment */
    private String freedom2;

    /** $column.columnComment */
    private String freedom3;

    /** $column.columnComment */
    private String freedom4;

    /** $column.columnComment */
    private String freedom5;

    /** 试验报告详情信息 */
    private List<TDamsconstrctionReportDetail> tDamsconstrctionReportDetailList;

    public List<TDamsconstrctionReportDetail> gettDamsconstrctionReportDetailList() {
        return tDamsconstrctionReportDetailList;
    }

    public void settDamsconstrctionReportDetailList(List<TDamsconstrctionReportDetail> tDamsconstrctionReportDetailList) {
        this.tDamsconstrctionReportDetailList = tDamsconstrctionReportDetailList;
    }

    public void setGid(Long gid)
    {
        this.gid = gid;
    }

    public Long getGid() 
    {
        return gid;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setType(Long type) 
    {
        this.type = type;
    }

    public Long getType() 
    {
        return type;
    }
    public void setDamgid(Long damgid) 
    {
        this.damgid = damgid;
    }

    public Long getDamgid() 
    {
        return damgid;
    }
    public void setReportTime(Date reportTime) 
    {
        this.reportTime = reportTime;
    }

    public Date getReportTime() 
    {
        return reportTime;
    }
    public void setReportPath(String reportPath) 
    {
        this.reportPath = reportPath;
    }

    public String getReportPath() 
    {
        return reportPath;
    }
    public void setDelflag(String delflag) 
    {
        this.delflag = delflag;
    }

    public String getDelflag() 
    {
        return delflag;
    }
    public void setFreedom1(String freedom1) 
    {
        this.freedom1 = freedom1;
    }

    public String getFreedom1() 
    {
        return freedom1;
    }
    public void setFreedom2(String freedom2) 
    {
        this.freedom2 = freedom2;
    }

    public String getFreedom2() 
    {
        return freedom2;
    }
    public void setFreedom3(String freedom3) 
    {
        this.freedom3 = freedom3;
    }

    public String getFreedom3() 
    {
        return freedom3;
    }
    public void setFreedom4(String freedom4) 
    {
        this.freedom4 = freedom4;
    }

    public String getFreedom4() 
    {
        return freedom4;
    }
    public void setFreedom5(String freedom5) 
    {
        this.freedom5 = freedom5;
    }

    public String getFreedom5() 
    {
        return freedom5;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("gid", getGid())
            .append("title", getTitle())
            .append("type", getType())
            .append("damgid", getDamgid())
            .append("reportTime", getReportTime())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("reportPath", getReportPath())
            .append("delflag", getDelflag())
            .append("freedom1", getFreedom1())
            .append("freedom2", getFreedom2())
            .append("freedom3", getFreedom3())
            .append("freedom4", getFreedom4())
            .append("freedom5", getFreedom5())
            .append("tDamsconstrctionReportDetailList", gettDamsconstrctionReportDetailList())
            .toString();
    }
}
